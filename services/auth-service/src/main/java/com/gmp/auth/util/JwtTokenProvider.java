package com.gmp.auth.util;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * JWT令牌提供者
 */
@Component
public class JwtTokenProvider {
    
    // 生成访问令牌
    public String generateAccessToken(String username, Set<String> roles, List<String> permissions) {
        // 简单实现，实际项目中应使用更安全的JWT生成方式
        return "access-token-" + username;
    }
    
    // 生成刷新令牌
    public String generateRefreshToken(String username) {
        // 简单实现，实际项目中应使用更安全的JWT生成方式
        return "refresh-token-" + username;
    }
    
    // 获取访问令牌过期时间
    public long getAccessTokenExpirationTime() {
        // 返回令牌过期时间（毫秒），这里返回2小时
        return 7200000;
    }
    
    // 生成JWT令牌
    public String generateToken(String username) {
        // 简单实现，实际项目中应使用更安全的JWT生成方式
        return "mock-jwt-token-" + username;
    }
    
    // 验证JWT令牌
    public boolean validateToken(String token) {
        // 简单实现，实际项目中应验证签名、过期时间等
        return token != null && token.startsWith("mock-jwt-token-");
    }
    
    // 从令牌中获取用户名
    public String getUsernameFromToken(String token) {
        // 简单实现，实际项目中应从JWT声明中提取
        if (token != null && token.startsWith("mock-jwt-token-")) {
            return token.substring("mock-jwt-token-".length());
        }
        return null;
    }
    
    // 验证刷新令牌
    public boolean validateRefreshToken(String token) {
        // 简单实现，验证刷新令牌是否有效
        return token != null && token.startsWith("refresh-token-");
    }
    
    // 从刷新令牌中获取用户名
    public String getUsernameFromRefreshToken(String token) {
        // 简单实现，从刷新令牌中提取用户名
        if (token != null && token.startsWith("refresh-token-")) {
            return token.substring("refresh-token-".length());
        }
        return null;
    }
}