package com.gmp.auth.dto;

/**
 * JWT令牌响应DTO
 *
 * @author GMP系统开发团队
 */
public class TokenResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    
    // 显式添加setter方法以避免Lombok编译问题
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
