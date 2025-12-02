package com.gmp.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 认证系统配置类
 */
@Configuration
@EnableConfigurationProperties(AuthSystemProperties.class)
public class AuthSystemConfig {
    // 配置属性由@EnableConfigurationProperties自动处理
}