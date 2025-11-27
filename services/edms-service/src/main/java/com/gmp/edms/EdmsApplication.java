package com.gmp.edms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * EDMS系统主应用类
 * 电子文档管理系统服务端应用入口
 * 
 * @author GMP Team
 * @version 1.0.0
 * @since 2023-08-01
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories("com.gmp.edms.repository")
@EntityScan("com.gmp.edms.entity")
public class EdmsApplication {
    
    /**
     * 应用入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EdmsApplication.class, args);
    }
}