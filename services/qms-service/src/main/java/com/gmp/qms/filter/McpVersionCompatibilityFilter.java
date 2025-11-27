package com.gmp.qms.filter;

import com.gmp.qms.util.McpVersionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MCP版本兼容性过滤器
 * 在请求处理前验证客户端版本的兼容性
 * 过滤掉不兼容版本的请求，确保系统安全性和稳定性
 */
@Slf4j
@Component
@Order(2) // 确保在认证过滤器之后执行
@RequiredArgsConstructor
public class McpVersionCompatibilityFilter extends OncePerRequestFilter {

    // MCP版本头名称
    public static final String MCP_VERSION_HEADER = "X-MCP-Version";
    // 系统名称头名称
    public static final String MCP_SYSTEM_HEADER = "X-MCP-System";
    // 不兼容版本响应消息
    private static final String INCOMPATIBLE_VERSION_MESSAGE = "Incompatible MCP version. Please update your client.";
    
    // 版本控制工具
    private final McpVersionControl versionControl;
    
    // 缓存已验证的版本兼容性结果，减少重复验证
    private final Map<String, Boolean> versionCache = new ConcurrentHashMap<>();
    
    // 不需要版本检查的路径
    private static final String[] EXCLUDED_PATHS = {
        "/health", 
        "/actuator", 
        "/api/v1/mcp/version",
        "/api/v1/mcp/compatibility"
    };
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // 检查是否需要跳过版本验证
        if (shouldSkipVersionCheck(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 从请求头获取版本信息
        String clientVersion = request.getHeader(MCP_VERSION_HEADER);
        String systemName = request.getHeader(MCP_SYSTEM_HEADER);
        
        // 检查必要的头信息
        if (clientVersion == null || clientVersion.isEmpty()) {
            handleMissingVersionHeader(response);
            return;
        }
        
        if (systemName == null || systemName.isEmpty()) {
            systemName = "Unknown-System";
            log.warn("Missing system name header for request from {}", request.getRemoteAddr());
        }
        
        try {
            // 检查版本缓存
            String cacheKey = systemName + ":" + clientVersion;
            Boolean isCompatible = versionCache.get(cacheKey);
            
            // 如果缓存中没有或需要刷新，则验证版本
            if (isCompatible == null) {
                isCompatible = versionControl.isCompatible(clientVersion);
                versionCache.put(cacheKey, isCompatible);
                
                // 记录版本兼容性信息
                logVersionInfo(systemName, clientVersion, isCompatible);
            }
            
            // 处理不兼容的版本
            if (!isCompatible) {
                handleIncompatibleVersion(response, systemName, clientVersion);
                return;
            }
            
            // 添加版本信息到请求属性，以便后续处理
            request.setAttribute("mcpClientVersion", clientVersion);
            request.setAttribute("mcpSystemName", systemName);
            
            // 继续处理请求
            filterChain.doFilter(request, response);
            
        } catch (IllegalArgumentException e) {
            // 处理版本格式错误
            handleInvalidVersionFormat(response, clientVersion, e.getMessage());
        } catch (Exception e) {
            // 处理其他异常
            log.error("Error during version compatibility check: {}", e.getMessage(), e);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write("Internal server error during version check");
        }
    }
    
    /**
     * 判断是否需要跳过版本检查
     */
    private boolean shouldSkipVersionCheck(String requestUri) {
        // 检查是否为排除路径
        for (String excludedPath : EXCLUDED_PATHS) {
            if (requestUri.startsWith(excludedPath)) {
                return true;
            }
        }
        
        // 非MCP API路径跳过检查
        return !requestUri.startsWith("/api/v1/mcp");
    }
    
    /**
     * 处理缺少版本头的情况
     */
    private void handleMissingVersionHeader(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Missing required header: X-MCP-Version\"}");
        log.warn("Request rejected: Missing X-MCP-Version header");
    }
    
    /**
     * 处理版本格式错误的情况
     */
    private void handleInvalidVersionFormat(HttpServletResponse response, String version,
                                          String errorMessage) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\":\"Invalid version format: %s\"}", errorMessage));
        log.warn("Request rejected: Invalid version format '{}' - {}", version, errorMessage);
    }
    
    /**
     * 处理不兼容版本的情况
     */
    private void handleIncompatibleVersion(HttpServletResponse response, String systemName,
                                         String clientVersion) throws IOException {
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setContentType("application/json");
        response.getWriter().write(String.format(
            "{\"error\":\"%s\",\"required_version\":\"%s\"}", 
            INCOMPATIBLE_VERSION_MESSAGE, McpVersionControl.getCurrentVersion()
        ));
        
        // 添加版本信息到响应头
        response.setHeader(MCP_VERSION_HEADER, McpVersionControl.getCurrentVersion());
        
        log.warn("Request rejected: System '{}' (version {}) is incompatible with current MCP version {}",
                 systemName, clientVersion, McpVersionControl.getCurrentVersion());
    }
    
    /**
     * 记录版本信息
     */
    private void logVersionInfo(String systemName, String clientVersion, boolean isCompatible) {
        String logMessage = String.format(
            "System: %s, Version: %s, Compatible: %s, Current Version: %s",
            systemName, clientVersion, isCompatible, McpVersionControl.getCurrentVersion()
        );
        
        if (isCompatible) {
            log.info(logMessage);
        } else {
            log.warn(logMessage);
        }
    }
    
    /**
     * 清除版本缓存
     * 用于在版本更新或配置更改后刷新缓存
     */
    public void clearVersionCache() {
        versionCache.clear();
        log.info("MCP version compatibility cache cleared");
    }
    
    /**
     * 获取当前版本缓存统计信息
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new ConcurrentHashMap<>();
        stats.put("cacheSize", versionCache.size());
        
        // 计算兼容和不兼容的版本数量
        long compatibleCount = versionCache.values().stream().filter(v -> v).count();
        long incompatibleCount = versionCache.size() - compatibleCount;
        
        stats.put("compatibleCount", compatibleCount);
        stats.put("incompatibleCount", incompatibleCount);
        
        return stats;
    }
}