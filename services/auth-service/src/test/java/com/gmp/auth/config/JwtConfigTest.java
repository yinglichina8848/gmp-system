package com.gmp.auth.config;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtConfig测试类
 * 测试JWT令牌配置和工具类的核心功能
 */
class JwtConfigTest {

    @InjectMocks
    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 设置测试配置 - 使用足够长度的密钥（至少256位）
        ReflectionTestUtils.setField(jwtConfig, "secret",
                "test-jwt-secret-key-very-secure-2024-long-key-for-gmp-system-auth-service-encryption");
        ReflectionTestUtils.setField(jwtConfig, "expiration", 3600000L); // 1小时
        ReflectionTestUtils.setField(jwtConfig, "refreshExpiration", 604800000L); // 7天
        ReflectionTestUtils.setField(jwtConfig, "issuer", "test-issuer");
    }

    @Test
    void testGenerateToken() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");
        permissions.add("write:user");

        // 执行测试
        String token = jwtConfig.generateToken(username, roles, permissions);

        // 验证结果
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGenerateRefreshToken() {
        // 准备测试数据
        String username = "testuser";

        // 执行测试
        String refreshToken = jwtConfig.generateRefreshToken(username);

        // 验证结果
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }

    @Test
    void testValidateToken() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 生成有效令牌
        String validToken = jwtConfig.generateToken(username, roles, permissions);

        // 执行测试
        boolean isValid = jwtConfig.validateToken(validToken);

        // 验证结果
        assertTrue(isValid);

        // 测试无效令牌
        String invalidToken = "invalid-jwt-token";
        boolean isInvalid = jwtConfig.validateToken(invalidToken);
        assertFalse(isInvalid);
    }

    @Test
    void testExtractUsername() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 生成令牌
        String token = jwtConfig.generateToken(username, roles, permissions);

        // 执行测试
        String extractedUsername = jwtConfig.extractUsername(token);

        // 验证结果
        assertEquals(username, extractedUsername);
    }

    @Test
    void testExtractRoles() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        roles.add("ADMIN");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 生成令牌
        String token = jwtConfig.generateToken(username, roles, permissions);

        // 执行测试
        Set<String> extractedRoles = new HashSet<>(jwtConfig.extractRoles(token));

        // 验证结果
        assertEquals(roles.size(), extractedRoles.size());
        assertTrue(extractedRoles.containsAll(roles));
    }

    @Test
    void testExtractPermissions() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");
        permissions.add("write:user");
        permissions.add("delete:user");

        // 生成令牌
        String token = jwtConfig.generateToken(username, roles, permissions);

        // 执行测试
        List<String> extractedPermissions = jwtConfig.extractPermissions(token);

        // 验证结果
        assertEquals(permissions.size(), extractedPermissions.size());
        assertTrue(extractedPermissions.containsAll(permissions));
    }

    @Test
    void testIsTokenNearExpiry() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 生成令牌
        String token = jwtConfig.generateToken(username, roles, permissions);

        // 执行测试 - 令牌刚生成，不应该即将过期
        boolean isNearExpiry = jwtConfig.isTokenNearExpiry(token, 300); // 5分钟阈值

        // 验证结果
        assertFalse(isNearExpiry);
    }

    @Test
    void testGetRemainingValidity() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 生成令牌
        String token = jwtConfig.generateToken(username, roles, permissions);

        // 执行测试
        long remainingValidity = jwtConfig.getRemainingValidity(token);

        // 验证结果
        assertTrue(remainingValidity > 0);
        assertTrue(remainingValidity <= 3600); // 不超过1小时
    }

    @Test
    void testRefreshAccessToken() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 生成刷新令牌
        String refreshToken = jwtConfig.generateRefreshToken(username);

        // 执行测试
        String newAccessToken = jwtConfig.refreshAccessToken(refreshToken, roles, permissions);

        // 验证结果
        assertNotNull(newAccessToken);
        assertFalse(newAccessToken.isEmpty());
    }

    @Test
    void testRefreshAccessTokenWithInvalidToken() {
        // 准备测试数据
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 执行测试 - 使用无效刷新令牌
        assertThrows(JwtException.class, () -> {
            jwtConfig.refreshAccessToken("invalid-refresh-token", roles, permissions);
        });
    }

    @Test
    void testRefreshAccessTokenWithWrongType() {
        // 准备测试数据
        String username = "testuser";
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        List<String> permissions = new ArrayList<>();
        permissions.add("read:user");

        // 生成访问令牌而不是刷新令牌
        String accessToken = jwtConfig.generateToken(username, roles, permissions);

        // 执行测试 - 使用错误类型的令牌
        assertThrows(JwtException.class, () -> {
            jwtConfig.refreshAccessToken(accessToken, roles, permissions);
        });
    }

    @Test
    void testGetExpiration() {
        // 执行测试
        long expiration = jwtConfig.getExpiration();

        // 验证结果
        assertEquals(3600000L, expiration);
    }

    @Test
    void testGetRefreshExpiration() {
        // 执行测试
        long refreshExpiration = jwtConfig.getRefreshExpiration();

        // 验证结果
        assertEquals(604800000L, refreshExpiration);
    }
}