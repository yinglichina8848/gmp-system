package com.gmp.auth.dto;

import lombok.Data;

@Data
public class MfaVerifyRequest {
    private String sessionId; // 用户输入的TOTP验证码
    private String totpCode;
    
    // 显式添加getter方法以避免Lombok编译问题
    public String getSessionId() {
        return sessionId;
    }
    
    public String getTotpCode() {
        return totpCode;
    }
}