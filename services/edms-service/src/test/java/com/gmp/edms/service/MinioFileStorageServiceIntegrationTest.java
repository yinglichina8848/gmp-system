package com.gmp.edms.service;

import com.gmp.edms.service.impl.MinioFileStorageServiceImpl;
import io.minio.*;
import io.minio.http.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * MinioFileStorageServiceImpl集成测试
 * 验证MinIO文件存储服务的核心功能
 */
class MinioFileStorageServiceIntegrationTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioFileStorageServiceImpl fileStorageService;

    @Captor
    private ArgumentCaptor<PutObjectArgs> putObjectArgsCaptor;

    @Captor
    private ArgumentCaptor<GetObjectArgs> getObjectArgsCaptor;

    @Captor
    private ArgumentCaptor<RemoveObjectArgs> removeObjectArgsCaptor;

    private MockMultipartFile testFile;
    private final String defaultBucket = "edms-files";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 直接设置服务的属性
        fileStorageService.setMinioClient(minioClient);
        fileStorageService.setDefaultBucket(defaultBucket);

        // 创建测试文件
        testFile = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "This is test content".getBytes(StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        // 清除模拟行为
        reset(minioClient, minioConfig);
    }

    /**
     * 测试文件上传功能
     */
    @Test
    void testUploadFile() throws Exception {
        // 准备模拟响应
        PutObjectResponse response = mock(PutObjectResponse.class);
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(response);

        // 执行上传
        String result = fileStorageService.uploadFile(testFile, defaultBucket, "edms", "documents");

        // 验证调用
        verify(minioClient).putObject(putObjectArgsCaptor.capture());
        PutObjectArgs args = putObjectArgsCaptor.getValue();

        // 验证参数
        assertEquals(defaultBucket, args.bucket());
        assertTrue(args.object().startsWith("edms/documents/"));
        assertTrue(args.object().endsWith(".txt"));

        // 验证返回结果
        assertNotNull(result);
        assertTrue(result.startsWith("edms/documents/"));
    }

    /**
     * 测试文件下载功能
     */
    @Test
    void testDownloadFile() throws Exception {
        // 准备测试数据
        String filePath = "edms/documents/" + UUID.randomUUID() + ".txt";
        String fileContent = "This is test content";

        // 模拟GetObjectResponse
        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(fileContent.getBytes(StandardCharsets.UTF_8));
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(response);

        // 执行下载
        byte[] result = fileStorageService.downloadFile(defaultBucket, filePath);

        // 验证调用
        verify(minioClient).getObject(getObjectArgsCaptor.capture());
        GetObjectArgs args = getObjectArgsCaptor.getValue();

        // 验证参数
        assertEquals(defaultBucket, args.bucket());
        assertEquals(filePath, args.object());

        // 验证返回内容
        assertNotNull(result);
        String resultContent = new String(result, StandardCharsets.UTF_8);
        assertEquals(fileContent, resultContent);
    }

    /**
     * 测试文件删除功能
     */
    @Test
    void testDeleteFile() throws Exception {
        // 准备测试数据
        String filePath = "edms/documents/" + UUID.randomUUID() + ".txt";

        // 模拟删除操作
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));

        // 执行删除
        fileStorageService.deleteFile(defaultBucket, filePath);

        // 验证调用
        verify(minioClient).removeObject(removeObjectArgsCaptor.capture());
        RemoveObjectArgs args = removeObjectArgsCaptor.getValue();

        // 验证参数
        assertEquals(defaultBucket, args.bucket());
        assertEquals(filePath, args.object());
    }

    /**
     * 测试生成预签名URL功能
     */
    @Test
    void testGeneratePresignedUrl() throws Exception {
        // 准备测试数据
        String filePath = "edms/documents/" + UUID.randomUUID() + ".txt";
        String expectedUrl = "http://localhost:9000/edms-files/" + filePath + "?X-Amz-Expires=3600&...";

        // 模拟生成URL
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class))).thenReturn(expectedUrl);

        // 执行生成URL
        String result = fileStorageService.generatePresignedUrl(defaultBucket, filePath, 3600);

        // 验证调用
        verify(minioClient).getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class));

        // 验证结果
        assertNotNull(result);
        assertEquals(expectedUrl, result);
    }

    /**
     * 测试获取文件信息功能
     */
    @Test
    void testGetFileInfo() throws Exception {
        // 准备测试数据
        String filePath = "edms/documents/test.txt";

        // 模拟获取对象信息
        StatObjectResponse statResponse = mock(StatObjectResponse.class);
        when(statResponse.size()).thenReturn(testFile.getSize());
        when(statResponse.contentType()).thenReturn("text/plain");
        when(minioClient.statObject(any(StatObjectArgs.class))).thenReturn(statResponse);

        // 执行获取文件信息
        Map<String, Object> result = fileStorageService.getFileInfo(defaultBucket, filePath);

        // 验证调用
        verify(minioClient).statObject(any(StatObjectArgs.class));

        // 验证结果
        assertNotNull(result);
        assertEquals("test.txt", result.get("fileName"));
        assertEquals("text/plain", result.get("contentType"));
        assertEquals(testFile.getSize(), result.get("size"));
    }

    /**
     * 测试批量上传文件功能
     */
    @Test
    void testBatchUploadFiles() throws Exception {
        // 准备测试数据
        MockMultipartFile file1 = new MockMultipartFile(
                "test1.txt",
                "test1.txt",
                "text/plain",
                "Content 1".getBytes(StandardCharsets.UTF_8));

        MockMultipartFile file2 = new MockMultipartFile(
                "test2.txt",
                "test2.txt",
                "text/plain",
                "Content 2".getBytes(StandardCharsets.UTF_8));

        // 模拟上传操作
        PutObjectResponse response = mock(PutObjectResponse.class);
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(response);

        // 执行批量上传
        Map<String, String> results = fileStorageService.batchUploadFiles(
                defaultBucket,
                Map.of("file1", file1, "file2", file2),
                "edms",
                "documents");

        // 验证调用次数
        verify(minioClient, times(2)).putObject(any(PutObjectArgs.class));

        // 验证结果
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.containsKey("file1"));
        assertTrue(results.containsKey("file2"));
        assertTrue(results.get("file1").startsWith("edms/documents/"));
        assertTrue(results.get("file2").startsWith("edms/documents/"));
    }
}
