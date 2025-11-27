package com.gmp.edms.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO配置类
 */
@Configuration
public class MinioConfiguration {

    @Value("${minio.url}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.default-bucket}")
    private String defaultBucket;

    /**
     * 创建MinIO客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 获取默认存储桶名称
     */
    @Bean(name = "defaultBucketName")
    public String defaultBucketName() {
        return defaultBucket;
    }

    /**
     * 获取EDMS专用存储桶名称
     */
    @Bean(name = "edmsBucketName")
    public String edmsBucketName() {
        return bucketName;
    }
}