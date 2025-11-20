package com.gmp.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 测试用Spring Boot应用配置
 * 提供BCrypt密码编码器Bean用于测试
 *
 * @author GMP系统开发团队
 */
@TestConfiguration
public class AuthServiceTestApplication {

    /**
     * 密码编码器 - 与生产环境使用相同的Bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * 测试应用程序主入口
     * 用于集成测试
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceTestApplication.class, args);
    }
}
