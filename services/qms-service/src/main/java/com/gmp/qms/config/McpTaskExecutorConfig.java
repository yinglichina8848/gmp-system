package com.gmp.qms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * MCP任务执行器配置，为异步处理提供专用线程池
 * 
 * @author GMP系统开发团队
 */
@Configuration
public class McpTaskExecutorConfig {
    
    @Autowired
    private McpPerformanceConfig performanceConfig;
    
    /**
     * 配置MCP专用线程池
     */
    @Bean(name = "mcpTaskExecutor")
    public TaskExecutor mcpTaskExecutor() {
        McpPerformanceConfig.ThreadPool poolConfig = performanceConfig.getThreadPool();
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(poolConfig.getCoreThreads());
        // 最大线程数
        executor.setMaxPoolSize(poolConfig.getMaxThreads());
        // 线程名称前缀
        executor.setThreadNamePrefix("mcp-executor-");
        // 队列容量
        executor.setQueueCapacity(poolConfig.getQueueCapacity());
        // 线程存活时间（非核心线程）
        executor.setKeepAliveSeconds(poolConfig.getKeepAliveSeconds());
        
        // 拒绝策略：当线程池饱和时如何处理新任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 初始化
        executor.initialize();
        
        return executor;
    }
    
    /**
     * 配置MCP监控专用线程池
     */
    @Bean(name = "mcpMonitoringTaskExecutor")
    public TaskExecutor mcpMonitoringTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 监控任务使用较小的线程池
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("mcp-monitoring-");
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(60);
        
        // 使用DiscardPolicy，监控任务可以丢弃以保证主业务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        
        return executor;
    }
    
    /**
     * 配置MCP缓存专用线程池
     */
    @Bean(name = "mcpCacheTaskExecutor")
    public TaskExecutor mcpCacheTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(6);
        executor.setThreadNamePrefix("mcp-cache-");
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(30);
        
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        
        return executor;
    }
    
    /**
     * 配置MCP重试专用线程池
     */
    @Bean(name = "mcpRetryTaskExecutor")
    public TaskExecutor mcpRetryTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setThreadNamePrefix("mcp-retry-");
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.initialize();
        
        return executor;
    }
}
