package com.gmp.auth.service.impl;

import com.gmp.auth.service.TokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * TokenBlacklistServiceImpl测试类
 */
class TokenBlacklistServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private TokenBlacklistServiceImpl tokenBlacklistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testIsTokenBlacklisted_NullToken() {
        boolean result = tokenBlacklistService.isTokenBlacklisted(null);
        assertFalse(result);
    }

    @Test
    void testIsTokenBlacklisted_ValidToken_NotBlacklisted() {
        String token = "valid-token";
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        
        boolean result = tokenBlacklistService.isTokenBlacklisted(token);
        assertFalse(result);
    }

    @Test
    void testIsTokenBlacklisted_ValidToken_Blacklisted() {
        String token = "blacklisted-token";
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        
        boolean result = tokenBlacklistService.isTokenBlacklisted(token);
        assertTrue(result);
    }

    @Test
    void testIsTokenBlacklisted_RedisReturnsNull() {
        String token = "test-token";
        when(redisTemplate.hasKey(anyString())).thenReturn(null);
        
        boolean result = tokenBlacklistService.isTokenBlacklisted(token);
        assertFalse(result);
    }

    @Test
    void testBlacklistToken_NullToken() {
        // 测试不抛出异常
        assertDoesNotThrow(() -> {
            tokenBlacklistService.blacklistToken(null, System.currentTimeMillis() + 3600000);
        });
    }

    @Test
    void testBlacklistToken_ValidToken() {
        String token = "test-token";
        long expiration = System.currentTimeMillis() + 3600000;
        
        assertDoesNotThrow(() -> {
            tokenBlacklistService.blacklistToken(token, expiration);
        });
        
        verify(valueOperations, times(1)).set(anyString(), anyLong(), any(Duration.class));
    }

    @Test
    void testBlacklistToken_ExpiredToken() {
        String token = "expired-token";
        long expiration = System.currentTimeMillis() - 3600000; // 已经过期
        
        assertDoesNotThrow(() -> {
            tokenBlacklistService.blacklistToken(token, expiration);
        });
        
        verify(valueOperations, times(1)).set(anyString(), anyLong(), any(Duration.class));
    }

    @Test
    void testBlacklistToken_RedisException() {
        String token = "test-token";
        long expiration = System.currentTimeMillis() + 3600000;
        
        doThrow(new RuntimeException("Redis error")).when(valueOperations).set(anyString(), anyLong(), any(Duration.class));
        
        assertDoesNotThrow(() -> {
            tokenBlacklistService.blacklistToken(token, expiration);
        });
    }

    @Test
    void testRemoveTokenFromBlacklist_NullToken() {
        assertDoesNotThrow(() -> {
            tokenBlacklistService.removeTokenFromBlacklist(null);
        });
        
        verify(redisTemplate, never()).delete(anyString());
    }

    @Test
    void testRemoveTokenFromBlacklist_ValidToken() {
        String token = "test-token";
        
        assertDoesNotThrow(() -> {
            tokenBlacklistService.removeTokenFromBlacklist(token);
        });
        
        verify(redisTemplate, times(1)).delete(anyString());
    }

    @Test
    void testRemoveTokenFromBlacklist_RedisException() {
        String token = "test-token";
        
        doThrow(new RuntimeException("Redis error")).when(redisTemplate).delete(anyString());
        
        assertDoesNotThrow(() -> {
            tokenBlacklistService.removeTokenFromBlacklist(token);
        });
    }

    @Test
    void testCleanExpiredTokens() {
        assertDoesNotThrow(() -> {
            tokenBlacklistService.cleanExpiredTokens();
        });
    }
}
