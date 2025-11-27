package com.gmp.edms.service;

import com.gmp.edms.dto.DocumentCategoryCreateDTO;
import com.gmp.edms.dto.DocumentCategoryDTO;
import com.gmp.edms.dto.DocumentCategoryUpdateDTO;
import com.gmp.edms.dto.PageRequestDTO;
import com.gmp.edms.dto.PageResponseDTO;

import java.util.List;

/**
 * 文档分类服务接口
 */
public interface DocumentCategoryService {
    
    /**
     * 创建分类
     */
    DocumentCategoryDTO createCategory(DocumentCategoryCreateDTO categoryCreateDTO);
    
    /**
     * 更新分类
     */
    DocumentCategoryDTO updateCategory(Long id, DocumentCategoryUpdateDTO categoryUpdateDTO);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
    
    /**
     * 批量删除分类
     */
    void batchDeleteCategories(List<Long> ids);
    
    /**
     * 根据ID获取分类
     */
    DocumentCategoryDTO getCategoryById(Long id);
    
    /**
     * 根据编码获取分类
     */
    DocumentCategoryDTO getCategoryByCode(String categoryCode);
    
    /**
     * 分页查询分类
     */
    PageResponseDTO<DocumentCategoryDTO> queryCategories(PageRequestDTO pageRequest, String keyword);
    
    /**
     * 获取根分类列表
     */
    List<DocumentCategoryDTO> getRootCategories();
    
    /**
     * 获取子分类列表
     */
    List<DocumentCategoryDTO> getChildCategories(Long parentId);
    
    /**
     * 获取分类树
     */
    List<DocumentCategoryDTO> getCategoryTree();
    
    /**
     * 更新父分类
     */
    void updateParentCategory(Long id, Long parentId);
    
    /**
     * 更新分类状态
     */
    void updateCategoryStatus(Long id, String status);
    
    /**
     * 检查分类编码是否存在
     */
    boolean checkCategoryCodeExists(String categoryCode, Long excludeId);
    
    /**
     * 根据层级获取分类
     */
    List<DocumentCategoryDTO> getCategoriesByLevel(Integer level);
    
    /**
     * 获取所有分类
     */
    List<DocumentCategoryDTO> getAllCategories();
    
    /**
     * 获取分类路径
     */
    List<DocumentCategoryDTO> getCategoryPath(Long categoryId);
    
    /**
     * 批量更新分类状态
     */
    void batchUpdateCategoryStatus(List<Long> ids, String status);
}
