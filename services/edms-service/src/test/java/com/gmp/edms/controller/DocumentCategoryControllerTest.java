package com.gmp.edms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.edms.dto.DocumentCategoryDTO;
import com.gmp.edms.service.DocumentCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DocumentCategoryController的单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 * @since 2023-08-01
 */
@WebMvcTest(DocumentCategoryController.class)
public class DocumentCategoryControllerTest {

    @Mock
    private DocumentCategoryService documentCategoryService;

    @InjectMocks
    private DocumentCategoryController documentCategoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DocumentCategoryDTO categoryDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentCategoryController).build();
        objectMapper = new ObjectMapper();
        
        // 初始化测试数据
        categoryDTO = DocumentCategoryDTO.builder()
                .id(1L)
                .code("TECH")
                .name("技术文档")
                .description("技术类文档分类")
                .parentId(null)
                .parentName(null)
                .level(1)
                .sortOrder(1)
                .isActive(true)
                .build();
    }

    @Test
    public void testCreateCategory() throws Exception {
        // 模拟service行为
        when(documentCategoryService.createCategory(any(DocumentCategoryDTO.class))).thenReturn(categoryDTO);
        
        // 执行测试
        mockMvc.perform(post("/api/edms/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("TECH"))
                .andExpect(jsonPath("$.name").value("技术文档"));
        
        // 验证调用
        verify(documentCategoryService).createCategory(any(DocumentCategoryDTO.class));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        // 模拟service行为
        when(documentCategoryService.updateCategory(anyLong(), any(DocumentCategoryDTO.class))).thenReturn(categoryDTO);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("TECH"));
        
        // 验证调用
        verify(documentCategoryService).updateCategory(1L, any(DocumentCategoryDTO.class));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        // 模拟service行为
        when(documentCategoryService.deleteCategory(anyLong())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(delete("/api/edms/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        // 验证调用
        verify(documentCategoryService).deleteCategory(1L);
    }

    @Test
    public void testBatchDeleteCategories() throws Exception {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Map<Long, Boolean> resultMap = new HashMap<>();
        resultMap.put(1L, true);
        resultMap.put(2L, true);
        resultMap.put(3L, false);
        
        // 模拟service行为
        when(documentCategoryService.batchDeleteCategories(anyList())).thenReturn(resultMap);
        
        // 执行测试
        mockMvc.perform(delete("/api/edms/categories/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['1']").value(true))
                .andExpect(jsonPath("$.['3']").value(false));
        
        // 验证调用
        verify(documentCategoryService).batchDeleteCategories(ids);
    }

    @Test
    public void testGetCategoryById() throws Exception {
        // 模拟service行为
        when(documentCategoryService.getCategoryById(anyLong())).thenReturn(categoryDTO);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("技术文档"));
        
        // 验证调用
        verify(documentCategoryService).getCategoryById(1L);
    }

    @Test
    public void testGetCategoryByCode() throws Exception {
        // 模拟service行为
        when(documentCategoryService.getCategoryByCode(anyString())).thenReturn(categoryDTO);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/code/TECH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("TECH"));
        
        // 验证调用
        verify(documentCategoryService).getCategoryByCode("TECH");
    }

    @Test
    public void testQueryCategories() throws Exception {
        // 准备测试数据
        Pageable pageable = PageRequest.of(0, 10);
        List<DocumentCategoryDTO> categoryDTOs = Arrays.asList(categoryDTO);
        Page<DocumentCategoryDTO> categoryPage = new PageImpl<>(categoryDTOs, pageable, 1);
        
        // 模拟service行为
        when(documentCategoryService.queryCategories(any(Pageable.class), anyString(), anyLong(), anyBoolean()))
                .thenReturn(categoryPage);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories")
                .param("page", "0")
                .param("size", "10")
                .param("keyword", "技术")
                .param("parentId", "1")
                .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        // 验证调用
        verify(documentCategoryService).queryCategories(eq(pageable), eq("技术"), eq(1L), eq(true));
    }

    @Test
    public void testGetRootCategories() throws Exception {
        // 准备测试数据
        List<DocumentCategoryDTO> rootCategories = Arrays.asList(categoryDTO);
        
        // 模拟service行为
        when(documentCategoryService.getRootCategories(anyBoolean())).thenReturn(rootCategories);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/roots")
                .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("TECH"));
        
        // 验证调用
        verify(documentCategoryService).getRootCategories(true);
    }

    @Test
    public void testGetChildCategories() throws Exception {
        // 准备测试数据
        List<DocumentCategoryDTO> childCategories = Arrays.asList(categoryDTO);
        
        // 模拟service行为
        when(documentCategoryService.getChildCategories(anyLong(), anyBoolean())).thenReturn(childCategories);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/1/children")
                .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("TECH"));
        
        // 验证调用
        verify(documentCategoryService).getChildCategories(1L, true);
    }

    @Test
    public void testGetCategoryTree() throws Exception {
        // 准备测试数据
        Map<String, Object> treeNode = new HashMap<>();
        treeNode.put("id", 1L);
        treeNode.put("code", "TECH");
        treeNode.put("name", "技术文档");
        treeNode.put("children", new ArrayList<>());
        List<Map<String, Object>> tree = Arrays.asList(treeNode);
        
        // 模拟service行为
        when(documentCategoryService.getCategoryTree(anyBoolean())).thenReturn(tree);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/tree")
                .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].code").value("TECH"))
                .andExpect(jsonPath("$[0].children.length()").value(0));
        
        // 验证调用
        verify(documentCategoryService).getCategoryTree(true);
    }

    @Test
    public void testUpdateParentCategory() throws Exception {
        // 模拟service行为
        when(documentCategoryService.updateParentCategory(anyLong(), anyLong())).thenReturn(categoryDTO);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/categories/1/parent")
                .param("parentId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        
        // 验证调用
        verify(documentCategoryService).updateParentCategory(1L, 2L);
    }

    @Test
    public void testUpdateCategoryStatus() throws Exception {
        // 模拟service行为
        when(documentCategoryService.updateCategoryStatus(anyLong(), anyBoolean())).thenReturn(categoryDTO);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/categories/1/status")
                .param("active", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.isActive").value(true));
        
        // 验证调用
        verify(documentCategoryService).updateCategoryStatus(1L, true);
    }

    @Test
    public void testCheckCategoryCodeExists() throws Exception {
        // 模拟service行为
        when(documentCategoryService.isCategoryCodeExists(anyString(), anyLong())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/exists/code")
                .param("code", "TECH")
                .param("excludeId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        // 验证调用
        verify(documentCategoryService).isCategoryCodeExists("TECH", 1L);
    }

    @Test
    public void testGetCategoriesByLevel() throws Exception {
        // 准备测试数据
        List<DocumentCategoryDTO> levelCategories = Arrays.asList(categoryDTO);
        
        // 模拟service行为
        when(documentCategoryService.getCategoriesByLevel(anyInt(), anyBoolean())).thenReturn(levelCategories);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/level/1")
                .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].level").value(1));
        
        // 验证调用
        verify(documentCategoryService).getCategoriesByLevel(1, true);
    }

    @Test
    public void testGetAllCategories() throws Exception {
        // 准备测试数据
        List<DocumentCategoryDTO> allCategories = Arrays.asList(categoryDTO);
        
        // 模拟service行为
        when(documentCategoryService.getAllCategories(anyBoolean())).thenReturn(allCategories);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/all")
                .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].code").value("TECH"));
        
        // 验证调用
        verify(documentCategoryService).getAllCategories(true);
    }

    @Test
    public void testGetCategoryPath() throws Exception {
        // 准备测试数据
        List<DocumentCategoryDTO> path = Arrays.asList(categoryDTO);
        
        // 模拟service行为
        when(documentCategoryService.getCategoryPath(anyLong())).thenReturn(path);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/categories/1/path"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
        
        // 验证调用
        verify(documentCategoryService).getCategoryPath(1L);
    }

    @Test
    public void testBatchUpdateCategoryStatus() throws Exception {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Map<Long, Boolean> resultMap = new HashMap<>();
        resultMap.put(1L, true);
        resultMap.put(2L, true);
        resultMap.put(3L, false);
        
        // 模拟service行为
        when(documentCategoryService.batchUpdateCategoryStatus(anyList(), anyBoolean())).thenReturn(resultMap);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/categories/batch/status")
                .param("active", "true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['1']").value(true))
                .andExpect(jsonPath("$.['3']").value(false));
        
        // 验证调用
        verify(documentCategoryService).batchUpdateCategoryStatus(ids, true);
    }
}
