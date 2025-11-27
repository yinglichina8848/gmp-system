package com.gmp.qms.service.reporting;

import com.gmp.qms.monitoring.McpMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP性能分析报告服务，用于生成定期性能报告和性能趋势分析
 * 
 * @author GMP系统开发团队
 */
@Service
public class McpPerformanceReportService {
    
    @Autowired
    private McpMonitoringService monitoringService;
    
    // 存储历史性能数据，用于趋势分析
    private final Map<String, List<PerformanceSnapshot>> historicalData = new ConcurrentHashMap<>();
    
    // 性能快照类，记录某一时刻的性能指标
    private static class PerformanceSnapshot {
        private final LocalDateTime timestamp;
        private final double successRate;
        private final double avgResponseTime;
        private final int totalCalls;
        private final int failedCalls;
        
        public PerformanceSnapshot(LocalDateTime timestamp, double successRate, double avgResponseTime, int totalCalls, int failedCalls) {
            this.timestamp = timestamp;
            this.successRate = successRate;
            this.avgResponseTime = avgResponseTime;
            this.totalCalls = totalCalls;
            this.failedCalls = failedCalls;
        }
    }
    
    /**
     * 生成当前系统性能报告
     */
    public Map<String, Object> generateCurrentReport() {
        McpMonitoringService.SystemMetrics metrics = monitoringService.getSystemMetrics();
        
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportTime", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        report.put("reportType", "CURRENT_SNAPSHOT");
        
        // 系统总体指标
        Map<String, Object> systemMetrics = new LinkedHashMap<>();
        systemMetrics.put("totalCalls", metrics.getTotalCalls());
        systemMetrics.put("successCalls", metrics.getSuccessCalls());
        systemMetrics.put("failedCalls", metrics.getFailedCalls());
        systemMetrics.put("successRate", String.format("%.2f%%", metrics.getSuccessRate()));
        systemMetrics.put("avgResponseTime", String.format("%.2fms", metrics.getAverageDuration()));
        systemMetrics.put("maxResponseTime", metrics.getMaxDuration() + "ms");
        systemMetrics.put("activeCalls", metrics.getActiveCalls());
        systemMetrics.put("peakConcurrentCalls", metrics.getPeakConcurrentCalls());
        
        report.put("systemMetrics", systemMetrics);
        
        // 工具性能指标
        Map<String, Object> toolMetrics = new LinkedHashMap<>();
        monitoringService.getAllToolMetrics().forEach((toolName, metricsData) -> {
            Map<String, Object> toolData = new LinkedHashMap<>();
            toolData.put("totalCalls", metricsData.getTotalCalls());
            toolData.put("successRate", String.format("%.2f%%", metricsData.getSuccessRate()));
            toolData.put("avgResponseTime", String.format("%.2fms", metricsData.getAverageDuration()));
            toolData.put("maxResponseTime", metricsData.getMaxDuration() + "ms");
            toolMetrics.put(toolName, toolData);
        });
        
        report.put("toolMetrics", toolMetrics);
        
        // 资源访问指标
        Map<String, Object> resourceMetrics = new LinkedHashMap<>();
        monitoringService.getAllResourceMetrics().forEach((resourceName, metricsData) -> {
            Map<String, Object> resourceData = new LinkedHashMap<>();
            resourceData.put("totalCalls", metricsData.getTotalCalls());
            resourceData.put("successRate", String.format("%.2f%%", metricsData.getSuccessRate()));
            resourceData.put("avgResponseTime", String.format("%.2fms", metricsData.getAverageDuration()));
            resourceMetrics.put(resourceName, resourceData);
        });
        
        report.put("resourceMetrics", resourceMetrics);
        
        // 性能建议
        List<String> recommendations = generateRecommendations(metrics);
        report.put("recommendations", recommendations);
        
        return report;
    }
    
    /**
     * 生成性能趋势报告
     */
    public Map<String, Object> generateTrendReport(int hours) {
        Map<String, Object> report = new LinkedHashMap<>();
        report.put("reportTime", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        report.put("reportType", "TREND_ANALYSIS");
        report.put("timeRange", hours + " hours");
        
        // 生成趋势数据
        Map<String, List<Map<String, Object>>> trends = new LinkedHashMap<>();
        
        // 按时间排序的快照列表
        List<PerformanceSnapshot> snapshots = new ArrayList<>(historicalData.getOrDefault("SYSTEM", Collections.emptyList()));
        snapshots.sort(Comparator.comparing(s -> s.timestamp));
        
        // 过滤最近N小时的数据
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(hours);
        List<PerformanceSnapshot> recentSnapshots = snapshots.stream()
            .filter(s -> s.timestamp.isAfter(cutoffTime))
            .collect(java.util.stream.Collectors.toList());
        
        // 准备时间序列数据
        List<Map<String, Object>> timeSeries = new ArrayList<>();
        for (PerformanceSnapshot snapshot : recentSnapshots) {
            Map<String, Object> dataPoint = new LinkedHashMap<>();
            dataPoint.put("timestamp", snapshot.timestamp.format(DateTimeFormatter.ISO_DATE_TIME));
            dataPoint.put("successRate", snapshot.successRate);
            dataPoint.put("avgResponseTime", snapshot.avgResponseTime);
            dataPoint.put("totalCalls", snapshot.totalCalls);
            timeSeries.add(dataPoint);
        }
        
        trends.put("system", timeSeries);
        report.put("trends", trends);
        
        // 计算趋势变化
        if (!recentSnapshots.isEmpty()) {
            PerformanceSnapshot first = recentSnapshots.get(0);
            PerformanceSnapshot last = recentSnapshots.get(recentSnapshots.size() - 1);
            
            Map<String, Object> trendChanges = new LinkedHashMap<>();
            trendChanges.put("successRateChange", String.format("%.2f%%", last.successRate - first.successRate));
            trendChanges.put("responseTimeChange", String.format("%.2f%%", 
                ((last.avgResponseTime - first.avgResponseTime) / first.avgResponseTime) * 100));
            trendChanges.put("callVolumeChange", String.format("%.2f%%", 
                ((last.totalCalls - first.totalCalls) / (double)first.totalCalls) * 100));
            
            report.put("trendChanges", trendChanges);
        }
        
        return report;
    }
    
    /**
     * 生成性能优化建议
     */
    private List<String> generateRecommendations(McpMonitoringService.SystemMetrics metrics) {
        List<String> recommendations = new ArrayList<>();
        
        // 基于成功率的建议
        if (metrics.getSuccessRate() < 85) {
            recommendations.add("警告: MCP调用成功率低于85%，建议检查外部系统连接和错误日志");
        } else if (metrics.getSuccessRate() < 95) {
            recommendations.add("提示: MCP调用成功率低于95%，可以考虑优化错误处理和重试策略");
        }
        
        // 基于响应时间的建议
        if (metrics.getAverageDuration() > 500) {
            recommendations.add("警告: 平均响应时间超过500ms，建议检查网络延迟和外部系统性能");
        } else if (metrics.getAverageDuration() > 200) {
            recommendations.add("提示: 平均响应时间超过200ms，考虑增加缓存或优化调用参数");
        }
        
        // 基于并发调用的建议
        if (metrics.getActiveCalls() > 100) {
            recommendations.add("提示: 当前活跃调用数较高，考虑增加线程池容量");
        }
        
        // 检查是否有监控数据
        if (metrics.getTotalCalls() == 0) {
            recommendations.add("注意: 系统尚未记录任何MCP调用，无法生成详细性能分析");
        }
        
        return recommendations;
    }
    
    /**
     * 定期保存性能快照（每5分钟）
     */
    @Scheduled(fixedRate = 300000) // 5分钟 = 300000毫秒
    public void savePerformanceSnapshot() {
        McpMonitoringService.SystemMetrics metrics = monitoringService.getSystemMetrics();
        
        // 仅在有数据时保存快照
        if (metrics.getTotalCalls() > 0) {
            PerformanceSnapshot snapshot = new PerformanceSnapshot(
                LocalDateTime.now(),
                metrics.getSuccessRate(),
                metrics.getAverageDuration(),
                metrics.getTotalCalls(),
                metrics.getFailedCalls()
            );
            
            // 保存系统级快照
            historicalData.computeIfAbsent("SYSTEM", k -> new ArrayList<>())
                .add(snapshot);
            
            // 保留最近24小时的数据（大约288个快照）
            cleanOldSnapshots("SYSTEM", 288);
        }
    }
    
    /**
     * 清理旧的快照数据
     */
    private void cleanOldSnapshots(String key, int maxSnapshots) {
        List<PerformanceSnapshot> snapshots = historicalData.get(key);
        if (snapshots != null && snapshots.size() > maxSnapshots) {
            // 移除最旧的数据，保留最新的N个快照
            snapshots.subList(0, snapshots.size() - maxSnapshots).clear();
        }
    }
    
    /**
     * 清除所有历史数据
     */
    public void clearHistoricalData() {
        historicalData.clear();
    }
    
    /**
     * 获取当前历史数据大小
     */
    public Map<String, Integer> getHistoricalDataSize() {
        Map<String, Integer> sizeMap = new LinkedHashMap<>();
        historicalData.forEach((key, snapshots) -> 
            sizeMap.put(key, snapshots.size())
        );
        return sizeMap;
    }
}
