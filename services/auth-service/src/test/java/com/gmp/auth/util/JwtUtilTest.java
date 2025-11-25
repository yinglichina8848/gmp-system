package com.gmp.auth.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String username;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        username = "testuser";
        userDetails = new User(username, "password", 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGenerateAccessToken_Success() {
        // 执行
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 验证
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtUtil.getUsernameFromToken(token));
    }

    @Test
    void testGenerateRefreshToken_Success() {
        // 执行
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        
        // 验证
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }

    @Test
    void testGenerateRefreshToken_NullUserDetails() {
        // 执行 & 验证
        assertThrows(Exception.class, () -> {
            jwtUtil.generateRefreshToken(null);
        });
    }

    @Test
    void testValidateToken_ValidToken() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        
        // 验证
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_NullToken() {
        // 执行
        boolean isValid = jwtUtil.validateToken(null, userDetails);
        
        // 验证
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        // 准备 - 使用无效格式的令牌
        String invalidToken = "invalid.jwt.token.format";
        
        // 执行
        boolean isValid = jwtUtil.validateToken(invalidToken, userDetails);
        
        // 验证
        assertFalse(isValid);
    }

    @Test
    void testGetUsernameFromToken_Success() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        
        // 验证
        assertEquals(username, extractedUsername);
    }

    @Test
    void testGetUsernameFromToken_InvalidToken() {
        // 准备 - 使用无效令牌
        String invalidToken = "invalid.jwt.token.format";
        
        // 执行 & 验证
        assertThrows(Exception.class, () -> {
            jwtUtil.getUsernameFromToken(invalidToken);
        });
    }

    // getOrganizationIdFromToken method doesn't exist in JwtUtil, test removed

    @Test
    void testGetExpirationDateFromToken_Success() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        Date extractedExpirationDate = jwtUtil.getExpirationDateFromToken(token);
        
        // 验证
        assertNotNull(extractedExpirationDate);
        // 确保过期时间在未来
        assertTrue(extractedExpirationDate.after(new Date()));
    }
    
    @Test
    void testIsTokenExpired_NotExpired() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        Boolean isExpired = jwtUtil.isTokenExpired(token);
        
        // 验证
        assertFalse(isExpired);
    }
    
    @Test
    void testIsTokenRevoked_NotRevoked() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        Boolean isRevoked = jwtUtil.isTokenRevoked(token);
        
        // 验证
        assertFalse(isRevoked);
    }
    
    @Test
    void testRevokeToken_Success() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        jwtUtil.revokeToken(token);
        Boolean isRevoked = jwtUtil.isTokenRevoked(token);
        
        // 验证
        assertTrue(isRevoked);
    }
    
    @Test
    void testGetTokenIdFromToken_Success() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        String tokenId = jwtUtil.getTokenIdFromToken(token);
        
        // 验证
        assertNotNull(tokenId);
        assertFalse(tokenId.isEmpty());
    }
    
    @Test
    void testParseTokenWithoutValidation_Success() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        Claims claims = jwtUtil.parseTokenWithoutValidation(token);
        
        // 验证
        assertNotNull(claims);
        assertEquals(username, claims.getSubject());
    }

    @Test
    void testValidateTokenForUserDetails_Success() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        
        // 验证
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenForUserDetails_WrongUsername() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 创建一个具有不同用户名的UserDetails对象
        UserDetails wrongUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username("wronguser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        
        // 执行
        boolean isValid = jwtUtil.validateToken(token, wrongUserDetails);
        
        // 验证
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenForUserDetails_NullToken() {
        // 执行
        boolean isValid = jwtUtil.validateToken(null, this.userDetails);
        
        // 验证
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenForUserDetails_NullUserDetails() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        boolean isValid = jwtUtil.validateToken(token, null);
        
        // 验证
        assertFalse(isValid);
    }

    @Test
    void testValidateTokenForUserDetails_ValidToken() {
        // 准备
        String token = jwtUtil.generateAccessToken(userDetails);
        
        // 执行
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        
        // 验证
        assertTrue(isValid);
    }
}
