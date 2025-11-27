package com.gmp.hr.service;

import com.gmp.hr.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工服务接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public interface EmployeeService {
    
    /**
     * 创建员工
     * 
     * @param employeeDTO 员工DTO
     * @return 创建的员工DTO
     */
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    
    /**
     * 根据ID获取员工
     * 
     * @param id 员工ID
     * @return 员工DTO
     */
    EmployeeDTO getEmployeeById(Long id);
    
    /**
     * 根据员工工号获取员工
     * 
     * @param employeeCode 员工工号
     * @return 员工DTO
     */
    EmployeeDTO getEmployeeByCode(String employeeCode);
    
    /**
     * 更新员工信息
     * 
     * @param id 员工ID
     * @param employeeDTO 员工DTO
     * @return 更新后的员工DTO
     */
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    
    /**
     * 删除员工（软删除）
     * 
     * @param id 员工ID
     */
    void deleteEmployee(Long id);
    
    /**
     * 分页查询员工列表
     * 
     * @param pageable 分页参数
     * @return 分页员工列表
     */
    Page<EmployeeDTO> getEmployees(Pageable pageable);
    
    /**
     * 根据部门ID查询员工列表
     * 
     * @param departmentId 部门ID
     * @return 员工列表
     */
    List<EmployeeDTO> getEmployeesByDepartment(Long departmentId);
    
    /**
     * 根据职位ID查询员工列表
     * 
     * @param positionId 职位ID
     * @return 员工列表
     */
    List<EmployeeDTO> getEmployeesByPosition(Long positionId);
    
    /**
     * 查询指定日期范围内入职的员工
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 员工列表
     */
    List<EmployeeDTO> getEmployeesByHireDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * 查询即将离职的员工（30天内）
     * 
     * @return 员工列表
     */
    List<EmployeeDTO> getEmployeesWithUpcomingDeparture();
}
