package com.gmp.qms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP性能缓存服务
 */
@Service
public class McpPerformanceCacheService {

    private static final Logger log = LoggerFactory.getLogger(McpPerformanceCacheService.class);
    private final Map<String, Object> cache = new ConcurrentHashMap<>();
    
    /**
     * 获取缓存对象
     * @return 缓存Map
     */
    public Map<String, Object> getCache() {
        return cache;
    }
    
    /**
     * 添加缓存项
     * @param key 缓存键
     * @param value 缓存值
     */
    public void put(String key, Object value) {
        cache.put(key, value);
        log.debug("添加缓存项: {} -> {}", key, value);
    }
    
    /**
     * 获取缓存项
     * @param key 缓存键
     * @return 缓存值
     */
    public Object get(String key) {
        Object value = cache.get(key);
        log.debug("获取缓存项: {} -> {}", key, value);
        return value;
    }
    
    /**
     * 清除缓存
     */
    public void clear() {
        cache.clear();
        log.debug("清除所有缓存");
    }
    
    /**
     * 获取缓存大小
     * @return 缓存项数量
     */
    public int size() {
        return cache.size();
    }
}