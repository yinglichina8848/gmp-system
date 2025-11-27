package com.gmp.auth.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 登录响应DTO
 *
 * @author GMP系统开发团队
 */
public class LoginResponse {

    public String accessToken;
    public String refreshToken;
    public String tokenType;
    public Long expiresIn;
    public Long userId; // 添加用户ID
    public String username;
    public String fullName;
    public Long organizationId; // 添加组织ID
    public Set<String> roles; // 修改为Set确保唯一性
    public List<String> permissions;
    public LocalDateTime loginTime;
    
    /**
     * 用户可访问的子系统代码列表
     */
    public List<String> accessibleSubsystems;
    
    /**
     * 用户子系统访问级别映射
     * key: 子系统代码
     * value: 访问级别 (0-无访问权限, 1-只读, 2-读写, 3-管理)
     */
    public Map<String, Integer> subsystemAccessLevels;
    
    // 显式添加setter方法以避免Lombok编译问题
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    /**
     * 为兼容测试代码添加的方法，委托给setAccessToken
     */
    public void setToken(String token) {
        this.setAccessToken(token);
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setRoles(List<String> roles) {
        if (roles != null) {
            this.roles = new java.util.HashSet<>(roles);
        }
    }
    
    // 添加设置Set类型roles的方法
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
    
    // 添加userId的getter和setter
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    // 添加organizationId的getter和setter
    public Long getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }
    

    
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
    
    // 添加getter方法
    public String getAccessToken() {
        return accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public Long getExpiresIn() {
        return expiresIn;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public Set<String> getRoles() {
        return roles;
    }
    
    public List<String> getPermissions() {
        return permissions;
    }
    
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    
    public List<String> getAccessibleSubsystems() {
        return accessibleSubsystems;
    }
    
    public void setAccessibleSubsystems(List<String> accessibleSubsystems) {
        this.accessibleSubsystems = accessibleSubsystems;
    }
    
    public Map<String, Integer> getSubsystemAccessLevels() {
        return subsystemAccessLevels;
    }
    
    public void setSubsystemAccessLevels(Map<String, Integer> subsystemAccessLevels) {
        this.subsystemAccessLevels = subsystemAccessLevels;
    }
}
