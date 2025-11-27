package com.gmp.qms.controller.dashboard;

import com.gmp.qms.monitoring.McpMonitoringService;
import com.gmp.qms.service.reporting.McpPerformanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * MCP集成监控控制台控制器
 * 提供MCP集成的可视化监控界面和数据接口
 * 
 * @author GMP系统开发团队
 */
@Controller
@RequestMapping("/dashboard/mcp")
public class McpDashboardController {

    @Autowired
    private McpMonitoringService monitoringService;

    @Autowired
    private McpPerformanceReportService reportService;

    /**
     * 监控控制台首页
     */
    @GetMapping
    public String dashboard(Model model) {
        // 获取系统整体性能指标
        Map<String, Long> systemMetrics = monitoringService.getSystemMetrics();
        
        // 获取各系统调用统计
        Map<String, Map<String, Long>> systemCallStats = monitoringService.getAllSystemCallStats();
        
        // 获取最近的性能报告
        Map<String, Object> latestReport = reportService.getLatestPerformanceReport();
        
        // 添加模型数据
        model.addAttribute("systemMetrics", systemMetrics);
        model.addAttribute("systemCallStats", systemCallStats);
        model.addAttribute("latestReport", latestReport);
        model.addAttribute("timestamp", System.currentTimeMillis());
        
        return "dashboard/mcp-dashboard";
    }

    /**
     * 获取实时监控数据（用于前端轮询）
     */
    @GetMapping("/realtime")
    @ResponseBody
    public Map<String, Object> getRealtimeData() {
        Map<String, Object> data = new java.util.HashMap<>();
        
        data.put("systemMetrics", monitoringService.getSystemMetrics());
        data.put("systemCallStats", monitoringService.getAllSystemCallStats());
        data.put("recentErrors", monitoringService.getRecentErrors(10));
        data.put("timestamp", System.currentTimeMillis());
        
        return data;
    }

    /**
     * 获取系统健康状态
     */
    @GetMapping("/health")
    @ResponseBody
    public Map<String, Object> getHealthStatus() {
        return monitoringService.getSystemHealthStatus();
    }

    /**
     * 获取性能趋势数据
     */
    @GetMapping("/trends")
    @ResponseBody
    public Map<String, Object> getPerformanceTrends(
            @RequestParam(defaultValue = "hour") String period,
            @RequestParam(defaultValue = "6") int count) {
        
        switch (period) {
            case "minute":
                return reportService.getPerformanceTrendsByMinute(count);
            case "hour":
                return reportService.getPerformanceTrendsByHour(count);
            case "day":
                return reportService.getPerformanceTrendsByDay(count);
            case "week":
                return reportService.getPerformanceTrendsByWeek(count);
            default:
                return reportService.getPerformanceTrendsByHour(6);
        }
    }

    /**
     * 获取系统调用详情
     */
    @GetMapping("/calls/detail")
    @ResponseBody
    public List<Map<String, Object>> getCallDetails(
            @RequestParam(required = false) String system,
            @RequestParam(defaultValue = "100") int limit) {
        
        if (system != null && !system.isEmpty()) {
            return monitoringService.getCallDetailsBySystem(system, limit);
        } else {
            return monitoringService.getRecentCallDetails(limit);
        }
    }

    /**
     * 获取错误日志
     */
    @GetMapping("/errors")
    @ResponseBody
    public Map<String, Object> getErrorLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("errors", monitoringService.getErrorLogs(page, size));
        result.put("total", monitoringService.getErrorLogCount());
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    /**
     * 获取缓存状态
     */
    @GetMapping("/cache/status")
    @ResponseBody
    public Map<String, Object> getCacheStatus() {
        return monitoringService.getCacheStats();
    }

    /**
     * 获取消息队列状态
     */
    @GetMapping("/mq/status")
    @ResponseBody
    public Map<String, Object> getMessageQueueStatus() {
        return monitoringService.getMessageQueueStats();
    }

    /**
     * 获取断路器状态
     */
    @GetMapping("/circuit-breakers")
    @ResponseBody
    public Map<String, Object> getCircuitBreakerStatus() {
        return monitoringService.getCircuitBreakerStats();
    }

    /**
     * 重置所有性能指标
     */
    @GetMapping("/reset")
    @ResponseBody
    public Map<String, Object> resetMetrics() {
        monitoringService.resetAllMetrics();
        
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("success", true);
        result.put("message", "所有性能指标已重置");
        
        return result;
    }
}
