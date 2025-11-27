package com.gmp.hr.service;

import com.gmp.hr.dto.DepartmentDTO;

import java.util.List;

/**
 * 部门服务接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public interface DepartmentService {
    
    /**
     * 创建部门
     * 
     * @param departmentDTO 部门DTO
     * @return 创建的部门DTO
     */
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    
    /**
     * 根据ID获取部门
     * 
     * @param id 部门ID
     * @return 部门DTO
     */
    DepartmentDTO getDepartmentById(Long id);
    
    /**
     * 根据部门代码获取部门
     * 
     * @param departmentCode 部门代码
     * @return 部门DTO
     */
    DepartmentDTO getDepartmentByCode(String departmentCode);
    
    /**
     * 更新部门信息
     * 
     * @param id 部门ID
     * @param departmentDTO 部门DTO
     * @return 更新后的部门DTO
     */
    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);
    
    /**
     * 删除部门（软删除）
     * 
     * @param id 部门ID
     */
    void deleteDepartment(Long id);
    
    /**
     * 获取所有部门列表
     * 
     * @return 部门列表
     */
    List<DepartmentDTO> getAllDepartments();
    
    /**
     * 获取所有顶级部门
     * 
     * @return 顶级部门列表
     */
    List<DepartmentDTO> getTopDepartments();
    
    /**
     * 根据父部门ID获取子部门
     * 
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<DepartmentDTO> getChildDepartments(Long parentId);
    
    /**
     * 获取部门树结构
     * 
     * @return 部门树结构
     */
    List<DepartmentDTO> getDepartmentTree();
    
    /**
     * 根据部门经理ID获取部门
     * 
     * @param managerId 部门经理ID
     * @return 部门DTO
     */
    DepartmentDTO getDepartmentByManager(Long managerId);
}