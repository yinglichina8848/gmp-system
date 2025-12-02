package com.gmp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @brief GMP API Gateway 应用主类
 * 
 * @details 该类是GMP系统的API网关服务入口，提供微服务架构下的统一API入口，
 * 负责请求路由、负载均衡、认证授权、限流熔断等功能。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.cloud.client.discovery.EnableDiscoveryClient
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    /**
     * @brief 应用程序入口方法
     * 
     * @details 启动Gateway应用程序，初始化Spring Boot上下文
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
