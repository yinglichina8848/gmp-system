package com.gmp.hr.controller;

import com.gmp.hr.dto.DepartmentDTO;
import com.gmp.hr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理控制器
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/hr/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 创建部门
     * 
     * @param departmentDTO 部门DTO
     * @return 创建的部门DTO
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }
    
    /**
     * 根据ID获取部门
     * 
     * @param id 部门ID
     * @return 部门DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(departmentDTO);
    }
    
    /**
     * 根据代码获取部门
     * 
     * @param code 部门代码
     * @return 部门DTO
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<DepartmentDTO> getDepartmentByCode(@PathVariable String code) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentByCode(code);
        return ResponseEntity.ok(departmentDTO);
    }
    
    /**
     * 更新部门
     * 
     * @param id 部门ID
     * @param departmentDTO 部门DTO
     * @return 更新后的部门DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(id, departmentDTO);
        return ResponseEntity.ok(updatedDepartment);
    }
    
    /**
     * 删除部门（软删除）
     * 
     * @param id 部门ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 获取所有部门
     * 
     * @return 部门DTO列表
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }
    
    /**
     * 获取顶级部门
     * 
     * @return 部门DTO列表
     */
    @GetMapping("/top")
    public ResponseEntity<List<DepartmentDTO>> getTopDepartments() {
        List<DepartmentDTO> departments = departmentService.getTopDepartments();
        return ResponseEntity.ok(departments);
    }
    
    /**
     * 根据父部门获取子部门
     * 
     * @param parentId 父部门ID
     * @return 部门DTO列表
     */
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<DepartmentDTO>> getChildDepartments(@PathVariable Long parentId) {
        List<DepartmentDTO> departments = departmentService.getChildDepartments(parentId);
        return ResponseEntity.ok(departments);
    }
    
    /**
     * 获取部门树结构
     * 
     * @return 部门DTO树结构
     */
    @GetMapping("/tree")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentTree() {
        List<DepartmentDTO> departmentTree = departmentService.getDepartmentTree();
        return ResponseEntity.ok(departmentTree);
    }
    
    /**
     * 根据经理ID获取部门
     * 
     * @param managerId 经理ID
     * @return 部门DTO
     */
    @GetMapping("/manager/{managerId}")
    public ResponseEntity<DepartmentDTO> getDepartmentByManager(@PathVariable Long managerId) {
        DepartmentDTO departmentDTO = departmentService.getDepartmentByManager(managerId);
        return ResponseEntity.ok(departmentDTO);
    }
}