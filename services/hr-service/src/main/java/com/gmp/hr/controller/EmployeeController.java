package com.gmp.hr.controller;

import com.gmp.hr.dto.EmployeeDTO;
import com.gmp.hr.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 员工管理控制器
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/hr/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    /**
     * 创建员工
     * 
     * @param employeeDTO 员工DTO
     * @return 创建的员工DTO
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
    
    /**
     * 根据ID获取员工
     * 
     * @param id 员工ID
     * @return 员工DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employeeDTO);
    }
    
    /**
     * 根据工号获取员工
     * 
     * @param employeeCode 工号
     * @return 员工DTO
     */
    @GetMapping("/code/{employeeCode}")
    public ResponseEntity<EmployeeDTO> getEmployeeByCode(@PathVariable String employeeCode) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeByCode(employeeCode);
        return ResponseEntity.ok(employeeDTO);
    }
    
    /**
     * 更新员工
     * 
     * @param id 员工ID
     * @param employeeDTO 员工DTO
     * @return 更新后的员工DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    /**
     * 删除员工（软删除）
     * 
     * @param id 员工ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 获取所有员工
     * 
     * @return 员工DTO列表
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
    
    /**
     * 根据部门获取员工列表
     * 
     * @param departmentId 部门ID
     * @return 员工DTO列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable Long departmentId) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByDepartment(departmentId);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * 根据职位获取员工列表
     * 
     * @param positionId 职位ID
     * @return 员工DTO列表
     */
    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPosition(@PathVariable Long positionId) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByPosition(positionId);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * 根据入职日期范围查询员工
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 员工DTO列表
     */
    @GetMapping("/hire-date-range")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByHireDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByHireDateRange(startDate, endDate);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * 分页查询员工
     * 
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @return 员工DTO列表
     */
    @GetMapping("/page")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByPage(page, size);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * 查询即将离职的员工
     * 
     * @param daysThreshold 离职天数阈值
     * @return 员工DTO列表
     */
    @GetMapping("/approaching-resignation")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesApproachingResignation(
            @RequestParam(defaultValue = "30") int daysThreshold) {
        List<EmployeeDTO> employees = employeeService.getEmployeesApproachingResignation(daysThreshold);
        return ResponseEntity.ok(employees);
    }
    
    /**
     * 根据状态查询员工
     * 
     * @param status 员工状态
     * @return 员工DTO列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByStatus(@PathVariable String status) {
        List<EmployeeDTO> employees = employeeService.getEmployeesByStatus(status);
        return ResponseEntity.ok(employees);
    }
}