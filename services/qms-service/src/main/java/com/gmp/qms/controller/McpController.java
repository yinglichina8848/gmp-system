package com.gmp.qms.controller;

import com.gmp.qms.config.McpToolRegistry;
import com.gmp.qms.config.McpResourceRegistry;
import com.gmp.qms.model.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * MCP控制器，处理模型上下文协议请求
 * 
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/mcp")
@RequiredArgsConstructor
public class McpController {
    
    private final McpToolRegistry mcpToolRegistry;
    private final McpResourceRegistry mcpResourceRegistry;
    private final McpToolExecutor mcpToolExecutor;
    private final McpResourceAccess mcpResourceAccess;
    
    /**
     * 获取MCP服务器信息
     */
    @GetMapping("/info")
    public ResponseEntity<ApiResponse> getServerInfo() {
        Map<String, Object> info = Map.of(
            "serverName", "qms-service",
            "version", "1.0.0",
            "description", "GMP质量管理系统MCP服务",
            "availableTools", mcpToolRegistry.getRegisteredTools().size(),
            "availableResources", mcpResourceRegistry.getRegisteredResources().size()
        );
        return ResponseEntity.ok(new ApiResponse(true, "Server info retrieved successfully", info));
    }
    
    /**
     * 获取所有可用工具列表
     */
    @GetMapping("/tools")
    public ResponseEntity<ApiResponse> getAvailableTools() {
        return ResponseEntity.ok(new ApiResponse(true, "Available tools retrieved successfully", 
                mcpToolRegistry.getRegisteredTools()));
    }
    
    /**
     * 获取工具详情
     */
    @GetMapping("/tools/{toolName}")
    public ResponseEntity<ApiResponse> getToolDetails(@PathVariable String toolName) {
        McpToolRegistry.McpToolDefinition tool = mcpToolRegistry.getToolDefinition(toolName);
        if (tool == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Tool not found", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Tool details retrieved successfully", tool));
    }
    
    /**
     * 执行MCP工具调用
     */
    @PostMapping("/tools/{toolName}")
    public ResponseEntity<ApiResponse> executeTool(@PathVariable String toolName, 
                                                 @RequestBody Map<String, Object> parameters) {
        try {
            // 检查工具是否存在
            if (mcpToolRegistry.getToolDefinition(toolName) == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Tool not found", null));
            }
            
            // 执行工具调用
            Object result = mcpToolExecutor.execute(toolName, parameters);
            return ResponseEntity.ok(new ApiResponse(true, "Tool executed successfully", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Tool execution failed: " + e.getMessage(), null));
        }
    }
    
    /**
     * 获取所有可用资源列表
     */
    @GetMapping("/resources")
    public ResponseEntity<ApiResponse> getAvailableResources() {
        return ResponseEntity.ok(new ApiResponse(true, "Available resources retrieved successfully", 
                mcpResourceRegistry.getRegisteredResources()));
    }
    
    /**
     * 获取资源详情
     */
    @GetMapping("/resources/{resourceName}")
    public ResponseEntity<ApiResponse> getResourceDetails(@PathVariable String resourceName) {
        McpResourceRegistry.McpResourceDefinition resource = mcpResourceRegistry.getResourceDefinition(resourceName);
        if (resource == null) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Resource not found", null));
        }
        return ResponseEntity.ok(new ApiResponse(true, "Resource details retrieved successfully", resource));
    }
    
    /**
     * 访问MCP资源
     */
    @PostMapping("/resources/{resourceName}")
    public ResponseEntity<ApiResponse> accessResource(@PathVariable String resourceName, 
                                                    @RequestBody Map<String, Object> parameters) {
        try {
            // 检查资源是否存在
            if (mcpResourceRegistry.getResourceDefinition(resourceName) == null) {
                return ResponseEntity.badRequest().body(new ApiResponse(false, "Resource not found", null));
            }
            
            // 访问资源
            Object result = mcpResourceAccess.access(resourceName, parameters);
            return ResponseEntity.ok(new ApiResponse(true, "Resource accessed successfully", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Resource access failed: " + e.getMessage(), null));
        }
    }
    
    /**
     * 健康检查端点
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheck() {
        return ResponseEntity.ok(new ApiResponse(true, "MCP Service is healthy", null));
    }
    
    /**
     * MCP工具执行器接口
     */
    public interface McpToolExecutor {
        Object execute(String toolName, Map<String, Object> parameters) throws Exception;
    }
    
    /**
     * MCP资源访问接口
     */
    public interface McpResourceAccess {
        Object access(String resourceName, Map<String, Object> parameters) throws Exception;
    }
}
