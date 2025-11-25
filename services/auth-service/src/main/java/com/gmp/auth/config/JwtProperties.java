package com.gmp.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT配置属性类
 * 用于从配置文件中加载JWT相关配置
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    /**
     * JWT签名密钥
     */
    private String secret = "default-secret-key-please-change-in-production";
    
    /**
     * 访问令牌过期时间（秒）
     */
    private long expiration = 3600; // 默认1小时
    
    /**
     * 刷新令牌过期时间（秒）
     */
    private long refreshExpiration = 86400; // 默认1天
    
    /**
     * 令牌前缀
     */
    private String tokenPrefix = "Bearer ";
    
    /**
     * 认证头名称
     */
    private String authHeader = "Authorization";

    // getter and setter methods
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getAuthHeader() {
        return authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }
}