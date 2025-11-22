package com.gmp.auth.dto;

import lombok.Data;

@Data
public class MfaEnableRequest {
    private String username;
    private String totpCode;
    private String secretKey; // 从前端传递回来的密钥，用于验证
    
    // 显式添加getter方法以避免Lombok编译问题
    public String getUsername() {
        return username;
    }
    
    public String getTotpCode() {
        return totpCode;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
}