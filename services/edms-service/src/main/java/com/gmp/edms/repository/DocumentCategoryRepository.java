package com.gmp.edms.repository;

import com.gmp.edms.entity.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 文档分类Repository接口
 */
@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Long>, JpaSpecificationExecutor<DocumentCategory> {
    
    /**
     * 根据分类编码查询分类
     */
    Optional<DocumentCategory> findByCategoryCode(String categoryCode);
    
    /**
     * 查询根分类列表
     */
    List<DocumentCategory> findByParentIdIsNull();
    
    /**
     * 查询子分类列表
     */
    List<DocumentCategory> findByParentId(Long parentId);
    
    /**
     * 根据状态查询分类列表
     */
    List<DocumentCategory> findByStatus(String status);
    
    /**
     * 根据层级查询分类
     */
    List<DocumentCategory> findByLevel(Integer level);
    
    /**
     * 检查分类编码是否存在
     */
    boolean existsByCategoryCode(String categoryCode);
    
    /**
     * 查询除了指定ID外的分类编码
     */
    boolean existsByCategoryCodeAndIdNot(String categoryCode, Long id);
    
    /**
     * 查询分类路径
     */
    @Query("SELECT d FROM DocumentCategory d WHERE d.id IN :categoryIds ORDER BY d.level")
    List<DocumentCategory> findCategoryPath(@Param("categoryIds") List<Long> categoryIds);
    
    /**
     * 查询某个分类及其所有子分类的ID列表
     */
    @Query("SELECT d.id FROM DocumentCategory d WHERE d.categoryPath LIKE CONCAT('%', :categoryCode, '%')")
    List<Long> findAllChildCategoryIds(@Param("categoryCode") String categoryCode);
    
    /**
     * 统计子分类数量
     */
    long countByParentId(Long parentId);
}
