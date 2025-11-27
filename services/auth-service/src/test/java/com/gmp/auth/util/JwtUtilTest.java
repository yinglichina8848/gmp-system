package com.gmp.auth.util;

import io.jsonwebtoken.Claims;
import org.assertj.core.data.Offset;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * JWT工具类单元测试
 * 覆盖JWT令牌生成、验证、解析等所有功能
 * 
 * 测试覆盖范围：
 * - JWT令牌生成
 * - JWT令牌验证
 * - JWT令牌解析
 * - 令牌过期检查
 * - 令牌撤销管理
 * - 刷新令牌处理
 * - 边界条件和异常情况
 *
 * @author GMP系统开发团队
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JWT工具类单元测试")
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String testSecret = "test-secret-key-for-jwt-unit-testing-that-is-long-enough";
    private String testUsername = "testuser";
    private Key testKey;

    @BeforeEach
    void setUp() {
        // 设置测试配置
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600L); // 1小时
        ReflectionTestUtils.setField(jwtUtil, "refreshExpiration", 86400L); // 24小时
        
        // 生成测试密钥
        testKey = Keys.hmacShaKeyFor(testSecret.getBytes());
    }

    @Test
    @DisplayName("生成访问令牌")
    void testGenerateToken() {
        // When
        String token = jwtUtil.generateToken(testUsername);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.").length).isEqualTo(3); // JWT应该有3个部分
        
        // 验证令牌内容
        String username = jwtUtil.getUsernameFromToken(token);
        assertThat(username).isEqualTo(testUsername);
    }

    @Test
    @DisplayName("生成刷新令牌")
    void testGenerateRefreshToken() {
        // When
        String refreshToken = jwtUtil.generateRefreshToken(testUsername);

        // Then
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken).isNotEmpty();
        assertThat(refreshToken.split("\\.").length).isEqualTo(3);
        
        // 验证令牌内容
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        assertThat(username).isEqualTo(testUsername);
    }

    @Test
    @DisplayName("从令牌获取用户名")
    void testGetUsernameFromToken() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        String username = jwtUtil.getUsernameFromToken(token);

        // Then
        assertThat(username).isEqualTo(testUsername);
    }

    @Test
    @DisplayName("从令牌获取过期时间")
    void testGetExpirationDateFromToken() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);

        // Then
        assertThat(expirationDate).isNotNull();
        assertThat(expirationDate).isAfter(new Date());
        
        // 验证过期时间大约在1小时后
        long expectedExpirationTime = System.currentTimeMillis() + 3600 * 1000;
        assertThat(expirationDate.getTime()).isCloseTo(expectedExpirationTime, Offset.offset(5000L)); // 允许5秒误差
    }

    @Test
    @DisplayName("从令牌获取令牌ID")
    void testGetTokenIdFromToken() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        String tokenId = jwtUtil.getTokenIdFromToken(token);

        // Then
        assertThat(tokenId).isNotNull();
        assertThat(tokenId).isNotEmpty();
    }

    @Test
    @DisplayName("验证有效令牌")
    void testValidateTokenValid() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        boolean isValid = jwtUtil.validateToken(token);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("验证无效令牌 - 空令牌")
    void testValidateTokenEmpty() {
        // When
        boolean isValid = jwtUtil.validateToken("");

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("验证无效令牌 - null令牌")
    void testValidateTokenNull() {
        // When
        boolean isValid = jwtUtil.validateToken(null);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("验证无效令牌 - 错误格式")
    void testValidateTokenMalformed() {
        // Given
        String malformedToken = "invalid.jwt.token";

        // When
        boolean isValid = jwtUtil.validateToken(malformedToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("验证过期令牌")
    void testValidateTokenExpired() throws InterruptedException {
        // Given - 创建一个立即过期的令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, testUsername);
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // 已过期
                .signWith(testKey)
                .compact();

        // When
        boolean isValid = jwtUtil.validateToken(expiredToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("检查令牌是否过期 - 未过期")
    void testIsTokenExpiredNotExpired() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        boolean isExpired = jwtUtil.isTokenExpired(token);

        // Then
        assertThat(isExpired).isFalse();
    }

    @Test
    @DisplayName("检查令牌是否过期 - 已过期")
    void testIsTokenExpiredExpired() {
        // Given - 创建过期令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, testUsername);
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(testKey)
                .compact();

        // When
        boolean isExpired = jwtUtil.isTokenExpired(expiredToken);

        // Then
        assertThat(isExpired).isTrue();
    }

    @Test
    @DisplayName("从令牌获取声明")
    void testGetAllClaimsFromToken() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        Claims claims = jwtUtil.parseToken(token);

        // Then
        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(testUsername);
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();
        assertThat(claims.getId()).isNotNull();
    }

    @Test
    @DisplayName("从令牌获取特定声明")
    void testGetClaimFromToken() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        String subject = jwtUtil.getClaimFromToken(token, Claims::getSubject);
        Date issuedAt = jwtUtil.getClaimFromToken(token, Claims::getIssuedAt);
        Date expiration = jwtUtil.getClaimFromToken(token, Claims::getExpiration);

        // Then
        assertThat(subject).isEqualTo(testUsername);
        assertThat(issuedAt).isNotNull();
        assertThat(expiration).isNotNull();
    }

    @Test
    @DisplayName("撤销令牌")
    void testRevokeToken() {
        // Given
        String token = jwtUtil.generateToken(testUsername);

        // When
        jwtUtil.revokeToken(token);

        // Then
        boolean isValid = jwtUtil.validateToken(token);
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("撤销多个令牌")
    void testRevokeMultipleTokens() {
        // Given
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        String token3 = jwtUtil.generateToken("user3");

        // When
        jwtUtil.revokeToken(token1);
        jwtUtil.revokeToken(token3);

        // Then
        assertThat(jwtUtil.validateToken(token1)).isFalse();
        assertThat(jwtUtil.validateToken(token2)).isTrue(); // 未撤销
        assertThat(jwtUtil.validateToken(token3)).isFalse();
    }

    @Test
    @DisplayName("撤销不存在的令牌")
    void testRevokeNonExistentToken() {
        // Given
        String nonExistentToken = "non.existent.token";

        // When & Then - 应该不抛出异常
        assertThatCode(() -> jwtUtil.revokeToken(nonExistentToken))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("生成带自定义声明的令牌")
    void testGenerateTokenWithClaims() {
        // Given
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("department", "IT");

        // When
        String token = jwtUtil.generateToken(extraClaims, testUsername);

        // Then
        assertThat(token).isNotNull();
        String username = jwtUtil.getUsernameFromToken(token);
        assertThat(username).isEqualTo(testUsername);
        
        Claims claims = jwtUtil.parseToken(token);
        assertThat(claims.get("role")).isEqualTo("ADMIN");
        assertThat(claims.get("department")).isEqualTo("IT");
    }

    @Test
    @DisplayName("令牌刷新 - 有效刷新令牌")
    void testRefreshTokenValid() {
        // Given
        String refreshToken = jwtUtil.generateRefreshToken(testUsername);

        // When
        String newToken = jwtUtil.refreshToken(refreshToken);

        // Then
        assertThat(newToken).isNotNull();
        assertThat(newToken).isNotEqualTo(refreshToken);
        String username = jwtUtil.getUsernameFromToken(newToken);
        assertThat(username).isEqualTo(testUsername);
    }

    @Test
    @DisplayName("令牌刷新 - 无效刷新令牌")
    void testRefreshTokenInvalid() {
        // Given
        String invalidRefreshToken = "invalid.refresh.token";

        // When & Then
        assertThatThrownBy(() -> jwtUtil.refreshToken(invalidRefreshToken))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("边界测试 - 极长用户名")
    void testEdgeCaseVeryLongUsername() {
        // Given
        String longUsername = "a".repeat(1000); // 1000个字符的用户名

        // When
        String token = jwtUtil.generateToken(longUsername);

        // Then
        assertThat(token).isNotNull();
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        assertThat(extractedUsername).isEqualTo(longUsername);
    }

    @Test
    @DisplayName("边界测试 - 特殊字符用户名")
    void testEdgeCaseSpecialCharacterUsername() {
        // Given
        String specialUsername = "test@user#$%^&*()_+-={}[]|\\:;\"'<>?,./";

        // When
        String token = jwtUtil.generateToken(specialUsername);

        // Then
        assertThat(token).isNotNull();
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        assertThat(extractedUsername).isEqualTo(specialUsername);
    }

    @Test
    @DisplayName("边界测试 - Unicode用户名")
    void testEdgeCaseUnicodeUsername() {
        // Given
        String unicodeUsername = "测试用户🔒";

        // When
        String token = jwtUtil.generateToken(unicodeUsername);

        // Then
        assertThat(token).isNotNull();
        String extractedUsername = jwtUtil.getUsernameFromToken(token);
        assertThat(extractedUsername).isEqualTo(unicodeUsername);
    }

    @Test
    @DisplayName("性能测试 - 大量令牌生成")
    void testPerformanceTokenGeneration() {
        // When
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            String token = jwtUtil.generateToken("user" + i);
            assertThat(token).isNotNull();
        }
        long endTime = System.currentTimeMillis();

        // Then
        assertThat(endTime - startTime).isLessThan(5000); // 应该在5秒内完成
    }

    @Test
    @DisplayName("性能测试 - 大量令牌验证")
    void testPerformanceTokenValidation() {
        // Given
        String[] tokens = new String[1000];
        for (int i = 0; i < 1000; i++) {
            tokens[i] = jwtUtil.generateToken("user" + i);
        }

        // When
        long startTime = System.currentTimeMillis();
        for (String token : tokens) {
            boolean isValid = jwtUtil.validateToken(token);
            assertThat(isValid).isTrue();
        }
        long endTime = System.currentTimeMillis();

        // Then
        assertThat(endTime - startTime).isLessThan(5000); // 应该在5秒内完成
    }

    @Test
    @DisplayName("并发测试 - 多线程令牌操作")
    void testConcurrentTokenOperations() throws InterruptedException {
        // Given
        int threadCount = 10;
        int tokensPerThread = 100;
        Thread[] threads = new Thread[threadCount];
        boolean[] results = new boolean[threadCount];

        // When
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < tokensPerThread; j++) {
                        String token = jwtUtil.generateToken("user" + threadIndex + "_" + j);
                        boolean isValid = jwtUtil.validateToken(token);
                        if (!isValid) {
                            results[threadIndex] = false;
                            return;
                        }
                    }
                    results[threadIndex] = true;
                } catch (Exception e) {
                    results[threadIndex] = false;
                }
            });
            threads[i].start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        // Then
        for (boolean result : results) {
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("安全性测试 - 令牌篡改检测")
    void testSecurityTokenTampering() {
        // Given
        String originalToken = jwtUtil.generateToken(testUsername);
        
        // 篡改令牌 - 修改签名部分
        String[] parts = originalToken.split("\\.");
        String tamperedToken = parts[0] + "." + parts[1] + ".tampered_signature";

        // When
        boolean isValid = jwtUtil.validateToken(tamperedToken);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("安全性测试 - 不同密钥生成的令牌验证")
    void testSecurityDifferentSecretTokens() {
        // Given - 使用不同的密钥生成令牌
        String differentSecret = "different-secret-key";
        Key differentKey = Keys.hmacShaKeyFor(differentSecret.getBytes());
        
        String tokenWithDifferentSecret = Jwts.builder()
                .setSubject(testUsername)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(differentKey)
                .compact();

        // When
        boolean isValid = jwtUtil.validateToken(tokenWithDifferentSecret);

        // Then
        assertThat(isValid).isFalse();
    }
}