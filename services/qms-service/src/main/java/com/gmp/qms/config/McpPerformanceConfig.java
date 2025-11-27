package com.gmp.qms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MCP性能配置类，包含消息队列、线程池等性能相关配置
 */
@Configuration
@ConfigurationProperties(prefix = "mcp.performance")
@Getter
@Setter
public class McpPerformanceConfig {

    // 消息队列配置
    private MessageQueue messageQueue = new MessageQueue();
    
    // 线程池配置
    private ThreadPool threadPool = new ThreadPool();
    
    // 缓存配置
    private Cache cache = new Cache();
    
    // 超时配置
    private Timeout timeout = new Timeout();
    
    /**
     * 消息队列配置类
     */
    @Getter
    @Setter
    public static class MessageQueue {
        // 最大重试次数
        private int maxRetries = 3;
        
        // 重试间隔（毫秒）
        private long retryInterval = 1000;
        
        // 消费者数量
        private int consumerCount = 5;
        
        // 预取数量
        private int prefetchCount = 10;
        
        // 队列容量
        private int queueCapacity = 1000;
    }
    
    /**
     * 线程池配置类
     */
    @Getter
    @Setter
    public static class ThreadPool {
        // 核心线程数
        private int corePoolSize = 10;
        
        // 最大线程数
        private int maxPoolSize = 50;
        
        // 队列容量
        private int queueCapacity = 100;
        
        // 线程存活时间（秒）
        private long keepAliveTime = 60;
    }
    
    /**
     * 缓存配置类
     */
    @Getter
    @Setter
    public static class Cache {
        // 缓存容量
        private int capacity = 1000;
        
        // 缓存过期时间（毫秒）
        private long expiryTime = 3600000;
        
        // 是否启用统计
        private boolean enableStats = true;
    }
    
    /**
     * 超时配置类
     */
    @Getter
    @Setter
    public static class Timeout {
        // 连接超时（毫秒）
        private long connectionTimeout = 5000;
        
        // 读取超时（毫秒）
        private long readTimeout = 10000;
        
        // 写入超时（毫秒）
        private long writeTimeout = 5000;
        
        // 请求超时（毫秒）
        private long requestTimeout = 30000;
    }
}