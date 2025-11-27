package com.gmp.lims.service.impl;

import com.gmp.lims.dto.TcmInspectionDTO;
import com.gmp.lims.entity.TcmInspection;
import com.gmp.lims.exception.ResourceNotFoundException;
import com.gmp.lims.repository.TcmInspectionRepository;
import com.gmp.lims.service.TcmInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 中药特色检验服务实现类
 */
@Service
@RequiredArgsConstructor
public class TcmInspectionServiceImpl implements TcmInspectionService {

    private final TcmInspectionRepository tcmInspectionRepository;

    @Override
    @Transactional
    public TcmInspection createTcmInspection(TcmInspectionDTO tcmInspectionDTO) {
        TcmInspection tcmInspection = new TcmInspection();
        BeanUtils.copyProperties(tcmInspectionDTO, tcmInspection);

        // 生成检验编号
        String inspectionCode = generateInspectionCode();
        tcmInspection.setInspectionCode(inspectionCode);

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        tcmInspection.setCreateDate(now);
        tcmInspection.setUpdateDate(now);

        return tcmInspectionRepository.save(tcmInspection);
    }

    @Override
    @Transactional
    public TcmInspection updateTcmInspection(Long id, TcmInspectionDTO tcmInspectionDTO) {
        TcmInspection existingInspection = tcmInspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色检验记录未找到，ID: " + id));

        BeanUtils.copyProperties(tcmInspectionDTO, existingInspection, "id", "inspectionCode", "createDate");

        // 更新时间
        existingInspection.setUpdateDate(LocalDateTime.now());

        return tcmInspectionRepository.save(existingInspection);
    }

    @Override
    @Transactional(readOnly = true)
    public TcmInspection getTcmInspectionById(Long id) {
        return tcmInspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色检验记录未找到，ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TcmInspection getTcmInspectionByCode(String inspectionCode) {
        return tcmInspectionRepository.findByInspectionCode(inspectionCode)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色检验记录未找到，检验编号: " + inspectionCode));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TcmInspection> getTcmInspections(Pageable pageable) {
        return tcmInspectionRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteTcmInspection(Long id) {
        TcmInspection tcmInspection = tcmInspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色检验记录未找到，ID: " + id));

        tcmInspectionRepository.delete(tcmInspection);
    }

    /**
     * 生成中药特色检验编号
     * 格式：TCM-INS-YYYYMMDD-XXXX (例如: TCM-INS-20251127-0001)
     *
     * @return 检验编号
     */
    private String generateInspectionCode() {
        String datePrefix = "TCM-INS-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 实际应用中这里应该查询数据库获取当日最大序号
        String sequence = "0001";
        return datePrefix + "-" + sequence;
    }
}