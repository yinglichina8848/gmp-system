package com.gmp.edms.service;

import com.gmp.edms.dto.CommonFileDTO;
import com.gmp.edms.entity.CommonFile;
import com.gmp.edms.repository.CommonFileRepository;
import com.gmp.edms.service.impl.CommonFileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 通用文件服务测试类
 */
@ExtendWith(MockitoExtension.class)
public class CommonFileServiceTest {

    @Mock
    private CommonFileRepository commonFileRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CommonFileServiceImpl commonFileService;

    private MultipartFile testFile;
    private CommonFile testCommonFile;
    private final String MODULE = "test-module";

    @BeforeEach
    void setUp() throws IOException {
        // 创建测试文件
        testFile = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "test content".getBytes());

        // 创建测试文件实体
        testCommonFile = new CommonFile();
        testCommonFile.setId(1L);
        testCommonFile.setFileName("test.txt");
        testCommonFile.setFileType("text/plain");
        testCommonFile.setFileSize(12L);
        testCommonFile.setFilePath("test-module/2025/11/27/uuid-test.txt");
        testCommonFile.setChecksum("test-checksum");
        testCommonFile.setBucketName("common-files");
        testCommonFile.setModule(MODULE);
        testCommonFile.setCreatedBy("test-user");
    }

    @Test
    void testUploadFile_Success() throws Exception {
        // 准备测试数据
        when(commonFileRepository.findByChecksum(anyString())).thenReturn(Optional.empty());
        when(commonFileRepository.save(any(CommonFile.class))).thenReturn(testCommonFile);
        when(fileStorageService.uploadFile(any(MultipartFile.class), anyString(), anyString()))
                .thenReturn("test-path");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("description", "test file");

        // 执行测试
        CommonFileDTO result = commonFileService.uploadFile(testFile, MODULE, metadata);

        // 验证结果
        assertNotNull(result);
        assertEquals("test.txt", result.getFileName());
        assertEquals("text/plain", result.getFileType());
        assertEquals(12L, result.getFileSize());
        assertEquals(MODULE, result.getModule());

        // 验证方法调用
        verify(commonFileRepository).save(any(CommonFile.class));
        verify(fileStorageService).uploadFile(eq(testFile), eq("common-files"), anyString());
    }

    @Test
    void testUploadFile_FileAlreadyExists() throws Exception {
        // 准备测试数据 - 文件已存在
        when(commonFileRepository.findByChecksum(anyString())).thenReturn(Optional.of(testCommonFile));

        // 执行测试
        CommonFileDTO result = commonFileService.uploadFile(testFile, MODULE, null);

        // 验证结果
        assertNotNull(result);
        assertEquals(testCommonFile.getId(), result.getId());

        // 验证不应该保存新文件或上传到存储
        verify(commonFileRepository, never()).save(any(CommonFile.class));
        verify(fileStorageService, never()).uploadFile(any(), anyString(), anyString());
    }

    @Test
    void testUploadFile_EmptyFile() {
        // 创建空文件
        MultipartFile emptyFile = new MockMultipartFile("empty.txt", new byte[0]);

        // 执行测试并验证异常
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            commonFileService.uploadFile(emptyFile, MODULE, null);
        });

        assertEquals("文件不能为空", exception.getMessage());
    }

    @Test
    void testUploadFile_EmptyModule() {
        // 执行测试并验证异常
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            commonFileService.uploadFile(testFile, "", null);
        });

        assertEquals("模块名称不能为空", exception.getMessage());
    }

    @Test
    void testDownloadFile_Success() throws Exception {
        // 准备测试数据
        when(commonFileRepository.findById(1L)).thenReturn(Optional.of(testCommonFile));
        when(fileStorageService.downloadFile(eq("common-files"), eq(testCommonFile.getFilePath())))
                .thenReturn(new ByteArrayInputStream("test content".getBytes()));

        // 执行测试
        InputStream result = commonFileService.downloadFile(1L);

        // 验证结果
        assertNotNull(result);
        verify(fileStorageService).downloadFile("common-files", testCommonFile.getFilePath());
    }

    @Test
    void testDownloadFile_FileNotFound() {
        // 准备测试数据
        when(commonFileRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行测试并验证异常
        Exception exception = assertThrows(Exception.class, () -> {
            commonFileService.downloadFile(1L);
        });

        assertEquals("文件不存在: 1", exception.getMessage());
    }

    @Test
    void testGetFileInfo_Success() throws Exception {
        // 准备测试数据
        when(commonFileRepository.findById(1L)).thenReturn(Optional.of(testCommonFile));

        // 执行测试
        CommonFileDTO result = commonFileService.getFileInfo(1L);

        // 验证结果
        assertNotNull(result);
        assertEquals(testCommonFile.getId(), result.getId());
        assertEquals(testCommonFile.getFileName(), result.getFileName());
    }

    @Test
    void testDeleteFile_Success() throws Exception {
        // 准备测试数据
        when(commonFileRepository.findById(1L)).thenReturn(Optional.of(testCommonFile));
        doNothing().when(fileStorageService).deleteFile(anyString(), anyString());

        // 执行测试
        assertDoesNotThrow(() -> {
            commonFileService.deleteFile(1L);
        });

        // 验证方法调用
        verify(fileStorageService).deleteFile("common-files", testCommonFile.getFilePath());
        verify(commonFileRepository).delete(testCommonFile);
    }

    @Test
    void testBatchDeleteFiles_Success() throws Exception {
        // 准备测试数据
        List<Long> fileIds = Arrays.asList(1L, 2L);
        List<CommonFile> files = Arrays.asList(testCommonFile);

        when(commonFileRepository.findByIdIn(fileIds)).thenReturn(files);
        doNothing().when(fileStorageService).deleteFile(anyString(), anyString());

        // 执行测试
        assertDoesNotThrow(() -> {
            commonFileService.batchDeleteFiles(fileIds);
        });

        // 验证方法调用
        verify(fileStorageService).deleteFile("common-files", testCommonFile.getFilePath());
        verify(commonFileRepository).delete(testCommonFile);
    }

    @Test
    void testGeneratePresignedUrl_Success() throws Exception {
        // 准备测试数据
        String expectedUrl = "http://minio:9000/presigned-url";
        when(commonFileRepository.findById(1L)).thenReturn(Optional.of(testCommonFile));
        when(fileStorageService.generatePresignedUrl(anyString(), anyString(), eq(3600)))
                .thenReturn(expectedUrl);

        // 执行测试
        String result = commonFileService.generatePresignedUrl(1L, 3600);

        // 验证结果
        assertEquals(expectedUrl, result);
        verify(fileStorageService).generatePresignedUrl("common-files", testCommonFile.getFilePath(), 3600);
    }

    @Test
    void testQueryFiles_Success() {
        // 准备测试数据
        List<CommonFile> files = Arrays.asList(testCommonFile);
        when(commonFileRepository.findByStatusOrderByCreatedAtDesc(eq("ACTIVE"), any()))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(files));

        // 执行测试
        Map<String, Object> result = commonFileService.queryFiles(null, null, 1, 10);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("content"));
        assertTrue(result.containsKey("totalElements"));
        assertTrue(result.containsKey("totalPages"));
    }

    @Test
    void testUpdateFileMetadata_Success() throws Exception {
        // 准备测试数据
        Map<String, Object> newMetadata = new HashMap<>();
        newMetadata.put("updated", "true");

        when(commonFileRepository.findById(1L)).thenReturn(Optional.of(testCommonFile));
        when(commonFileRepository.save(any(CommonFile.class))).thenReturn(testCommonFile);

        // 执行测试
        assertDoesNotThrow(() -> {
            commonFileService.updateFileMetadata(1L, newMetadata);
        });

        // 验证方法调用
        verify(commonFileRepository).save(testCommonFile);
    }

    @Test
    void testGetFileStatistics_Success() {
        // 准备测试数据
        // 修复类型推断问题 - 明确指定泛型类型
        List<Object[]> countStats = Arrays.<Object[]>asList(new Object[] { "test-module", 5L });
        List<Object[]> sizeStats = Arrays.<Object[]>asList(new Object[] { "test-module", 1024L });

        when(commonFileRepository.countByModule()).thenReturn(countStats);
        when(commonFileRepository.sumFileSizeByModule()).thenReturn(sizeStats);
        when(commonFileRepository.countByModuleAndStatus(eq("test-module"), eq("ACTIVE")))
                .thenReturn(5L);

        // 执行测试
        Map<String, Object> result = commonFileService.getFileStatistics("test-module");

        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey("countByModule"));
        assertTrue(result.containsKey("sizeByModule"));
        assertTrue(result.containsKey("currentModuleCount"));
        assertEquals(5L, result.get("currentModuleCount"));
    }
}