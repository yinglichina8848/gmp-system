package com.gmp.auth.controller;

import com.gmp.auth.dto.PasswordChangeRequest;
import com.gmp.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * PasswordController测试类
 */
class PasswordControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private PasswordController passwordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 模拟SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testChangePassword_Success() {
        // 准备测试数据
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        // 模拟当前登录用户
        when(authentication.getName()).thenReturn("testuser");

        // 模拟AuthService的changePassword方法
        when(authService.changePassword(anyString(), any(PasswordChangeRequest.class))).thenReturn(true);

        // 执行测试
        var response = passwordController.changePassword(request);

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).changePassword(eq("testuser"), any(PasswordChangeRequest.class));
    }

    @Test
    void testChangePassword_Failure() {
        // 准备测试数据
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setCurrentPassword("oldPassword");
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        // 模拟当前登录用户
        when(authentication.getName()).thenReturn("testuser");

        // 模拟AuthService的changePassword方法抛出异常
        when(authService.changePassword(anyString(), any(PasswordChangeRequest.class))).thenThrow(new RuntimeException("密码修改失败"));

        // 执行测试
        var response = passwordController.changePassword(request);

        // 验证结果
        assertEquals(400, response.getStatusCodeValue());
        verify(authService, times(1)).changePassword(eq("testuser"), any(PasswordChangeRequest.class));
    }

    @Test
    void testResetPassword_Success() {
        // 准备测试数据
        String username = "testuser";
        Map<String, String> request = Map.of("newPassword", "newPassword123");

        // 模拟AuthService的resetPassword方法
        when(authService.resetPassword(eq(username), eq("newPassword123"))).thenReturn(true);

        // 执行测试
        var response = passwordController.resetPassword(username, request);

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).resetPassword(eq(username), eq("newPassword123"));
    }

    @Test
    void testResetPassword_EmptyPassword() {
        // 准备测试数据
        String username = "testuser";
        Map<String, String> request = Map.of("newPassword", "");

        // 执行测试
        var response = passwordController.resetPassword(username, request);

        // 验证结果
        assertEquals(400, response.getStatusCodeValue());
        verify(authService, never()).resetPassword(anyString(), anyString());
    }

    @Test
    void testResetPassword_NullPassword() {
        // 准备测试数据
        String username = "testuser";
        Map<String, String> request = Map.of();

        // 执行测试
        var response = passwordController.resetPassword(username, request);

        // 验证结果
        assertEquals(400, response.getStatusCodeValue());
        verify(authService, never()).resetPassword(anyString(), anyString());
    }

    @Test
    void testResetPassword_Failure() {
        // 准备测试数据
        String username = "testuser";
        Map<String, String> request = Map.of("newPassword", "newPassword123");

        // 模拟AuthService的resetPassword方法抛出异常
        when(authService.resetPassword(eq(username), eq("newPassword123"))).thenThrow(new RuntimeException("密码重置失败"));

        // 执行测试
        var response = passwordController.resetPassword(username, request);

        // 验证结果
        assertEquals(400, response.getStatusCodeValue());
        verify(authService, times(1)).resetPassword(eq(username), eq("newPassword123"));
    }

    @Test
    void testGetPasswordRequirements_ShortStringResult() {
        // 模拟AuthService的getPasswordComplexityRequirements方法返回短字符串
        when(authService.getPasswordComplexityRequirements()).thenReturn("密码至少8位");

        // 执行测试
        var response = passwordController.getPasswordRequirements();

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(authService, times(1)).getPasswordComplexityRequirements();
    }

    @Test
    void testGetPasswordRequirements_NullResult() {
        // 模拟AuthService的getPasswordComplexityRequirements方法返回null
        when(authService.getPasswordComplexityRequirements()).thenReturn(null);

        // 执行测试
        var response = passwordController.getPasswordRequirements();

        // 验证结果
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        verify(authService, times(1)).getPasswordComplexityRequirements();
    }

    @Test
    void testGetPasswordRequirements_Failure() {
        // 模拟AuthService的getPasswordComplexityRequirements方法抛出异常
        when(authService.getPasswordComplexityRequirements()).thenThrow(new RuntimeException("获取密码复杂度要求失败"));

        // 执行测试
        var response = passwordController.getPasswordRequirements();

        // 验证结果
        assertEquals(500, response.getStatusCodeValue());
        verify(authService, times(1)).getPasswordComplexityRequirements();
    }
}
