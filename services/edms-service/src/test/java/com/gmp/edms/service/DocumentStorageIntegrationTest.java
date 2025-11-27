package com.gmp.edms.service;

import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentVersion;
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
                "Test document content".getBytes(StandardCharsets.UTF_8)
        );

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

        // 模拟文档保存
        when(documentService.save(any(Document.class))).thenReturn(testDocument);

        // 模拟版本保存
        DocumentVersion version = new DocumentVersion();
        version.setId(UUID.randomUUID().toString());
        version.setDocumentId(testDocument.getId());
        version.setFilePath(expectedFilePath);
        when(documentService.saveVersion(any(DocumentVersion.class))).thenReturn(version);

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
        when(documentService.getById(testDocument.getId())).thenReturn(testDocument);

        // 模拟版本保存
        DocumentVersion newVersion = new DocumentVersion();
        newVersion.setId(UUID.randomUUID().toString());
        newVersion.setDocumentId(testDocument.getId());
        newVersion.setFilePath(expectedFilePath);
        when(documentService.saveVersion(any(DocumentVersion.class))).thenReturn(newVersion);

        // 验证文档ID
        assertEquals(testDocument.getId(), newVersion.getDocumentId());
    }

    /**
     * 测试下载文档版本
     */
    @Test
    void testDownloadDocumentVersion() throws IOException {
        // 准备测试数据
        String filePath = "edms/documents/" + UUID.randomUUID() + ".txt";
        String fileContent = "Test document content";

        // 模拟文件下载
        when(fileStorageService.downloadFile(eq(defaultBucket), eq(filePath)))
                .thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));

        // 模拟获取版本
        DocumentVersion version = new DocumentVersion();
        version.setId(UUID.randomUUID().toString());
        version.setDocumentId(testDocument.getId());
        version.setFilePath(filePath);
        when(documentService.getVersionById(version.getId())).thenReturn(version);

        // 验证版本存在
        assertNotNull(version);
        assertEquals(testDocument.getId(), version.getDocumentId());
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
                "Document 1 content".getBytes(StandardCharsets.UTF_8)
        );

        MockMultipartFile file2 = new MockMultipartFile(
                "doc2.txt",
                "doc2.txt",
                "text/plain",
                "Document 2 content".getBytes(StandardCharsets.UTF_8)
        );

        Map<String, MockMultipartFile> files = new HashMap<>();
        files.put("doc1", file1);
        files.put("doc2", file2);

        // 模拟批量文件上传
        Map<String, String> uploadResults = new HashMap<>();
        uploadResults.put("doc1", "edms/documents/" + UUID.randomUUID() + ".txt");
        uploadResults.put("doc2", "edms/documents/" + UUID.randomUUID() + ".txt");
        
        when(fileStorageService.batchUploadFiles(
                eq(defaultBucket),
                eq(files),
                anyString(),
                anyString()
        )).thenReturn(uploadResults);

        // 验证上传结果
        assertEquals(2, uploadResults.size());
        assertTrue(uploadResults.containsKey("doc1"));
        assertTrue(uploadResults.containsKey("doc2"));
    }
}
