package com.gmp.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private UserDetails testUser;

    @BeforeEach
    void setUp() {
        // 手动初始化JwtUtil对象，而不是依赖Spring的依赖注入
        jwtUtil = new JwtUtil();
        
        // 手动设置JwtUtil的属性
        try {
            // 使用反射设置私有字段
            Field jwtSecretField = JwtUtil.class.getDeclaredField("jwtSecret");
            jwtSecretField.setAccessible(true);
            // 使用一个至少32个字符的密钥，满足JWT HMAC-SHA算法的256位要求
            jwtSecretField.set(jwtUtil, "test-secret-key-for-gmp-system-1234567890");
            
            Field expirationField = JwtUtil.class.getDeclaredField("expiration");
            expirationField.setAccessible(true);
            expirationField.set(jwtUtil, 3600L);
            
            Field refreshExpirationField = JwtUtil.class.getDeclaredField("refreshExpiration");
            refreshExpirationField.setAccessible(true);
            refreshExpirationField.set(jwtUtil, 86400L);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JwtUtil", e);
        }
        
        // 初始化测试用户
        testUser = new User(
                "testuser",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void testGenerateAccessToken() {
        // 执行
        String token = jwtUtil.generateAccessToken(testUser);
        
        // 验证
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // 验证令牌可以解析
        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testGenerateToken() {
        // 执行
        String token = jwtUtil.generateToken("testuser");
        
        // 验证
        assertNotNull(token);
        assertFalse(token.isEmpty());
        
        // 验证令牌可以解析
        String username = jwtUtil.getUsernameFromToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testGenerateRefreshToken() {
        // 执行
        String refreshToken = jwtUtil.generateRefreshToken(testUser);
        
        // 验证
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        
        // 验证令牌可以解析
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        assertEquals("testuser", username);
    }

    @Test
    void testGetUsernameFromToken() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        String username = jwtUtil.getUsernameFromToken(token);
        
        // 验证
        assertEquals("testuser", username);
    }

    @Test
    void testGetExpirationDateFromToken() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
        
        // 验证
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testIsTokenExpired() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        boolean isExpired = jwtUtil.isTokenExpired(token);
        
        // 验证
        assertFalse(isExpired);
    }

    @Test
    void testValidateToken() {
        // 准备
        String token = jwtUtil.generateToken(testUser.getUsername());
        
        // 执行
        boolean isValid = jwtUtil.validateToken(token, testUser);
        
        // 验证
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_InvalidToken() {
        // 准备
        String invalidToken = "invalid-token";
        
        // 执行
        boolean isValid = jwtUtil.validateToken(invalidToken, testUser);
        
        // 验证
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_NullToken() {
        // 执行
        boolean isValid = jwtUtil.validateToken(null, testUser);
        
        // 验证
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_SingleParameter() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        boolean isValid = jwtUtil.validateToken(token);
        
        // 验证
        assertTrue(isValid);
    }

    @Test
    void testRevokeToken() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        jwtUtil.revokeToken(token);
        boolean isRevoked = jwtUtil.isTokenRevoked(token);
        
        // 验证
        assertTrue(isRevoked);
    }

    @Test
    void testIsTokenRevoked() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        boolean isRevoked = jwtUtil.isTokenRevoked(token);
        
        // 验证
        assertFalse(isRevoked);
        
        // 撤销令牌
        jwtUtil.revokeToken(token);
        isRevoked = jwtUtil.isTokenRevoked(token);
        
        // 验证
        assertTrue(isRevoked);
    }

    @Test
    void testRefreshToken() {
        // 准备
        String refreshToken = jwtUtil.generateRefreshToken(testUser);
        
        // 执行
        String newToken = jwtUtil.refreshToken(refreshToken);
        
        // 验证
        assertNotNull(newToken);
        assertFalse(newToken.isEmpty());
        assertNotEquals(refreshToken, newToken);
        
        // 验证新令牌有效
        boolean isValid = jwtUtil.validateToken(newToken);
        assertTrue(isValid);
    }

    @Test
    void testCleanupRevokedTokens() {
        // 执行
        jwtUtil.cleanupRevokedTokens();
        
        // 验证 - 该方法目前只是记录日志，没有实际逻辑，所以只需要验证不抛出异常
        assertDoesNotThrow(() -> jwtUtil.cleanupRevokedTokens());
    }

    @Test
    void testGenerateTokenWithExtraClaims() {
        // 准备
        Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("customClaim", "customValue");
        extraClaims.put("numberClaim", 123);
        
        // 执行
        String token = jwtUtil.generateToken(extraClaims, "testuser");
        
        // 验证
        assertNotNull(token);
        
        // 验证额外声明存在
        Claims claims = jwtUtil.parseToken(token);
        assertEquals("customValue", claims.get("customClaim"));
        assertEquals(123, claims.get("numberClaim"));
    }

    @Test
    void testParseToken() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        Claims claims = jwtUtil.parseToken(token);
        
        // 验证
        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
        assertNotNull(claims.getExpiration());
        assertNotNull(claims.getId());
    }

    @Test
    void testParseTokenWithoutValidation() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        Claims claims = jwtUtil.parseTokenWithoutValidation(token);
        
        // 验证
        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void testGetTokenIdFromToken() {
        // 准备
        String token = jwtUtil.generateToken("testuser");
        
        // 执行
        String tokenId = jwtUtil.getTokenIdFromToken(token);
        
        // 验证
        assertNotNull(tokenId);
        assertFalse(tokenId.isEmpty());
    }
}
