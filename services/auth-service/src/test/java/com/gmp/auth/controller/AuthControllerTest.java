package com.gmp.auth.controller;

import com.gmp.auth.config.JwtConfig;
import com.gmp.auth.dto.ApiResponse;
import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.service.AuthService;
import com.gmp.auth.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AuthController测试类
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtConfig jwtConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // 重置所有模拟对象
        reset(authService, jwtConfig);
    }

    @Test
    void testLogin_Success() throws Exception {
        // 准备测试数据
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername("testuser");
        loginResponse.setToken("mock-jwt-token");
        loginResponse.setRefreshToken("mock-refresh-token");
        loginResponse.setRoles(List.of("ROLE_USER"));

        // 模拟AuthService的login方法
        when(authService.login(any(LoginRequest.class), anyString(), anyString())).thenReturn(loginResponse);

        // 执行测试
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("登录成功"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.token").value("mock-jwt-token"));

        // 验证调用
        verify(authService, times(1)).login(any(LoginRequest.class), anyString(), anyString());
    }

    @Test
    void testLogin_Failure() throws Exception {
        // 准备测试数据
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("wrongpassword");

        // 模拟AuthService的login方法抛出异常
        when(authService.login(any(LoginRequest.class), anyString(), anyString())).thenThrow(new RuntimeException("用户名或密码错误"));

        // 执行测试
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("LOGIN_FAILED"))
                .andExpect(jsonPath("$.message").value("用户名或密码错误"))
                .andExpect(jsonPath("$.data").doesNotExist());

        // 验证调用
        verify(authService, times(1)).login(any(LoginRequest.class), anyString(), anyString());
    }

    @Test
    void testLogout_Success() throws Exception {
        // 模拟从令牌中提取用户名
        when(jwtConfig.extractUsername(anyString())).thenReturn("testuser");

        // 执行测试
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer mock-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("登出成功"));

        // 验证调用
        verify(authService, times(1)).logout(eq("testuser"), eq("Bearer mock-jwt-token"));
    }

    @Test
    void testLogout_Failure() throws Exception {
        // 模拟从令牌中提取用户名失败
        when(jwtConfig.extractUsername(anyString())).thenThrow(new RuntimeException("无效令牌"));

        // 执行测试
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("登出成功"));

        // 验证调用
        verify(authService, times(1)).logout(eq("anonymous"), eq("Bearer invalid-token"));
    }

    @Test
    void testGetUsers() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("获取用户列表成功"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void testGetUserPermissions() throws Exception {
        // 模拟AuthService的getUserPermissions方法
        List<String> permissions = List.of("read", "write");
        when(authService.getUserPermissions(eq("testuser"))).thenReturn(permissions);

        // 执行测试
        mockMvc.perform(get("/api/auth/permissions/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("获取用户权限成功"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0]").value("read"))
                .andExpect(jsonPath("$.data[1]").value("write"));

        // 验证调用
        verify(authService, times(1)).getUserPermissions(eq("testuser"));
    }

    @Test
    void testGetUserRoles() throws Exception {
        // 模拟AuthService的getUserRoles方法
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");
        when(authService.getUserRoles(eq("testuser"))).thenReturn(roles);

        // 执行测试
        mockMvc.perform(get("/api/auth/roles/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("获取用户角色成功"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));

        // 验证调用
        verify(authService, times(1)).getUserRoles(eq("testuser"));
    }

    @Test
    void testCheckPermission_HasPermission() throws Exception {
        // 模拟AuthService的hasPermission方法
        when(authService.hasPermission(eq("testuser"), eq("read"))).thenReturn(true);

        // 执行测试
        mockMvc.perform(get("/api/auth/check/testuser/permission")
                .param("permission", "read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("权限检查完成"))
                .andExpect(jsonPath("$.data.hasPermission").value(true));

        // 验证调用
        verify(authService, times(1)).hasPermission(eq("testuser"), eq("read"));
    }

    @Test
    void testCheckPermission_NoPermission() throws Exception {
        // 模拟AuthService的hasPermission方法
        when(authService.hasPermission(eq("testuser"), eq("admin"))).thenReturn(false);

        // 执行测试
        mockMvc.perform(get("/api/auth/check/testuser/permission")
                .param("permission", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("权限检查完成"))
                .andExpect(jsonPath("$.data.hasPermission").value(false));

        // 验证调用
        verify(authService, times(1)).hasPermission(eq("testuser"), eq("admin"));
    }

    @Test
    void testCheckRole_HasRole() throws Exception {
        // 模拟AuthService的hasRole方法
        when(authService.hasRole(eq("testuser"), eq("ROLE_USER"))).thenReturn(true);

        // 执行测试
        mockMvc.perform(get("/api/auth/check/testuser/role")
                .param("role", "ROLE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("角色检查完成"))
                .andExpect(jsonPath("$.data.hasRole").value(true));

        // 验证调用
        verify(authService, times(1)).hasRole(eq("testuser"), eq("ROLE_USER"));
    }

    @Test
    void testCheckRole_NoRole() throws Exception {
        // 模拟AuthService的hasRole方法
        when(authService.hasRole(eq("testuser"), eq("ROLE_ADMIN"))).thenReturn(false);

        // 执行测试
        mockMvc.perform(get("/api/auth/check/testuser/role")
                .param("role", "ROLE_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("角色检查完成"))
                .andExpect(jsonPath("$.data.hasRole").value(false));

        // 验证调用
        verify(authService, times(1)).hasRole(eq("testuser"), eq("ROLE_ADMIN"));
    }

    @Test
    void testHealth() throws Exception {
        // 执行测试
        mockMvc.perform(get("/api/auth/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("服务健康检查正常"))
                .andExpect(jsonPath("$.data.status").value("UP"))
                .andExpect(jsonPath("$.data.service").value("auth-service"));
    }
}
