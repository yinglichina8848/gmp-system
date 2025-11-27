package com.gmp.hr.service.impl;

import com.gmp.hr.dto.DepartmentDTO;
import com.gmp.hr.entity.Department;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.mapper.DepartmentMapper;
import com.gmp.hr.repository.DepartmentRepository;
import com.gmp.hr.repository.EmployeeRepository;
import com.gmp.hr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;
    
    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository,
                               EmployeeRepository employeeRepository,
                               DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentMapper = departmentMapper;
    }
    
    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        // 验证部门代码唯一性
        Optional<Department> existingDepartment = departmentRepository.findByDepartmentCodeAndDeletedFalse(departmentDTO.getDepartmentCode());
        if (existingDepartment.isPresent()) {
            throw new RuntimeException("部门代码已存在");
        }
        
        // 验证部门名称唯一性
        Optional<Department> existingName = departmentRepository.findByDepartmentNameAndDeletedFalse(departmentDTO.getDepartmentName());
        if (existingName.isPresent()) {
            throw new RuntimeException("部门名称已存在");
        }
        
        // 创建实体
        Department department = departmentMapper.toEntity(departmentDTO);
        
        // 设置关联关系
        if (departmentDTO.getParentId() != null) {
            Department parent = departmentRepository.findById(departmentDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("父部门不存在"));
            
            // 检查是否会形成循环引用
            if (isCircularReference(parent, departmentDTO.getParentId())) {
                throw new RuntimeException("不能设置为自己或子部门的父部门");
            }
            
            department.setParent(parent);
        }
        
        if (departmentDTO.getManagerId() != null) {
            Employee manager = employeeRepository.findById(departmentDTO.getManagerId())
                    .orElseThrow(() -> new RuntimeException("部门经理不存在"));
            department.setManager(manager);
        }
        
        // 设置默认值
        department.setDeleted(false);
        
        // 保存部门
        Department savedDepartment = departmentRepository.save(department);
        
        return departmentMapper.toDTO(savedDepartment);
    }
    
    /**
     * 检查是否会形成循环引用
     * 
     * @param department 部门
     * @param targetId 目标部门ID
     * @return 是否形成循环引用
     */
    private boolean isCircularReference(Department department, Long targetId) {
        if (department.getId().equals(targetId)) {
            return true;
        }
        if (department.getParent() != null) {
            return isCircularReference(department.getParent(), targetId);
        }
        return false;
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("部门不存在"));
        
        if (Boolean.TRUE.equals(department.getDeleted())) {
            throw new RuntimeException("部门不存在");
        }
        
        DepartmentDTO dto = departmentMapper.toDTO(department);
        // 设置员工数量
        dto.setEmployeeCount(employeeRepository.findByDepartmentIdAndDeletedFalse(id).size());
        
        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByCode(String departmentCode) {
        Department department = departmentRepository.findByDepartmentCodeAndDeletedFalse(departmentCode)
                .orElseThrow(() -> new RuntimeException("部门不存在"));
        
        DepartmentDTO dto = departmentMapper.toDTO(department);
        // 设置员工数量
        dto.setEmployeeCount(employeeRepository.findByDepartmentIdAndDeletedFalse(department.getId()).size());
        
        return dto;
    }
    
    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("部门不存在"));
        
        if (Boolean.TRUE.equals(department.getDeleted())) {
            throw new RuntimeException("部门不存在");
        }
        
        // 验证部门代码唯一性（如果有修改）
        if (!department.getDepartmentCode().equals(departmentDTO.getDepartmentCode())) {
            Optional<Department> existingDepartment = departmentRepository.findByDepartmentCodeAndDeletedFalse(departmentDTO.getDepartmentCode());
            if (existingDepartment.isPresent()) {
                throw new RuntimeException("部门代码已存在");
            }
        }
        
        // 验证部门名称唯一性（如果有修改）
        if (!department.getDepartmentName().equals(departmentDTO.getDepartmentName())) {
            Optional<Department> existingName = departmentRepository.findByDepartmentNameAndDeletedFalse(departmentDTO.getDepartmentName());
            if (existingName.isPresent()) {
                throw new RuntimeException("部门名称已存在");
            }
        }
        
        // 更新基本信息
        departmentMapper.updateEntityFromDTO(departmentDTO, department);
        
        // 更新关联关系
        if (departmentDTO.getParentId() != null) {
            Department parent = departmentRepository.findById(departmentDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("父部门不存在"));
            
            // 检查是否会形成循环引用
            if (isCircularReference(parent, id)) {
                throw new RuntimeException("不能设置为自己或子部门的父部门");
            }
            
            department.setParent(parent);
        } else {
            department.setParent(null);
        }
        
        if (departmentDTO.getManagerId() != null) {
            Employee manager = employeeRepository.findById(departmentDTO.getManagerId())
                    .orElseThrow(() -> new RuntimeException("部门经理不存在"));
            department.setManager(manager);
        } else {
            department.setManager(null);
        }
        
        // 保存更新
        Department updatedDepartment = departmentRepository.save(department);
        
        DepartmentDTO dto = departmentMapper.toDTO(updatedDepartment);
        // 设置员工数量
        dto.setEmployeeCount(employeeRepository.findByDepartmentIdAndDeletedFalse(id).size());
        
        return dto;
    }
    
    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("部门不存在"));
        
        if (Boolean.TRUE.equals(department.getDeleted())) {
            throw new RuntimeException("部门不存在");
        }
        
        // 检查是否有子部门
        List<Department> childDepartments = departmentRepository.findByParentIdAndDeletedFalse(id);
        if (!childDepartments.isEmpty()) {
            throw new RuntimeException("该部门下还有子部门，无法删除");
        }
        
        // 检查是否有员工
        List<Employee> employees = employeeRepository.findByDepartmentIdAndDeletedFalse(id);
        if (!employees.isEmpty()) {
            throw new RuntimeException("该部门下还有员工，无法删除");
        }
        
        // 软删除
        department.setDeleted(true);
        departmentRepository.save(department);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .filter(dept -> Boolean.FALSE.equals(dept.getDeleted()))
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getTopDepartments() {
        List<Department> departments = departmentRepository.findByParentIsNullAndDeletedFalse();
        return departments.stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getChildDepartments(Long parentId) {
        List<Department> departments = departmentRepository.findByParentIdAndDeletedFalse(parentId);
        return departments.stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDTO> getDepartmentTree() {
        // 获取所有顶级部门
        List<Department> topDepartments = departmentRepository.findByParentIsNullAndDeletedFalse();
        List<DepartmentDTO> departmentTree = new ArrayList<>();
        
        // 构建部门树
        for (Department dept : topDepartments) {
            DepartmentDTO dto = buildDepartmentTree(dept);
            departmentTree.add(dto);
        }
        
        return departmentTree;
    }
    
    /**
     * 递归构建部门树
     * 
     * @param department 部门实体
     * @return 部门DTO树节点
     */
    private DepartmentDTO buildDepartmentTree(Department department) {
        DepartmentDTO dto = departmentMapper.toDTO(department);
        // 设置员工数量
        dto.setEmployeeCount(employeeRepository.findByDepartmentIdAndDeletedFalse(department.getId()).size());
        
        // 递归获取子部门
        List<Department> childDepartments = departmentRepository.findByParentIdAndDeletedFalse(department.getId());
        if (!childDepartments.isEmpty()) {
            List<DepartmentDTO> childDTOs = new ArrayList<>();
            for (Department child : childDepartments) {
                DepartmentDTO childDTO = buildDepartmentTree(child);
                childDTOs.add(childDTO);
            }
            dto.setChildren(childDTOs);
        }
        
        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentDTO getDepartmentByManager(Long managerId) {
        Department department = departmentRepository.findByManagerIdAndDeletedFalse(managerId)
                .orElseThrow(() -> new RuntimeException("该员工不是任何部门的经理"));
        
        DepartmentDTO dto = departmentMapper.toDTO(department);
        // 设置员工数量
        dto.setEmployeeCount(employeeRepository.findByDepartmentIdAndDeletedFalse(department.getId()).size());
        
        return dto;
    }
}