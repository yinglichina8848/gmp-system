package com.gmp.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.auth.dto.*;
import com.gmp.auth.entity.*;
import com.gmp.auth.repository.*;
import com.gmp.auth.service.TokenBlacklistService;
import com.gmp.auth.AuthApplication;

import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * GMP认证系统高级集成测试
 * 测试组织-角色-权限管理、令牌黑名单、密码重置等高级功能
 *
 * @author GMP系统开发团队
 */
@Slf4j
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = AuthApplication.class
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("GMP认证系统高级集成测试") 
public class AuthAdvancedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private UserOrganizationRoleRepository userOrganizationRoleRepository;
    
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admin";    
    private static final String ADMIN_PASSWORD = "Password123!";
    private static final String TEST_ORGANIZATION = "TEST_ORG";
    
    private String validAdminUsername;
    private Organization testOrganization;
    private Role adminRole;
    private Role userRole;

    /**
     * 测试前准备环境数据，包括用户、组织、角色、权限等
     */
    @BeforeEach
    void setUp() {
        log.info("🔄 准备高级集成测试环境数据...");

        // PostgreSQL环境下，基础数据已通过data-test.sql脚本初始化
        // 这里只清理运行时生成的数据
        log.info("清理令牌黑名单");
        
        // 验证必要的测试数据是否存在
        if (roleRepository.findByRoleCode("SYS_ADMIN").isEmpty()) {
            log.warn("⚠️ 测试数据未初始化，正在重新创建...");
            // 清理测试数据
            cleanTestData();
            
            // 创建测试组织
            createTestOrganization();
            
            // 创建测试角色和权限
            createTestRolesAndPermissions();
            
            // 创建测试用户
            createTestAdminUser();
        }
        
        log.info("✅ 高级集成测试环境准备完成");
    }
    
    /**
     * 清理测试数据
     */
    private void cleanTestData() {
        // 按照依赖顺序清理数据
        userOrganizationRoleRepository.deleteAll();
        rolePermissionRepository.deleteAll();
        userRepository.deleteAll();
        permissionRepository.deleteAll();
        roleRepository.deleteAll();
        organizationRepository.deleteAll();
        
        // 清理令牌黑名单
        log.info("清理令牌黑名单");
    }
    
    /**
     * 创建测试组织
     */
    private void createTestOrganization() {
        testOrganization = Organization.builder()
            .organizationCode(TEST_ORGANIZATION)
            .organizationName("测试组织")
            .status(Organization.Status.ACTIVE)
            .build();
        organizationRepository.save(testOrganization);
        log.info("✅ 创建测试组织: {}", testOrganization.getOrganizationCode());
    }
    
    /**
     * 创建测试角色和权限 - 基于示例文档中的核心认证角色和权限定义
     */
    private void createTestRolesAndPermissions() {
        // 1. 创建系统级角色
        adminRole = Role.builder()
            .roleCode("SYS_ADMIN")
            .roleName("系统管理员")
            .build();
        
        Role securityAdminRole = Role.builder()
            .roleCode("SECURITY_ADMIN")
            .roleName("安全管理员")
            .build();
        
        Role auditorRole = Role.builder()
            .roleCode("AUDITOR")
            .roleName("审计员")
            .build();
        
        // 2. 创建GMP核心角色
        Role gmpAdminRole = Role.builder()
            .roleCode("GMP_ADMIN")
            .roleName("GMP管理员")
            .build();
        
        Role qualityDirectorRole = Role.builder()
            .roleCode("QUALITY_DIRECTOR")
            .roleName("质量总监")
            .build();
        
        // 3. 创建普通用户角色
        userRole = Role.builder()
            .roleCode("GENERAL_USER")
            .roleName("普通用户")
            .build();
        
        // 保存所有角色
        List<Role> roles = Arrays.asList(
            adminRole, securityAdminRole, auditorRole, 
            gmpAdminRole, qualityDirectorRole, userRole
        );
        roleRepository.saveAll(roles);
        
        // 4. 创建各类权限
        // 系统管理权限
        Permission sysAdminPerm = Permission.builder()
            .permissionCode("SYS_ADMIN_ACCESS")
            .permissionName("系统管理访问")
            .groupName("系统管理")
            .build();
        
        Permission auditLogReadPerm = Permission.builder()
            .permissionCode("AUDIT_LOG_READ")
            .permissionName("查看审计日志")
            .groupName("系统管理")
            .build();
        
        // 用户权限管理
        Permission userReadPerm = Permission.builder()
            .permissionCode("USER_READ")
            .permissionName("读取用户")
            .groupName("用户与权限管理")
            .build();
        
        Permission userCreatePerm = Permission.builder()
            .permissionCode("USER_CREATE")
            .permissionName("创建用户")
            .groupName("用户与权限管理")
            .build();
        
        Permission roleAssignPerm = Permission.builder()
            .permissionCode("ROLE_ASSIGN")
            .permissionName("分配角色")
            .groupName("用户与权限管理")
            .build();
        
        // GMP合规权限
        Permission qualityDocManagePerm = Permission.builder()
            .permissionCode("QUALITY_DOC_MANAGE")
            .permissionName("质量文档管理")
            .groupName("GMP合规管理")
            .build();
        
        Permission validationManagePerm = Permission.builder()
            .permissionCode("VALIDATION_MANAGE")
            .permissionName("验证管理")
            .groupName("GMP合规管理")
            .build();
        
        // 生产管理权限
        Permission batchRecordAccessPerm = Permission.builder()
            .permissionCode("BATCH_RECORD_ACCESS")
            .permissionName("批记录访问")
            .groupName("生产管理")
            .build();
        
        // 保存所有权限
        List<Permission> permissions = Arrays.asList(
            sysAdminPerm, auditLogReadPerm, 
            userReadPerm, userCreatePerm, roleAssignPerm,
            qualityDocManagePerm, validationManagePerm,
            batchRecordAccessPerm
        );
        permissionRepository.saveAll(permissions);
        
        // 5. 关联角色和权限 - 基于角色权限矩阵
        // 系统管理员 - 所有权限
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), sysAdminPerm.getId()));
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), auditLogReadPerm.getId()));
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), userReadPerm.getId()));
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), userCreatePerm.getId()));
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), roleAssignPerm.getId()));
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), qualityDocManagePerm.getId()));
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), validationManagePerm.getId()));
        rolePermissionRepository.save(new RolePermission(adminRole.getId(), batchRecordAccessPerm.getId()));
        
        // 安全管理员 - 部分系统管理和用户权限
        rolePermissionRepository.save(new RolePermission(securityAdminRole.getId(), auditLogReadPerm.getId()));
        rolePermissionRepository.save(new RolePermission(securityAdminRole.getId(), userReadPerm.getId()));
        rolePermissionRepository.save(new RolePermission(securityAdminRole.getId(), userCreatePerm.getId()));
        rolePermissionRepository.save(new RolePermission(securityAdminRole.getId(), roleAssignPerm.getId()));
        
        // 审计员 - 审计日志权限
        rolePermissionRepository.save(new RolePermission(auditorRole.getId(), auditLogReadPerm.getId()));
        
        // GMP管理员 - GMP相关权限
        rolePermissionRepository.save(new RolePermission(gmpAdminRole.getId(), qualityDocManagePerm.getId()));
        rolePermissionRepository.save(new RolePermission(gmpAdminRole.getId(), validationManagePerm.getId()));
        
        // 质量总监 - 质量管理权限
        rolePermissionRepository.save(new RolePermission(qualityDirectorRole.getId(), qualityDocManagePerm.getId()));
        rolePermissionRepository.save(new RolePermission(qualityDirectorRole.getId(), batchRecordAccessPerm.getId()));
        
        // 普通用户 - 基本读取权限
        rolePermissionRepository.save(new RolePermission(userRole.getId(), userReadPerm.getId()));
        
        log.info("✅ 创建测试角色和权限完成 - 基于示例文档定义");
    }
    
    /**
     * 创建测试管理员用户
     */
    private void createTestAdminUser() {
        String validUsername = ADMIN_USERNAME.replaceAll("[^a-zA-Z0-9_", "_");
        String validEmail = validUsername.toLowerCase() + "@example.com";
        
        User adminUser = User.builder()
            .username(validUsername)
            .email(validEmail)
            .fullName("测试管理员")
            .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
            .userStatus(User.UserStatus.ACTIVE)
            .loginAttempts(0)
            .build();
        
        userRepository.save(adminUser);
        validAdminUsername = validUsername;
        
        // 关联用户-组织-角色
        UserOrganizationRole uor = UserOrganizationRole.builder()
            .userId(adminUser.getId())
            .organizationId(testOrganization.getId())
            .roleId(adminRole.getId())
            .build();
        userOrganizationRoleRepository.save(uor);
        
        log.info("✅ 创建测试管理员用户: {}", validUsername);
    }

    /**
     * 测试场景1: 组织-角色-权限管理集成测试
     */
    @Test
    @DisplayName("🏢 测试组织-角色-权限管理集成")
    void testOrganizationRolePermissionIntegration() throws Exception {
        log.info("🚀 开始测试组织-角色-权限管理集成...");
        
        // 登录获取令牌
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);
        assertThat(accessToken).isNotNull();
        
        // 测试获取用户在特定组织的角色
        MvcResult roleResult = mockMvc.perform(get("/api/auth/organizations/" + TEST_ORGANIZATION + "/users/" + validAdminUsername + "/roles")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String roleResponseJson = roleResult.getResponse().getContentAsString();
        ApiResponse<?> roleResponse = objectMapper.readValue(roleResponseJson, ApiResponse.class);
        
        assertThat(roleResponse.isSuccess()).isTrue();
        assertThat(roleResponse.getData()).isNotNull();
        
        // 验证用户在组织中拥有管理员角色
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> roles = (List<Map<String, Object>>) roleResponse.getData();
        assertThat(roles).isNotEmpty();
        
        log.info("✅ 组织-角色-权限管理集成测试通过");
    }
    
    /**
     * 测试场景2: 令牌黑名单功能集成测试
     */
    @Test
    @DisplayName("🚫 测试令牌黑名单功能集成")
    void testTokenBlacklistIntegration() throws Exception {
        log.info("🚀 开始测试令牌黑名单功能集成...");
        
        // 登录获取令牌
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);
        assertThat(accessToken).isNotNull();
        
        // 验证令牌当前有效
        MvcResult validateResult = mockMvc.perform(post("/api/auth/validate")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String validateResponseJson = validateResult.getResponse().getContentAsString();
        ApiResponse<?> validateResponse = objectMapper.readValue(validateResponseJson, ApiResponse.class);
        assertThat(validateResponse.isSuccess()).isTrue();
        
        // 执行登出，将令牌加入黑名单
        MvcResult logoutResult = mockMvc.perform(post("/api/auth/logout")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String logoutResponseJson = logoutResult.getResponse().getContentAsString();
        ApiResponse<?> logoutResponse = objectMapper.readValue(logoutResponseJson, ApiResponse.class);
        assertThat(logoutResponse.isSuccess()).isTrue();
        
        // 验证登出后的令牌已加入黑名单（使用服务直接验证）
        boolean isBlacklisted = tokenBlacklistService.isTokenBlacklisted(accessToken);
        assertThat(isBlacklisted).isTrue();
        
        log.info("✅ 令牌黑名单功能集成测试通过");
    }
    
    /**
     * 测试场景3: 密码重置功能集成测试
     */
    @Test
    @DisplayName("🔐 测试密码重置功能集成")
    void testPasswordResetIntegration() throws Exception {
        log.info("🚀 开始测试密码重置功能集成...");
        
        // 登录获取令牌
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);
        assertThat(accessToken).isNotNull();
        
        // 创建密码重置请求
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setOldPassword(ADMIN_PASSWORD);
        resetRequest.setNewPassword("NewPass123!");
        resetRequest.setConfirmPassword("NewPass123!");
        
        String resetRequestJson = objectMapper.writeValueAsString(resetRequest);
        
        // 执行密码重置
        MvcResult resetResult = mockMvc.perform(post("/api/auth/reset-password")
            .contentType(MediaType.APPLICATION_JSON)
            .content(resetRequestJson)
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String resetResponseJson = resetResult.getResponse().getContentAsString();
        ApiResponse<?> resetResponse = objectMapper.readValue(resetResponseJson, ApiResponse.class);
        
        assertThat(resetResponse.isSuccess()).isTrue();
        
        // 验证旧密码不再有效
        try {
            String invalidToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);
            assertThat(invalidToken).isNull();
        } catch (Exception e) {
            // 预期登录失败
        }
        
        // 验证新密码有效
        String newToken = performLoginAndGetToken(validAdminUsername, "NewPass123!");
        assertThat(newToken).isNotNull();
        
        log.info("✅ 密码重置功能集成测试通过");
    }
    
    /**
     * 测试场景4: 子系统访问权限集成测试
     */
    @Test
    @DisplayName("🔌 测试子系统访问权限集成")
    void testSubsystemAccessIntegration() throws Exception {
        log.info("🚀 开始测试子系统访问权限集成...");
        
        // 登录获取令牌
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);
        assertThat(accessToken).isNotNull();
        
        // 测试子系统访问权限检查
        MvcResult accessResult = mockMvc.perform(get("/api/auth/subsystems/check")
            .param("subsystemCode", "ADMIN_PORTAL")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String accessResponseJson = accessResult.getResponse().getContentAsString();
        ApiResponse<?> accessResponse = objectMapper.readValue(accessResponseJson, ApiResponse.class);
        
        assertThat(accessResponse.isSuccess()).isTrue();
        
        // 获取用户可访问的子系统列表
        MvcResult subsystemsResult = mockMvc.perform(get("/api/auth/subsystems/accessible")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String subsystemsResponseJson = subsystemsResult.getResponse().getContentAsString();
        ApiResponse<?> subsystemsResponse = objectMapper.readValue(subsystemsResponseJson, ApiResponse.class);
        
        assertThat(subsystemsResponse.isSuccess()).isTrue();
        
        log.info("✅ 子系统访问权限集成测试通过");
    }
    
    /**
     * 测试场景5: 完整的组织管理员操作流程
     */
    @Test
    @DisplayName("🔄 测试完整的组织管理员操作流程")
    void testCompleteOrganizationAdminFlow() throws Exception {
        log.info("🚀 开始测试完整的组织管理员操作流程...");
        
        // 1. 登录获取令牌
        log.info("Step 1: 登录获取管理员令牌");
        String accessToken = performLoginAndGetToken(validAdminUsername, ADMIN_PASSWORD);
        assertThat(accessToken).isNotNull();
        
        // 2. 创建新用户
        log.info("Step 2: 创建新用户");
        CreateUserRequest createRequest = new CreateUserRequest();
        createRequest.setUsername("newuser");
        createRequest.setEmail("newuser@example.com");
        createRequest.setFullName("新测试用户");
        createRequest.setPassword("NewUser123!");
        
        String createRequestJson = objectMapper.writeValueAsString(createRequest);
        
        MvcResult createResult = mockMvc.perform(post("/api/auth/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(createRequestJson)
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String createResponseJson = createResult.getResponse().getContentAsString();
        ApiResponse<?> createResponse = objectMapper.readValue(createResponseJson, ApiResponse.class);
        assertThat(createResponse.isSuccess()).isTrue();
        
        // 3. 分配用户角色
        log.info("Step 3: 分配用户角色");
        AssignRoleRequest assignRequest = new AssignRoleRequest();
        assignRequest.setUsername("newuser");
        assignRequest.setOrganizationCode(TEST_ORGANIZATION);
        assignRequest.setRoleCode("USER");
        
        String assignRequestJson = objectMapper.writeValueAsString(assignRequest);
        
        MvcResult assignResult = mockMvc.perform(post("/api/auth/roles/assign")
            .contentType(MediaType.APPLICATION_JSON)
            .content(assignRequestJson)
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String assignResponseJson = assignResult.getResponse().getContentAsString();
        ApiResponse<?> assignResponse = objectMapper.readValue(assignResponseJson, ApiResponse.class);
        assertThat(assignResponse.isSuccess()).isTrue();
        
        // 4. 验证用户权限
        log.info("Step 4: 验证用户权限");
        MvcResult permResult = mockMvc.perform(get("/api/auth/check/newuser/permission")
            .param("permission", "READ_USER")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andReturn();
        
        String permResponseJson = permResult.getResponse().getContentAsString();
        ApiResponse<?> permResponse = objectMapper.readValue(permResponseJson, ApiResponse.class);
        assertThat(permResponse.isSuccess()).isTrue();
        
        // 5. 管理员登出
        log.info("Step 5: 管理员登出");
        mockMvc.perform(post("/api/auth/logout")
            .header("Authorization", "Bearer " + accessToken))
            .andExpect(status().isOk());
        
        log.info("✅ 完整的组织管理员操作流程测试通过");
    }
    
    /**
     * 测试场景6: 多角色权限验证测试 - 基于示例文档中的角色权限矩阵
     */
    @Test
    @DisplayName("🔍 测试多角色权限验证 - 基于角色权限矩阵")
    void testMultiRolePermissionValidation() throws Exception {
        log.info("🚀 开始测试多角色权限验证...");
        
        // 创建不同角色的测试用户
        createMultiRoleTestUsers();
        
        // 1. 系统管理员角色权限测试
        log.info("Step 1: 测试系统管理员权限");
        String sysAdminToken = performLoginAndGetToken("sysadmin", "Password123!");
        
        // 验证系统管理员拥有所有权限
        verifyPermission(sysAdminToken, "SYS_ADMIN_ACCESS", true);
        verifyPermission(sysAdminToken, "AUDIT_LOG_READ", true);
        verifyPermission(sysAdminToken, "USER_CREATE", true);
        verifyPermission(sysAdminToken, "QUALITY_DOC_MANAGE", true);
        verifyPermission(sysAdminToken, "VALIDATION_MANAGE", true);
        
        // 2. 安全管理员角色权限测试
        log.info("Step 2: 测试安全管理员权限");
        String securityAdminToken = performLoginAndGetToken("securityadmin", "Password123!");
        
        // 验证安全管理员权限
        verifyPermission(securityAdminToken, "AUDIT_LOG_READ", true);
        verifyPermission(securityAdminToken, "USER_READ", true);
        verifyPermission(securityAdminToken, "USER_CREATE", true);
        verifyPermission(securityAdminToken, "SYS_ADMIN_ACCESS", false); // 不应有系统管理权限
        
        // 3. 审计员角色权限测试
        log.info("Step 3: 测试审计员权限");
        String auditorToken = performLoginAndGetToken("auditor", "Password123!");
        
        // 验证审计员权限 - 只有审计日志读取权限
        verifyPermission(auditorToken, "AUDIT_LOG_READ", true);
        verifyPermission(auditorToken, "USER_CREATE", false); // 不应有用户创建权限
        verifyPermission(auditorToken, "QUALITY_DOC_MANAGE", false); // 不应有质量文档管理权限
        
        // 4. GMP管理员角色权限测试
        log.info("Step 4: 测试GMP管理员权限");
        String gmpAdminToken = performLoginAndGetToken("gmpadmin", "Password123!");
        
        // 验证GMP管理员权限
        verifyPermission(gmpAdminToken, "QUALITY_DOC_MANAGE", true);
        verifyPermission(gmpAdminToken, "VALIDATION_MANAGE", true);
        verifyPermission(gmpAdminToken, "USER_CREATE", false); // 不应有用户创建权限
        
        // 5. 普通用户角色权限测试
        log.info("Step 5: 测试普通用户权限");
        String userToken = performLoginAndGetToken("testuser", "Password123!");
        
        // 验证普通用户权限 - 只有基本读取权限
        verifyPermission(userToken, "USER_READ", true);
        verifyPermission(userToken, "USER_CREATE", false); // 不应有用户创建权限
        verifyPermission(userToken, "AUDIT_LOG_READ", false); // 不应有审计日志权限
        verifyPermission(userToken, "QUALITY_DOC_MANAGE", false); // 不应有质量文档管理权限
        
        log.info("✅ 多角色权限验证测试通过");
    }
    
    /**
     * 创建多角色测试用户
     */
    private void createMultiRoleTestUsers() {
        // 查找已创建的角色
        Role sysAdminRole = roleRepository.findByRoleCode("SYS_ADMIN").orElse(null);
        Role securityAdminRole = roleRepository.findByRoleCode("SECURITY_ADMIN").orElse(null);
        Role auditorRole = roleRepository.findByRoleCode("AUDITOR").orElse(null);
        Role gmpAdminRole = roleRepository.findByRoleCode("GMP_ADMIN").orElse(null);
        Role generalUserRole = roleRepository.findByRoleCode("GENERAL_USER").orElse(null);
        
        // 创建系统管理员用户
        createTestUserWithRole("sysadmin", "系统管理员", sysAdminRole);
        
        // 创建安全管理员用户
        createTestUserWithRole("securityadmin", "安全管理员", securityAdminRole);
        
        // 创建审计员用户
        createTestUserWithRole("auditor", "审计员", auditorRole);
        
        // 创建GMP管理员用户
        createTestUserWithRole("gmpadmin", "GMP管理员", gmpAdminRole);
        
        // 创建普通用户
        createTestUserWithRole("testuser", "测试用户", generalUserRole);
        
        log.info("✅ 创建多角色测试用户完成");
    }
    
    /**
     * 创建测试用户并分配角色
     */
    private void createTestUserWithRole(String username, String fullName, Role role) {
        if (role == null) return;
        
        String validUsername = username.replaceAll("[^a-zA-Z0-9_", "_");
        String validEmail = validUsername.toLowerCase() + "@example.com";
        
        User user = User.builder()
            .username(validUsername)
            .email(validEmail)
            .fullName(fullName)
            .passwordHash(passwordEncoder.encode("Password123!"))
            .userStatus(User.UserStatus.ACTIVE)
            .loginAttempts(0)
            .build();
        
        userRepository.save(user);
        
        // 关联用户-组织-角色
        UserOrganizationRole uor = UserOrganizationRole.builder()
            .userId(user.getId())
            .organizationId(testOrganization.getId())
            .roleId(role.getId())
            .build();
        userOrganizationRoleRepository.save(uor);
    }
    
    /**
     * 验证用户是否拥有特定权限
     */
    private void verifyPermission(String token, String permissionCode, boolean shouldHaveAccess) throws Exception {
        MvcResult permResult = mockMvc.perform(get("/api/auth/check/permission")
            .param("permission", permissionCode)
            .header("Authorization", "Bearer " + token))
            .andReturn();
        
        String permResponseJson = permResult.getResponse().getContentAsString();
        ApiResponse<?> permResponse = objectMapper.readValue(permResponseJson, ApiResponse.class);
        
        if (shouldHaveAccess) {
            assertThat(permResponse.isSuccess()).isTrue();
        } else {
            assertThat(permResponse.isSuccess()).isFalse();
        }
        
        log.info("  - 验证权限 [{}]: 应{}有访问权限, 实际结果: {}", 
            permissionCode, shouldHaveAccess ? "" : "不", permResponse.isSuccess() ? "有" : "无");
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