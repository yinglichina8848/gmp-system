package com.gmp.hr.service;

import com.gmp.hr.dto.QualificationDTO;

import java.util.Date;
import java.util.List;

/**
 * 资质服务接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public interface QualificationService {
    
    /**
     * 创建员工资质
     * 
     * @param qualificationDTO 资质DTO
     * @return 创建的资质DTO
     */
    QualificationDTO createQualification(QualificationDTO qualificationDTO);
    
    /**
     * 根据ID获取资质
     * 
     * @param id 资质ID
     * @return 资质DTO
     */
    QualificationDTO getQualificationById(Long id);
    
    /**
     * 根据证书编号获取资质
     * 
     * @param certificateNumber 证书编号
     * @return 资质DTO
     */
    QualificationDTO getQualificationByCertificateNumber(String certificateNumber);
    
    /**
     * 更新资质
     * 
     * @param id 资质ID
     * @param qualificationDTO 资质DTO
     * @return 更新后的资质DTO
     */
    QualificationDTO updateQualification(Long id, QualificationDTO qualificationDTO);
    
    /**
     * 删除资质
     * 
     * @param id 资质ID
     */
    void deleteQualification(Long id);
    
    /**
     * 获取所有资质
     * 
     * @return 资质DTO列表
     */
    List<QualificationDTO> getAllQualifications();
    
    /**
     * 根据员工ID获取资质列表
     * 
     * @param employeeId 员工ID
     * @return 资质DTO列表
     */
    List<QualificationDTO> getQualificationsByEmployee(Long employeeId);
    
    /**
     * 根据资质类型ID获取资质列表
     * 
     * @param qualificationTypeId 资质类型ID
     * @return 资质DTO列表
     */
    List<QualificationDTO> getQualificationsByType(Long qualificationTypeId);
    
    /**
     * 获取即将过期的资质
     * 
     * @param daysThreshold 过期天数阈值
     * @return 资质DTO列表
     */
    List<QualificationDTO> getExpiringQualifications(int daysThreshold);
    
    /**
     * 获取已过期的资质
     * 
     * @return 资质DTO列表
     */
    List<QualificationDTO> getExpiredQualifications();
    
    /**
     * 根据员工ID和资质类型ID获取资质
     * 
     * @param employeeId 员工ID
     * @param qualificationTypeId 资质类型ID
     * @return 资质DTO
     */
    QualificationDTO getQualificationByEmployeeAndType(Long employeeId, Long qualificationTypeId);
    
    /**
     * 根据日期范围查询资质
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 资质DTO列表
     */
    List<QualificationDTO> getQualificationsByDateRange(Date startDate, Date endDate);
}