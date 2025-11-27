package com.gmp.equipment.service.impl;

import com.gmp.equipment.dto.MaintenanceRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.entity.MaintenanceRecord;
import com.gmp.equipment.repository.MaintenanceRecordRepository;
import com.gmp.equipment.service.MaintenanceRecordService;
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
 * 设备维护记录服务实现类
 * 实现维护记录相关的业务逻辑
 */
@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Override
    public MaintenanceRecordDTO getById(Long id) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维护记录不存在: " + id));
        return convertToDTO(maintenanceRecord);
    }

    @Override
    public MaintenanceRecordDTO getByMaintenanceNumber(String maintenanceNumber) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findByMaintenanceNumber(maintenanceNumber)
                .orElseThrow(() -> new RuntimeException("维护记录不存在: " + maintenanceNumber));
        return convertToDTO(maintenanceRecord);
    }

    @Override
    public PageResultDTO<MaintenanceRecordDTO> listPage(Long equipmentId, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNum - 1, 
                pageSize,
                Sort.by(Sort.Direction.DESC, "maintenanceDate")
        );
        
        Page<MaintenanceRecord> page;
        if (equipmentId != null) {
            page = maintenanceRecordRepository.findByEquipmentId(equipmentId, pageRequest);
        } else {
            page = maintenanceRecordRepository.findAll(pageRequest);
        }
        
        return convertToPageResultDTO(page);
    }

    @Override
    public List<MaintenanceRecordDTO> listByEquipmentId(Long equipmentId) {
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByEquipmentIdOrderByMaintenanceDateDesc(equipmentId);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceRecordDTO> listByMaintenancePlanId(Long maintenancePlanId) {
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByMaintenancePlanId(maintenancePlanId);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceRecordDTO> listByDateRange(Date startDate, Date endDate) {
        List<MaintenanceRecord> records = maintenanceRecordRepository.findByMaintenanceDateBetween(startDate, endDate);
        return records.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public MaintenanceRecordDTO getLatestByEquipmentId(Long equipmentId) {
        MaintenanceRecord record = maintenanceRecordRepository.findTopByEquipmentIdOrderByMaintenanceDateDesc(equipmentId)
                .orElse(null);
        return record != null ? convertToDTO(record) : null;
    }

    @Override
    @Transactional
    public MaintenanceRecordDTO create(MaintenanceRecordDTO maintenanceRecordDTO) {
        // 检查维护编号是否已存在
        if (existsByMaintenanceNumber(maintenanceRecordDTO.getMaintenanceNumber(), null)) {
            throw new RuntimeException("维护编号已存在: " + maintenanceRecordDTO.getMaintenanceNumber());
        }

        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        BeanUtils.copyProperties(maintenanceRecordDTO, maintenanceRecord);
        maintenanceRecord.setCreatedBy(maintenanceRecordDTO.getCreatedBy() != null ? maintenanceRecordDTO.getCreatedBy() : "system");
        maintenanceRecord.setUpdatedBy(maintenanceRecordDTO.getUpdatedBy() != null ? maintenanceRecordDTO.getUpdatedBy() : "system");

        MaintenanceRecord saved = maintenanceRecordRepository.save(maintenanceRecord);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public MaintenanceRecordDTO update(Long id, MaintenanceRecordDTO maintenanceRecordDTO) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维护记录不存在: " + id));

        // 检查维护编号是否已存在（排除当前记录）
        if (existsByMaintenanceNumber(maintenanceRecordDTO.getMaintenanceNumber(), id)) {
            throw new RuntimeException("维护编号已存在: " + maintenanceRecordDTO.getMaintenanceNumber());
        }

        BeanUtils.copyProperties(maintenanceRecordDTO, maintenanceRecord);
        maintenanceRecord.setId(id);
        maintenanceRecord.setUpdatedBy(maintenanceRecordDTO.getUpdatedBy() != null ? maintenanceRecordDTO.getUpdatedBy() : "system");

        MaintenanceRecord updated = maintenanceRecordRepository.save(maintenanceRecord);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维护记录不存在: " + id));
        maintenanceRecordRepository.delete(maintenanceRecord);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        List<MaintenanceRecord> records = maintenanceRecordRepository.findAllById(ids);
        if (records.size() != ids.size()) {
            throw new RuntimeException("部分维护记录不存在");
        }
        maintenanceRecordRepository.deleteAll(records);
    }

    @Override
    public boolean existsByMaintenanceNumber(String maintenanceNumber, Long id) {
        if (id == null) {
            return maintenanceRecordRepository.findByMaintenanceNumber(maintenanceNumber).isPresent();
        } else {
            return maintenanceRecordRepository.existsByMaintenanceNumberAndIdNot(maintenanceNumber, id);
        }
    }

    /**
     * 将实体类转换为DTO
     * @param maintenanceRecord 维护记录实体
     * @return 维护记录DTO
     */
    private MaintenanceRecordDTO convertToDTO(MaintenanceRecord maintenanceRecord) {
        MaintenanceRecordDTO dto = new MaintenanceRecordDTO();
        BeanUtils.copyProperties(maintenanceRecord, dto);
        return dto;
    }

    /**
     * 将分页实体转换为分页DTO
     * @param page 分页实体
     * @return 分页DTO
     */
    private PageResultDTO<MaintenanceRecordDTO> convertToPageResultDTO(Page<MaintenanceRecord> page) {
        PageResultDTO<MaintenanceRecordDTO> pageResult = new PageResultDTO<>();
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