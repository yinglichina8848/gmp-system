package com.gmp.auth.dto;

import java.time.LocalDateTime;
import java.util.List;
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
    public String username;
    public String fullName;
    public List<String> roles;
    public List<String> permissions;
    public LocalDateTime loginTime;
    
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
        this.roles = roles;
    }
    
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
    
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}
