package com.gmp.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.dto.PasswordChangeRequest;
import com.gmp.auth.dto.TokenResponse;
import com.gmp.auth.service.AuthService;
import com.gmp.auth.service.PasswordPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 认证控制器单元测试
 * 测试AuthController的所有REST API端点
 * 
 * 测试覆盖范围：
 * - 用户登录API
 * - 用户登出API
 * - 令牌刷新API
 * - 密码管理API
 * - 权限验证API
 * - 异常处理
 * - 输入验证
 *
 * @author GMP系统开发团队
 */
@WebMvcTest(AuthController.class)
@DisplayName("认证控制器单元测试")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private PasswordPolicyService passwordPolicyService;

    private LoginRequest validLoginRequest;
    private LoginResponse mockLoginResponse;
    private PasswordChangeRequest validPasswordChangeRequest;

    @BeforeEach
    void setUp() {
        // 设置有效的登录请求
        validLoginRequest = new LoginRequest();
        validLoginRequest.setUsername("testuser");
        validLoginRequest.setPassword("password123!");
        validLoginRequest.setMfaCode(null);

        // 设置模拟登录响应
        mockLoginResponse = new LoginResponse();
        mockLoginResponse.setToken("jwt-token");
        mockLoginResponse.setRefreshToken("refresh-token");
        mockLoginResponse.setTokenType("Bearer");
        mockLoginResponse.setExpiresIn(3600L);
        mockLoginResponse.setUsername("testuser");
        mockLoginResponse.setRoles(Arrays.asList("USER"));
        mockLoginResponse.setPermissions(Arrays.asList("READ_USER"));

        // 设置有效的密码修改请求
        validPasswordChangeRequest = new PasswordChangeRequest();
        validPasswordChangeRequest.setCurrentPassword("oldPassword123!");
        validPasswordChangeRequest.setNewPassword("newPassword123!");
        validPasswordChangeRequest.setConfirmPassword("newPassword123!");
    }

    @Test
    @DisplayName("用户登录成功")
    void testLoginSuccess() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class), anyString(), anyString()))
                .thenReturn(mockLoginResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest))
                        .header("X-Forwarded-For", "127.0.0.1")
                        .header("User-Agent", "TestAgent"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.roles[0]").value("USER"))
                .andExpect(jsonPath("$.permissions[0]").value("READ_USER"));
    }

    @Test
    @DisplayName("用户登录失败 - 无效凭据")
    void testLoginFailureInvalidCredentials() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class), anyString(), anyString()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("用户名或密码错误"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("认证失败"));
    }

    @Test
    @DisplayName("用户登录失败 - 缺少必填字段")
    void testLoginFailureMissingFields() throws Exception {
        // Given
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setUsername(""); // 空用户名
        invalidRequest.setPassword("password123!");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("用户登出成功")
    void testLogoutSuccess() throws Exception {
        // Given
        String token = "jwt-token";

        // When & Then
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("登出成功"));
    }

    @Test
    @DisplayName("用户登出失败 - 缺少授权头")
    void testLogoutFailureMissingAuthHeader() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("刷新令牌成功")
    void testRefreshTokenSuccess() throws Exception {
        // Given
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("new-access-token");
        tokenResponse.setRefreshToken("new-refresh-token");
        tokenResponse.setTokenType("Bearer");
        tokenResponse.setExpiresIn(3600);

        when(authService.refreshToken(anyString())).thenReturn(tokenResponse);

        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\": \"refresh-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    @DisplayName("刷新令牌失败 - 无效刷新令牌")
    void testRefreshTokenFailureInvalidToken() throws Exception {
        // Given
        when(authService.refreshToken(anyString()))
                .thenThrow(new com.gmp.auth.exception.TokenException("无效的刷新令牌"));

        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\": \"invalid-token\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("令牌无效"));
    }

    @Test
    @DisplayName("验证令牌成功")
    void testValidateTokenSuccess() throws Exception {
        // Given
        when(authService.validateToken(anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\": \"valid-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    @DisplayName("验证令牌失败 - 无效令牌")
    void testValidateTokenFailureInvalidToken() throws Exception {
        // Given
        when(authService.validateToken(anyString())).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\": \"invalid-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(false));
    }

    @Test
    @DisplayName("修改密码成功")
    void testChangePasswordSuccess() throws Exception {
        // Given
        when(authService.changePassword(anyString(), any(PasswordChangeRequest.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/auth/change-password")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPasswordChangeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("密码修改成功"));
    }

    @Test
    @DisplayName("修改密码失败 - 当前密码错误")
    void testChangePasswordFailureWrongCurrentPassword() throws Exception {
        // Given
        when(authService.changePassword(anyString(), any(PasswordChangeRequest.class)))
                .thenThrow(new com.gmp.auth.exception.InvalidPasswordException("当前密码错误"));

        // When & Then
        mockMvc.perform(post("/api/auth/change-password")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPasswordChangeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("密码错误"));
    }

    @Test
    @DisplayName("修改密码失败 - 新密码不符合策略")
    void testChangePasswordFailureInvalidNewPassword() throws Exception {
        // Given
        when(authService.changePassword(anyString(), any(PasswordChangeRequest.class)))
                .thenThrow(new com.gmp.auth.exception.InvalidPasswordException("新密码不符合复杂度要求"));

        // When & Then
        mockMvc.perform(post("/api/auth/change-password")
                        .header("Authorization", "Bearer valid-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPasswordChangeRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("密码错误"));
    }

    @Test
    @DisplayName("获取用户权限成功")
    void testGetUserPermissionsSuccess() throws Exception {
        // Given
        List<String> permissions = Arrays.asList("READ_USER", "WRITE_USER", "DELETE_USER");
        when(authService.getUserPermissions(anyString())).thenReturn(permissions);

        // When & Then
        mockMvc.perform(get("/api/auth/permissions")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("READ_USER"))
                .andExpect(jsonPath("$[1]").value("WRITE_USER"))
                .andExpect(jsonPath("$[2]").value("DELETE_USER"));
    }

    @Test
    @DisplayName("获取用户角色成功")
    void testGetUserRolesSuccess() throws Exception {
        // Given
        Set<String> roles = new HashSet<>(Arrays.asList("USER", "ADMIN"));
        when(authService.getUserRoles(anyString())).thenReturn(roles);

        // When & Then
        mockMvc.perform(get("/api/auth/roles")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("检查权限成功 - 有权限")
    void testCheckPermissionSuccessHasPermission() throws Exception {
        // Given
        when(authService.hasPermission(anyString(), anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/auth/check-permission")
                        .header("Authorization", "Bearer valid-token")
                        .param("permission", "READ_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasPermission").value(true));
    }

    @Test
    @DisplayName("检查权限成功 - 无权限")
    void testCheckPermissionSuccessNoPermission() throws Exception {
        // Given
        when(authService.hasPermission(anyString(), anyString())).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/auth/check-permission")
                        .header("Authorization", "Bearer valid-token")
                        .param("permission", "DELETE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasPermission").value(false));
    }

    @Test
    @DisplayName("检查角色成功 - 有角色")
    void testCheckRoleSuccessHasRole() throws Exception {
        // Given
        when(authService.hasRole(anyString(), anyString())).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/auth/check-role")
                        .header("Authorization", "Bearer valid-token")
                        .param("role", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasRole").value(true));
    }

    @Test
    @DisplayName("检查角色成功 - 无角色")
    void testCheckRoleSuccessNoRole() throws Exception {
        // Given
        when(authService.hasRole(anyString(), anyString())).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/auth/check-role")
                        .header("Authorization", "Bearer valid-token")
                        .param("role", "SUPER_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasRole").value(false));
    }

    @Test
    @DisplayName("获取密码复杂度要求成功")
    void testGetPasswordComplexityRequirementsSuccess() throws Exception {
        // Given
        String requirements = "密码必须满足以下要求：\n1. 长度至少8个字符\n2. 包含数字";
        when(passwordPolicyService.getPasswordComplexityRequirements()).thenReturn(requirements);

        // When & Then
        mockMvc.perform(get("/api/auth/password-requirements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.requirements").value(requirements));
    }

    @Test
    @DisplayName("边界测试 - 空JSON请求体")
    void testEdgeCaseEmptyJsonRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("边界测试 - 无效JSON格式")
    void testEdgeCaseInvalidJsonFormat() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("边界测试 - 缺少Content-Type头")
    void testEdgeCaseMissingContentType() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("边界测试 - 超长用户名")
    void testEdgeCaseVeryLongUsername() throws Exception {
        // Given
        LoginRequest longUsernameRequest = new LoginRequest();
        longUsernameRequest.setUsername("a".repeat(1000)); // 超长用户名
        longUsernameRequest.setPassword("password123!");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longUsernameRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("边界测试 - 特殊字符用户名")
    void testEdgeCaseSpecialCharacterUsername() throws Exception {
        // Given
        LoginRequest specialCharRequest = new LoginRequest();
        specialCharRequest.setUsername("test@user#$%^&*()");
        specialCharRequest.setPassword("password123!");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specialCharRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("安全性测试 - SQL注入尝试")
    void testSecuritySqlInjectionAttempt() throws Exception {
        // Given
        LoginRequest sqlInjectionRequest = new LoginRequest();
        sqlInjectionRequest.setUsername("admin'; DROP TABLE users; --");
        sqlInjectionRequest.setPassword("password");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sqlInjectionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("安全性测试 - XSS攻击尝试")
    void testSecurityXssAttackAttempt() throws Exception {
        // Given
        LoginRequest xssRequest = new LoginRequest();
        xssRequest.setUsername("<script>alert('xss')</script>");
        xssRequest.setPassword("password");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(xssRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("性能测试 - 并发请求")
    void testPerformanceConcurrentRequests() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class), anyString(), anyString()))
                .thenReturn(mockLoginResponse);

        // When & Then - 执行多个并发请求
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("异常处理测试 - 服务器内部错误")
    void testExceptionHandlingInternalServerError() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class), anyString(), anyString()))
                .thenThrow(new RuntimeException("数据库连接失败"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("服务器内部错误"));
    }

    @Test
    @DisplayName("异常处理测试 - 账户锁定")
    void testExceptionHandlingAccountLocked() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class), anyString(), anyString()))
                .thenThrow(new com.gmp.auth.exception.AccountLockedException("账户已被锁定"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("账户已锁定"));
    }

    @Test
    @DisplayName("异常处理测试 - 账户禁用")
    void testExceptionHandlingAccountDisabled() throws Exception {
        // Given
        when(authService.login(any(LoginRequest.class), anyString(), anyString()))
                .thenThrow(new com.gmp.auth.exception.AccountDisabledException("账户已被禁用"));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("账户已禁用"));
    }
}