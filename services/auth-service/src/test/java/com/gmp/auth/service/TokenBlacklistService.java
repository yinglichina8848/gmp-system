package com.gmp.auth.service;

/**
 * Token黑名单服务接口
 */
public interface TokenBlacklistService {
    /**
     * 将token加入黑名单
     * @param token 要加入黑名单的token
     * @param expirationTime token的过期时间戳
     */
    void blacklistToken(String token, long expirationTime);
    
    /**
     * 检查token是否在黑名单中
     * @param token 要检查的token
     * @return 如果token在黑名单中返回true，否则返回false
     */
    boolean isTokenBlacklisted(String token);
}