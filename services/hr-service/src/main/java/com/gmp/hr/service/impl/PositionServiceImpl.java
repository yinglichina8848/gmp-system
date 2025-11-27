package com.gmp.hr.service.impl;

import com.gmp.hr.dto.PositionDTO;
import com.gmp.hr.entity.Department;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.entity.Position;
import com.gmp.hr.repository.DepartmentRepository;
import com.gmp.hr.repository.EmployeeRepository;
import com.gmp.hr.repository.PositionRepository;
import com.gmp.hr.service.PositionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 职位服务实现类
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository,
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository) {
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public PositionDTO createPosition(PositionDTO positionDTO) {
        // 验证职位代码唯一性
        Optional<Position> existingPosition = positionRepository
                .findByPositionCodeAndDeletedFalse(positionDTO.getPositionCode());
        if (existingPosition.isPresent()) {
            throw new RuntimeException("职位代码已存在");
        }

        // 验证必填字段
        if (positionDTO.getPositionName() == null || positionDTO.getPositionName().trim().isEmpty()) {
            throw new RuntimeException("职位名称不能为空");
        }

        // 验证关联的部门是否存在
        if (positionDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(positionDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("部门不存在"));

            if (Boolean.TRUE.equals(department.getDeleted())) {
                throw new RuntimeException("部门不存在");
            }
        }

        // 创建实体
        Position position = new Position();
        position.setPositionCode(positionDTO.getPositionCode());
        position.setPositionName(positionDTO.getPositionName());
        position.setDescription(positionDTO.getDescription());
        position.setLevel(positionDTO.getLevel());
        // 移除状态设置，因为Position实体没有status字段

        if (positionDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(positionDTO.getDepartmentId()).get();
            position.setDepartment(department);
        }

        // 设置默认值
        position.setDeleted(false);

        // 保存职位
        Position savedPosition = positionRepository.save(position);

        // 转换为DTO并设置员工数量
        return convertToDTO(savedPosition);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionDTO getPositionById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("职位不存在: " + id));

        // 验证职位是否已被删除
        if (Boolean.TRUE.equals(position.getDeleted())) {
            throw new RuntimeException("职位已被删除: " + id);
        }

        return convertToDTO(position);
    }

    @Override
    @Transactional(readOnly = true)
    public PositionDTO getPositionByCode(String positionCode) {
        Position position = positionRepository.findByPositionCodeAndDeletedFalse(positionCode)
                .orElseThrow(() -> new RuntimeException("职位不存在"));

        return convertToDTO(position);
    }

    @Override
    @Transactional
    public PositionDTO updatePosition(Long id, PositionDTO positionDTO) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("职位不存在"));

        if (Boolean.TRUE.equals(position.getDeleted())) {
            throw new RuntimeException("职位不存在");
        }

        // 验证职位代码唯一性（如果有修改）
        if (!position.getPositionCode().equals(positionDTO.getPositionCode())) {
            Optional<Position> existingPosition = positionRepository
                    .findByPositionCodeAndDeletedFalse(positionDTO.getPositionCode());
            if (existingPosition.isPresent()) {
                throw new RuntimeException("职位代码已存在");
            }
        }

        // 验证必填字段
        if (positionDTO.getPositionName() == null || positionDTO.getPositionName().trim().isEmpty()) {
            throw new RuntimeException("职位名称不能为空");
        }

        // 验证关联的部门是否存在
        if (positionDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(positionDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("部门不存在"));

            if (Boolean.TRUE.equals(department.getDeleted())) {
                throw new RuntimeException("部门不存在");
            }
        }

        // 更新基本信息
        position.setPositionCode(positionDTO.getPositionCode());
        position.setPositionName(positionDTO.getPositionName());
        position.setDescription(positionDTO.getDescription());
        position.setLevel(positionDTO.getLevel());
        // 移除状态设置，因为Position实体没有status字段

        if (positionDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(positionDTO.getDepartmentId()).get();
            position.setDepartment(department);
        } else {
            position.setDepartment(null);
        }

        // 保存更新
        Position updatedPosition = positionRepository.save(position);

        return convertToDTO(updatedPosition);
    }

    @Override
    @Transactional
    public void deletePosition(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("职位不存在"));

        if (Boolean.TRUE.equals(position.getDeleted())) {
            throw new RuntimeException("职位不存在");
        }

        // 检查是否有员工持有该职位
        List<Employee> employees = employeeRepository.findByPositionIdAndDeletedFalse(id);
        if (!employees.isEmpty()) {
            throw new RuntimeException("该职位下还有员工，无法删除");
        }

        // 软删除
        position.setDeleted(true);
        positionRepository.save(position);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionDTO> getAllPositions() {
        List<Position> positions = positionRepository.findAll();
        return positions.stream()
                .filter(pos -> Boolean.FALSE.equals(pos.getDeleted()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionDTO> getPositionsByDepartment(Long departmentId) {
        List<Position> positions = positionRepository.findByDepartmentIdAndDeletedFalse(departmentId);
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionDTO> getPositionsByLevel(Integer level) {
        // 查询指定级别的职位
        List<Position> positions = positionRepository.findAll();
        return positions.stream()
                .filter(pos -> Boolean.FALSE.equals(pos.getDeleted()) && level.equals(pos.getLevel()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PositionDTO> getPositionsByStatus(String status) {
        // 注意：Position实体没有status字段，仅返回未删除的职位
        List<Position> positions = positionRepository.findAll();
        return positions.stream()
                .filter(pos -> Boolean.FALSE.equals(pos.getDeleted()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将职位实体转换为DTO，并设置员工数量
     * 
     * @param position 职位实体
     * @return 职位DTO
     */
    private PositionDTO convertToDTO(Position position) {
        PositionDTO dto = new PositionDTO();
        dto.setId(position.getId());
        dto.setPositionCode(position.getPositionCode());
        dto.setPositionName(position.getPositionName());
        // 设置部门信息
        if (position.getDepartment() != null) {
            dto.setDepartmentId(position.getDepartment().getId());
            dto.setDepartmentName(position.getDepartment().getDepartmentName());
        }
        dto.setDescription(position.getDescription());
        dto.setLevel(position.getLevel());
        // 使用DTO中实际存在的时间戳方法
        dto.setCreatedAt(position.getCreatedAt());
        dto.setCreatedBy(position.getCreatedBy());
        dto.setUpdatedAt(position.getUpdatedAt());
        dto.setUpdatedBy(position.getUpdatedBy());

        // 查询员工数量
        dto.setEmployeeCount(employeeRepository.findAll().size()); // 使用通用查询方法

        return dto;
    }
}