package com.gmp.equipment.service.impl;

import com.gmp.equipment.dto.MaintenancePlanDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.entity.MaintenancePlan;
import com.gmp.equipment.repository.MaintenancePlanRepository;
import com.gmp.equipment.service.MaintenancePlanService;
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
 * 设备维护计划服务实现类
 * 实现维护计划相关的业务逻辑
 */
@Service
public class MaintenancePlanServiceImpl implements MaintenancePlanService {

    @Autowired
    private MaintenancePlanRepository maintenancePlanRepository;

    @Override
    public MaintenancePlanDTO getById(Long id) {
        MaintenancePlan maintenancePlan = maintenancePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维护计划不存在: " + id));
        return convertToDTO(maintenancePlan);
    }

    @Override
    public MaintenancePlanDTO getByPlanNumber(String planNumber) {
        MaintenancePlan maintenancePlan = maintenancePlanRepository.findByPlanNumber(planNumber)
                .orElseThrow(() -> new RuntimeException("维护计划不存在: " + planNumber));
        return convertToDTO(maintenancePlan);
    }

    @Override
    public PageResultDTO<MaintenancePlanDTO> listPage(Long equipmentId, Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNum - 1, 
                pageSize,
                Sort.by(Sort.Direction.DESC, "planDate")
        );
        
        Page<MaintenancePlan> page;
        if (equipmentId != null) {
            page = maintenancePlanRepository.findByEquipmentId(equipmentId, pageRequest);
        } else {
            page = maintenancePlanRepository.findAll(pageRequest);
        }
        
        return convertToPageResultDTO(page);
    }

    @Override
    public List<MaintenancePlanDTO> listByEquipmentId(Long equipmentId) {
        List<MaintenancePlan> plans = maintenancePlanRepository.findByEquipmentIdOrderByPlanDateDesc(equipmentId);
        return plans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MaintenancePlanDTO> listByDateRange(Date startDate, Date endDate) {
        List<MaintenancePlan> plans = maintenancePlanRepository.findByPlanDateBetween(startDate, endDate);
        return plans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MaintenancePlanDTO> listByStatus(String status) {
        List<MaintenancePlan> plans = maintenancePlanRepository.findByStatus(status);
        return plans.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public MaintenancePlanDTO getLatestByEquipmentId(Long equipmentId) {
        MaintenancePlan plan = maintenancePlanRepository.findTopByEquipmentIdOrderByPlanDateDesc(equipmentId)
                .orElse(null);
        return plan != null ? convertToDTO(plan) : null;
    }

    @Override
    @Transactional
    public MaintenancePlanDTO create(MaintenancePlanDTO maintenancePlanDTO) {
        // 检查计划编号是否已存在
        if (existsByPlanNumber(maintenancePlanDTO.getPlanNumber(), null)) {
            throw new RuntimeException("计划编号已存在: " + maintenancePlanDTO.getPlanNumber());
        }

        MaintenancePlan maintenancePlan = new MaintenancePlan();
        BeanUtils.copyProperties(maintenancePlanDTO, maintenancePlan);
        maintenancePlan.setCreatedBy(maintenancePlanDTO.getCreatedBy() != null ? maintenancePlanDTO.getCreatedBy() : "system");
        maintenancePlan.setUpdatedBy(maintenancePlanDTO.getUpdatedBy() != null ? maintenancePlanDTO.getUpdatedBy() : "system");

        MaintenancePlan saved = maintenancePlanRepository.save(maintenancePlan);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public MaintenancePlanDTO update(Long id, MaintenancePlanDTO maintenancePlanDTO) {
        MaintenancePlan maintenancePlan = maintenancePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维护计划不存在: " + id));

        // 检查计划编号是否已存在（排除当前记录）
        if (existsByPlanNumber(maintenancePlanDTO.getPlanNumber(), id)) {
            throw new RuntimeException("计划编号已存在: " + maintenancePlanDTO.getPlanNumber());
        }

        BeanUtils.copyProperties(maintenancePlanDTO, maintenancePlan);
        maintenancePlan.setId(id);
        maintenancePlan.setUpdatedBy(maintenancePlanDTO.getUpdatedBy() != null ? maintenancePlanDTO.getUpdatedBy() : "system");

        MaintenancePlan updated = maintenancePlanRepository.save(maintenancePlan);
        return convertToDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MaintenancePlan maintenancePlan = maintenancePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维护计划不存在: " + id));
        maintenancePlanRepository.delete(maintenancePlan);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        List<MaintenancePlan> plans = maintenancePlanRepository.findAllById(ids);
        if (plans.size() != ids.size()) {
            throw new RuntimeException("部分维护计划不存在");
        }
        maintenancePlanRepository.deleteAll(plans);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String status) {
        MaintenancePlan maintenancePlan = maintenancePlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("维护计划不存在: " + id));
        maintenancePlan.setStatus(status);
        maintenancePlan.setUpdatedBy("system");
        maintenancePlanRepository.save(maintenancePlan);
    }

    @Override
    public boolean existsByPlanNumber(String planNumber, Long id) {
        if (id == null) {
            return maintenancePlanRepository.findByPlanNumber(planNumber).isPresent();
        } else {
            return maintenancePlanRepository.existsByPlanNumberAndIdNot(planNumber, id);
        }
    }

    /**
     * 将实体类转换为DTO
     * @param maintenancePlan 维护计划实体
     * @return 维护计划DTO
     */
    private MaintenancePlanDTO convertToDTO(MaintenancePlan maintenancePlan) {
        MaintenancePlanDTO dto = new MaintenancePlanDTO();
        BeanUtils.copyProperties(maintenancePlan, dto);
        return dto;
    }

    /**
     * 将分页实体转换为分页DTO
     * @param page 分页实体
     * @return 分页DTO
     */
    private PageResultDTO<MaintenancePlanDTO> convertToPageResultDTO(Page<MaintenancePlan> page) {
        PageResultDTO<MaintenancePlanDTO> pageResult = new PageResultDTO<>();
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