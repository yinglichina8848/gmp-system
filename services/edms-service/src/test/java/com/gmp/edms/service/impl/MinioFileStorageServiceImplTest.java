package com.gmp.edms.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.ListObjectsV2Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MinioFileStorageServiceImplTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioFileStorageServiceImpl fileStorageService;

    private String bucketName = "test-bucket";
    private String objectName = "test/file.pdf";
    private MultipartFile multipartFile;
    private byte[] testData = "test content".getBytes();

    @BeforeEach
    void setUp() throws IOException {
        // 设置默认桶名
        fileStorageService.setBucketName(bucketName);
        
        // 创建测试文件
        multipartFile = new MockMultipartFile(
                "file.pdf",
                "file.pdf",
                "application/pdf",
                testData
        );
    }

    @Test
    void testUploadFile() throws Exception {
        // 准备
        String uniqueFileName = "test/" + System.currentTimeMillis() + "-file.pdf";
        when(minioClient.putObject(any(PutObjectArgs.class))).thenReturn(new ObjectWriteResponse(null, null, null, null, null, null, null, null));

        // 执行
        String result = fileStorageService.uploadFile("test/", multipartFile);

        // 验证
        assertNotNull(result);
        assertTrue(result.startsWith("test/"));
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testDownloadFile() throws Exception {
        // 准备
        InputStream inputStream = new ByteArrayInputStream(testData);
        GetObjectResponse response = mock(GetObjectResponse.class);
        when(response.readAllBytes()).thenReturn(testData);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(response);

        // 执行
        byte[] result = fileStorageService.downloadFile(objectName);

        // 验证
        assertNotNull(result);
        assertEquals(testData.length, result.length);
        verify(minioClient, times(1)).getObject(any(GetObjectArgs.class));
    }

    @Test
    void testDeleteFile() throws Exception {
        // 准备
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));

        // 执行
        fileStorageService.deleteFile(objectName);

        // 验证
        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void testBatchDeleteFiles() throws Exception {
        // 准备
        List<String> filePaths = List.of("test/file1.pdf", "test/file2.pdf");
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));

        // 执行
        fileStorageService.batchDeleteFiles(filePaths);

        // 验证
        verify(minioClient, times(2)).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void testFileExists() throws Exception {
        // 准备
        StatObjectResponse statObjectResponse = mock(StatObjectResponse.class);
        when(minioClient.statObject(any(StatObjectArgs.class))).thenReturn(statObjectResponse);

        // 执行
        boolean result = fileStorageService.fileExists(objectName);

        // 验证
        assertTrue(result);
        verify(minioClient, times(1)).statObject(any(StatObjectArgs.class));
    }

    @Test
    void testFileExists_FileNotFound() throws Exception {
        // 准备
        when(minioClient.statObject(any(StatObjectArgs.class))).thenThrow(ErrorResponseException.class);

        // 执行
        boolean result = fileStorageService.fileExists(objectName);

        // 验证
        assertFalse(result);
    }

    @Test
    void testGeneratePresignedUrl() throws Exception {
        // 准备
        String expectedUrl = "https://minio.example.com/test-bucket/test/file.pdf?presigned=true";
        when(minioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class))).thenReturn(expectedUrl);

        // 执行
        String result = fileStorageService.generatePresignedUrl(objectName, 3600);

        // 验证
        assertEquals(expectedUrl, result);
        verify(minioClient, times(1)).getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class));
    }

    @Test
    void testGetFileInfo() throws Exception {
        // 准备
        StatObjectResponse statObjectResponse = mock(StatObjectResponse.class);
        when(statObjectResponse.size()).thenReturn(1024L);
        when(statObjectResponse.contentType()).thenReturn("application/pdf");
        when(minioClient.statObject(any(StatObjectArgs.class))).thenReturn(statObjectResponse);

        // 执行
        Object result = fileStorageService.getFileInfo(objectName);

        // 验证
        assertNotNull(result);
        verify(minioClient, times(1)).statObject(any(StatObjectArgs.class));
    }

    @Test
    void testCopyFile() throws Exception {
        // 准备
        String sourceObjectName = "test/source.pdf";
        String targetObjectName = "test/target.pdf";
        when(minioClient.copyObject(any(CopyObjectArgs.class))).thenReturn(new CopyObjectResponse(null, null));

        // 执行
        String result = fileStorageService.copyFile(sourceObjectName, targetObjectName);

        // 验证
        assertEquals(targetObjectName, result);
        verify(minioClient, times(1)).copyObject(any(CopyObjectArgs.class));
    }

    @Test
    void testMoveFile() throws Exception {
        // 准备
        String sourceObjectName = "test/source.pdf";
        String targetObjectName = "test/target.pdf";
        when(minioClient.copyObject(any(CopyObjectArgs.class))).thenReturn(new CopyObjectResponse(null, null));
        doNothing().when(minioClient).removeObject(any(RemoveObjectArgs.class));

        // 执行
        String result = fileStorageService.moveFile(sourceObjectName, targetObjectName);

        // 验证
        assertEquals(targetObjectName, result);
        verify(minioClient, times(1)).copyObject(any(CopyObjectArgs.class));
        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void testListFiles() throws Exception {
        // 准备
        String prefix = "test/";
        ListObjectsV2Response response = mock(ListObjectsV2Response.class);
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        
        when(item1.objectName()).thenReturn("test/file1.pdf");
        when(item2.objectName()).thenReturn("test/file2.pdf");
        
        Iterable<ListObjectsV2Response> responses = () -> List.of(response).iterator();
        when(response.items()).thenReturn(List.of(item1, item2));
        when(minioClient.listObjects(any(ListObjectsArgs.class))).thenReturn(responses);

        // 执行
        List<String> result = fileStorageService.listFiles(prefix);

        // 验证
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(minioClient, times(1)).listObjects(any(ListObjectsArgs.class));
    }

    @Test
    void testCreateBucket() throws Exception {
        // 准备
        String newBucketName = "new-bucket";
        doNothing().when(minioClient).makeBucket(any(MakeBucketArgs.class));

        // 执行
        fileStorageService.createBucket(newBucketName);

        // 验证
        verify(minioClient, times(1)).makeBucket(any(MakeBucketArgs.class));
    }

    @Test
    void testDeleteBucket() throws Exception {
        // 准备
        String bucketToDelete = "delete-bucket";
        doNothing().when(minioClient).removeBucket(any(RemoveBucketArgs.class));

        // 执行
        fileStorageService.deleteBucket(bucketToDelete);

        // 验证
        verify(minioClient, times(1)).removeBucket(any(RemoveBucketArgs.class));
    }

    @Test
    void testBucketExists() throws Exception {
        // 准备
        when(minioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        // 执行
        boolean result = fileStorageService.bucketExists(bucketName);

        // 验证
        assertTrue(result);
        verify(minioClient, times(1)).bucketExists(any(BucketExistsArgs.class));
    }

    @Test
    void testGetBucketList() throws Exception {
        // 准备
        Bucket bucket1 = new Bucket("bucket1", null, null);
        Bucket bucket2 = new Bucket("bucket2", null, null);
        when(minioClient.listBuckets()).thenReturn(List.of(bucket1, bucket2));

        // 执行
        List<String> result = fileStorageService.getBucketList();

        // 验证
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("bucket1"));
        assertTrue(result.contains("bucket2"));
        verify(minioClient, times(1)).listBuckets();
    }

    @Test
    void testGetFileChecksum() throws Exception {
        // 准备
        StatObjectResponse statObjectResponse = mock(StatObjectResponse.class);
        when(statObjectResponse.etag()).thenReturn("test-checksum");
        when(minioClient.statObject(any(StatObjectArgs.class))).thenReturn(statObjectResponse);

        // 执行
        String result = fileStorageService.getFileChecksum(objectName);

        // 验证
        assertEquals("test-checksum", result);
        verify(minioClient, times(1)).statObject(any(StatObjectArgs.class));
    }

    // 内部Bucket类用于测试
    private static class Bucket {
        private String name;
        private Object creationDate;
        private Object owner;

        public Bucket(String name, Object creationDate, Object owner) {
            this.name = name;
            this.creationDate = creationDate;
            this.owner = owner;
        }

        public String name() {
            return name;
        }
    }
}
