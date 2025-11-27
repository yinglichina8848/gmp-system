package com.gmp.edms.service.impl;

import com.gmp.edms.dto.DocumentCategoryDTO;
import com.gmp.edms.entity.DocumentCategory;
import com.gmp.edms.repository.DocumentCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentCategoryServiceImplTest {

    @Mock
    private DocumentCategoryRepository documentCategoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DocumentCategoryServiceImpl documentCategoryService;

    private DocumentCategory category;
    private DocumentCategoryDTO categoryDTO;
    private DocumentCategory parentCategory;
    private DocumentCategoryDTO parentCategoryDTO;

    @BeforeEach
    void setUp() {
        // 创建父分类测试数据
        parentCategory = new DocumentCategory();
        parentCategory.setId(1L);
        parentCategory.setCategoryCode("ROOT001");
        parentCategory.setCategoryName("根分类");
        parentCategory.setParentId(null);
        parentCategory.setLevel(1);
        parentCategory.setPath("1/");
        parentCategory.setStatus("ACTIVE");

        parentCategoryDTO = new DocumentCategoryDTO();
        parentCategoryDTO.setId(1L);
        parentCategoryDTO.setCategoryCode("ROOT001");
        parentCategoryDTO.setCategoryName("根分类");
        parentCategoryDTO.setParentId(null);
        parentCategoryDTO.setLevel(1);
        parentCategoryDTO.setPath("1/");
        parentCategoryDTO.setStatus("ACTIVE");

        // 创建子分类测试数据
        category = new DocumentCategory();
        category.setId(2L);
        category.setCategoryCode("SUB001");
        category.setCategoryName("子分类");
        category.setParentId(1L);
        category.setLevel(2);
        category.setPath("1/2/");
        category.setStatus("ACTIVE");

        categoryDTO = new DocumentCategoryDTO();
        categoryDTO.setId(2L);
        categoryDTO.setCategoryCode("SUB001");
        categoryDTO.setCategoryName("子分类");
        categoryDTO.setParentId(1L);
        categoryDTO.setLevel(2);
        categoryDTO.setPath("1/2/");
        categoryDTO.setStatus("ACTIVE");
        categoryDTO.setParentCategoryName("根分类");
    }

    @Test
    void testCreateCategory() {
        // 准备
        when(documentCategoryRepository.findByCategoryCode(categoryDTO.getCategoryCode())).thenReturn(Optional.empty());
        when(documentCategoryRepository.findById(categoryDTO.getParentId())).thenReturn(Optional.of(parentCategory));
        when(documentCategoryRepository.save(any(DocumentCategory.class))).thenReturn(category);
        when(modelMapper.map(categoryDTO, DocumentCategory.class)).thenReturn(category);
        when(modelMapper.map(category, DocumentCategoryDTO.class)).thenReturn(categoryDTO);

        // 执行
        DocumentCategoryDTO result = documentCategoryService.createCategory(categoryDTO);

        // 验证
        assertNotNull(result);
        assertEquals("SUB001", result.getCategoryCode());
        verify(documentCategoryRepository, times(1)).save(any(DocumentCategory.class));
    }

    @Test
    void testUpdateCategory() {
        // 准备
        when(documentCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(documentCategoryRepository.findById(categoryDTO.getParentId())).thenReturn(Optional.of(parentCategory));
        when(documentCategoryRepository.existsByCategoryCodeAndIdNot(categoryDTO.getCategoryCode(), category.getId())).thenReturn(false);
        when(documentCategoryRepository.save(any(DocumentCategory.class))).thenReturn(category);
        when(modelMapper.map(categoryDTO, DocumentCategory.class)).thenReturn(category);
        when(modelMapper.map(category, DocumentCategoryDTO.class)).thenReturn(categoryDTO);

        // 执行
        DocumentCategoryDTO result = documentCategoryService.updateCategory(category.getId(), categoryDTO);

        // 验证
        assertNotNull(result);
        assertEquals("SUB001", result.getCategoryCode());
        verify(documentCategoryRepository, times(1)).save(any(DocumentCategory.class));
    }

    @Test
    void testDeleteCategory() {
        // 准备
        when(documentCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(documentCategoryRepository.existsByParentId(category.getId())).thenReturn(false);
        doNothing().when(documentCategoryRepository).delete(category);

        // 执行
        documentCategoryService.deleteCategory(category.getId());

        // 验证
        verify(documentCategoryRepository, times(1)).delete(category);
    }

    @Test
    void testBatchDeleteCategories() {
        // 准备
        List<Long> ids = Arrays.asList(1L, 2L);
        when(documentCategoryRepository.findAllById(ids)).thenReturn(Arrays.asList(category));
        when(documentCategoryRepository.existsByParentIdIn(ids)).thenReturn(false);
        doNothing().when(documentCategoryRepository).deleteAll(Arrays.asList(category));

        // 执行
        documentCategoryService.batchDeleteCategories(ids);

        // 验证
        verify(documentCategoryRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void testGetCategoryById() {
        // 准备
        when(documentCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(modelMapper.map(category, DocumentCategoryDTO.class)).thenReturn(categoryDTO);

        // 执行
        DocumentCategoryDTO result = documentCategoryService.getCategoryById(category.getId());

        // 验证
        assertNotNull(result);
        assertEquals("SUB001", result.getCategoryCode());
        verify(documentCategoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void testGetCategoryByCode() {
        // 准备
        when(documentCategoryRepository.findByCategoryCode(category.getCategoryCode())).thenReturn(Optional.of(category));
        when(modelMapper.map(category, DocumentCategoryDTO.class)).thenReturn(categoryDTO);

        // 执行
        DocumentCategoryDTO result = documentCategoryService.getCategoryByCode(category.getCategoryCode());

        // 验证
        assertNotNull(result);
        assertEquals("SUB001", result.getCategoryCode());
        verify(documentCategoryRepository, times(1)).findByCategoryCode(category.getCategoryCode());
    }

    @Test
    void testListCategories() {
        // 准备
        List<DocumentCategory> categories = Arrays.asList(category, parentCategory);
        Page<DocumentCategory> page = new PageImpl<>(categories);
        Pageable pageable = PageRequest.of(0, 10);
        when(documentCategoryRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(category, DocumentCategoryDTO.class)).thenReturn(categoryDTO);
        when(modelMapper.map(parentCategory, DocumentCategoryDTO.class)).thenReturn(parentCategoryDTO);

        // 执行
        Page<DocumentCategoryDTO> result = documentCategoryService.listCategories(pageable);

        // 验证
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(documentCategoryRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetRootCategories() {
        // 准备
        List<DocumentCategory> rootCategories = Collections.singletonList(parentCategory);
        when(documentCategoryRepository.findByParentIdIsNullAndStatus("ACTIVE")).thenReturn(rootCategories);
        when(modelMapper.map(parentCategory, DocumentCategoryDTO.class)).thenReturn(parentCategoryDTO);

        // 执行
        List<DocumentCategoryDTO> result = documentCategoryService.getRootCategories();

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(documentCategoryRepository, times(1)).findByParentIdIsNullAndStatus("ACTIVE");
    }

    @Test
    void testGetChildCategories() {
        // 准备
        List<DocumentCategory> childCategories = Collections.singletonList(category);
        when(documentCategoryRepository.findByParentIdAndStatus(parentCategory.getId(), "ACTIVE")).thenReturn(childCategories);
        when(modelMapper.map(category, DocumentCategoryDTO.class)).thenReturn(categoryDTO);

        // 执行
        List<DocumentCategoryDTO> result = documentCategoryService.getChildCategories(parentCategory.getId());

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(documentCategoryRepository, times(1)).findByParentIdAndStatus(parentCategory.getId(), "ACTIVE");
    }

    @Test
    void testGetCategoryTree() {
        // 准备
        when(documentCategoryRepository.findByParentIdIsNullAndStatus("ACTIVE")).thenReturn(Collections.singletonList(parentCategory));
        when(documentCategoryRepository.findByParentIdAndStatus(parentCategory.getId(), "ACTIVE")).thenReturn(Collections.singletonList(category));
        when(modelMapper.map(parentCategory, DocumentCategoryDTO.class)).thenReturn(parentCategoryDTO);
        when(modelMapper.map(category, DocumentCategoryDTO.class)).thenReturn(categoryDTO);

        // 执行
        List<DocumentCategoryDTO> result = documentCategoryService.getCategoryTree();

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size()); // 只有根分类
        verify(documentCategoryRepository, times(1)).findByParentIdIsNullAndStatus("ACTIVE");
        verify(documentCategoryRepository, times(1)).findByParentIdAndStatus(parentCategory.getId(), "ACTIVE");
    }

    @Test
    void testUpdateCategoryStatus() {
        // 准备
        when(documentCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(documentCategoryRepository.save(any(DocumentCategory.class))).thenReturn(category);

        // 执行
        documentCategoryService.updateCategoryStatus(category.getId(), "INACTIVE");

        // 验证
        verify(documentCategoryRepository, times(1)).save(any(DocumentCategory.class));
    }

    @Test
    void testCheckCategoryCodeExists() {
        // 准备
        when(documentCategoryRepository.findByCategoryCode(category.getCategoryCode())).thenReturn(Optional.of(category));

        // 执行
        boolean result = documentCategoryService.checkCategoryCodeExists(category.getCategoryCode());

        // 验证
        assertTrue(result);
        verify(documentCategoryRepository, times(1)).findByCategoryCode(category.getCategoryCode());
    }

    @Test
    void testGetCategoryPath() {
        // 准备
        when(documentCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(documentCategoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(parentCategory));

        // 执行
        List<String> result = documentCategoryService.getCategoryPath(category.getId());

        // 验证
        assertNotNull(result);
        verify(documentCategoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void testUpdateParentCategory() {
        // 准备
        when(documentCategoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(documentCategoryRepository.findById(parentCategory.getId())).thenReturn(Optional.of(parentCategory));
        when(documentCategoryRepository.save(any(DocumentCategory.class))).thenReturn(category);
        when(documentCategoryRepository.existsByParentIdIn(Collections.singletonList(category.getId()))).thenReturn(false);

        // 执行
        documentCategoryService.updateParentCategory(category.getId(), parentCategory.getId());

        // 验证
        verify(documentCategoryRepository, times(1)).save(any(DocumentCategory.class));
    }

    @Test
    void testBatchUpdateCategoryStatus() {
        // 准备
        List<Long> ids = Arrays.asList(1L, 2L);
        when(documentCategoryRepository.findAllById(ids)).thenReturn(Arrays.asList(category));
        when(documentCategoryRepository.saveAll(anyList())).thenReturn(Arrays.asList(category));

        // 执行
        documentCategoryService.batchUpdateCategoryStatus(ids, "INACTIVE");

        // 验证
        verify(documentCategoryRepository, times(1)).saveAll(anyList());
    }
}
