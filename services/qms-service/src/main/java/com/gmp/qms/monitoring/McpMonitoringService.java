package com.gmp.qms.monitoring;

import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MCP监控服务，用于收集和管理MCP调用的性能指标
 * 
 * @author GMP系统开发团队
 */
@Service
public class McpMonitoringService {
    
    /**
     * 工具调用统计信息
     */
    private final ConcurrentHashMap<String, ToolMetrics> toolMetrics = new ConcurrentHashMap<>();
    
    /**
     * 资源访问统计信息
     */
    private final ConcurrentHashMap<String, ResourceMetrics> resourceMetrics = new ConcurrentHashMap<>();
    
    /**
     * 系统整体MCP调用统计
     */
    private final SystemMetrics systemMetrics = new SystemMetrics();
    
    /**
     * 记录工具调用开始
     * 
     * @param toolName 工具名称
     * @return 计时对象
     */
    public McpTimer startToolCall(String toolName) {
        systemMetrics.incrementTotalCalls();
        toolMetrics.computeIfAbsent(toolName, k -> new ToolMetrics()).incrementTotalCalls();
        return new McpTimer(toolName, false);
    }
    
    /**
     * 记录资源访问开始
     * 
     * @param resourceName 资源名称
     * @return 计时对象
     */
    public McpTimer startResourceAccess(String resourceName) {
        systemMetrics.incrementTotalCalls();
        resourceMetrics.computeIfAbsent(resourceName, k -> new ResourceMetrics()).incrementTotalCalls();
        return new McpTimer(resourceName, true);
    }
    
    /**
     * 记录工具调用结束
     * 
     * @param timer 计时对象
     * @param success 是否成功
     */
    public void endCall(McpTimer timer, boolean success) {
        long duration = timer.stop();
        
        if (timer.isResourceAccess) {
            ResourceMetrics metrics = resourceMetrics.get(timer.name);
            if (metrics != null) {
                metrics.recordDuration(duration);
                if (success) {
                    metrics.incrementSuccessCalls();
                } else {
                    metrics.incrementFailedCalls();
                    systemMetrics.incrementFailedCalls();
                }
            }
        } else {
            ToolMetrics metrics = toolMetrics.get(timer.name);
            if (metrics != null) {
                metrics.recordDuration(duration);
                if (success) {
                    metrics.incrementSuccessCalls();
                } else {
                    metrics.incrementFailedCalls();
                    systemMetrics.incrementFailedCalls();
                }
            }
        }
        
        if (success) {
            systemMetrics.incrementSuccessCalls();
        }
        
        systemMetrics.recordDuration(duration);
    }
    
    /**
     * 获取工具调用指标
     * 
     * @param toolName 工具名称
     * @return 工具指标
     */
    public ToolMetrics getToolMetrics(String toolName) {
        return toolMetrics.getOrDefault(toolName, new ToolMetrics());
    }
    
    /**
     * 获取资源访问指标
     * 
     * @param resourceName 资源名称
     * @return 资源指标
     */
    public ResourceMetrics getResourceMetrics(String resourceName) {
        return resourceMetrics.getOrDefault(resourceName, new ResourceMetrics());
    }
    
    /**
     * 获取系统整体指标
     * 
     * @return 系统指标
     */
    public SystemMetrics getSystemMetrics() {
        return systemMetrics;
    }
    
    /**
     * 清除所有指标
     */
    public void resetMetrics() {
        toolMetrics.clear();
        resourceMetrics.clear();
        systemMetrics.reset();
    }
    
    /**
     * MCP计时对象
     */
    public class McpTimer {
        private final String name;
        private final boolean isResourceAccess;
        private final StopWatch stopWatch;
        
        public McpTimer(String name, boolean isResourceAccess) {
            this.name = name;
            this.isResourceAccess = isResourceAccess;
            this.stopWatch = new StopWatch();
            this.stopWatch.start();
        }
        
        public long stop() {
            stopWatch.stop();
            return stopWatch.getTotalTimeMillis();
        }
    }
    
    /**
     * 工具调用指标
     */
    public static class ToolMetrics {
        private final AtomicInteger totalCalls = new AtomicInteger(0);
        private final AtomicInteger successCalls = new AtomicInteger(0);
        private final AtomicInteger failedCalls = new AtomicInteger(0);
        private final AtomicLong totalDuration = new AtomicLong(0);
        private final AtomicLong maxDuration = new AtomicLong(0);
        
        public void incrementTotalCalls() {
            totalCalls.incrementAndGet();
        }
        
        public void incrementSuccessCalls() {
            successCalls.incrementAndGet();
        }
        
        public void incrementFailedCalls() {
            failedCalls.incrementAndGet();
        }
        
        public void recordDuration(long duration) {
            totalDuration.addAndGet(duration);
            maxDuration.getAndUpdate(current -> Math.max(current, duration));
        }
        
        public int getTotalCalls() {
            return totalCalls.get();
        }
        
        public int getSuccessCalls() {
            return successCalls.get();
        }
        
        public int getFailedCalls() {
            return failedCalls.get();
        }
        
        public long getTotalDuration() {
            return totalDuration.get();
        }
        
        public long getMaxDuration() {
            return maxDuration.get();
        }
        
        public double getAverageDuration() {
            int calls = totalCalls.get();
            return calls > 0 ? (double) totalDuration.get() / calls : 0;
        }
        
        public double getSuccessRate() {
            int calls = totalCalls.get();
            return calls > 0 ? (double) successCalls.get() / calls * 100 : 0;
        }
    }
    
    /**
     * 资源访问指标
     */
    public static class ResourceMetrics extends ToolMetrics {
        // 可扩展特定于资源的指标
    }
    
    /**
     * 系统整体指标
     */
    public static class SystemMetrics extends ToolMetrics {
        private final AtomicInteger activeCalls = new AtomicInteger(0);
        private final AtomicInteger peakConcurrentCalls = new AtomicInteger(0);
        
        public void incrementActiveCalls() {
            int current = activeCalls.incrementAndGet();
            peakConcurrentCalls.getAndUpdate(peak -> Math.max(peak, current));
        }
        
        public void decrementActiveCalls() {
            activeCalls.decrementAndGet();
        }
        
        public int getActiveCalls() {
            return activeCalls.get();
        }
        
        public int getPeakConcurrentCalls() {
            return peakConcurrentCalls.get();
        }
        
        public void reset() {
            totalCalls.set(0);
            successCalls.set(0);
            failedCalls.set(0);
            totalDuration.set(0);
            maxDuration.set(0);
            activeCalls.set(0);
            peakConcurrentCalls.set(0);
        }
    }
}
