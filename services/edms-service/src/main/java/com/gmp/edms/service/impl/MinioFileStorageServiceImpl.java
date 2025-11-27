package com.gmp.edms.service.impl;

import com.gmp.edms.service.FileStorageService;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.GetObjectArgs;
import io.minio.ListBucketsArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteArgs;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * MinIO文件存储服务实现
 */
@Service
public class MinioFileStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(MinioFileStorageServiceImpl.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.default-bucket}")
    private String defaultBucket;

    @Override
    public String uploadFile(MultipartFile file, String bucketName, String prefix) throws Exception {
        // 如果没有指定桶名，使用默认桶
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        // 确保桶存在
        if (!bucketExists(bucketName)) {
            createBucket(bucketName);
        }

        // 生成唯一的文件名
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String filename = timestamp + "_" + uuid + (extension.isEmpty() ? "" : "." + extension);

        // 构建文件路径
        String filePath = prefix != null && !prefix.isEmpty()
                ? prefix.endsWith("/") ? prefix + filename : prefix + "/" + filename
                : filename;

        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .stream(file.getInputStream(), file.getSize(), ObjectWriteArgs.MIN_MULTIPART_SIZE)
                        .contentType(file.getContentType())
                        .build());

        return filePath;
    }

    @Override
    public InputStream downloadFile(String bucketName, String filePath) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .build());
    }

    @Override
    public void deleteFile(String bucketName, String filePath) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .build());
    }

    @Override
    public void batchDeleteFiles(String bucketName, List<String> filePaths) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        List<DeleteObject> deleteObjects = filePaths.stream()
                .map(filePath -> new DeleteObject(filePath))
                .collect(Collectors.toList());

        Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucketName)
                        .objects(deleteObjects)
                        .build());

        // 检查是否有删除失败的文件
        List<String> failedDeletions = new ArrayList<>();
        for (Result<DeleteError> result : results) {
            try {
                DeleteError error = result.get();
                failedDeletions.add(error.objectName() + ": " + error.message());
            } catch (Exception e) {
                failedDeletions.add("Unknown error: " + e.getMessage());
            }
        }

        if (!failedDeletions.isEmpty()) {
            throw new Exception("Some files failed to delete: " + String.join(", ", failedDeletions));
        }
    }

    @Override
    public boolean fileExists(String bucketName, String filePath) {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePath)
                            .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generatePresignedUrl(String bucketName, String filePath, int expiration) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        // 生成预签名URL，过期时间以秒为单位
        return minioClient.getPresignedObjectUrl(
                io.minio.GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(filePath)
                        .expiry(expiration)
                        .build());
    }

    @Override
    public Map<String, Object> getFileInfo(String bucketName, String filePath) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        io.minio.StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .build());

        Map<String, Object> fileInfo = new HashMap<>();
        fileInfo.put("size", stat.size());
        fileInfo.put("contentType", stat.contentType());
        fileInfo.put("etag", stat.etag());
        fileInfo.put("lastModified", stat.lastModified());
        fileInfo.put("versionId", stat.versionId());

        return fileInfo;
    }

    @Override
    public void copyFile(String sourceBucket, String sourcePath, String targetBucket, String targetPath)
            throws Exception {
        if (sourceBucket == null || sourceBucket.isEmpty()) {
            sourceBucket = defaultBucket;
        }
        if (targetBucket == null || targetBucket.isEmpty()) {
            targetBucket = defaultBucket;
        }

        minioClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder().bucket(sourceBucket).object(sourcePath).build())
                        .bucket(targetBucket)
                        .object(targetPath)
                        .build());
    }

    @Override
    public void moveFile(String sourceBucket, String sourcePath, String targetBucket, String targetPath)
            throws Exception {
        // 先复制文件
        copyFile(sourceBucket, sourcePath, targetBucket, targetPath);

        // 再删除源文件
        deleteFile(sourceBucket, sourcePath);
    }

    @Override
    public List<String> listFiles(String bucketName, String prefix) {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        List<String> filePaths = new ArrayList<>();

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(true)
                            .build());

            for (Result<Item> result : results) {
                Item item = result.get();
                filePaths.add(item.objectName());
            }
        } catch (Exception e) {
            // 记录错误但不抛出异常
            e.printStackTrace();
        }

        return filePaths;
    }

    @Override
    public void createBucket(String bucketName) throws Exception {
        minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
    }

    @Override
    public void deleteBucket(String bucketName) throws Exception {
        minioClient.removeBucket(
                RemoveBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
    }

    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(bucketName)
                            .build());
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> listBuckets() {
        List<String> bucketNames = new ArrayList<>();

        try {
            List<Bucket> buckets = minioClient.listBuckets(ListBucketsArgs.builder().build());
            for (Bucket bucket : buckets) {
                bucketNames.add(bucket.name());
            }
        } catch (Exception e) {
            // 记录错误但不抛出异常
            e.printStackTrace();
        }

        return bucketNames;
    }

    @Override
    public boolean directoryExists(String bucketName, String directoryPath) {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        try {
            // 确保目录路径以斜杠结尾
            String normalizedPath = directoryPath;
            if (!normalizedPath.endsWith("/")) {
                normalizedPath = normalizedPath + "/";
            }

            // 列出对象并检查是否存在以指定前缀开头的对象
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(normalizedPath)
                            .maxKeys(1) // 只需要检查是否存在至少一个对象
                            .build());

            // 如果有结果，则目录存在
            return results.iterator().hasNext();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void createDirectory(String bucketName, String directoryPath) {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        // 在MinIO中创建目录实际上是创建一个空对象，以/结尾
        String normalizedPath = directoryPath.endsWith("/") ? directoryPath : directoryPath + "/";

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(normalizedPath)
                    .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                    .contentType("application/x-directory")
                    .build());
        } catch (io.minio.errors.ErrorResponseException | io.minio.errors.InsufficientDataException | 
                 io.minio.errors.InternalException | io.minio.errors.InvalidResponseException | 
                 io.minio.errors.ServerException | io.minio.errors.XmlParserException | 
                 java.io.IOException | java.security.NoSuchAlgorithmException | 
                 java.security.InvalidKeyException e) {
            throw new RuntimeException("Failed to create directory: " + directoryPath, e);
        }
    }

    @Override
    public void deleteDirectory(String bucketName, String directoryPath) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        // 列出目录下所有文件
        List<String> files = listFiles(bucketName, directoryPath);

        // 批量删除文件
        if (!files.isEmpty()) {
            batchDeleteFiles(bucketName, files);
        }
    }

    @Override
    public String getContentType(String bucketName, String filePath) {
        // 简化实现，根据文件扩展名返回内容类型
        String contentType = "application/octet-stream";
        if (filePath != null) {
            if (filePath.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filePath.endsWith(".doc") || filePath.endsWith(".docx")) {
                contentType = "application/msword";
            } else if (filePath.endsWith(".xls") || filePath.endsWith(".xlsx")) {
                contentType = "application/vnd.ms-excel";
            } else if (filePath.endsWith(".ppt") || filePath.endsWith(".pptx")) {
                contentType = "application/vnd.ms-powerpoint";
            } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filePath.endsWith(".png")) {
                contentType = "image/png";
            } else if (filePath.endsWith(".gif")) {
                contentType = "image/gif";
            } else if (filePath.endsWith(".txt")) {
                contentType = "text/plain";
            } else if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
                contentType = "text/html";
            } else if (filePath.endsWith(".zip")) {
                contentType = "application/zip";
            } else if (filePath.endsWith(".rar")) {
                contentType = "application/x-rar-compressed";
            }
        }
        return contentType;
    }

    @Override
    public long getFileLastModified(String bucketName, String filePath) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        io.minio.StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(filePath)
                        .build());
        return stat.lastModified().toInstant().toEpochMilli();
    }

    @Override
    public long getFileSize(String bucketName, String filePath) throws Exception {
        if (bucketName == null || bucketName.trim().isEmpty()) {
            bucketName = defaultBucket;
        }

        try {
            io.minio.StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePath)
                            .build());
            return stat.size();
        } catch (Exception e) {
            log.error("Error getting file size for bucket: {} and path: {}", bucketName, filePath, e);
            throw e;
        }
    }

    @Override
    public Map<String, Long> getBucketStorageInfo(String bucketName) throws Exception {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        // 检查桶是否存在
        if (!bucketExists(bucketName)) {
            throw new Exception("Bucket does not exist: " + bucketName);
        }

        // 统计桶中的对象数量和总大小
        long totalSize = 0;
        int objectCount = 0;

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .recursive(true)
                        .build());

        for (Result<Item> result : results) {
            Item item = result.get();
            if (!item.isDir()) {
                totalSize += item.size();
                objectCount++;
            }
        }

        Map<String, Long> storageInfo = new HashMap<>();
        storageInfo.put("objectCount", (long) objectCount);
        storageInfo.put("totalSize", totalSize);

        return storageInfo;
    }
}