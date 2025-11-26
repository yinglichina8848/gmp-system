package com.gmp.auth.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * Redis实现的Token黑名单服务
 */
public class RedisTokenBlacklistService implements TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    
    public RedisTokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public void blacklistToken(String token, long expirationTime) {
        long currentTime = System.currentTimeMillis();
        if (token != null && expirationTime > currentTime) {
            long expireSeconds = (expirationTime - currentTime) / 1000;
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            valueOps.set("blacklist:" + token, "1", expireSeconds, TimeUnit.SECONDS);
        }
    }
    
    @Override
    public boolean isTokenBlacklisted(String token) {
        if (token == null) {
            return false;
        }
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        String value = valueOps.get("blacklist:" + token);
        return "1".equals(value);
    }
}