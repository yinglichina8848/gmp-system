package com.gmp.hr.repository;

import com.gmp.hr.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 员工数据访问接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    
    /**
     * 根据员工工号查找员工
     * 
     * @param employeeCode 员工工号
     * @return 员工信息
     */
    Optional<Employee> findByEmployeeCodeAndDeletedFalse(String employeeCode);
    
    /**
     * 根据邮箱查找员工
     * 
     * @param email 邮箱
     * @return 员工信息
     */
    Optional<Employee> findByEmailAndDeletedFalse(String email);
    
    /**
     * 根据身份证号查找员工
     * 
     * @param idCard 身份证号
     * @return 员工信息
     */
    Optional<Employee> findByIdCardAndDeletedFalse(String idCard);
    
    /**
     * 根据部门ID查找员工列表
     * 
     * @param departmentId 部门ID
     * @return 员工列表
     */
    List<Employee> findByDepartmentIdAndDeletedFalse(Long departmentId);
    
    /**
     * 根据职位ID查找员工列表
     * 
     * @param positionId 职位ID
     * @return 员工列表
     */
    List<Employee> findByPositionIdAndDeletedFalse(Long positionId);
    
    /**
     * 根据员工状态查找员工列表
     * 
     * @param status 员工状态
     * @param pageable 分页参数
     * @return 分页员工列表
     */
    Page<Employee> findByStatusAndDeletedFalse(String status, Pageable pageable);
    
    /**
     * 查找在指定日期范围内入职的员工
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 员工列表
     */
    List<Employee> findByHireDateBetweenAndDeletedFalse(LocalDate startDate, LocalDate endDate);
    
    /**
     * 查找即将过期的员工合同（30天内）
     * 
     * @param date 截止日期
     * @return 员工列表
     */
    @Query("SELECT e FROM Employee e WHERE e.departureDate IS NOT NULL AND e.departureDate BETWEEN CURRENT_DATE AND :date AND e.deleted = false")
    List<Employee> findEmployeesWithUpcomingDeparture(LocalDate date);
    
    /**
     * 软删除员工
     * 
     * @param id 员工ID
     * @return 更新记录数
     */
    @Modifying
    @Query("UPDATE Employee e SET e.deleted = true WHERE e.id = :id")
    int softDeleteById(Long id);
}