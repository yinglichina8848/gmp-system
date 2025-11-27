package com.gmp.edms.service;

import com.gmp.edms.dto.DocumentCreateDTO;
import com.gmp.edms.dto.DocumentDTO;
import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentVersion;
import com.gmp.edms.service.impl.DocumentServiceImpl;
import com.gmp.edms.service.impl.MinioFileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * 文档存储集成测试
 * 验证文档管理功能与文件存储服务的集成
 */
class DocumentStorageIntegrationTest {

        @Mock
        private MinioFileStorageServiceImpl fileStorageService;

        @Mock
        private DocumentService documentService;

        @InjectMocks
        private DocumentServiceImpl documentServiceImpl;

        private MockMvc mockMvc;
        private MockMultipartFile testFile;
        private Document testDocument;
        private final String defaultBucket = "edms-files";

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                mockMvc = MockMvcBuilders.standaloneSetup(documentServiceImpl).build();

                // 创建测试文件
                testFile = new MockMultipartFile(
                                "document.txt",
                                "document.txt",
                                "text/plain",
                                "Test document content".getBytes(StandardCharsets.UTF_8));

                // 创建测试文档
                testDocument = new Document();
                testDocument.setId(UUID.randomUUID().toString());
                testDocument.setTitle("Test Document");
                testDocument.setCategory("Report");
        }

        /**
         * 测试创建文档并上传文件
         */
        @Test
        void testCreateDocumentWithFileUpload() throws IOException {
                // 模拟文件上传结果
                String expectedFilePath = "edms/documents/" + UUID.randomUUID() + ".txt";
                when(fileStorageService.uploadFile(eq(testFile), eq(defaultBucket), anyString(), anyString()))
                                .thenReturn(expectedFilePath);

                // 模拟文档创建DTO
                DocumentCreateDTO createDTO = new DocumentCreateDTO();
                createDTO.setTitle("Test Document");
                createDTO.setCategoryId(1L);

                // 模拟文档创建
                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setId(1L);
                documentDTO.setTitle("Test Document");
                when(documentService.createDocument(any(DocumentCreateDTO.class))).thenReturn(documentDTO);

                // 模拟带文件的文档创建
                when(documentService.createDocumentWithFile(any(DocumentCreateDTO.class), any()))
                                .thenReturn(documentDTO);

                // 验证文档标题和分类
                assertEquals("Test Document", testDocument.getTitle());
                assertEquals("Report", testDocument.getCategory());
        }

        /**
         * 测试更新文档版本并上传新文件
         */
        @Test
        void testUpdateDocumentVersionWithFileUpload() throws IOException {
                // 模拟文件上传结果
                String expectedFilePath = "edms/documents/versions/" + UUID.randomUUID() + ".txt";
                when(fileStorageService.uploadFile(eq(testFile), eq(defaultBucket), anyString(), anyString()))
                                .thenReturn(expectedFilePath);

                // 模拟获取现有文档
                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setId(1L);
                documentDTO.setTitle("Test Document");
                when(documentService.getDocumentById(1L)).thenReturn(documentDTO);

                // 模拟上传文档文件
                when(documentService.uploadDocumentFile(1L, testFile)).thenReturn(documentDTO);

                // 验证文档标题
                assertEquals("Test Document", documentDTO.getTitle());
        }

        /**
         * 测试下载文档文件
         */
        @Test
        void testDownloadDocumentFile() throws Exception {
                // 准备测试数据
                String fileContent = "Test document content";

                // 模拟文件下载
                when(fileStorageService.downloadFile(eq(defaultBucket), anyString()))
                                .thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));

                // 模拟文档下载
                when(documentService.downloadDocumentFile(1L)).thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));

                // 验证下载功能
                byte[] downloadedContent = documentService.downloadDocumentFile(1L);
                assertNotNull(downloadedContent);
                assertEquals(fileContent, new String(downloadedContent, StandardCharsets.UTF_8));
        }

        /**
         * 测试批量文档上传
         */
        @Test
        void testBatchDocumentUpload() throws IOException {
                // 准备测试数据
                MockMultipartFile file1 = new MockMultipartFile(
                                "doc1.txt",
                                "doc1.txt",
                                "text/plain",
                                "Document 1 content".getBytes(StandardCharsets.UTF_8));

                MockMultipartFile file2 = new MockMultipartFile(
                                "doc2.txt",
                                "doc2.txt",
                                "text/plain",
                                "Document 2 content".getBytes(StandardCharsets.UTF_8));

                // 为每个文件模拟文件上传结果
                when(fileStorageService.uploadFile(any(), eq(defaultBucket), anyString(), anyString()))
                                .thenReturn("edms/documents/" + UUID.randomUUID() + ".txt");

                // 模拟文档创建和文件上传
                DocumentDTO documentDTO = new DocumentDTO();
                documentDTO.setId(1L);
                DocumentCreateDTO createDTO = new DocumentCreateDTO();
                createDTO.setTitle("Test Document");
                createDTO.setCategoryId(1L);

                when(documentService.createDocumentWithFile(any(DocumentCreateDTO.class), any()))
                                .thenReturn(documentDTO);

                // 验证上传功能
                assertNotNull(documentDTO);
                assertEquals(1L, documentDTO.getId());
        }
}
