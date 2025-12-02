package com.gmp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.auth.dto.ApiResponse;
import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.entity.User;
import com.gmp.auth.repository.OperationLogRepository;
import com.gmp.auth.repository.UserRepository;
import com.gmp.auth.AuthApplication;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GMP认证系统集成测试
 * 模拟用户操作的完整认证流程
 *
 * @author GMP系统开发团队
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = AuthApplication.class
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("GMP认证系统集成测试") 
public class AuthIntegrationTest {

    // 使用手动Logger实例替代@Slf4j
    private static final Logger log = LoggerFactory.getLogger(AuthIntegrationTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Password123!";
    private static final String USER_USERNAME = "testuser";
    private static final String USER_PASSWORD = "Test123!";
    
    // 存储验证后的用户名，用于测试断言
    private String validAdminUsername;
    private String validUserUsername;

    /**
     * 测试前准备环境数据
     * 注意：PostgreSQL环境下，数据初始化通过data-test.sql脚本完成
     * 这里验证必要的测试数据是否存在，如果不存在则创建
     */
    @BeforeEach
    void setUp() {
        log.info("🔄 准备集成测试环境数据...");

        // 确保操作日志表已清空
        operationLogRepository.deleteAll();
        
        // 验证测试数据是否已初始化，如果不存在则创建
        User adminUser = userRepository.findByUsername(ADMIN_USERNAME).orElse(null);
        User regularUser = userRepository.findByUsername(USER_USERNAME).orElse(null);
        
        // 如果用户不存在，则创建
        if (adminUser == null) {
            createTestUser(ADMIN_USERNAME, ADMIN_PASSWORD, "ADMIN");
        } else {
            validAdminUsername = ADMIN_USERNAME;
        }
        
        if (regularUser == null) {
            createTestUser(USER_USERNAME, USER_PASSWORD, "USER");
        } else {
            validUserUsername = USER_USERNAME;
        }

        log.info("✅ 集成测试环境准备完成");
    }

    /**
     * 创建测试用户
     */
    private void createTestUser(String username, String password, String roleName) {
        // 确保用户名符合验证规则（只包含字母、数字和下划线）
        String validUsername = username.replaceAll("[^a-zA-Z0-9_]", "");
        // 确保用户名不以数字开头且长度在3-50之间
        if (validUsername.length() < 3) {
            validUsername = "test" + validUsername;
        } else if (validUsername.length() > 50) {
            validUsername = validUsername.substring(0, 50);
        }
        // 构造有效的邮箱格式 - 确保使用有效的域名
        String validEmail = validUsername.toLowerCase() + "@test-gmp.com";
        
        // 直接创建User对象并设置字段，确保所有必要字段都被正确设置
        User user = new User();
        user.setUsername(validUsername);
        user.setEmail(validEmail);
        user.setFullName(username + " User");
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setUserStatus(User.UserStatus.ACTIVE);
        user.setLoginAttempts(0);
        user.setMobile("13800138000"); // 添加有效的手机号
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setVersion(1);
        user.setMfaEnabled(false);
                
        log.info("👤 准备创建测试用户: {} (验证后: {}) 邮箱: {}", 
                 username, validUsername, validEmail);
        
        try {
            userRepository.save(user);
            // 保存验证后的用户名到类变量，用于测试断言
            if ("ADMIN".equals(roleName)) {
                validAdminUsername = validUsername;
            } else {
                validUserUsername = validUsername;
            }
            log.info("✅ 测试用户创建成功: {} 角色: {}", validUsername, roleName);
        } catch (Exception e) {
            log.error("❌ 创建测试用户失败: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 测试场景1: 成功用户登录
     */
    @Test
    @DisplayName("🎫 测试用户成功登录流程")
    void testSuccessfulUserLogin() throws Exception {
        log.info("🚀 开始测试用户登录...");

        // 模拟用户登录，使用验证后的用户名
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(validAdminUsername);
        loginRequest.setPassword(ADMIN_PASSWORD);

        String requestJson = objectMapper.writeValueAsString(loginRequest);
        log.info("📤 发送登录请求: {}", requestJson);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        log.info("📥 登录响应: {}", responseJson);

        ApiResponse<LoginResponse> apiResponse = objectMapper.readValue(responseJson, objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, LoginResponse.class));

        // 验证响应结构
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getCode()).isEqualTo("200");
        assertThat(apiResponse.getData()).isNotNull();

        // 验证登录响应内容
        LoginResponse loginResponse = apiResponse.getData();
        assertThat(loginResponse.getAccessToken()).isNotNull();
        assertThat(loginResponse.getRefreshToken()).isNotNull();
        assertThat(loginResponse.getUsername()).isEqualTo(validAdminUsername);

        log.info("✅ 用户登录测试通过");
    }

    /**
     * 测试场景2: 用户登录失败 - 密码错误
     */
    @Test
    @DisplayName("❌ 测试用户登录失败 - 密码错误")
    void testFailedLoginWithWrongPassword() throws Exception {
        log.info("🚀 开始测试登录失败场景...");

        // 使用错误的密码
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(validAdminUsername);
        loginRequest.setPassword("WrongPassword123!");

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<?> apiResponse = objectMapper.readValue(responseJson, ApiResponse.class);

        // 验证登录失败响应
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getCode()).isEqualTo("LOGIN_FAILED");
        assertThat(apiResponse.getMessage()).contains("用户名或密码错误");

        log.info("✅ 登录失败测试通过");
    }

    /**
     * 测试场景3: 用户权限检查
     */
    @Test
    @DisplayName("🔐 测试用户权限检查")
    void testUserPermissionCheck() throws Exception {
        log.info("🚀 开始测试权限检查...");

        // 首先登录获取令牌
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);

        // 检查admin用户是否有READ_USER权限
        MvcResult result = mockMvc.perform(get("/api/auth/check/" + validAdminUsername + "/permission")
                .param("permission", "READ_USER")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        log.info("📥 权限检查响应: {}", responseJson);

        ApiResponse<?> apiResponse = objectMapper.readValue(responseJson, ApiResponse.class);
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).isNotNull();

        // 验证权限检查结果
        @SuppressWarnings("unchecked")
        var data = (java.util.Map<String, Object>) apiResponse.getData();
        assertThat(data.get("hasPermission")).isNotNull();

        log.info("✅ 用户权限检查测试通过");
    }

    /**
     * 测试场景4: 用户角色检查
     */
    @Test
    @DisplayName("👤 测试用户角色检查")
    void testUserRoleCheck() throws Exception {
        log.info("🚀 开始测试角色检查...");

        // 检查admin用户是否具有ADMIN角色
        MvcResult result = mockMvc.perform(get("/api/auth/check/" + validAdminUsername + "/role")
                .param("role", "ADMIN")
                .header("Authorization", "Bearer dummy_token"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        log.info("📥 角色检查响应: {}", responseJson);

        ApiResponse<?> apiResponse = objectMapper.readValue(responseJson, ApiResponse.class);
        assertThat(apiResponse.isSuccess()).isTrue();

        log.info("✅ 用户角色检查测试通过");
    }

    /**
     * 测试场景5: 系统健康检查
     */
    @Test
    @DisplayName("💚 测试系统健康检查接口")
    void testHealthCheck() throws Exception {
        log.info("🚀 开始测试健康检查...");

        MvcResult result = mockMvc.perform(get("/api/auth/health"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<?> apiResponse = objectMapper.readValue(responseJson, ApiResponse.class);

        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).isNotNull();

        @SuppressWarnings("unchecked")
        var data = (java.util.Map<String, Object>) apiResponse.getData();
        assertThat(data.get("status")).isEqualTo("UP");
        assertThat(data.get("service")).isEqualTo("auth-service");
        assertThat(data.get("timestamp")).isNotNull();

        log.info("✅ 系统健康检查测试通过");
    }

    /**
     * 测试场景6: 获取用户权限列表
     */
    @Test
    @DisplayName("📋 测试获取用户权限列表")
    void testGetUserPermissions() throws Exception {
        log.info("🚀 开始测试用户权限列表获取...");

        // 首先登录获取令牌
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);

        MvcResult result = mockMvc.perform(get("/api/auth/permissions/" + validAdminUsername)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<?> apiResponse = objectMapper.readValue(responseJson, ApiResponse.class);

        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).isNotNull();

        log.info("✅ 用户权限列表获取测试通过");
    }

    /**
     * 测试场景7: 用户登出
     */
    @Test
    @DisplayName("📤 测试用户登出功能")
    void testUserLogout() throws Exception {
        log.info("🚀 开始测试用户登出...");

        // 先登录
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);

        // 执行登出
        MvcResult result = mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ApiResponse<?> apiResponse = objectMapper.readValue(responseJson, ApiResponse.class);

        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).contains("登出成功");

        log.info("✅ 用户登出测试通过");
    }

    /**
     * 测试场景8: 完整用户操作流程
     */
    @Test
    @DisplayName("🔄 测试完整的用户操作流程")
    void testCompleteUserFlow() throws Exception {
        log.info("🚀 开始测试完整的用户操作流程...");

        // 1. 用户登录
        log.info("Step 1: 用户登录");
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);
        assertThat(accessToken).isNotNull().isNotEmpty();

        // 2. 检查用户权限
        log.info("Step 2: 检查用户权限");
        MvcResult permResult = mockMvc.perform(get("/api/auth/check/" + validAdminUsername + "/permission")
                .param("permission", "READ_USER")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        ApiResponse<?> permResponse = objectMapper.readValue(permResult.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(permResponse.isSuccess()).isTrue();

        // 3. 检查用户角色
        log.info("Step 3: 检查用户角色");
        MvcResult roleResult = mockMvc.perform(get("/api/auth/check/" + validAdminUsername + "/role")
                .param("role", "ADMIN")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        ApiResponse<?> roleResponse = objectMapper.readValue(roleResult.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(roleResponse.isSuccess()).isTrue();

        // 4. 获取用户权限列表
        log.info("Step 4: 获取用户权限列表");
        MvcResult permsResult = mockMvc.perform(get("/api/auth/permissions/" + validAdminUsername)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        ApiResponse<?> permsResponse = objectMapper.readValue(permsResult.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(permsResponse.isSuccess()).isTrue();

        // 5. 用户登出
        log.info("Step 5: 用户登出");
        MvcResult logoutResult = mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        ApiResponse<?> logoutResponse = objectMapper.readValue(logoutResult.getResponse().getContentAsString(), ApiResponse.class);
        assertThat(logoutResponse.isSuccess()).isTrue();

        log.info("✅ 完整的用户操作流程测试通过");
    }

    /**
     * 帮助方法：执行登录并返回访问令牌
     */
    private String performLoginAndGetToken(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        String requestJson = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        
        // 处理空响应或非JSON响应
        if (responseJson == null || responseJson.trim().isEmpty()) {
            log.warn("登录响应为空，用户名: {}", username);
            return null;
        }
        
        try {
            ApiResponse<LoginResponse> apiResponse = objectMapper.readValue(responseJson, 
                    objectMapper.getTypeFactory().constructParametricType(ApiResponse.class, LoginResponse.class));

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                return apiResponse.getData().getAccessToken();
            }
        } catch (Exception e) {
            log.error("解析登录响应失败: {}", e.getMessage());
        }

        return null;
    }
}
