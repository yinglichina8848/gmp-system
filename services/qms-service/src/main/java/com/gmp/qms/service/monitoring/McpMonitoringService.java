package com.gmp.qms.service.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * MCP监控服务，负责收集和管理MCP通信的性能指标、错误日志和系统健康状态
 */
@Slf4j
@Service
public class McpMonitoringService {

    // 计数器存储
    private final Map<String, AtomicLong> counters = new ConcurrentHashMap<>();
    
    // 性能指标存储
    private final Map<String, List<Long>> performanceMetrics = new ConcurrentHashMap<>();
    
    // 错误日志存储
    private final List<ErrorLog> errorLogs = Collections.synchronizedList(new ArrayList<>());
    
    // 系统调用统计
    private final Map<String, SystemCallStats> systemCallStats = new ConcurrentHashMap<>();
    
    // 缓存统计
    private final CacheStats cacheStats = new CacheStats();
    
    // 消息队列统计
    private final MessageQueueStats messageQueueStats = new MessageQueueStats();
    
    // 断路器统计
    private final Map<String, CircuitBreakerStats> circuitBreakerStats = new ConcurrentHashMap<>();

    /**
     * 启动计时
     * @param operationName 操作名称
     * @return McpTimer实例
     */
    public McpTimer startTimer(String operationName) {
        return new McpTimer(operationName);
    }

    /**
     * 增加计数器
     * @param counterName 计数器名称
     * @param tag 标签
     */
    public void incrementCounter(String counterName, String tag) {
        String key = counterName + ":" + tag;
        counters.computeIfAbsent(key, k -> new AtomicLong(0)).incrementAndGet();
    }

    /**
     * 获取系统指标
     * @return 系统指标
     */
    public SystemMetrics getSystemMetrics() {
        SystemMetrics metrics = new SystemMetrics();
        metrics.setTotalCalls(getTotalCalls());
        metrics.setSuccessRate(getSuccessRate());
        metrics.setAvgResponseTime(getAverageResponseTime());
        metrics.setErrorCount(getErrorCount());
        return metrics;
    }

    /**
     * 获取所有系统调用统计
     * @return 系统调用统计映射
     */
    public Map<String, SystemCallStats> getAllSystemCallStats() {
        return new HashMap<>(systemCallStats);
    }

    /**
     * 获取最近的错误
     * @param limit 限制数量
     * @return 错误日志列表
     */
    public List<ErrorLog> getRecentErrors(int limit) {
        synchronized (errorLogs) {
            int size = errorLogs.size();
            int start = Math.max(0, size - limit);
            return new ArrayList<>(errorLogs.subList(start, size));
        }
    }

    /**
     * 获取系统健康状态
     * @return 系统健康状态
     */
    public SystemHealthStatus getSystemHealthStatus() {
        SystemHealthStatus status = new SystemHealthStatus();
        status.setOverallStatus("HEALTHY"); // 默认健康
        status.setActiveConnections(getActiveConnections());
        status.setErrorRate(getErrorRate());
        status.setServiceUptime(getServiceUptime());
        return status;
    }

    /**
     * 根据系统获取调用详情
     * @param systemName 系统名称
     * @param limit 限制数量
     * @return 调用详情列表
     */
    public List<CallDetail> getCallDetailsBySystem(String systemName, int limit) {
        return new ArrayList<>(); // 简化实现，返回空列表
    }

    /**
     * 获取最近的调用详情
     * @param limit 限制数量
     * @return 调用详情列表
     */
    public List<CallDetail> getRecentCallDetails(int limit) {
        return new ArrayList<>(); // 简化实现，返回空列表
    }

    /**
     * 获取错误日志
     * @param page 页码
     * @param pageSize 每页大小
     * @return 错误日志列表
     */
    public List<ErrorLog> getErrorLogs(int page, int pageSize) {
        synchronized (errorLogs) {
            int start = page * pageSize;
            int end = Math.min(start + pageSize, errorLogs.size());
            if (start >= errorLogs.size()) {
                return new ArrayList<>();
            }
            return new ArrayList<>(errorLogs.subList(start, end));
        }
    }

    /**
     * 获取错误日志数量
     * @return 错误日志数量
     */
    public int getErrorLogCount() {
        return errorLogs.size();
    }

    /**
     * 获取缓存统计
     * @return 缓存统计
     */
    public CacheStats getCacheStats() {
        return cacheStats;
    }

    /**
     * 获取消息队列统计
     * @return 消息队列统计
     */
    public MessageQueueStats getMessageQueueStats() {
        return messageQueueStats;
    }

    /**
     * 获取断路器统计
     * @return 断路器统计映射
     */
    public Map<String, CircuitBreakerStats> getCircuitBreakerStats() {
        return new HashMap<>(circuitBreakerStats);
    }

    /**
     * 重置所有指标
     */
    public void resetAllMetrics() {
        counters.clear();
        performanceMetrics.clear();
        errorLogs.clear();
        systemCallStats.clear();
        cacheStats.reset();
        messageQueueStats.reset();
        circuitBreakerStats.clear();
    }

    // 辅助方法
    private long getTotalCalls() {
        return counters.values().stream().mapToLong(AtomicLong::get).sum();
    }

    private double getSuccessRate() {
        // 简化实现
        return 99.5;
    }

    private long getAverageResponseTime() {
        // 简化实现
        return 100;
    }

    private long getErrorCount() {
        return errorLogs.size();
    }

    private double getErrorRate() {
        long totalCalls = getTotalCalls();
        return totalCalls > 0 ? (double) getErrorCount() / totalCalls * 100 : 0;
    }

    private int getActiveConnections() {
        // 简化实现
        return 10;
    }

    private long getServiceUptime() {
        // 简化实现
        return System.currentTimeMillis() / 1000; // 假设服务刚启动
    }

    /**
     * MCP计时器内部类
     */
    public class McpTimer {
        private final String operationName;
        private final long startTime;

        public McpTimer(String operationName) {
            this.operationName = operationName;
            this.startTime = System.currentTimeMillis();
        }

        /**
         * 完成计时
         * @param success 是否成功
         */
        public void complete(boolean success) {
            long duration = System.currentTimeMillis() - startTime;
            
            // 记录性能指标
            performanceMetrics.computeIfAbsent(operationName, k -> Collections.synchronizedList(new ArrayList<>()))
                    .add(duration);
            
            // 更新系统调用统计
            SystemCallStats stats = systemCallStats.computeIfAbsent(operationName, k -> new SystemCallStats());
            stats.incrementCalls();
            if (success) {
                stats.incrementSuccesses();
            } else {
                stats.incrementFailures();
            }
            stats.addResponseTime(duration);
            
            log.debug("Operation {} completed in {}ms, success: {}", operationName, duration, success);
        }
    }

    // 数据模型类
    public static class SystemMetrics {
        private long totalCalls;
        private double successRate;
        private long avgResponseTime;
        private long errorCount;

        // Getters and Setters
        public long getTotalCalls() { return totalCalls; }
        public void setTotalCalls(long totalCalls) { this.totalCalls = totalCalls; }
        public double getSuccessRate() { return successRate; }
        public void setSuccessRate(double successRate) { this.successRate = successRate; }
        public long getAvgResponseTime() { return avgResponseTime; }
        public void setAvgResponseTime(long avgResponseTime) { this.avgResponseTime = avgResponseTime; }
        public long getErrorCount() { return errorCount; }
        public void setErrorCount(long errorCount) { this.errorCount = errorCount; }
    }

    public static class SystemCallStats {
        private long calls = 0;
        private long successes = 0;
        private long failures = 0;
        private long totalResponseTime = 0;

        public void incrementCalls() { calls++; }
        public void incrementSuccesses() { successes++; }
        public void incrementFailures() { failures++; }
        public void addResponseTime(long time) { totalResponseTime += time; }

        // Getters
        public long getCalls() { return calls; }
        public long getSuccesses() { return successes; }
        public long getFailures() { return failures; }
        public double getAverageResponseTime() { 
            return calls > 0 ? (double) totalResponseTime / calls : 0; 
        }
    }

    public static class ErrorLog {
        private String message;
        private String stackTrace;
        private String timestamp;
        private String system;

        // Getters and Setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getStackTrace() { return stackTrace; }
        public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        public String getSystem() { return system; }
        public void setSystem(String system) { this.system = system; }
    }

    public static class SystemHealthStatus {
        private String overallStatus;
        private int activeConnections;
        private double errorRate;
        private long serviceUptime;

        // Getters and Setters
        public String getOverallStatus() { return overallStatus; }
        public void setOverallStatus(String overallStatus) { this.overallStatus = overallStatus; }
        public int getActiveConnections() { return activeConnections; }
        public void setActiveConnections(int activeConnections) { this.activeConnections = activeConnections; }
        public double getErrorRate() { return errorRate; }
        public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
        public long getServiceUptime() { return serviceUptime; }
        public void setServiceUptime(long serviceUptime) { this.serviceUptime = serviceUptime; }
    }

    public static class CallDetail {
        private String system;
        private String operation;
        private long responseTime;
        private boolean success;
        private String timestamp;

        // Getters and Setters
        public String getSystem() { return system; }
        public void setSystem(String system) { this.system = system; }
        public String getOperation() { return operation; }
        public void setOperation(String operation) { this.operation = operation; }
        public long getResponseTime() { return responseTime; }
        public void setResponseTime(long responseTime) { this.responseTime = responseTime; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }

    public static class CacheStats {
        private long hits = 0;
        private long misses = 0;

        public void incrementHits() { hits++; }
        public void incrementMisses() { misses++; }
        public void reset() { hits = 0; misses = 0; }

        // Getters
        public long getHits() { return hits; }
        public long getMisses() { return misses; }
        public double getHitRate() {
            long total = hits + misses;
            return total > 0 ? (double) hits / total * 100 : 0;
        }
    }

    public static class MessageQueueStats {
        private long messagesPublished = 0;
        private long messagesConsumed = 0;
        private long queueSize = 0;

        public void incrementPublished() { messagesPublished++; }
        public void incrementConsumed() { messagesConsumed++; }
        public void setQueueSize(long size) { this.queueSize = size; }
        public void reset() { 
            messagesPublished = 0; 
            messagesConsumed = 0; 
            queueSize = 0; 
        }

        // Getters
        public long getMessagesPublished() { return messagesPublished; }
        public long getMessagesConsumed() { return messagesConsumed; }
        public long getQueueSize() { return queueSize; }
    }

    public static class CircuitBreakerStats {
        private String state;
        private long failures;
        private long lastFailureTime;

        // Getters and Setters
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public long getFailures() { return failures; }
        public void setFailures(long failures) { this.failures = failures; }
        public long getLastFailureTime() { return lastFailureTime; }
        public void setLastFailureTime(long lastFailureTime) { this.lastFailureTime = lastFailureTime; }
    }
}