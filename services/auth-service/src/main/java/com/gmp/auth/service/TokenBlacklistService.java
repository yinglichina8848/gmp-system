package com.gmp.auth.service;

/**
 * 令牌黑名单服务
 * 用于管理已撤销的JWT令牌
 */
public interface TokenBlacklistService {
    
    /**
     * 将令牌加入黑名单
     * @param token JWT令牌
     * @param expiration 令牌过期时间（毫秒）
     */
    void blacklistToken(String token, long expiration);
    
    /**
     * 检查令牌是否在黑名单中
     * @param token JWT令牌
     * @return true表示令牌在黑名单中，false表示不在
     */
    boolean isTokenBlacklisted(String token);
    
    /**
     * 从黑名单中移除令牌
     * @param token JWT令牌
     */
    void removeTokenFromBlacklist(String token);
    
    /**
     * 清理过期的黑名单令牌
     */
    void cleanExpiredTokens();
}