package com.gmp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

/**
 * 测试用Spring Boot应用配置
 * 使用生产环境的配置类
 *
 * @author GMP系统开发团队
 */
@TestConfiguration
public class AuthServiceTestApplication {

    /**
     * 测试应用程序主入口
     * 用于集成测试
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceTestApplication.class, args);
    }
}
