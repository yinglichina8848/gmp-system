package com.gmp.edms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.dto.DocumentVersionDTO;
import com.gmp.edms.service.DocumentService;
import com.gmp.edms.service.DocumentVersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * DocumentController与文件存储服务集成测试
 * 验证文档管理功能与文件存储服务的协同工作
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DocumentStorageIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private DocumentVersionService documentVersionService;

    /**
     * 测试文档创建与文件上传集成
     */
    @Test
    void testCreateDocumentWithFileUpload() throws Exception {
        // 准备测试数据
        MockMultipartFile documentFile = new MockMultipartFile(
                "file",
                "test-document.pdf",
                "application/pdf",
                "PDF content".getBytes()
        );

        DocumentDTO mockDocumentDTO = new DocumentDTO();
        mockDocumentDTO.setId(1L);
        mockDocumentDTO.setTitle("Test Document");
        mockDocumentDTO.setCategoryId(1L);

        DocumentVersionDTO mockVersionDTO = new DocumentVersionDTO();
        mockVersionDTO.setId(1L);
        mockVersionDTO.setDocumentId(1L);
        mockVersionDTO.setVersion("1.0");
        mockVersionDTO.setFileName("test-document.pdf");
        mockVersionDTO.setFilePath("edms/documents/test-document.pdf");

        // 配置模拟行为
        when(documentService.createDocument(any(DocumentDTO.class))).thenReturn(mockDocumentDTO);
        when(documentVersionService.createVersion(anyLong(), any(MockMultipartFile.class), anyString(), anyMap())).thenReturn(mockVersionDTO);

        // 执行请求
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/documents/with-file")
                .file(documentFile)
                .param("title", "Test Document")
                .param("categoryId", "1")
                .param("version", "1.0")
                .param("description", "Test description")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.document.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.document.title").value("Test Document"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version.fileName").value("test-document.pdf"));
    }

    /**
     * 测试文档版本更新与文件上传集成
     */
    @Test
    void testUpdateDocumentVersionWithFileUpload() throws Exception {
        // 准备测试数据
        MockMultipartFile documentFile = new MockMultipartFile(
                "file",
                "updated-document.pdf",
                "application/pdf",
                "Updated PDF content".getBytes()
        );

        DocumentVersionDTO mockVersionDTO = new DocumentVersionDTO();
        mockVersionDTO.setId(2L);
        mockVersionDTO.setDocumentId(1L);
        mockVersionDTO.setVersion("2.0");
        mockVersionDTO.setFileName("updated-document.pdf");
        mockVersionDTO.setFilePath("edms/documents/updated-document.pdf");

        // 配置模拟行为
        when(documentVersionService.createVersion(eq(1L), any(MockMultipartFile.class), eq("2.0"), anyMap())).thenReturn(mockVersionDTO);

        // 执行请求
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/documents/1/versions")
                .file(documentFile)
                .param("version", "2.0")
                .param("description", "Updated version")
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.documentId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value("2.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName").value("updated-document.pdf"));
    }

    /**
     * 测试文档版本下载功能
     */
    @Test
    void testDownloadDocumentVersion() throws Exception {
        // 配置模拟行为 - 这里主要测试控制器逻辑，文件下载实际会由FileStorageService处理
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/documents/versions/1/download"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * 测试批量文档上传与处理
     */
    @Test
    void testBatchDocumentUpload() throws Exception {
        // 准备测试数据
        MockMultipartFile file1 = new MockMultipartFile(
                "files[0]",
                "doc1.pdf",
                "application/pdf",
                "Content 1".getBytes()
        );
        
        MockMultipartFile file2 = new MockMultipartFile(
                "files[1]",
                "doc2.pdf",
                "application/pdf",
                "Content 2".getBytes()
        );

        // 准备文档元数据JSON
        Map<String, Object> metadata1 = new HashMap<>();
        metadata1.put("title", "Document 1");
        metadata1.put("categoryId", 1L);
        metadata1.put("version", "1.0");

        Map<String, Object> metadata2 = new HashMap<>();
        metadata2.put("title", "Document 2");
        metadata2.put("categoryId", 2L);
        metadata2.put("version", "1.0");

        List<Map<String, Object>> metadataList = Arrays.asList(metadata1, metadata2);
        String metadataJson = objectMapper.writeValueAsString(metadataList);
        
        MockMultipartFile metadataFile = new MockMultipartFile(
                "metadata",
                "metadata.json",
                "application/json",
                metadataJson.getBytes()
        );

        // 执行请求 - 测试控制器能否正确接收批量上传请求
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/documents/batch-upload")
                .file(file1)
                .file(file2)
                .file(metadataFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
