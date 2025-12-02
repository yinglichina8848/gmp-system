package com.gmp.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JwtTokenProvider测试类
 */
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
    }

    @Test
    void testGenerateAccessToken_NormalUser() {
        String username = "testuser";
        Set<String> roles = Set.of("ROLE_USER");
        List<String> permissions = List.of("read", "write");
        
        String token = jwtTokenProvider.generateAccessToken(username, roles, permissions);
        assertEquals("access-token-testuser", token);
    }

    @Test
    void testGenerateAccessToken_EmptyRolesAndPermissions() {
        String username = "testuser";
        Set<String> roles = Collections.emptySet();
        List<String> permissions = Collections.emptyList();
        
        String token = jwtTokenProvider.generateAccessToken(username, roles, permissions);
        assertEquals("access-token-testuser", token);
    }

    @Test
    void testGenerateAccessToken_NullRolesAndPermissions() {
        String username = "testuser";
        Set<String> roles = null;
        List<String> permissions = null;
        
        String token = jwtTokenProvider.generateAccessToken(username, roles, permissions);
        assertEquals("access-token-testuser", token);
    }

    @Test
    void testGenerateRefreshToken_NormalUser() {
        String username = "testuser";
        String token = jwtTokenProvider.generateRefreshToken(username);
        assertEquals("refresh-token-testuser", token);
    }

    @Test
    void testGenerateRefreshToken_EmptyUsername() {
        String username = "";
        String token = jwtTokenProvider.generateRefreshToken(username);
        assertEquals("refresh-token-", token);
    }

    @Test
    void testGetAccessTokenExpirationTime() {
        long expirationTime = jwtTokenProvider.getAccessTokenExpirationTime();
        assertEquals(7200000, expirationTime);
    }

    @Test
    void testGenerateToken_NormalUser() {
        String username = "testuser";
        String token = jwtTokenProvider.generateToken(username);
        assertEquals("mock-jwt-token-testuser", token);
    }

    @Test
    void testGenerateToken_EmptyUsername() {
        String username = "";
        String token = jwtTokenProvider.generateToken(username);
        assertEquals("mock-jwt-token-", token);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = "mock-jwt-token-testuser";
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {
        String token = "invalid-token";
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_NullToken() {
        String token = null;
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_EmptyToken() {
        String token = "";
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testGetUsernameFromToken_ValidToken() {
        String token = "mock-jwt-token-testuser";
        String username = jwtTokenProvider.getUsernameFromToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testGetUsernameFromToken_InvalidToken() {
        String token = "invalid-token";
        String username = jwtTokenProvider.getUsernameFromToken(token);
        assertNull(username);
    }

    @Test
    void testGetUsernameFromToken_NullToken() {
        String token = null;
        String username = jwtTokenProvider.getUsernameFromToken(token);
        assertNull(username);
    }

    @Test
    void testGetUsernameFromToken_EmptyToken() {
        String token = "";
        String username = jwtTokenProvider.getUsernameFromToken(token);
        assertNull(username);
    }

    @Test
    void testValidateRefreshToken_ValidToken() {
        String token = "refresh-token-testuser";
        assertTrue(jwtTokenProvider.validateRefreshToken(token));
    }

    @Test
    void testValidateRefreshToken_InvalidToken() {
        String token = "invalid-refresh-token";
        assertFalse(jwtTokenProvider.validateRefreshToken(token));
    }

    @Test
    void testValidateRefreshToken_NullToken() {
        String token = null;
        assertFalse(jwtTokenProvider.validateRefreshToken(token));
    }

    @Test
    void testValidateRefreshToken_EmptyToken() {
        String token = "";
        assertFalse(jwtTokenProvider.validateRefreshToken(token));
    }

    @Test
    void testGetUsernameFromRefreshToken_ValidToken() {
        String token = "refresh-token-testuser";
        String username = jwtTokenProvider.getUsernameFromRefreshToken(token);
        assertEquals("testuser", username);
    }

    @Test
    void testGetUsernameFromRefreshToken_InvalidToken() {
        String token = "invalid-refresh-token";
        String username = jwtTokenProvider.getUsernameFromRefreshToken(token);
        assertNull(username);
    }

    @Test
    void testGetUsernameFromRefreshToken_NullToken() {
        String token = null;
        String username = jwtTokenProvider.getUsernameFromRefreshToken(token);
        assertNull(username);
    }

    @Test
    void testGetUsernameFromRefreshToken_EmptyToken() {
        String token = "";
        String username = jwtTokenProvider.getUsernameFromRefreshToken(token);
        assertNull(username);
    }
}