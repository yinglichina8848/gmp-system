package com.gmp.hr.service;

import com.gmp.hr.dto.QualificationTypeDTO;

import java.util.List;

/**
 * 资质类型服务接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public interface QualificationTypeService {
    
    /**
     * 创建资质类型
     * 
     * @param qualificationTypeDTO 资质类型DTO
     * @return 创建的资质类型DTO
     */
    QualificationTypeDTO createQualificationType(QualificationTypeDTO qualificationTypeDTO);
    
    /**
     * 根据ID获取资质类型
     * 
     * @param id 资质类型ID
     * @return 资质类型DTO
     */
    QualificationTypeDTO getQualificationTypeById(Long id);
    
    /**
     * 根据资质代码获取资质类型
     * 
     * @param typeCode 资质代码
     * @return 资质类型DTO
     */
    QualificationTypeDTO getQualificationTypeByCode(String typeCode);
    
    /**
     * 更新资质类型
     * 
     * @param id 资质类型ID
     * @param qualificationTypeDTO 资质类型DTO
     * @return 更新后的资质类型DTO
     */
    QualificationTypeDTO updateQualificationType(Long id, QualificationTypeDTO qualificationTypeDTO);
    
    /**
     * 删除资质类型
     * 
     * @param id 资质类型ID
     */
    void deleteQualificationType(Long id);
    
    /**
     * 获取所有资质类型
     * 
     * @return 资质类型DTO列表
     */
    List<QualificationTypeDTO> getAllQualificationTypes();
    
    /**
     * 根据资质名称查询资质类型
     * 
     * @param typeName 资质名称
     * @return 资质类型DTO列表
     */
    List<QualificationTypeDTO> getQualificationTypesByName(String typeName);
    
    /**
     * 获取需要定期更新的资质类型
     * 
     * @return 资质类型DTO列表
     */
    List<QualificationTypeDTO> getQualificationTypesNeedUpdate();
}