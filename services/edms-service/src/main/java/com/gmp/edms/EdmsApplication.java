package com.gmp.edms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @brief EDMS系统主应用类
 * 
 * @details 电子文档管理系统服务端应用入口，负责初始化Spring Boot上下文，
 * 启用服务发现、JPA仓库扫描和实体扫描。
 * 
 * @author GMP Team
 * @version 1.0.0
 * @since 2023-08-01
 * 
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.cloud.client.discovery.EnableDiscoveryClient
 * @see org.springframework.data.jpa.repository.config.EnableJpaRepositories
 * @see org.springframework.boot.autoconfigure.domain.EntityScan
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories("com.gmp.edms.repository")
@EntityScan("com.gmp.edms.entity")
public class EdmsApplication {
    
    /**
     * @brief 应用入口方法
     * 
     * @details 启动EDMS应用程序，初始化Spring Boot上下文和所有组件
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(EdmsApplication.class, args);
    }
}