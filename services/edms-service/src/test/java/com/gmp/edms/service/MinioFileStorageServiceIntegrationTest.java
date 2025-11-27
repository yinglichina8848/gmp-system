package com.gmp.edms.service;

import io.minio.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MinIO文件存储服务集成测试
 */
@SpringBootTest
@ActiveProfiles("test")
public class MinioFileStorageServiceIntegrationTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private MinioClient minioClient;

    private static final String TEST_BUCKET = "test-bucket";
    private static final String TEST_PREFIX = "test/";

    @BeforeEach
    void setUp() throws Exception {
        // 创建测试桶（如果不存在）
        if (!bucketExists(TEST_BUCKET)) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(TEST_BUCKET)
                            .build()
            );
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        // 清理测试数据
        if (bucketExists(TEST_BUCKET)) {
            // 删除测试桶中的所有对象
            List<String> objects = listObjects(TEST_BUCKET, TEST_PREFIX);
            for (String object : objects) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(TEST_BUCKET)
                                .object(object)
                                .build()
                );
            }
        }
    }

    @Test
    void testUploadFile_Success() throws Exception {
        // 准备测试文件
        MultipartFile file = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                "Hello, MinIO!".getBytes()
        );

        // 执行上传
        String filePath = fileStorageService.uploadFile(file, TEST_BUCKET, TEST_PREFIX);

        // 验证结果
        assertNotNull(filePath);
        assertTrue(filePath.startsWith(TEST_PREFIX));
        
        // 验证文件确实存在于MinIO中
        assertTrue(fileExists(TEST_BUCKET, filePath));
        
        // 验证文件内容
        String content = readFileContent(TEST_BUCKET, filePath);
        assertEquals("Hello, MinIO!", content);
    }

    @Test
    void testDownloadFile_Success() throws Exception {
        // 先上传一个文件
        MultipartFile uploadFile = new MockMultipartFile(
                "download-test.txt",
                "download-test.txt",
                "text/plain",
                "Download test content".getBytes()
        );
        String filePath = fileStorageService.uploadFile(uploadFile, TEST_BUCKET, TEST_PREFIX);

        // 执行下载
        InputStream downloadStream = fileStorageService.downloadFile(TEST_BUCKET, filePath);

        // 验证结果
        assertNotNull(downloadStream);
        String content = new String(downloadStream.readAllBytes());
        assertEquals("Download test content", content);
    }

    @Test
    void testDeleteFile_Success() throws Exception {
        // 先上传一个文件
        MultipartFile uploadFile = new MockMultipartFile(
                "delete-test.txt",
                "delete-test.txt",
                "text/plain",
                "Delete test content".getBytes()
        );
        String filePath = fileStorageService.uploadFile(uploadFile, TEST_BUCKET, TEST_PREFIX);

        // 验证文件存在
        assertTrue(fileExists(TEST_BUCKET, filePath));

        // 执行删除
        fileStorageService.deleteFile(TEST_BUCKET, filePath);

        // 验证文件已删除
        assertFalse(fileExists(TEST_BUCKET, filePath));
    }

    @Test
    void testBatchDeleteFiles_Success() throws Exception {
        // 上传多个文件
        String[] filePaths = new String[3];
        for (int i = 0; i < 3; i++) {
            MultipartFile file = new MockMultipartFile(
                    "batch-test-" + i + ".txt",
                    "batch-test-" + i + ".txt",
                    "text/plain",
                    ("Batch test content " + i).getBytes()
            );
            filePaths[i] = fileStorageService.uploadFile(file, TEST_BUCKET, TEST_PREFIX);
        }

        // 验证所有文件都存在
        for (String filePath : filePaths) {
            assertTrue(fileExists(TEST_BUCKET, filePath));
        }

        // 执行批量删除
        fileStorageService.batchDeleteFiles(TEST_BUCKET, List.of(filePaths));

        // 验证所有文件都已删除
        for (String filePath : filePaths) {
            assertFalse(fileExists(TEST_BUCKET, filePath));
        }
    }

    @Test
    void testGeneratePresignedUrl_Success() throws Exception {
        // 上传一个文件
        MultipartFile file = new MockMultipartFile(
                "presigned-test.txt",
                "presigned-test.txt",
                "text/plain",
                "Presigned URL test".getBytes()
        );
        String filePath = fileStorageService.uploadFile(file, TEST_BUCKET, TEST_PREFIX);

        // 生成预签名URL
        String presignedUrl = fileStorageService.generatePresignedUrl(TEST_BUCKET, filePath, 3600);

        // 验证结果
        assertNotNull(presignedUrl);
        assertTrue(presignedUrl.contains(TEST_BUCKET));
        assertTrue(presignedUrl.contains(filePath));
    }

    @Test
    void testGetFileInfo_Success() throws Exception {
        // 上传一个文件
        MultipartFile file = new MockMultipartFile(
                "info-test.txt",
                "info-test.txt",
                "text/plain",
                "File info test".getBytes()
        );
        String filePath = fileStorageService.uploadFile(file, TEST_BUCKET, TEST_PREFIX);

        // 获取文件信息
        Map<String, Object> fileInfo = fileStorageService.getFileInfo(TEST_BUCKET, filePath);

        // 验证结果
        assertNotNull(fileInfo);
        assertTrue(fileInfo.containsKey("size"));
        assertTrue(fileInfo.containsKey("contentType"));
        assertTrue(fileInfo.containsKey("etag"));
        assertEquals(15L, fileInfo.get("size")); // "File info test".length()
        assertEquals("text/plain", fileInfo.get("contentType"));
    }

    @Test
    void testCopyFile_Success() throws Exception {
        // 上传源文件
        MultipartFile sourceFile = new MockMultipartFile(
                "copy-source.txt",
                "copy-source.txt",
                "text/plain",
                "Copy source content".getBytes()
        );
        String sourcePath = fileStorageService.uploadFile(sourceFile, TEST_BUCKET, TEST_PREFIX);
        String targetPath = TEST_PREFIX + "copy-target.txt";

        // 执行复制
        fileStorageService.copyFile(TEST_BUCKET, sourcePath, TEST_BUCKET, targetPath);

        // 验证目标文件存在且内容正确
        assertTrue(fileExists(TEST_BUCKET, targetPath));
        String targetContent = readFileContent(TEST_BUCKET, targetPath);
        assertEquals("Copy source content", targetContent);
        
        // 验证源文件仍然存在
        assertTrue(fileExists(TEST_BUCKET, sourcePath));
    }

    @Test
    void testMoveFile_Success() throws Exception {
        // 上传源文件
        MultipartFile sourceFile = new MockMultipartFile(
                "move-source.txt",
                "move-source.txt",
                "text/plain",
                "Move source content".getBytes()
        );
        String sourcePath = fileStorageService.uploadFile(sourceFile, TEST_BUCKET, TEST_PREFIX);
        String targetPath = TEST_PREFIX + "move-target.txt";

        // 执行移动
        fileStorageService.moveFile(TEST_BUCKET, sourcePath, TEST_BUCKET, targetPath);

        // 验证目标文件存在且内容正确
        assertTrue(fileExists(TEST_BUCKET, targetPath));
        String targetContent = readFileContent(TEST_BUCKET, targetPath);
        assertEquals("Move source content", targetContent);
        
        // 验证源文件已删除
        assertFalse(fileExists(TEST_BUCKET, sourcePath));
    }

    @Test
    void testCreateDirectory_Success() throws Exception {
        String directoryPath = TEST_PREFIX + "test-directory/";

        // 创建目录
        fileStorageService.createDirectory(TEST_BUCKET, directoryPath);

        // 验证目录存在
        assertTrue(directoryExists(TEST_BUCKET, directoryPath));
    }

    @Test
    void testListFiles_Success() throws Exception {
        // 上传几个文件
        for (int i = 0; i < 3; i++) {
            MultipartFile file = new MockMultipartFile(
                    "list-test-" + i + ".txt",
                    "list-test-" + i + ".txt",
                    "text/plain",
                    ("List test content " + i).getBytes()
            );
            fileStorageService.uploadFile(file, TEST_BUCKET, TEST_PREFIX);
        }

        // 列出文件
        List<String> files = fileStorageService.listFiles(TEST_BUCKET, TEST_PREFIX);

        // 验证结果
        assertNotNull(files);
        assertEquals(3, files.size());
        for (String filePath : files) {
            assertTrue(filePath.startsWith(TEST_PREFIX));
            assertTrue(fileExists(TEST_BUCKET, filePath));
        }
    }

    @Test
    void testGetBucketStorageInfo_Success() throws Exception {
        // 上传几个文件
        long totalSize = 0;
        for (int i = 0; i < 3; i++) {
            MultipartFile file = new MockMultipartFile(
                    "storage-test-" + i + ".txt",
                    "storage-test-" + i + ".txt",
                    "text/plain",
                    ("Storage test content " + i).getBytes()
            );
            String filePath = fileStorageService.uploadFile(file, TEST_BUCKET, TEST_PREFIX);
            totalSize += file.getSize();
        }

        // 获取存储信息
        Map<String, Long> storageInfo = fileStorageService.getBucketStorageInfo(TEST_BUCKET);

        // 验证结果
        assertNotNull(storageInfo);
        assertTrue(storageInfo.containsKey("objectCount"));
        assertTrue(storageInfo.containsKey("totalSize"));
        assertEquals(3L, storageInfo.get("objectCount"));
        assertEquals(totalSize, storageInfo.get("totalSize"));
    }

    // 辅助方法
    private boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
        } catch (Exception e) {
            return false;
        }
    }

    private boolean fileExists(String bucketName, String filePath) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePath)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean directoryExists(String bucketName, String directoryPath) {
        try {
            Iterable<Result<io.minio.messages.Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(directoryPath)
                            .maxKeys(1)
                            .build()
            );
            return results.iterator().hasNext();
        } catch (Exception e) {
            return false;
        }
    }

    private List<String> listObjects(String bucketName, String prefix) {
        List<String> objects = new ArrayList<>();
        try {
            Iterable<Result<io.minio.messages.Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );
            for (Result<io.minio.messages.Item> result : results) {
                objects.add(result.get().objectName());
            }
        } catch (Exception e) {
            // 忽略错误
        }
        return objects;
    }

    private String readFileContent(String bucketName, String filePath) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .build()
        );
        return new String(stream.readAllBytes());
    }
}