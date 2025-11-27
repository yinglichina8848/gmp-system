package com.gmp.qms.service.impl;

import com.gmp.qms.dto.TcmDeviationDTO;
import com.gmp.qms.entity.TcmDeviation;
import com.gmp.qms.exception.ResourceNotFoundException;
import com.gmp.qms.repository.TcmDeviationRepository;
import com.gmp.qms.service.TcmDeviationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 中药特色偏差管理服务实现类
 *
 * @author GMP系统开发团队
 */
@Service
@RequiredArgsConstructor
public class TcmDeviationServiceImpl implements TcmDeviationService {

    private final TcmDeviationRepository tcmDeviationRepository;

    @Override
    @Transactional
    public TcmDeviation createTcmDeviation(TcmDeviationDTO tcmDeviationDTO) {
        TcmDeviation tcmDeviation = new TcmDeviation();
        BeanUtils.copyProperties(tcmDeviationDTO, tcmDeviation);

        // 生成中药特色偏差编号
        String deviationCode = generateTcmDeviationCode();
        tcmDeviation.setDeviationCode(deviationCode);

        return tcmDeviationRepository.save(tcmDeviation);
    }

    @Override
    @Transactional
    public TcmDeviation updateTcmDeviation(Long id, TcmDeviationDTO tcmDeviationDTO) {
        TcmDeviation existingTcmDeviation = tcmDeviationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色偏差未找到，ID: " + id));

        BeanUtils.copyProperties(tcmDeviationDTO, existingTcmDeviation, "id", "deviationCode", "createdAt", "createdBy");

        return tcmDeviationRepository.save(existingTcmDeviation);
    }

    @Override
    @Transactional(readOnly = true)
    public TcmDeviation getTcmDeviationById(Long id) {
        return tcmDeviationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色偏差未找到，ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public TcmDeviation getTcmDeviationByCode(String deviationCode) {
        return tcmDeviationRepository.findByDeviationCode(deviationCode)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色偏差未找到，编号: " + deviationCode));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TcmDeviation> getTcmDeviations(Pageable pageable) {
        return tcmDeviationRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteTcmDeviation(Long id) {
        TcmDeviation tcmDeviation = tcmDeviationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("中药特色偏差未找到，ID: " + id));

        tcmDeviationRepository.delete(tcmDeviation);
    }

    /**
     * 生成中药特色偏差编号
     * 格式：TCM-DEV-YYYYMMDD-XXXX (例如: TCM-DEV-20251127-0001)
     *
     * @return 偏差编号
     */
    private String generateTcmDeviationCode() {
        String datePrefix = "TCM-DEV-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 实际应用中这里应该查询数据库获取当日最大序号
        String sequence = "0001";
        return datePrefix + "-" + sequence;
    }
}