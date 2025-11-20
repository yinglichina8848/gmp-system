package com.gmp.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录请求DTO
 *
 * @author GMP系统开发团队
 */
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Boolean rememberMe = false;

    private String captchaCode;
    
    // 添加额外字段以匹配AuthServiceImpl的使用
    private String ipAddress;
    private String userAgent;
    
    // 显式添加getter方法以避免Lombok编译问题
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public Boolean getRememberMe() {
        return rememberMe;
    }
    
    public String getCaptchaCode() {
        return captchaCode;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    // Setter methods
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
    
    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
