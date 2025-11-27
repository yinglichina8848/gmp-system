package com.gmp.equipment.service.impl;

import com.gmp.equipment.dto.CalibrationRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.entity.CalibrationRecord;
import com.gmp.equipment.repository.CalibrationRecordRepository;
import com.gmp.equipment.service.CalibrationRecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备校准记录服务实现类
 * 实现校准记录相关的业务逻辑
 */
@Service
public class CalibrationRecordServiceImpl implements CalibrationRecordService {

    @Autowired
    private CalibrationRecordRepository calibrationRecordRepository;

    @Override
    public CalibrationRecordDTO getById(Long id) {
        CalibrationRecord calibrationRecord = calibrationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("校准记录不存在: " + id));
        return convertToDTO(calibrationRecord);
    }

    @Override
    public CalibrationRecordDTO getByCalibrationNumber(String calibrationNumber) {
        CalibrationRecord calibrationRecord = calibrationRecordRepository.findByCalibrationNumber(calibrationNumber)
                .orElseThrow(() -> new RuntimeException("校准记录不存在: " + calibrationNumber));
        return convertToDTO(calibrationRecord);
    }

    @Override
    public PageResultDTO<CalibrationRecordDTO> listPage(Long equipmentId, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNum - 1, 
                pageSize,
                Sort.by(Sort.Direction.DESC, "calibrationDate")
        );
        
        Page<CalibrationRecord> page;
        if (equipmentId != null) {
            page = calibrationRecordRepository.findByEquipmentId(equipmentId, pageRequest);
        } else {
            page = calibrationRecordRepository.findAll(pageRequest);
        }
        
        return convertToPageResultDTO(page);
    }

    @Override
    public List<CalibrationRecordDTO> listByEquipmentId(Long equipmentId) {
        List<CalibrationRecord> records = calibrationRecordRepository.findByEquipmentIdOrderByCalibrationDateDesc(equipmentId);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CalibrationRecordDTO> listByDateRange(Date startDate, Date endDate) {
        List<CalibrationRecord> records = calibrationRecordRepository.findByCalibrationDateBetween(startDate, endDate);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CalibrationRecordDTO> listByResult(String calibrationResult) {
        List<CalibrationRecord> records = calibrationRecordRepository.findByCalibrationResult(calibrationResult);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CalibrationRecordDTO getLatestByEquipmentId(Long equipmentId) {
        CalibrationRecord record = calibrationRecordRepository.findTopByEquipmentIdOrderByCalibrationDateDesc(equipmentId)
                .orElse(null);
        return record != null ? convertToDTO(record) : null;
    }

    @Override
    @Transactional
    public CalibrationRecordDTO create(CalibrationRecordDTO calibrationRecordDTO) {
        // 检查校准编号是否已存在
        if (existsByCalibrationNumber(calibrationRecordDTO.getCalibrationNumber(), null)) {
            throw new RuntimeException("校准编号已存在: " + calibrationRecordDTO.getCalibrationNumber());
        }

        CalibrationRecord calibrationRecord = new CalibrationRecord();
        BeanUtils.copyProperties(calibrationRecordDTO, calibrationRecord);
        calibrationRecord.setCreatedBy(calibrationRecordDTO.getCreatedBy() != null ? calibrationRecordDTO.getCreatedBy() : "system");
        calibrationRecord.setUpdatedBy(calibrationRecordDTO.getUpdatedBy() != null ? calibrationRecordDTO.getUpdatedBy() : "system");

        CalibrationRecord saved = calibrationRecordRepository.save(calibrationRecord);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public CalibrationRecordDTO update(Long id, CalibrationRecordDTO calibrationRecordDTO) {
        CalibrationRecord calibrationRecord = calibrationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("校准记录不存在: " + id));

        // 检查校准编号是否已存在（排除当前记录）
        if (existsByCalibrationNumber(calibrationRecordDTO.getCalibrationNumber(), id)) {
            throw new RuntimeException("校准编号已存在: " + calibrationRecordDTO.getCalibrationNumber());
        }

        BeanUtils.copyProperties(calibrationRecordDTO, calibrationRecord);
        calibrationRecord.setId(id);
        calibrationRecord.setUpdatedBy(calibrationRecordDTO.getUpdatedBy() != null ? calibrationRecordDTO.getUpdatedBy() : "system");

        CalibrationRecord updated = calibrationRecordRepository.save(calibrationRecord);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CalibrationRecord calibrationRecord = calibrationRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("校准记录不存在: " + id));
        calibrationRecordRepository.delete(calibrationRecord);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        List<CalibrationRecord> records = calibrationRecordRepository.findAllById(ids);
        if (records.size() != ids.size()) {
            throw new RuntimeException("部分校准记录不存在");
        }
        calibrationRecordRepository.deleteAll(records);
    }

    @Override
    public boolean existsByCalibrationNumber(String calibrationNumber, Long id) {
        if (id == null) {
            return calibrationRecordRepository.findByCalibrationNumber(calibrationNumber).isPresent();
        } else {
            return calibrationRecordRepository.existsByCalibrationNumberAndIdNot(calibrationNumber, id);
        }
    }

    /**
     * 将实体类转换为DTO
     * @param calibrationRecord 校准记录实体
     * @return 校准记录DTO
     */
    private CalibrationRecordDTO convertToDTO(CalibrationRecord calibrationRecord) {
        CalibrationRecordDTO dto = new CalibrationRecordDTO();
        BeanUtils.copyProperties(calibrationRecord, dto);
        return dto;
    }

    /**
     * 将分页实体转换为分页DTO
     * @param page 分页实体
     * @return 分页DTO
     */
    private PageResultDTO<CalibrationRecordDTO> convertToPageResultDTO(Page<CalibrationRecord> page) {
        PageResultDTO<CalibrationRecordDTO> pageResult = new PageResultDTO<>();
        pageResult.setTotal(page.getTotalElements());
        pageResult.setRecords(page.getContent().stream().map(this::convertToDTO).collect(Collectors.toList()));
        pageResult.setPageNum(page.getNumber() + 1);
        pageResult.setPageSize(page.getSize());
        pageResult.setPages(page.getTotalPages());
        pageResult.setHasNextPage(page.hasNext());
        pageResult.setHasPreviousPage(page.hasPrevious());
        return pageResult;
    }
}