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

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public List<String> listFiles(String bucketName, String prefix, boolean recursive) {
        if (bucketName == null || bucketName.isEmpty()) {
            bucketName = defaultBucket;
        }

        List<String> filePaths = new ArrayList<>();

        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(prefix)
                            .recursive(recursive)
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

    @Override
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

    /**
     * 计算文件MD5校验和
     */
    private String calculateFileChecksum(InputStream inputStream) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            md.update(buffer, 0, bytesRead);
        }

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
