package com.gmp.home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * GMP系统主页服务
 * 提供首页展示（v0.2.0 - 初始静态主页版本）
 * 验证微服务核心架构已正常部署
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeApplication.class, args);
    }
}
