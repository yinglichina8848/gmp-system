package com.gmp.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 认证系统配置类
 */
@Configuration
@EnableConfigurationProperties(AuthSystemProperties.class)
public class AuthSystemConfig {
    
    /**
     * 配置认证系统属性
     */
    @Bean
    public AuthSystemProperties authSystemProperties() {
        return new AuthSystemProperties();
    }
}