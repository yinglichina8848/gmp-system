package com.gmp.qms.config;

import lombok.Getter;
import lombok.Setter;

/**
 * MCP服务器配置类，定义MCP服务器的连接参数和行为
 * 
 * @author GMP系统开发团队
 */
@Getter
@Setter
public class McpServerConfig {
    
    // 服务器基本配置
    private String serverUrl = "http://localhost:8081/api/mcp";
    private String serverName = "mcp.config.qmsService";
    private String version = "1.0.0";
    
    // 连接池配置
    private int maxConnections = 20;
    private int connectionTimeout = 5000;
    private int socketTimeout = 30000;
    
    // 会话管理配置
    private boolean enableSession = true;
    private int sessionTimeout = 3600000; // 1小时
    
    // 消息格式配置
    private boolean compressMessages = true;
    private int maxMessageSize = 1024 * 1024; // 1MB
    
    // 重试机制配置
    private int maxRetries = 3;
    private long retryDelay = 1000;
    
    // 健康检查配置
    private boolean enableHealthCheck = true;
    private int healthCheckInterval = 60000; // 1分钟
    
    /**
     * 获取健康状态检查URL
     */
    public String getHealthCheckUrl() {
        return serverUrl + "/health";
    }
}
