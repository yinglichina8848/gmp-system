package com.gmp.equipment.service.impl;

import com.gmp.equipment.dto.EquipmentDTO;
import com.gmp.equipment.dto.EquipmentQueryDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.entity.Equipment;
import com.gmp.equipment.repository.EquipmentRepository;
import com.gmp.equipment.service.EquipmentService;
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
 * 设备服务实现类
 * 实现设备相关的业务逻辑
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public EquipmentDTO getById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + id));
        return convertToDTO(equipment);
    }

    @Override
    public EquipmentDTO getByCode(String code) {
        Equipment equipment = equipmentRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + code));
        return convertToDTO(equipment);
    }

    @Override
    public EquipmentDTO getBySerialNumber(String serialNumber) {
        Equipment equipment = equipmentRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + serialNumber));
        return convertToDTO(equipment);
    }

    @Override
    public PageResultDTO<EquipmentDTO> listPage(EquipmentQueryDTO queryDTO) {
        // 构建查询条件
        PageRequest pageRequest = PageRequest.of(
                queryDTO.getPageNum() - 1, 
                queryDTO.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        
        Page<Equipment> page;
        if (queryDTO.getEquipmentTypeId() != null) {
            page = equipmentRepository.findByEquipmentTypeId(queryDTO.getEquipmentTypeId(), pageRequest);
        } else if (queryDTO.getStatus() != null) {
            page = equipmentRepository.findByStatus(queryDTO.getStatus(), pageRequest);
        } else if (queryDTO.getCode() != null) {
            page = equipmentRepository.findByCodeContaining(queryDTO.getCode(), pageRequest);
        } else if (queryDTO.getName() != null) {
            page = equipmentRepository.findByNameContaining(queryDTO.getName(), pageRequest);
        } else {
            page = equipmentRepository.findAll(pageRequest);
        }
        
        return convertToPageResultDTO(page);
    }

    @Override
    public List<EquipmentDTO> listByTypeId(Long equipmentTypeId) {
        List<Equipment> equipmentList = equipmentRepository.findByEquipmentTypeId(equipmentTypeId);
        return equipmentList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EquipmentDTO> listByStatus(String status) {
        List<Equipment> equipmentList = equipmentRepository.findByStatus(status);
        return equipmentList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EquipmentDTO> listNeedCalibration(Date date) {
        List<Equipment> equipmentList = equipmentRepository.findByNextCalibrationDateBeforeAndStatusNot(date, "报废");
        return equipmentList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EquipmentDTO> listNeedMaintenance(Date date) {
        List<Equipment> equipmentList = equipmentRepository.findByNextMaintenanceDateBeforeAndStatusNot(date, "报废");
        return equipmentList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EquipmentDTO create(EquipmentDTO equipmentDTO) {
        // 检查编码是否已存在
        if (existsByCode(equipmentDTO.getCode(), null)) {
            throw new RuntimeException("设备编码已存在: " + equipmentDTO.getCode());
        }
        // 检查序列号是否已存在
        if (existsBySerialNumber(equipmentDTO.getSerialNumber(), null)) {
            throw new RuntimeException("设备序列号已存在: " + equipmentDTO.getSerialNumber());
        }

        Equipment equipment = new Equipment();
        BeanUtils.copyProperties(equipmentDTO, equipment);
        equipment.setCreatedBy(equipmentDTO.getCreatedBy() != null ? equipmentDTO.getCreatedBy() : "system");
        equipment.setUpdatedBy(equipmentDTO.getUpdatedBy() != null ? equipmentDTO.getUpdatedBy() : "system");

        Equipment saved = equipmentRepository.save(equipment);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public EquipmentDTO update(Long id, EquipmentDTO equipmentDTO) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + id));

        // 检查编码是否已存在（排除当前记录）
        if (existsByCode(equipmentDTO.getCode(), id)) {
            throw new RuntimeException("设备编码已存在: " + equipmentDTO.getCode());
        }
        // 检查序列号是否已存在（排除当前记录）
        if (existsBySerialNumber(equipmentDTO.getSerialNumber(), id)) {
            throw new RuntimeException("设备序列号已存在: " + equipmentDTO.getSerialNumber());
        }

        BeanUtils.copyProperties(equipmentDTO, equipment);
        equipment.setId(id);
        equipment.setUpdatedBy(equipmentDTO.getUpdatedBy() != null ? equipmentDTO.getUpdatedBy() : "system");

        Equipment updated = equipmentRepository.save(equipment);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + id));
        equipmentRepository.delete(equipment);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        List<Equipment> equipmentList = equipmentRepository.findAllById(ids);
        if (equipmentList.size() != ids.size()) {
            throw new RuntimeException("部分设备不存在");
        }
        equipmentRepository.deleteAll(equipmentList);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + id));
        equipment.setStatus(status);
        equipment.setUpdatedBy("system");
        equipmentRepository.save(equipment);
    }

    @Override
    @Transactional
    public void updateNextCalibrationDate(Long id, Date nextCalibrationDate) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + id));
        equipment.setNextCalibrationDate(nextCalibrationDate);
        equipment.setUpdatedBy("system");
        equipmentRepository.save(equipment);
    }

    @Override
    @Transactional
    public void updateNextMaintenanceDate(Long id, Date nextMaintenanceDate) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备不存在: " + id));
        equipment.setNextMaintenanceDate(nextMaintenanceDate);
        equipment.setUpdatedBy("system");
        equipmentRepository.save(equipment);
    }

    @Override
    public boolean existsByCode(String code, Long id) {
        if (id == null) {
            return equipmentRepository.findByCode(code).isPresent();
        } else {
            return equipmentRepository.existsByCodeAndIdNot(code, id);
        }
    }

    @Override
    public boolean existsBySerialNumber(String serialNumber, Long id) {
        if (id == null) {
            return equipmentRepository.findBySerialNumber(serialNumber).isPresent();
        } else {
            return equipmentRepository.existsBySerialNumberAndIdNot(serialNumber, id);
        }
    }

    /**
     * 将实体类转换为DTO
     * @param equipment 设备实体
     * @return 设备DTO
     */
    private EquipmentDTO convertToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        BeanUtils.copyProperties(equipment, dto);
        return dto;
    }

    /**
     * 将分页实体转换为分页DTO
     * @param page 分页实体
     * @return 分页DTO
     */
    private PageResultDTO<EquipmentDTO> convertToPageResultDTO(Page<Equipment> page) {
        PageResultDTO<EquipmentDTO> pageResult = new PageResultDTO<>();
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