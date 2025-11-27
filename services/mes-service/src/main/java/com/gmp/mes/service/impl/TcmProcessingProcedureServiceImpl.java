package com.gmp.mes.service.impl;

import com.gmp.mes.dto.TcmProcessingProcedureDTO;
import com.gmp.mes.entity.TcmProcessingProcedure;
import com.gmp.mes.exception.ResourceNotFoundException;
import com.gmp.mes.repository.TcmProcessingProcedureRepository;
import com.gmp.mes.service.TcmProcessingProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 中药炮制工艺服务实现类
 *
 * @author GMP系统开发团队
 */
@Service
@RequiredArgsConstructor
public class TcmProcessingProcedureServiceImpl implements TcmProcessingProcedureService {

    private final TcmProcessingProcedureRepository tcmProcessingProcedureRepository;

    @Override
    @Transactional
    public TcmProcessingProcedure createTcmProcessingProcedure(TcmProcessingProcedureDTO tcmProcessingProcedureDTO) {
        TcmProcessingProcedure tcmProcessingProcedure = new TcmProcessingProcedure();
        BeanUtils.copyProperties(tcmProcessingProcedureDTO, tcmProcessingProcedure);

        // 生成工艺编号
        String procedureNumber = generateProcedureNumber();
        tcmProcessingProcedure.setProcedureNumber(procedureNumber);

        return tcmProcessingProcedureRepository.save(tcmProcessingProcedure);
    }

    @Override
    @Transactional
    public TcmProcessingProcedure updateTcmProcessingProcedure(Long id, TcmProcessingProcedureDTO tcmProcessingProcedureDTO) {
        TcmProcessingProcedure existingProcedure = tcmProcessingProcedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药炮制工艺未找到，ID: " + id));

        BeanUtils.copyProperties(tcmProcessingProcedureDTO, existingProcedure, "id", "procedureNumber", "createdAt");

        return tcmProcessingProcedureRepository.save(existingProcedure);
    }

    @Override
    @Transactional(readOnly = true)
    public TcmProcessingProcedure getTcmProcessingProcedureById(Long id) {
        return tcmProcessingProcedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药炮制工艺未找到，ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TcmProcessingProcedure getTcmProcessingProcedureByNumber(String procedureNumber) {
        return tcmProcessingProcedureRepository.findByProcedureNumber(procedureNumber)
                .orElseThrow(() -> new ResourceNotFoundException("中药炮制工艺未找到，编号: " + procedureNumber));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TcmProcessingProcedure> getTcmProcessingProcedures(Pageable pageable) {
        return tcmProcessingProcedureRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteTcmProcessingProcedure(Long id) {
        TcmProcessingProcedure tcmProcessingProcedure = tcmProcessingProcedureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药炮制工艺未找到，ID: " + id));

        tcmProcessingProcedureRepository.delete(tcmProcessingProcedure);
    }

    /**
     * 生成中药炮制工艺编号
     * 格式：TCM-PROC-YYYYMMDD-XXXX (例如: TCM-PROC-20251127-0001)
     *
     * @return 工艺编号
     */
    private String generateProcedureNumber() {
        String datePrefix = "TCM-PROC-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 实际应用中这里应该查询数据库获取当日最大序号
        String sequence = "0001";
        return datePrefix + "-" + sequence;
    }
}