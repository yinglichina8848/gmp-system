package com.gmp.qms.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.qms.config.McpCircuitBreakerConfig;
import com.gmp.qms.config.McpMessageQueueConfig;
import com.gmp.qms.config.McpPerformanceConfig;
import com.gmp.qms.security.McpAuthenticationToken;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Map;

/**
 * MCP集成测试基类，为所有MCP相关测试提供共享功能和配置
 * 
 * @author GMP系统开发团队
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class McpIntegrationTestBase {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected McpCircuitBreakerConfig mockCircuitBreakerConfig;

    @MockBean
    protected McpPerformanceConfig mockPerformanceConfig;

    @MockBean
    protected McpMessageQueueConfig mockMessageQueueConfig;

    protected MockMvc mockMvc;
    protected McpAuthenticationToken testToken;

    /**
     * 设置测试环境
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        // 创建测试用的认证令牌
        createTestAuthenticationToken();

        // 设置默认的测试配置
        setupTestConfigurations();
    }

    /**
     * 创建测试用的认证令牌
     */
    protected void createTestAuthenticationToken() {
        // 创建测试令牌，具有测试系统所需的权限
        Map<String, Object> claims = Collections.singletonMap("system", "TEST");
        Map<String, String> permissions = Collections.singletonMap("mcp:integration:test", "*");
        this.testToken = new McpAuthenticationToken("test-app", "test-user", "test-tenant", claims, permissions);
    }