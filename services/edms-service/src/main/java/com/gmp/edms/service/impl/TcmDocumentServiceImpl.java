package com.gmp.edms.service.impl;

import com.gmp.edms.dto.TcmDocumentDTO;
import com.gmp.edms.entity.TcmDocument;
import com.gmp.edms.exception.ResourceNotFoundException;
import com.gmp.edms.repository.TcmDocumentRepository;
import com.gmp.edms.service.TcmDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 中药特色文档服务实现类
 */
@Service
@RequiredArgsConstructor
public class TcmDocumentServiceImpl implements TcmDocumentService {

    private final TcmDocumentRepository tcmDocumentRepository;

    @Override
    @Transactional
    public TcmDocument createTcmDocument(TcmDocumentDTO tcmDocumentDTO) {
        TcmDocument tcmDocument = new TcmDocument();
        BeanUtils.copyProperties(tcmDocumentDTO, tcmDocument);

        // 生成文档编号
        String documentNumber = generateDocumentNumber();
        try {
            java.lang.reflect.Field numberField = TcmDocument.class.getDeclaredField("documentNumber");
            numberField.setAccessible(true);
            numberField.set(tcmDocument, documentNumber);
        } catch (Exception e) {
            // 忽略错误
        }

        return tcmDocumentRepository.save(tcmDocument);
    }

    @Override
    @Transactional
    public TcmDocument updateTcmDocument(Long id, TcmDocumentDTO tcmDocumentDTO) {
        TcmDocument existingDocument = tcmDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色文档未找到，ID: " + id));

        BeanUtils.copyProperties(tcmDocumentDTO, existingDocument, "id", "documentNumber", "createdAt");

        return tcmDocumentRepository.save(existingDocument);
    }

    @Override
    @Transactional(readOnly = true)
    public TcmDocument getTcmDocumentById(Long id) {
        return tcmDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色文档未找到，ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TcmDocument getTcmDocumentByNumber(String documentNumber) {
        return tcmDocumentRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色文档未找到，文档编号: " + documentNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TcmDocument> getTcmDocumentsByHerbName(String herbName) {
        return tcmDocumentRepository.findByHerbName(herbName);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TcmDocument> getTcmDocuments(Pageable pageable) {
        return tcmDocumentRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteTcmDocument(Long id) {
        TcmDocument tcmDocument = tcmDocumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色文档未找到，ID: " + id));

        tcmDocumentRepository.delete(tcmDocument);
    }

    /**
     * 生成中药特色文档编号
     * 格式：TCM-DOC-YYYYMMDD-XXXX (例如: TCM-DOC-20251127-0001)
     *
     * @return 文档编号
     */
    private String generateDocumentNumber() {
        String datePrefix = "TCM-DOC-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 实际应用中这里应该查询数据库获取当日最大序号
        String sequence = "0001";
        return datePrefix + "-" + sequence;
    }
}