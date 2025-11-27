package com.gmp.hr.repository;

import com.gmp.hr.entity.Qualification;
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
 * 资质数据访问接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Repository
public interface QualificationRepository extends JpaRepository<Qualification, Long>, JpaSpecificationExecutor<Qualification> {
    
    /**
     * 根据证书编号查找资质
     * 
     * @param certificateNumber 证书编号
     * @return 资质信息
     */
    Optional<Qualification> findByCertificateNumberAndDeletedFalse(String certificateNumber);
    
    /**
     * 根据员工ID查找资质列表
     * 
     * @param employeeId 员工ID
     * @return 资质列表
     */
    List<Qualification> findByEmployeeIdAndDeletedFalse(Long employeeId);
    
    /**
     * 根据资质类型ID查找资质列表
     * 
     * @param qualificationTypeId 资质类型ID
     * @return 资质列表
     */
    List<Qualification> findByQualificationTypeIdAndDeletedFalse(Long qualificationTypeId);
    
    /**
     * 查找即将过期的资质（30天内）
     * 
     * @param date 截止日期
     * @return 资质列表
     */
    @Query("SELECT q FROM Qualification q WHERE q.expiryDate IS NOT NULL AND q.expiryDate BETWEEN CURRENT_DATE AND :date AND q.deleted = false")
    List<Qualification> findExpiringQualifications(LocalDate date);
    
    /**
     * 根据员工ID和资质类型ID查找资质
     * 
     * @param employeeId 员工ID
     * @param qualificationTypeId 资质类型ID
     * @return 资质列表
     */
    List<Qualification> findByEmployeeIdAndQualificationTypeIdAndDeletedFalse(Long employeeId, Long qualificationTypeId);
    
    /**
     * 软删除资质
     * 
     * @param id 资质ID
     * @return 更新记录数
     */
    @Modifying
    @Query("UPDATE Qualification q SET q.deleted = true WHERE q.id = :id")
    int softDeleteById(Long id);
}