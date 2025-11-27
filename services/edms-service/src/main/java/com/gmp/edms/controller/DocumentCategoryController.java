package com.gmp.edms.controller;

import com.gmp.edms.dto.ApiResponse;
import com.gmp.edms.dto.DocumentCategoryCreateDTO;
import com.gmp.edms.dto.DocumentCategoryDTO;
import com.gmp.edms.dto.DocumentCategoryUpdateDTO;
import com.gmp.edms.dto.PageRequestDTO;
import com.gmp.edms.dto.PageResponseDTO;
import com.gmp.edms.service.DocumentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文档分类管理Controller
 */
@RestController
@RequestMapping("/api/categories")
public class DocumentCategoryController {
    
    @Autowired
    private DocumentCategoryService documentCategoryService;
    
    /**
     * 创建分类
     */
    @PostMapping
    public ApiResponse<DocumentCategoryDTO> createCategory(@RequestBody DocumentCategoryCreateDTO categoryCreateDTO) {
        
        DocumentCategoryDTO categoryDTO = documentCategoryService.createCategory(categoryCreateDTO);
        
        return ApiResponse.success("分类创建成功", categoryDTO);
    }
    
    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    public ApiResponse<DocumentCategoryDTO> updateCategory(@PathVariable Long id, 
                                                          @RequestBody DocumentCategoryUpdateDTO categoryUpdateDTO) {
        
        DocumentCategoryDTO categoryDTO = documentCategoryService.updateCategory(id, categoryUpdateDTO);
        
        return ApiResponse.success("分类更新成功", categoryDTO);
    }
    
    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        
        documentCategoryService.deleteCategory(id);
        
        return ApiResponse.success("分类删除成功");
    }
    
    /**
     * 批量删除分类
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteCategories(@RequestBody List<Long> ids) {
        
        documentCategoryService.batchDeleteCategories(ids);
        
        return ApiResponse.success("分类批量删除成功");
    }
    
    /**
     * 获取分类详情
     */
    @GetMapping("/{id}")
    public ApiResponse<DocumentCategoryDTO> getCategoryById(@PathVariable Long id) {
        
        DocumentCategoryDTO categoryDTO = documentCategoryService.getCategoryById(id);
        
        return ApiResponse.success("获取分类成功", categoryDTO);
    }
    
    /**
     * 根据编码获取分类
     */
    @GetMapping("/code/{code}")
    public ApiResponse<DocumentCategoryDTO> getCategoryByCode(@PathVariable String code) {
        
        DocumentCategoryDTO categoryDTO = documentCategoryService.getCategoryByCode(code);
        
        return ApiResponse.success("获取分类成功", categoryDTO);
    }
    
    /**
     * 分页查询分类
     */
    @GetMapping("/page")
    public ApiResponse<PageResponseDTO<DocumentCategoryDTO>> queryCategories(PageRequestDTO pageRequest,
                                                                          @RequestParam(required = false) String keyword) {
        
        PageResponseDTO<DocumentCategoryDTO> pageResponse = documentCategoryService.queryCategories(pageRequest, keyword);
        
        return ApiResponse.success("查询分类成功", pageResponse);
    }
    
    /**
     * 获取根分类列表
     */
    @GetMapping("/root")
    public ApiResponse<List<DocumentCategoryDTO>> getRootCategories() {
        
        List<DocumentCategoryDTO> categories = documentCategoryService.getRootCategories();
        
        return ApiResponse.success("获取根分类成功", categories);
    }
    
    /**
     * 获取子分类列表
     */
    @GetMapping("/{parentId}/children")
    public ApiResponse<List<DocumentCategoryDTO>> getChildCategories(@PathVariable Long parentId) {
        
        List<DocumentCategoryDTO> categories = documentCategoryService.getChildCategories(parentId);
        
        return ApiResponse.success("获取子分类成功", categories);
    }
    
    /**
     * 获取分类树
     */
    @GetMapping("/tree")
    public ApiResponse<List<DocumentCategoryDTO>> getCategoryTree() {
        
        List<DocumentCategoryDTO> categoryTree = documentCategoryService.getCategoryTree();
        
        return ApiResponse.success("获取分类树成功", categoryTree);
    }
    
    /**
     * 更新父分类
     */
    @PutMapping("/{id}/parent")
    public ApiResponse<Void> updateParentCategory(@PathVariable Long id, 
                                                @RequestParam Long parentId) {
        
        documentCategoryService.updateParentCategory(id, parentId);
        
        return ApiResponse.success("父分类更新成功");
    }
    
    /**
     * 更新分类状态
     */
    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateCategoryStatus(@PathVariable Long id, 
                                                @RequestParam String status) {
        
        documentCategoryService.updateCategoryStatus(id, status);
        
        return ApiResponse.success("分类状态更新成功");
    }
    
    /**
     * 检查分类编码是否存在
     */
    @GetMapping("/code/check")
    public ApiResponse<Boolean> checkCategoryCode(@RequestParam String categoryCode,
                                               @RequestParam(required = false) Long excludeId) {
        
        boolean exists = documentCategoryService.checkCategoryCodeExists(categoryCode, excludeId);
        
        return ApiResponse.success("检查成功", exists);
    }
    
    /**
     * 根据层级获取分类
     */
    @GetMapping("/level/{level}")
    public ApiResponse<List<DocumentCategoryDTO>> getCategoriesByLevel(@PathVariable Integer level) {
        
        List<DocumentCategoryDTO> categories = documentCategoryService.getCategoriesByLevel(level);
        
        return ApiResponse.success("获取分类成功", categories);
    }
    
    /**
     * 获取所有分类
     */
    @GetMapping
    public ApiResponse<List<DocumentCategoryDTO>> getAllCategories() {
        
        List<DocumentCategoryDTO> categories = documentCategoryService.getAllCategories();
        
        return ApiResponse.success("获取所有分类成功", categories);
    }
    
    /**
     * 获取分类路径
     */
    @GetMapping("/{categoryId}/path")
    public ApiResponse<List<DocumentCategoryDTO>> getCategoryPath(@PathVariable Long categoryId) {
        
        List<DocumentCategoryDTO> path = documentCategoryService.getCategoryPath(categoryId);
        
        return ApiResponse.success("获取分类路径成功", path);
    }
    
    /**
     * 批量更新分类状态
     */
    @PutMapping("/batch/status")
    public ApiResponse<Void> batchUpdateCategoryStatus(@RequestParam List<Long> ids,
                                                     @RequestParam String status) {
        
        documentCategoryService.batchUpdateCategoryStatus(ids, status);
        
        return ApiResponse.success("分类状态批量更新成功");
    }
}
