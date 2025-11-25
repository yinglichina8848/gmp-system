package com.gmp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.auth.dto.LoginRequest;
import com.gmp.auth.dto.LoginResponse;
import com.gmp.auth.entity.Subsystem;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 子系统访问控制集成测试
 * 测试子系统API接口和安全拦截器的功能
 *
 * @author GMP系统开发团队
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubsystemAccessIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() throws Exception {
        // 重新配置MockMvc以包含过滤器
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 管理员登录获取token
        LoginRequest adminLogin = new LoginRequest();
        adminLogin.setUsername("admin");
        adminLogin.setPassword("admin123");

        MvcResult adminResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String adminResponse = adminResult.getResponse().getContentAsString();
        LoginResponse adminLoginResponse = objectMapper.readValue(adminResponse, LoginResponse.class);
        adminToken = adminLoginResponse.accessToken;

        // 普通用户登录获取token
        LoginRequest userLogin = new LoginRequest();
        userLogin.setUsername("user");
        userLogin.setPassword("user123");

        try {
            MvcResult userResult = mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userLogin)))
                    .andExpect(status().isOk())
                    .andReturn();

            String userResponse = userResult.getResponse().getContentAsString();
            LoginResponse userLoginResponse = objectMapper.readValue(userResponse, LoginResponse.class);
            userToken = userLoginResponse.accessToken;
        } catch (Exception e) {
            // 如果普通用户不存在，使用管理员token进行测试
            userToken = adminToken;
        }
    }

    @Test
    @Order(1)
    void testGetAllSubsystems_WithValidToken() throws Exception {
        // 使用有效token访问获取所有子系统的接口
        mockMvc.perform(get("/api/subsystems")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(2)
    void testGetAllSubsystems_WithoutToken() throws Exception {
        // 不提供token访问获取所有子系统的接口
        mockMvc.perform(get("/api/subsystems")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(3)
    void testGetAllSubsystems_WithInvalidToken() throws Exception {
        // 使用无效token访问获取所有子系统的接口
        mockMvc.perform(get("/api/subsystems")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(4)
    void testCreateSubsystem_WithAdminPermission() throws Exception {
        // 创建新子系统的请求体
        Subsystem newSubsystem = new Subsystem();
        newSubsystem.setSubsystemCode("INTEG_TEST");
        newSubsystem.setSubsystemName("集成测试子系统");
        newSubsystem.setDescription("用于集成测试的子系统");
        newSubsystem.setEnabled(true);

        // 管理员应该能够创建子系统
        MvcResult result = mockMvc.perform(post("/api/subsystems")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSubsystem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        // 解析响应获取创建的子系统ID
        String response = result.getResponse().getContentAsString();
        Subsystem createdSubsystem = objectMapper.readValue(response, Subsystem.class);
        assertNotNull(createdSubsystem.getId());
        
        // 清理测试数据 - 删除创建的子系统
        mockMvc.perform(delete("/api/subsystems/" + createdSubsystem.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    void testUpdateSubsystem_WithValidPermission() throws Exception {
        // 首先创建一个测试子系统
        Subsystem testSubsystem = new Subsystem();
        testSubsystem.setSubsystemCode("UPDATE_TEST");
        testSubsystem.setSubsystemName("更新测试子系统");
        testSubsystem.setDescription("待更新的测试子系统");
        testSubsystem.setEnabled(true);

        MvcResult createResult = mockMvc.perform(post("/api/subsystems")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSubsystem)))
                .andExpect(status().isOk())
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        Subsystem created = objectMapper.readValue(createResponse, Subsystem.class);

        // 更新子系统信息
        created.setSubsystemName("已更新测试子系统");
        created.setDescription("这是更新后的描述");

        // 更新操作
        mockMvc.perform(put("/api/subsystems/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.subsystemName").value("已更新测试子系统"));

        // 清理测试数据
        mockMvc.perform(delete("/api/subsystems/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void testDeleteSubsystem_WithValidPermission() throws Exception {
        // 首先创建一个测试子系统
        Subsystem testSubsystem = new Subsystem();
        testSubsystem.setSubsystemCode("DELETE_TEST");
        testSubsystem.setSubsystemName("删除测试子系统");
        testSubsystem.setDescription("待删除的测试子系统");
        testSubsystem.setEnabled(true);

        MvcResult createResult = mockMvc.perform(post("/api/subsystems")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testSubsystem)))
                .andExpect(status().isOk())
                .andReturn();

        String createResponse = createResult.getResponse().getContentAsString();
        Subsystem created = objectMapper.readValue(createResponse, Subsystem.class);

        // 删除操作
        mockMvc.perform(delete("/api/subsystems/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 验证删除成功 - 尝试获取已删除的子系统应该返回404
        mockMvc.perform(get("/api/subsystems/" + created.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    void testCheckSubsystemAccess_WithAccess() throws Exception {
        // 检查子系统访问权限 - 有权限的情况
        mockMvc.perform(get("/api/subsystems/check-access/AUTH")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.hasAccess").value(true));
    }

    @Test
    @Order(8)
    void testCheckSubsystemAccess_NoAccess() throws Exception {
        // 检查不存在的子系统访问权限
        mockMvc.perform(get("/api/subsystems/check-access/NON_EXISTENT")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.hasAccess").value(false));
    }

    @Test
    @Order(9)
    void testGetUserAccessibleSubsystems() throws Exception {
        // 获取用户可访问的子系统列表
        mockMvc.perform(get("/api/subsystems/accessible")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }
}
