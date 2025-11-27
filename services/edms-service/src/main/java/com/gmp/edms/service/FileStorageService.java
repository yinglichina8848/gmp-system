package com.gmp.edms.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file, String bucketName, String prefix) throws Exception;

    /**
     * 下载文件
     */
    InputStream downloadFile(String bucketName, String filePath) throws Exception;

    /**
     * 删除文件
     */
    void deleteFile(String bucketName, String filePath) throws Exception;

    /**
     * 批量删除文件
     */
    void batchDeleteFiles(String bucketName, List<String> filePaths) throws Exception;

    /**
     * 检查文件是否存在
     */
    boolean fileExists(String bucketName, String filePath);

    /**
     * 生成预签名URL
     */
    String generatePresignedUrl(String bucketName, String filePath, int expiration) throws Exception;

    /**
     * 获取文件信息
     */
    Map<String, Object> getFileInfo(String bucketName, String filePath) throws Exception;

    /**
     * 复制文件
     */
    void copyFile(String sourceBucket, String sourcePath, String targetBucket, String targetPath) throws Exception;

    /**
     * 创建存储桶
     */
    void createBucket(String bucketName) throws Exception;

    /**
     * 检查存储桶是否存在
     */
    boolean bucketExists(String bucketName) throws Exception;

    /**
     * 删除存储桶
     */
    void deleteBucket(String bucketName) throws Exception;

    /**
     * 列出所有存储桶
     */
    List<String> listBuckets() throws Exception;

    /**
     * 列出存储桶中的文件
     */
    List<String> listFiles(String bucketName, String prefix) throws Exception;

    /**
     * 获取文件大小
     */
    long getFileSize(String bucketName, String filePath) throws Exception;

    /**
     * 获取文件最后修改时间
     */
    long getFileLastModified(String bucketName, String filePath) throws Exception;

    /**
     * 获取文件内容类型
     */
    String getContentType(String bucketName, String filePath) throws Exception;

    /**
     * 创建目录
     */
    void createDirectory(String bucketName, String directoryPath) throws Exception;

    /**
     * 检查目录是否存在
     */
    boolean directoryExists(String bucketName, String directoryPath);

    /**
     * 删除目录
     */
    void deleteDirectory(String bucketName, String directoryPath) throws Exception;

    /**
     * 移动文件
     */
    void moveFile(String sourceBucket, String sourcePath, String targetBucket, String targetPath) throws Exception;

    /**
     * 获取存储桶存储使用情况
     */
    Map<String, Long> getBucketStorageInfo(String bucketName) throws Exception;
}
