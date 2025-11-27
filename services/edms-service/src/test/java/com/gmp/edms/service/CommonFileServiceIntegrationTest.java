package com.gmp.edms.service;

import com.gmp.edms.dto.CommonFileDTO;
import com.gmp.edms.entity.CommonFile;
import com.gmp.edms.repository.CommonFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CommonFileService集成测试
 * 验证文件存储功能整合的正确性
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CommonFileServiceIntegrationTest {

    @Autowired
    private CommonFileService commonFileService;

    @Autowired
    private CommonFileRepository commonFileRepository;

    private MockMultipartFile testFile;
    private Map<String, Object> metadata;

    @BeforeEach
    void setUp() {
        // 创建测试文件
        testFile = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "This is test content".getBytes());

        // 创建测试元数据
        metadata = new HashMap<>();
        metadata.put("description", "Test file");
        metadata.put("version", "1.0");
    }

    /**
     * 测试文件上传功能
     */
    @Test
    void testUploadFile() throws Exception {
        // 执行上传
        CommonFileDTO fileDTO = commonFileService.uploadFile(testFile, "test-module", metadata);

        // 验证上传结果
        assertNotNull(fileDTO);
        assertEquals("test.txt", fileDTO.getFileName());
        assertEquals("text/plain", fileDTO.getFileType());
        assertEquals("test-module", fileDTO.getModule());

        // 验证数据库记录
        CommonFile savedFile = commonFileRepository.findById(fileDTO.getId()).orElse(null);
        assertNotNull(savedFile);
        assertEquals(fileDTO.getId(), savedFile.getId());

        // 验证文件内容
        InputStream inputStream = commonFileService.downloadFile(fileDTO.getId());
        byte[] content = inputStream.readAllBytes();
        assertEquals("This is test content", new String(content));
    }

    /**
     * 测试批量上传文件功能
     */
    @Test
    void testBatchUploadFiles() throws Exception {
        // 创建第二个测试文件
        MockMultipartFile testFile2 = new MockMultipartFile(
                "test2.txt",
                "test2.txt",
                "text/plain",
                "This is test content 2".getBytes());

        List<MockMultipartFile> files = List.of(testFile, testFile2);

        // 执行批量上传
        List<CommonFileDTO> fileDTOs = commonFileService.batchUploadFiles(files, "test-module", metadata);

        // 验证上传结果
        assertNotNull(fileDTOs);
        assertEquals(2, fileDTOs.size());

        // 验证每个文件
        for (CommonFileDTO fileDTO : fileDTOs) {
            assertNotNull(fileDTO.getId());
            assertTrue(fileDTO.getFileName().startsWith("test"));
            assertEquals("text/plain", fileDTO.getFileType());
        }
    }

    /**
     * 测试文件删除功能
     */
    @Test
    void testDeleteFile() throws Exception {
        // 先上传文件
        CommonFileDTO fileDTO = commonFileService.uploadFile(testFile, "test-module", metadata);
        Long fileId = fileDTO.getId();

        // 验证文件存在
        assertTrue(commonFileRepository.existsById(fileId));

        // 执行删除
        commonFileService.deleteFile(fileId);

        // 验证文件已删除
        assertFalse(commonFileRepository.existsById(fileId));
    }

    /**
     * 测试获取文件信息功能
     */
    @Test
    void testGetFileInfo() throws Exception {
        // 先上传文件
        CommonFileDTO uploadedFileDTO = commonFileService.uploadFile(testFile, "test-module", metadata);

        // 获取文件信息
        CommonFileDTO fileInfo = commonFileService.getFileInfo(uploadedFileDTO.getId());

        // 验证文件信息
        assertNotNull(fileInfo);
        assertEquals(uploadedFileDTO.getId(), fileInfo.getId());
        assertEquals("test.txt", fileInfo.getFileName());
        assertEquals("text/plain", fileInfo.getFileType());
        assertEquals("test-module", fileInfo.getModule());
        assertNotNull(fileInfo.getMetadata());
        assertEquals("Test file", fileInfo.getMetadata().get("description"));
    }

    /**
     * 测试生成预签名URL功能
     */
    @Test
    void testGeneratePresignedUrl() throws Exception {
        // 先上传文件
        CommonFileDTO fileDTO = commonFileService.uploadFile(testFile, "test-module", metadata);

        // 生成预签名URL
        String presignedUrl = commonFileService.generatePresignedUrl(fileDTO.getId(), 3600);

        // 验证URL格式
        assertNotNull(presignedUrl);
        assertTrue(presignedUrl.startsWith("http"));
        assertTrue(presignedUrl.contains("X-Amz-Expires"));
    }

    /**
     * 测试查询文件功能
     */
    @Test
    void testQueryFiles() throws Exception {
        // 上传测试文件
        commonFileService.uploadFile(testFile, "test-module", metadata);

        // 查询文件
        Map<String, Object> filters = new HashMap<>();
        filters.put("fileName", "test.txt");
        Map<String, Object> result = commonFileService.queryFiles("test-module", filters, 1, 10);

        // 验证查询结果
        assertNotNull(result);
        assertNotNull(result.get("content"));
        List<?> files = (List<?>) result.get("content");
        assertTrue(files.size() > 0);
    }

    /**
     * 测试更新文件元数据功能
     */
    @Test
    void testUpdateFileMetadata() throws Exception {
        // 先上传文件
        CommonFileDTO fileDTO = commonFileService.uploadFile(testFile, "test-module", metadata);

        // 更新元数据
        Map<String, Object> newMetadata = new HashMap<>();
        newMetadata.put("description", "Updated test file");
        newMetadata.put("version", "1.1");
        newMetadata.put("updated", true);

        commonFileService.updateFileMetadata(fileDTO.getId(), newMetadata);

        // 验证更新结果
        CommonFileDTO updatedFileDTO = commonFileService.getFileInfo(fileDTO.getId());
        assertNotNull(updatedFileDTO.getMetadata());
        assertEquals("Updated test file", updatedFileDTO.getMetadata().get("description"));
        assertEquals("1.1", updatedFileDTO.getMetadata().get("version"));
        assertTrue((Boolean) updatedFileDTO.getMetadata().get("updated"));
    }

    /**
     * 测试获取文件统计信息功能
     */
    @Test
    void testGetFileStatistics() throws Exception {
        // 上传测试文件
        commonFileService.uploadFile(testFile, "test-module", metadata);

        // 获取统计信息
        Map<String, Object> statistics = commonFileService.getFileStatistics("test-module");

        // 验证统计信息不为空
        assertNotNull(statistics);
    }
}