package com.gmp.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * GMP认证系统配置属性
 * <p>
 * 用于管理认证系统的各种配置参数，包括初始化配置、JWT配置和测试配置
 */
@Data
@Validated
@ConfigurationProperties(prefix = "gmp.auth")
public class AuthSystemProperties {

    /**
     * 系统初始化配置
     */
    private Initialization initialization = new Initialization();

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 测试配置
     */
    private Test test = new Test();

    /**
     * 初始化配置
     */
    @Data
    public static class Initialization {
        /**
         * 是否跳过已存在的数据初始化
         */
        private boolean skipExisting = true;

        /**
         * 是否启用自动初始化
         */
        private boolean enabled = true;
    }

    /**
     * JWT配置
     */
    @Data
    public static class Jwt {
        /**
         * JWT密钥
         */
        private String secret = "default-secret-key-for-development-only"; // 开发环境默认密钥

        /**
         * 访问令牌有效期
         */
        private Duration expiration = Duration.ofHours(2);

        /**
         * 刷新令牌有效期
         */
        private Duration refreshExpiration = Duration.ofDays(7);

        /**
         * 令牌前缀
         */
        private String tokenPrefix = "Bearer ";

        /**
         * 令牌头名称
         */
        private String header = "Authorization";
    }

    /**
     * 测试配置
     */
    @Data
    public static class Test {
        /**
         * 是否启用测试模式
         */
        private boolean enabled = true;

        /**
         * 测试用户名前缀
         */
        private String userPrefix = "test_";
    }
}