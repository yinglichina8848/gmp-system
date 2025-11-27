package com.gmp.qms.security;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

/**
 * MCP主体，存储MCP客户端的身份信息和相关上下文数据
 * 
 * @author GMP系统开发团队
 */
@Getter
@ToString
@AllArgsConstructor
public class McpPrincipal {
    
    /**
     * 客户端唯一标识
     */
    private final String clientId;
    
    /**
     * 客户端类型（如：AI模型、系统服务等）
     */
    private final String clientType;
    
    /**
     * 原始JWT声明，包含所有认证信息
     */
    private final Claims claims;
    
    /**
     * 获取客户端的角色列表
     */
    public Set<String> getRoles() {
        Object roles = claims.get("roles");
        if (roles instanceof Set) {
            return (Set<String>) roles;
        }
        return Set.of();
    }
    
    /**
     * 获取客户端的权限列表
     */
    public Set<String> getPermissions() {
        Object permissions = claims.get("permissions");
        if (permissions instanceof Set) {
            return (Set<String>) permissions;
        }
        return Set.of();
    }
    
    /**
     * 获取客户端的上下文信息
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getContext() {
        Object context = claims.get("context");
        if (context instanceof Map) {
            return (Map<String, Object>) context;
        }
        return Map.of();
    }
    
    /**
     * 检查客户端是否有指定的角色
     */
    public boolean hasRole(String role) {
        return getRoles().contains(role);
    }
    
    /**
     * 检查客户端是否有指定的权限
     */
    public boolean hasPermission(String permission) {
        return getPermissions().contains(permission);
    }
    
    /**
     * 获取声明中的自定义属性
     */
    public <T> T getClaim(String key, Class<T> valueType) {
        return claims.get(key, valueType);
    }
    
    /**
     * 检查令牌是否过期
     */
    public boolean isExpired() {
        long now = System.currentTimeMillis() / 1000;
        Long expiration = claims.getExpiration().getTime() / 1000;
        return expiration < now;
    }
}
