package com.gmp.hr.repository;

import com.gmp.hr.entity.QualificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 资质类型数据访问接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Repository
public interface QualificationTypeRepository extends JpaRepository<QualificationType, Long>, JpaSpecificationExecutor<QualificationType> {
    
    /**
     * 根据资质代码查找资质类型
     * 
     * @param code 资质代码
     * @return 资质类型信息
     */
    Optional<QualificationType> findByCodeAndDeletedFalse(String code);
    
    /**
     * 根据资质名称查找资质类型
     * 
     * @param name 资质名称
     * @return 资质类型信息
     */
    Optional<QualificationType> findByNameAndDeletedFalse(String name);
    
    /**
     * 查找所有需要更新的资质类型
     * 
     * @param needsUpdate 是否需要更新
     * @return 资质类型列表
     */
    List<QualificationType> findByNeedsUpdateAndDeletedFalse(Boolean needsUpdate);
    
    /**
     * 软删除资质类型
     * 
     * @param id 资质类型ID
     * @return 更新记录数
     */
    @Modifying
    @Query("UPDATE QualificationType q SET q.deleted = true WHERE q.id = :id")
    int softDeleteById(Long id);
}