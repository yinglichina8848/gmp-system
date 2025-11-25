package com.gmp.auth.service.impl;

import com.gmp.auth.service.TokenBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * 令牌黑名单服务实现
 * 使用Redis存储已撤销的JWT令牌
 */
@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    
    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistServiceImpl.class);
    
    private static final String BLACKLIST_PREFIX = "token:blacklist:";
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Cacheable(value = "revokedTokens", key = "#token")
    public boolean isTokenBlacklisted(String token) {
        if (token == null) {
            return false;
        }
        
        // 使用Redis检查令牌是否在黑名单中
        String key = BLACKLIST_PREFIX + generateTokenKey(token);
        Boolean exists = redisTemplate.hasKey(key);
        return exists != null && exists;
    }
    
    @Override
    @CacheEvict(value = "revokedTokens", key = "#token")
    public void blacklistToken(String token, long expiration) {
        if (token == null) {
            logger.warn("尝试将空令牌加入黑名单");
            return;
        }
        
        try {
            // 生成令牌的键
            String key = BLACKLIST_PREFIX + generateTokenKey(token);
            
            // 计算过期时间
            long now = System.currentTimeMillis();
            long ttl = Math.max(1, expiration - now); // 确保至少有1毫秒的过期时间
            
            // 将令牌加入黑名单并设置过期时间
            redisTemplate.opsForValue().set(key, System.currentTimeMillis(), Duration.ofMillis(ttl));
            logger.debug("令牌已成功加入黑名单，过期时间: {}毫秒", ttl);
        } catch (Exception e) {
            logger.error("将令牌加入黑名单失败: {}", e.getMessage());
        }
    }
    
    @Override
    @CacheEvict(value = "revokedTokens", key = "#token")
    public void removeTokenFromBlacklist(String token) {
        if (token == null) {
            return;
        }
        
        try {
            String key = BLACKLIST_PREFIX + generateTokenKey(token);
            redisTemplate.delete(key);
            logger.debug("令牌已从黑名单中移除");
        } catch (Exception e) {
            logger.error("从黑名单中移除令牌失败: {}", e.getMessage());
        }
    }
    
    @Override
    public void cleanExpiredTokens() {
        // Redis会自动过期，不需要手动清理
        // 此方法保留用于兼容性和未来扩展
        logger.debug("清理过期的黑名单令牌");
    }
    
    /**
     * 生成令牌的键
     * 为了安全考虑，不对原始令牌进行哈希
     */
    private String generateTokenKey(String token) {
        // 使用UUID来确保键的唯一性
        // 在实际应用中，可以考虑使用令牌的签名部分或ID部分
        return UUID.nameUUIDFromBytes(token.getBytes()).toString();
    }
}