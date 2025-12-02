package com.gmp.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// 接口和实现应该在单独的文件中定义，这里只保留测试类
@ExtendWith(MockitoExtension.class)
public class TokenBlacklistServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisTokenBlacklistService tokenBlacklistService;

    private String testToken;
    private long expirationTime;

    @BeforeEach
    void setUp() {
        testToken = "test-jwt-token";
        expirationTime = System.currentTimeMillis() + 3600000; // 1小时后过期
        
        // 模拟RedisTemplate的opsForValue()方法返回我们的mock，使用lenient允许不必要的stubbing
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testBlacklistToken_Success() {
        // 准备
        // 计算token应该在Redis中存在的时间（以秒为单位）
        long expireSeconds = (expirationTime - System.currentTimeMillis()) / 1000;
        
        // 执行
        tokenBlacklistService.blacklistToken(testToken, expirationTime);
        
        // 验证
        verify(valueOperations).set("blacklist:" + testToken, "1", expireSeconds, TimeUnit.SECONDS);
    }

    @Test
    void testBlacklistToken_AlreadyExpired() {
        // 准备 - 设置一个已经过期的时间戳
        long expiredTime = System.currentTimeMillis() - 1000; // 1秒前
        
        // 执行 - 应该不会调用Redis操作
        tokenBlacklistService.blacklistToken(testToken, expiredTime);
        
        // 验证 - 确认没有调用Redis
        verify(valueOperations, never()).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    void testIsTokenBlacklisted_TokenIsBlacklisted() {
        // 准备
        when(valueOperations.get("blacklist:" + testToken)).thenReturn("1");
        
        // 执行
        boolean isBlacklisted = tokenBlacklistService.isTokenBlacklisted(testToken);
        
        // 验证
        assertTrue(isBlacklisted);
        verify(valueOperations).get("blacklist:" + testToken);
    }

    @Test
    void testIsTokenBlacklisted_TokenNotBlacklisted() {
        // 准备
        when(valueOperations.get("blacklist:" + testToken)).thenReturn(null);
        
        // 执行
        boolean isBlacklisted = tokenBlacklistService.isTokenBlacklisted(testToken);
        
        // 验证
        assertFalse(isBlacklisted);
        verify(valueOperations).get("blacklist:" + testToken);
    }

    @Test
    void testIsTokenBlacklisted_NullToken() {
        // 执行 - 空token应该始终返回false
        boolean isBlacklisted = tokenBlacklistService.isTokenBlacklisted(null);
        
        // 验证 - 确认没有调用Redis
        assertFalse(isBlacklisted);
        verify(valueOperations, never()).get(anyString());
    }

    @Test
    void testBlacklistTokenWithDifferentExpirations() {
        // 准备 - 测试不同过期时间的token
        String token1 = "token-1-hour"; // 1小时过期
        long expiration1 = System.currentTimeMillis() + 3600000;
        
        String token2 = "token-1-day"; // 1天过期
        long expiration2 = System.currentTimeMillis() + 86400000;
        
        String token3 = "token-10-minutes"; // 10分钟过期
        long expiration3 = System.currentTimeMillis() + 600000;
        
        // 执行
        tokenBlacklistService.blacklistToken(token1, expiration1);
        tokenBlacklistService.blacklistToken(token2, expiration2);
        tokenBlacklistService.blacklistToken(token3, expiration3);
        
        // 验证 - 确认每个token都被正确地加入黑名单，使用匹配器匹配所有参数
        verify(valueOperations).set(eq("blacklist:" + token1), eq("1"), anyLong(), eq(TimeUnit.SECONDS));
        verify(valueOperations).set(eq("blacklist:" + token2), eq("1"), anyLong(), eq(TimeUnit.SECONDS));
        verify(valueOperations).set(eq("blacklist:" + token3), eq("1"), anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void testBlacklistTokenWithZeroExpiration() {
        // 准备 - 设置零过期时间
        long zeroExpiration = 0;
        
        // 执行 - 应该不会调用Redis操作
        tokenBlacklistService.blacklistToken(testToken, zeroExpiration);
        
        // 验证 - 确认没有调用Redis
        verify(valueOperations, never()).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));
    }
}
