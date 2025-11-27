package com.gmp.qms.test;

import com.gmp.qms.service.messaging.McpMessageService;
import com.gmp.qms.monitoring.McpMonitoringService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * MCP服务集成测试，测试MCP服务的核心功能
 * 
 * @author GMP系统开发团队
 */
public class McpServiceIntegrationTest extends McpIntegrationTestBase {

    @Autowired
    private McpMessageService mcpMessageService;

    @MockBean
    private McpMonitoringService mockMonitoringService;

    /**
     * 测试发送消息到EDMS系统
     */
    @Test
    public void testSendToEdms() throws Exception {
        // 准备测试数据
        Map<String, String> testPayload = new HashMap<>();
        testPayload.put("documentId", "DOC-001");
        testPayload.put("action", "APPROVE");
        
        // Mock监控服务
        McpMonitoringService.McpTimer mockTimer = Mockito.mock(McpMonitoringService.McpTimer.class);
        when(mockMonitoringService.startTimer(anyString())).thenReturn(mockTimer);
        doNothing().when(mockTimer).complete(true);
        
        // 执行测试
        String messageId = mcpMessageService.sendToEdms(testPayload, "DOCUMENT_ACTION");
        
        // 验证结果
        assertNotNull(messageId, "Message ID should not be null");
        assertTrue(messageId.length() > 0, "Message ID should have valid length");
        
        // 验证监控服务调用
        Mockito.verify(mockMonitoringService).startTimer("mq_send_edms");
        Mockito.verify(mockTimer).complete(true);
    }

    /**
     * 测试发送优先级消息
     */
    @Test
    public void testSendPriorityMessage() throws Exception {
        // 准备测试数据
        Map<String, String> testPayload = new HashMap<>();
        testPayload.put("alertId", "ALERT-001");
        testPayload.put("severity", "HIGH");
        
        // Mock监控服务
        McpMonitoringService.McpTimer mockTimer = Mockito.mock(McpMonitoringService.McpTimer.class);
        when(mockMonitoringService.startTimer(anyString())).thenReturn(mockTimer);
        doNothing().when(mockTimer).complete(true);
        
        // 执行测试
        String messageId = mcpMessageService.sendPriorityMessage("mes", testPayload, "ALERT_NOTIFY", 9);
        
        // 验证结果
        assertNotNull(messageId, "Message ID should not be null");
        
        // 验证监控服务调用
        Mockito.verify(mockMonitoringService).startTimer("mq_send_priority_mes");
    }

    /**
     * 测试发送带回调的消息
     */
    @Test
    public void testSendMessageWithCallback() throws Exception {
        // 准备测试数据
        Map<String, String> testPayload = new HashMap<>();
        testPayload.put("taskId", "TASK-001");
        testPayload.put("status", "COMPLETED");
        
        // Mock监控服务
        McpMonitoringService.McpTimer mockTimer = Mockito.mock(McpMonitoringService.McpTimer.class);
        when(mockMonitoringService.startTimer(anyString())).thenReturn(mockTimer);
        doNothing().when(mockTimer).complete(true);
        
        // 创建回调
        TestMessageCallback callback = new TestMessageCallback();
        
        // 执行测试
        String messageId = mcpMessageService.sendMessageWithCallback("lims", testPayload, "TASK_UPDATE", callback);
        
        // 验证结果
        assertNotNull(messageId, "Message ID should not be null");
    }

    /**
     * 测试MCP性能指标API
     */
    @Test
    public void testMcpMetricsApi() throws Exception {
        // 模拟性能指标数据
        Map<String, Long> mockMetrics = new HashMap<>();
        mockMetrics.put("totalCalls", 100L);
        mockMetrics.put("successCalls", 95L);
        mockMetrics.put("errorCalls", 5L);
        
        // Mock监控服务
        when(mockMonitoringService.getSystemMetrics()).thenReturn(mockMetrics);
        
        // 执行API调用
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mcp/metrics")
                .headers(createHeaders(createMcpHeaders())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalCalls").value(100))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.successCalls").value(95))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.errorCalls").value(5));
    }

    /**
     * 测试回调实现
     */
    private static class TestMessageCallback implements McpMessageService.MessageCallback {
        private boolean success = false;
        private String errorMessage = null;
        private String receivedMessageId = null;

        @Override
        public void onSuccess(String messageId) {
            this.success = true;
            this.receivedMessageId = messageId;
        }

        @Override
        public void onFailure(String messageId, String error) {
            this.success = false;
            this.receivedMessageId = messageId;
            this.errorMessage = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public String getReceivedMessageId() {
            return receivedMessageId;
        }
    }

    /**
     * 创建HTTP头
     */
    private org.springframework.http.HttpHeaders createHeaders(Map<String, String> mcpHeaders) {
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        mcpHeaders.forEach(headers::set);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        return headers;
    }
}
