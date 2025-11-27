package com.gmp.qms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP配置类，用于配置模型上下文协议相关组件
 * 
 * @author GMP系统开发团队
 */
@Configuration
public class McpConfig {

    /**
     * 暂时注释掉MCP相关Bean定义，因为这些类当前不存在
     * 后续实现MCP功能时可以取消注释并添加相应的依赖
     */
    /*
     * @Bean
     * public McpServerConfig mcpServerConfig() {
     * return new McpServerConfig();
     * }
     * 
     * @Bean
     * public McpToolRegistry mcpToolRegistry() {
     * return new McpToolRegistry();
     * }
     * 
     * @Bean
     * public McpResourceRegistry mcpResourceRegistry() {
     * return new McpResourceRegistry();
     * }
     * 
     * @Bean
     * public McpSecurityFilter mcpSecurityFilter() {
     * return new McpSecurityFilter();
     * }
     */
}
