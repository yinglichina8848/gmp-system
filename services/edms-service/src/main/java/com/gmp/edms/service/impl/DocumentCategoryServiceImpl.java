package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentCategoryCreateDTO;
import com.gmp.edms.dto.DocumentCategoryDTO;
import com.gmp.edms.dto.DocumentCategoryUpdateDTO;
import com.gmp.edms.dto.PageRequestDTO;
import com.gmp.edms.dto.PageResponseDTO;
import com.gmp.edms.entity.DocumentCategory;
import com.gmp.edms.repository.DocumentCategoryRepository;
import com.gmp.edms.service.DocumentCategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文档分类服务实现
 */
@Service
public class DocumentCategoryServiceImpl implements DocumentCategoryService {

    @Autowired
    private DocumentCategoryRepository documentCategoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    @CacheEvict(value = "documentCategories", allEntries = true)
    public DocumentCategoryDTO createCategory(DocumentCategoryCreateDTO categoryCreateDTO) {
        try {
            // 检查分类编码是否已存在
            String categoryCode = (String) getFieldValue(categoryCreateDTO, "categoryCode");
            if (documentCategoryRepository.existsByCategoryCode(categoryCode)) {
                throw new RuntimeException("分类编码已存在: " + categoryCode);
            }

            // 创建分类实体
            DocumentCategory category = modelMapper.map(categoryCreateDTO, DocumentCategory.class);

            // 设置层级和路径
            Long parentId = (Long) getFieldValue(categoryCreateDTO, "parentId");
            if (parentId != null) {
                DocumentCategory parentCategory = documentCategoryRepository.findById(parentId)
                        .orElseThrow(() -> new RuntimeException("父分类不存在: " + parentId));

                // 设置父分类
                setFieldValue(category, "parent", parentCategory);

                // 设置分类路径
                String parentPath = (String) getFieldValue(parentCategory, "categoryPath");
                parentPath = parentPath != null ? parentPath : "";
                setFieldValue(category, "categoryPath",
                        parentPath + "/" + getFieldValue(parentCategory, "categoryCode"));
            } else {
                // 根分类
                setFieldValue(category, "categoryPath", getFieldValue(category, "categoryCode"));
                setFieldValue(category, "parent", null);
            }

            // 设置默认值
            setFieldValue(category, "status", "ACTIVE");
            setFieldValue(category, "createdAt", LocalDateTime.now());
            setFieldValue(category, "updatedAt", LocalDateTime.now());

            // 保存分类
            category = documentCategoryRepository.save(category);

            // 更新子分类的路径
            updateChildCategoryPaths((Long) getFieldValue(category, "id"),
                    (String) getFieldValue(category, "categoryPath"));

            // 转换为DTO并返回
            return modelMapper.map(category, DocumentCategoryDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("创建分类失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "documentCategories", key = "#id")
    public DocumentCategoryDTO updateCategory(Long id, DocumentCategoryUpdateDTO categoryUpdateDTO) {
        try {
            // 查找分类
            DocumentCategory category = documentCategoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("分类不存在: " + id));

            // 更新分类信息
            if (getFieldValue(categoryUpdateDTO, "categoryName") != null) {
                setFieldValue(category, "categoryName", getFieldValue(categoryUpdateDTO, "categoryName"));
            }
            if (getFieldValue(categoryUpdateDTO, "description") != null) {
                setFieldValue(category, "description", getFieldValue(categoryUpdateDTO, "description"));
            }
            if (getFieldValue(categoryUpdateDTO, "sortOrder") != null) {
                setFieldValue(category, "sortOrder", getFieldValue(categoryUpdateDTO, "sortOrder"));
            }

            // 设置状态
            String statusValue = (String) getFieldValue(categoryUpdateDTO, "status");
            if (statusValue != null) {
                setFieldValue(category, "status", statusValue);
            }

            // 处理父分类变更
            Long parentIdValue = (Long) getFieldValue(categoryUpdateDTO, "parentId");
            if (parentIdValue != null) {
                updateParent(category, parentIdValue);
            }

            // 更新时间戳
            setFieldValue(category, "updatedAt", LocalDateTime.now());

            // 保存更新
            category = documentCategoryRepository.save(category);

            // 转换为DTO并返回
            return modelMapper.map(category, DocumentCategoryDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("更新分类失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "documentCategories", key = "#id")
    public void deleteCategory(Long id) {
        // 检查分类是否存在
        DocumentCategory category = documentCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + id));

        // 检查是否有子分类
        long childCount = documentCategoryRepository.countByParentId(id);
        if (childCount > 0) {
            throw new RuntimeException("该分类下存在子分类，无法删除");
        }

        // 删除分类
        documentCategoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void batchDeleteCategories(List<Long> ids) {
        // 批量删除分类
        documentCategoryRepository.deleteAllById(ids);
    }

    @Override
    @Cacheable(value = "documentCategories", key = "#id")
    public DocumentCategoryDTO getCategoryById(Long id) {
        // 查找分类
        DocumentCategory category = documentCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + id));

        // 转换为DTO并返回
        DocumentCategoryDTO dto = modelMapper.map(category, DocumentCategoryDTO.class);

        // 如果有父分类ID，设置父分类名称
        Long parentIdValue = (Long) getFieldValue(dto, "parentId");
        if (parentIdValue != null) {
            DocumentCategory parent = documentCategoryRepository.findById(parentIdValue).orElse(null);
            if (parent != null) {
                setFieldValue(dto, "parentName", getFieldValue(parent, "categoryName"));
            }
        }

        return dto;
    }

    @Override
    @Cacheable(value = "documentCategories", key = "#categoryCode")
    public DocumentCategoryDTO getCategoryByCode(String categoryCode) {
        // 根据编码查找分类
        DocumentCategory category = documentCategoryRepository.findByCategoryCode(categoryCode)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + categoryCode));

        // 转换为DTO并返回
        return modelMapper.map(category, DocumentCategoryDTO.class);
    }

    @Override
    public PageResponseDTO<DocumentCategoryDTO> queryCategories(PageRequestDTO pageRequest, String keyword) {
        try {
            // 使用反射获取分页参数
            String sortOrder = (String) getFieldValue(pageRequest, "sortOrder");
            sortOrder = sortOrder != null ? sortOrder : "asc";

            String sortBy = (String) getFieldValue(pageRequest, "sortBy");
            sortBy = sortBy != null ? sortBy : "id";

            Integer pageNoValue = (Integer) getFieldValue(pageRequest, "pageNo");
            int pageNo = pageNoValue != null ? pageNoValue : 1;

            Integer pageSizeValue = (Integer) getFieldValue(pageRequest, "pageSize");
            int pageSize = pageSizeValue != null ? pageSizeValue : 10;

            // 构建分页请求
            Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

            // 执行查询
            Page<DocumentCategory> page = documentCategoryRepository.findAll(pageable);

            // 转换为DTO并返回
            List<DocumentCategoryDTO> categoryDTOs = page.getContent().stream()
                    .map(category -> modelMapper.map(category, DocumentCategoryDTO.class))
                    .collect(Collectors.toList());

            return new PageResponseDTO<>(categoryDTOs, page.getTotalElements(), pageNo, pageSize);
        } catch (Exception e) {
            throw new RuntimeException("查询分类失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DocumentCategoryDTO> getRootCategories() {
        // 查询根分类
        List<DocumentCategory> rootCategories = documentCategoryRepository.findByParentIdIsNull();

        // 转换为DTO并返回
        return rootCategories.stream()
                .map(category -> modelMapper.map(category, DocumentCategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentCategoryDTO> getChildCategories(Long parentId) {
        // 查询子分类
        List<DocumentCategory> childCategories = documentCategoryRepository.findByParentId(parentId);

        // 转换为DTO并返回
        return childCategories.stream()
                .map(category -> modelMapper.map(category, DocumentCategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentCategoryDTO> getCategoryTree() {
        try {
            // 获取所有分类
            List<DocumentCategory> allCategories = documentCategoryRepository.findAll();

            // 构建分类树
            Map<Long, DocumentCategoryDTO> categoryMap = new HashMap<>();
            List<DocumentCategoryDTO> rootCategories = new ArrayList<>();

            // 首先将所有分类转换为DTO并放入Map
            for (DocumentCategory category : allCategories) {
                DocumentCategoryDTO dto = modelMapper.map(category, DocumentCategoryDTO.class);
                // 设置children列表
                setFieldValue(dto, "children", new ArrayList<>());
                categoryMap.put((Long) getFieldValue(category, "id"), dto);
            }

            // 构建树结构
            for (DocumentCategory category : allCategories) {
                DocumentCategoryDTO dto = categoryMap.get(getFieldValue(category, "id"));

                Long parentIdValue = (Long) getFieldValue(dto, "parentId");

                if (parentIdValue == null) {
                    // 根分类
                    rootCategories.add(dto);
                } else {
                    // 子分类，添加到父分类的children列表
                    DocumentCategoryDTO parentDto = categoryMap.get(parentIdValue);
                    if (parentDto != null) {
                        List<DocumentCategoryDTO> children = (List<DocumentCategoryDTO>) getFieldValue(parentDto,
                                "children");
                        if (children == null) {
                            children = new ArrayList<>();
                            setFieldValue(parentDto, "children", children);
                        }
                        children.add(dto);
                    }
                }
            }

            return rootCategories;
        } catch (Exception e) {
            throw new RuntimeException("获取分类树失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void updateParentCategory(Long id, Long parentId) {
        // 查找分类
        DocumentCategory category = documentCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + id));

        // 更新父分类
        updateParent(category, parentId);

        // 保存更新
        documentCategoryRepository.save(category);

        // 更新子分类路径
        updateChildCategoryPaths(id, (String) getFieldValue(category, "categoryPath"));
    }

    @Override
    @Transactional
    public void updateCategoryStatus(Long id, String status) {
        // 查找分类
        DocumentCategory category = documentCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + id));

        // 更新状态
        setFieldValue(category, "status", status);

        // 更新时间戳
        setFieldValue(category, "updatedAt", LocalDateTime.now());

        // 保存更新
        documentCategoryRepository.save(category);
    }

    @Override
    public boolean checkCategoryCodeExists(String categoryCode, Long excludeId) {
        if (excludeId != null) {
            return documentCategoryRepository.existsByCategoryCodeAndIdNot(categoryCode, excludeId);
        } else {
            return documentCategoryRepository.existsByCategoryCode(categoryCode);
        }
    }

    @Override
    public List<DocumentCategoryDTO> getCategoriesByLevel(Integer level) {
        // 根据层级查询分类
        List<DocumentCategory> categories = documentCategoryRepository.findByLevel(level);

        // 转换为DTO并返回
        return categories.stream()
                .map(category -> modelMapper.map(category, DocumentCategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentCategoryDTO> getAllCategories() {
        // 获取所有分类
        List<DocumentCategory> categories = documentCategoryRepository.findAll();

        // 转换为DTO并返回
        return categories.stream()
                .map(category -> modelMapper.map(category, DocumentCategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentCategoryDTO> getCategoryPath(Long categoryId) {
        // 查找分类
        DocumentCategory category = documentCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("分类不存在: " + categoryId));

        // 解析分类路径并获取所有父分类
        String path = (String) getFieldValue(category, "categoryPath");
        if (path == null || path.isEmpty()) {
            return new ArrayList<>();
        }

        // 这里简化处理，实际应该根据路径查询所有父分类
        List<DocumentCategoryDTO> pathCategories = new ArrayList<>();
        DocumentCategoryDTO currentDTO = modelMapper.map(category, DocumentCategoryDTO.class);
        pathCategories.add(currentDTO);

        // 通过DTO的parentId逐步获取父分类
        boolean hasNextParent = true;
        while (hasNextParent) {
            try {
                Long parentIdValue = (Long) getFieldValue(currentDTO, "parentId");

                if (parentIdValue == null) {
                    hasNextParent = false;
                } else {
                    DocumentCategory parentCategory = documentCategoryRepository.findById(parentIdValue)
                            .orElse(null);
                    if (parentCategory == null) {
                        break;
                    }
                    DocumentCategoryDTO parentDTO = modelMapper.map(parentCategory, DocumentCategoryDTO.class);
                    pathCategories.add(0, parentDTO);
                    // 为下一轮循环准备
                    currentDTO = parentDTO;
                }
            } catch (Exception e) {
                hasNextParent = false;
            }
        }

        return pathCategories;
    }

    @Override
    @Transactional
    public void batchUpdateCategoryStatus(List<Long> ids, String status) {
        // 批量更新状态
        for (Long id : ids) {
            updateCategoryStatus(id, status);
        }
    }

    /**
     * 更新父分类
     */
    private void updateParent(DocumentCategory category, Long parentId) {
        // 检查是否形成循环引用
        if (parentId != null && checkCircularReference((Long) getFieldValue(category, "id"), parentId)) {
            throw new RuntimeException("不能将分类设置为其自身或子分类的子分类");
        }

        if (parentId != null) {
            DocumentCategory parentCategory = documentCategoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("父分类不存在: " + parentId));

            // 设置层级
            Integer parentLevel = (Integer) getFieldValue(parentCategory, "level");
            if (parentLevel == null) {
                parentLevel = 0;
            }
            setFieldValue(category, "level", parentLevel + 1);

            // 设置分类路径
            String parentPath = (String) getFieldValue(parentCategory, "categoryPath");
            parentPath = parentPath != null ? parentPath : "";
            setFieldValue(category, "categoryPath", parentPath + "/" + getFieldValue(category, "categoryCode"));

            // 设置父分类
            setFieldValue(category, "parent", parentCategory);
        } else {
            // 根分类
            setFieldValue(category, "level", 1);
            setFieldValue(category, "categoryPath", getFieldValue(category, "categoryCode"));
            setFieldValue(category, "parent", null);
        }
    }

    /**
     * 检查是否存在循环引用
     */
    private boolean checkCircularReference(Long categoryId, Long parentId) {
        if (categoryId.equals(parentId)) {
            return true;
        }

        DocumentCategory parent = documentCategoryRepository.findById(parentId).orElse(null);
        Long currentParentId = parentId;

        // 不使用getParent()方法，而是通过repository查询父分类
        while (parent != null) {
            if (((Long) getFieldValue(parent, "id")).equals(categoryId)) {
                return true;
            }

            // 查询下一级父分类
            try {
                Long nextParentId = (Long) getFieldValue(parent, "parentId");
                if (nextParentId != null) {
                    currentParentId = nextParentId;
                    parent = documentCategoryRepository.findById(currentParentId).orElse(null);
                } else {
                    parent = null;
                }
            } catch (Exception e) {
                parent = null;
            }
        }

        return false;
    }

    /**
     * 更新子分类的路径
     */
    private void updateChildCategoryPaths(Long parentId, String parentPath) {
        List<DocumentCategory> childCategories = documentCategoryRepository.findByParentId(parentId);

        for (DocumentCategory child : childCategories) {
            // 更新子分类的路径
            setFieldValue(child, "categoryPath", parentPath + "/" + getFieldValue(child, "categoryCode"));

            // 更新层级
            setFieldValue(child, "level", parentPath.split("/").length + 1);

            documentCategoryRepository.save(child);

            // 递归更新子分类的子分类
            updateChildCategoryPaths((Long) getFieldValue(child, "id"), (String) getFieldValue(child, "categoryPath"));
        }
    }

    /**
     * 使用反射安全获取字段值
     */
    private Object getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            // 忽略所有异常
        }
        return null;
    }

    /**
     * 使用反射安全设置字段值
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            // 忽略所有异常
        }
    }

    /**
     * 递归查找字段，包括父类
     */
    private java.lang.reflect.Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && superClass != Object.class) {
                return findField(superClass, fieldName);
            }
            return null;
        }
    }
}