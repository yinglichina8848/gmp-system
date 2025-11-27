package com.gmp.edms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentVersionDTO;
import com.gmp.edms.service.DocumentService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * DocumentController的单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 * @since 2023-08-01
 */
@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private DocumentDTO documentDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(documentController).build();
        objectMapper = new ObjectMapper();
        
        // 初始化测试数据
        documentDTO = DocumentDTO.builder()
                .id(1L)
                .documentCode("DOC-2023-0001")
                .title("测试文档")
                .description("这是一个测试文档")
                .documentType("PDF")
                .authorId("user123")
                .status("ACTIVE")
                .approvalStatus("APPROVED")
                .categoryId(1L)
                .categoryName("技术文档")
                .build();
    }

    @Test
    public void testCreateDocument() throws Exception {
        // 模拟service行为
        when(documentService.createDocument(any(DocumentDTO.class))).thenReturn(documentDTO);
        
        // 执行测试
        mockMvc.perform(post("/api/edms/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(documentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.documentCode").value("DOC-2023-0001"))
                .andExpect(jsonPath("$.title").value("测试文档"));
        
        // 验证调用
        verify(documentService).createDocument(any(DocumentDTO.class));
    }

    @Test
    public void testUpdateDocument() throws Exception {
        // 模拟service行为
        when(documentService.updateDocument(anyLong(), any(DocumentDTO.class))).thenReturn(documentDTO);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/documents/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(documentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.documentCode").value("DOC-2023-0001"));
        
        // 验证调用
        verify(documentService).updateDocument(1L, any(DocumentDTO.class));
    }

    @Test
    public void testDeleteDocument() throws Exception {
        // 模拟service行为
        when(documentService.deleteDocument(anyLong(), anyString())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(delete("/api/edms/documents/1")
                .param("operatorId", "admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        // 验证调用
        verify(documentService).deleteDocument(1L, "admin");
    }

    @Test
    public void testBatchDeleteDocuments() throws Exception {
        // 准备测试数据
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Map<Long, Boolean> resultMap = new HashMap<>();
        resultMap.put(1L, true);
        resultMap.put(2L, true);
        resultMap.put(3L, false);
        
        // 模拟service行为
        when(documentService.batchDeleteDocuments(anyList(), anyString())).thenReturn(resultMap);
        
        // 执行测试
        mockMvc.perform(delete("/api/edms/documents/batch")
                .param("operatorId", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['1']").value(true))
                .andExpect(jsonPath("$.['3']").value(false));
        
        // 验证调用
        verify(documentService).batchDeleteDocuments(ids, "admin");
    }

    @Test
    public void testGetDocumentById() throws Exception {
        // 模拟service行为
        when(documentService.getDocumentById(anyLong())).thenReturn(documentDTO);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/documents/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("测试文档"));
        
        // 验证调用
        verify(documentService).getDocumentById(1L);
    }

    @Test
    public void testGetDocumentByCode() throws Exception {
        // 模拟service行为
        when(documentService.getDocumentByCode(anyString())).thenReturn(documentDTO);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/documents/code/DOC-2023-0001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.documentCode").value("DOC-2023-0001"));
        
        // 验证调用
        verify(documentService).getDocumentByCode("DOC-2023-0001");
    }

    @Test
    public void testQueryDocuments() throws Exception {
        // 准备测试数据
        Pageable pageable = PageRequest.of(0, 10);
        List<DocumentDTO> documentDTOs = Arrays.asList(documentDTO);
        Page<DocumentDTO> documentPage = new PageImpl<>(documentDTOs, pageable, 1);
        
        // 模拟service行为
        when(documentService.queryDocuments(any(Pageable.class), anyString(), anyLong(), anyString(), anyString()))
                .thenReturn(documentPage);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/documents")
                .param("page", "0")
                .param("size", "10")
                .param("keyword", "测试")
                .param("categoryId", "1")
                .param("documentType", "PDF")
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        // 验证调用
        verify(documentService).queryDocuments(eq(pageable), eq("测试"), eq(1L), eq("PDF"), eq("ACTIVE"));
    }

    @Test
    public void testUploadDocumentVersion() throws Exception {
        // 准备测试数据
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );
        
        DocumentVersionDTO versionDTO = DocumentVersionDTO.builder()
                .id(1L)
                .versionNumber("1.0.0")
                .fileName("test.pdf")
                .filePath("/documents/test.pdf")
                .build();
        
        // 模拟service行为
        when(documentService.uploadDocumentVersion(anyLong(), anyString(), anyString(), anyBoolean(), 
                anyString(), any(), anyString(), anyLong(), anyString())).thenReturn(versionDTO);
        
        // 执行测试
        mockMvc.perform(multipart("/api/edms/documents/1/versions")
                .file(file)
                .param("versionNumber", "1.0.0")
                .param("description", "初始版本")
                .param("isActive", "true")
                .param("uploaderId", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.versionNumber").value("1.0.0"));
        
        // 验证调用
        verify(documentService).uploadDocumentVersion(anyLong(), anyString(), anyString(), anyBoolean(), 
                anyString(), any(), anyString(), anyLong(), anyString());
    }

    @Test
    public void testDeleteDocumentVersion() throws Exception {
        // 模拟service行为
        when(documentService.deleteDocumentVersion(anyLong(), anyString())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(delete("/api/edms/documents/1/versions/1.0.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        // 验证调用
        verify(documentService).deleteDocumentVersion(1L, "1.0.0");
    }

    @Test
    public void testGetDocumentVersions() throws Exception {
        // 准备测试数据
        DocumentVersionDTO versionDTO = DocumentVersionDTO.builder()
                .id(1L)
                .versionNumber("1.0.0")
                .build();
        List<DocumentVersionDTO> versions = Arrays.asList(versionDTO);
        
        // 模拟service行为
        when(documentService.getDocumentVersions(anyLong())).thenReturn(versions);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/documents/1/versions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].versionNumber").value("1.0.0"));
        
        // 验证调用
        verify(documentService).getDocumentVersions(1L);
    }

    @Test
    public void testUpdateDocumentStatus() throws Exception {
        // 模拟service行为
        when(documentService.updateDocumentStatus(anyLong(), anyString())).thenReturn(documentDTO);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/documents/1/status")
                .param("status", "INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
        
        // 验证调用
        verify(documentService).updateDocumentStatus(1L, "INACTIVE");
    }

    @Test
    public void testApproveDocument() throws Exception {
        // 模拟service行为
        when(documentService.approveDocument(anyLong(), anyBoolean(), anyString(), anyString())).thenReturn(documentDTO);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/documents/1/approve")
                .param("approved", "true")
                .param("comments", "审批通过")
                .param("approverId", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.approvalStatus").value("APPROVED"));
        
        // 验证调用
        verify(documentService).approveDocument(1L, true, "审批通过", "admin");
    }

    @Test
    public void testGetDocumentStatistics() throws Exception {
        // 准备测试数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", 100L);
        stats.put("activeCount", 80L);
        stats.put("pdfCount", 50L);
        
        // 模拟service行为
        when(documentService.getDocumentStatistics()).thenReturn(stats);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/documents/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(100L))
                .andExpect(jsonPath("$.activeCount").value(80L))
                .andExpect(jsonPath("$.pdfCount").value(50L));
        
        // 验证调用
        verify(documentService).getDocumentStatistics();
    }

    @Test
    public void testToggleFavorite() throws Exception {
        // 模拟service行为
        when(documentService.toggleFavorite(anyLong(), anyString())).thenReturn(true);
        
        // 执行测试
        mockMvc.perform(put("/api/edms/documents/1/favorite")
                .param("userId", "user123"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        
        // 验证调用
        verify(documentService).toggleFavorite(1L, "user123");
    }

    @Test
    public void testGetUserFavoriteDocuments() throws Exception {
        // 准备测试数据
        Pageable pageable = PageRequest.of(0, 10);
        List<DocumentDTO> documentDTOs = Arrays.asList(documentDTO);
        Page<DocumentDTO> documentPage = new PageImpl<>(documentDTOs, pageable, 1);
        
        // 模拟service行为
        when(documentService.getUserFavoriteDocuments(anyString(), any(Pageable.class)))
                .thenReturn(documentPage);
        
        // 执行测试
        mockMvc.perform(get("/api/edms/documents/favorites/user123")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
        
        // 验证调用
        verify(documentService).getUserFavoriteDocuments("user123", pageable);
    }
}
