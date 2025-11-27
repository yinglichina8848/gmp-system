package com.gmp.qms.controller;

import com.gmp.qms.monitoring.McpMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * MCP性能指标控制器，用于暴露监控数据和性能指标
 * 
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/mcp/metrics")
@PreAuthorize("hasRole('ADMIN') or hasPermission('MCP_METRICS', 'VIEW')")
public class McpMetricsController {
    
    @Autowired
    private McpMonitoringService monitoringService;
    
    /**
     * 获取系统整体MCP性能指标
     */
    @GetMapping("/system")
    public ResponseEntity<?> getSystemMetrics() {
        McpMonitoringService.SystemMetrics metrics = monitoringService.getSystemMetrics();
        
        Map<String, Object> response = Map.of(
            "totalCalls", metrics.getTotalCalls(),
            "successCalls", metrics.getSuccessCalls(),
            "failedCalls", metrics.getFailedCalls(),
            "successRate", String.format("%.2f%%", metrics.getSuccessRate()),
            "avgResponseTime", String.format("%.2fms", metrics.getAverageDuration()),
            "maxResponseTime", metrics.getMaxDuration() + "ms",
            "activeCalls", metrics.getActiveCalls(),
            "peakConcurrentCalls", metrics.getPeakConcurrentCalls()
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取指定工具的性能指标
     */
    @GetMapping("/tools/{toolName}")
    public ResponseEntity<?> getToolMetrics(@PathVariable String toolName) {
        McpMonitoringService.ToolMetrics metrics = monitoringService.getToolMetrics(toolName);
        
        if (metrics.getTotalCalls() == 0) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = Map.of(
            "toolName", toolName,
            "totalCalls", metrics.getTotalCalls(),
            "successCalls", metrics.getSuccessCalls(),
            "failedCalls", metrics.getFailedCalls(),
            "successRate", String.format("%.2f%%", metrics.getSuccessRate()),
            "avgResponseTime", String.format("%.2fms", metrics.getAverageDuration()),
            "maxResponseTime", metrics.getMaxDuration() + "ms"
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取指定资源的访问指标
     */
    @GetMapping("/resources/{resourceName}")
    public ResponseEntity<?> getResourceMetrics(@PathVariable String resourceName) {
        McpMonitoringService.ResourceMetrics metrics = monitoringService.getResourceMetrics(resourceName);
        
        if (metrics.getTotalCalls() == 0) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> response = Map.of(
            "resourceName", resourceName,
            "totalCalls", metrics.getTotalCalls(),
            "successCalls", metrics.getSuccessCalls(),
            "failedCalls", metrics.getFailedCalls(),
            "successRate", String.format("%.2f%%", metrics.getSuccessRate()),
            "avgResponseTime", String.format("%.2fms", metrics.getAverageDuration()),
            "maxResponseTime", metrics.getMaxDuration() + "ms"
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 重置所有性能指标
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetMetrics() {
        monitoringService.resetMetrics();
        return ResponseEntity.ok(Map.of("message", "MCP metrics have been reset successfully"));
    }
    
    /**
     * 获取性能健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<?> getHealthStatus() {
        McpMonitoringService.SystemMetrics metrics = monitoringService.getSystemMetrics();
        
        // 计算健康状态
        String status;
        String message;
        
        if (metrics.getTotalCalls() == 0) {
            status = "UNKNOWN";
            message = "No MCP calls recorded yet";
        } else if (metrics.getSuccessRate() >= 95) {
            status = "HEALTHY";
            message = "MCP performance is healthy";
        } else if (metrics.getSuccessRate() >= 80) {
            status = "DEGRADED";
            message = "MCP performance is slightly degraded";
        } else {
            status = "UNHEALTHY";
            message = "MCP performance is unhealthy, please investigate";
        }
        
        Map<String, Object> response = Map.of(
            "status", status,
            "message", message,
            "successRate", String.format("%.2f%%", metrics.getSuccessRate()),
            "avgResponseTime", String.format("%.2fms", metrics.getAverageDuration())
        );
        
        return ResponseEntity.ok(response);
    }
}
