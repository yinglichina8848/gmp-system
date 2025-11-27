package com.gmp.equipment.service.impl;

import com.gmp.equipment.dto.EquipmentTypeDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.entity.EquipmentType;
import com.gmp.equipment.repository.EquipmentTypeRepository;
import com.gmp.equipment.service.EquipmentTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备类型服务实现类
 * 实现设备类型相关的业务逻辑
 */
@Service
public class EquipmentTypeServiceImpl implements EquipmentTypeService {

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    @Override
    public EquipmentTypeDTO getById(Long id) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备类型不存在: " + id));
        return convertToDTO(equipmentType);
    }

    @Override
    public EquipmentTypeDTO getByCode(String code) {
        EquipmentType equipmentType = equipmentTypeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("设备类型不存在: " + code));
        return convertToDTO(equipmentType);
    }

    @Override
    public List<EquipmentTypeDTO> listAll() {
        List<EquipmentType> equipmentTypes = equipmentTypeRepository.findAll();
        return equipmentTypes.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<EquipmentTypeDTO> listEnabled() {
        List<EquipmentType> equipmentTypes = equipmentTypeRepository.findAll();
        return equipmentTypes.stream()
                .filter(EquipmentType::getEnabled)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResultDTO<EquipmentTypeDTO> listPage(Integer pageNum, Integer pageSize) {
        Page<EquipmentType> page = equipmentTypeRepository.findAll(PageRequest.of(pageNum - 1, pageSize));
        return convertToPageResultDTO(page);
    }

    @Override
    @Transactional
    public EquipmentTypeDTO create(EquipmentTypeDTO equipmentTypeDTO) {
        // 检查编码是否已存在
        if (existsByCode(equipmentTypeDTO.getCode(), null)) {
            throw new RuntimeException("设备类型编码已存在: " + equipmentTypeDTO.getCode());
        }
        // 检查名称是否已存在
        if (existsByName(equipmentTypeDTO.getName(), null)) {
            throw new RuntimeException("设备类型名称已存在: " + equipmentTypeDTO.getName());
        }

        EquipmentType equipmentType = new EquipmentType();
        BeanUtils.copyProperties(equipmentTypeDTO, equipmentType);
        equipmentType.setCreatedBy(equipmentTypeDTO.getCreatedBy() != null ? equipmentTypeDTO.getCreatedBy() : "system");
        equipmentType.setUpdatedBy(equipmentTypeDTO.getUpdatedBy() != null ? equipmentTypeDTO.getUpdatedBy() : "system");

        EquipmentType saved = equipmentTypeRepository.save(equipmentType);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public EquipmentTypeDTO update(Long id, EquipmentTypeDTO equipmentTypeDTO) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备类型不存在: " + id));

        // 检查编码是否已存在（排除当前记录）
        if (existsByCode(equipmentTypeDTO.getCode(), id)) {
            throw new RuntimeException("设备类型编码已存在: " + equipmentTypeDTO.getCode());
        }
        // 检查名称是否已存在（排除当前记录）
        if (existsByName(equipmentTypeDTO.getName(), id)) {
            throw new RuntimeException("设备类型名称已存在: " + equipmentTypeDTO.getName());
        }

        BeanUtils.copyProperties(equipmentTypeDTO, equipmentType);
        equipmentType.setId(id);
        equipmentType.setUpdatedBy(equipmentTypeDTO.getUpdatedBy() != null ? equipmentTypeDTO.getUpdatedBy() : "system");

        EquipmentType updated = equipmentTypeRepository.save(equipmentType);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备类型不存在: " + id));
        equipmentTypeRepository.delete(equipmentType);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        List<EquipmentType> equipmentTypes = equipmentTypeRepository.findAllById(ids);
        if (equipmentTypes.size() != ids.size()) {
            throw new RuntimeException("部分设备类型不存在");
        }
        equipmentTypeRepository.deleteAll(equipmentTypes);
    }

    @Override
    @Transactional
    public void enable(Long id, Boolean enabled) {
        EquipmentType equipmentType = equipmentTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("设备类型不存在: " + id));
        equipmentType.setEnabled(enabled);
        equipmentTypeRepository.save(equipmentType);
    }

    @Override
    public boolean existsByCode(String code, Long id) {
        if (id == null) {
            return equipmentTypeRepository.findByCode(code).isPresent();
        } else {
            return equipmentTypeRepository.existsByCodeAndIdNot(code, id);
        }
    }

    @Override
    public boolean existsByName(String name, Long id) {
        if (id == null) {
            return equipmentTypeRepository.findByName(name).isPresent();
        } else {
            return equipmentTypeRepository.existsByNameAndIdNot(name, id);
        }
    }

    /**
     * 将实体类转换为DTO
     * @param equipmentType 设备类型实体
     * @return 设备类型DTO
     */
    private EquipmentTypeDTO convertToDTO(EquipmentType equipmentType) {
        EquipmentTypeDTO dto = new EquipmentTypeDTO();
        BeanUtils.copyProperties(equipmentType, dto);
        return dto;
    }

    /**
     * 将分页实体转换为分页DTO
     * @param page 分页实体
     * @return 分页DTO
     */
    private PageResultDTO<EquipmentTypeDTO> convertToPageResultDTO(Page<EquipmentType> page) {
        PageResultDTO<EquipmentTypeDTO> pageResult = new PageResultDTO<>();
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