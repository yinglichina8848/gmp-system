package com.gmp.auth.interceptor;

import com.gmp.auth.config.JwtConfig;
import com.gmp.auth.dto.ApiResponse;
import com.gmp.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 子系统访问权限控制拦截器
 * 用于拦截API请求，验证用户对子系统的访问权限
 *
 * @author GMP系统开发团队
 */
@Component
public class SubsystemAccessInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SubsystemAccessInterceptor.class);

    @Autowired
    private JwtConfig jwtConfig;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private ObjectMapper objectMapper;

    // 不需要验证的路径列表
    private static final Set<String> EXCLUDED_PATHS = new HashSet<>(Arrays.asList(
            "/api/auth/login",
            "/api/auth/logout",
            "/api/auth/check/",
            "/api/auth/health",
            "/api/auth/admin/",
            "/api/auth/permissions/",
            "/api/subsystems/check/",
            "/api/subsystems/accessible/",
            "/api/subsystems/access-levels/"
    ));

    // 子系统代码映射表，用于从请求路径中提取子系统代码
    private static final Set<String> SUBSYSTEM_PATHS = new HashSet<>(Arrays.asList(
            "/api/subsystems/",
            "/api/users/",
            "/api/roles/",
            "/api/permissions/"
    ));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        
        log.debug("拦截请求: {} {}", method, requestUri);
        
        // 检查是否需要排除拦截
        if (shouldExclude(requestUri)) {
            log.debug("路径已排除拦截: {}", requestUri);
            return true;
        }
        
        // 从请求头获取令牌
        String token = extractTokenFromHeader(request);
        if (token == null) {
            log.warn("请求缺少令牌: {}", requestUri);
            sendErrorResponse(response, "MISSING_TOKEN", "缺少访问令牌");
            return false;
        }
        
        // 验证令牌
        if (!jwtConfig.validateToken(token)) {
            log.warn("无效的令牌: {}", requestUri);
            sendErrorResponse(response, "INVALID_TOKEN", "无效的访问令牌");
            return false;
        }
        
        // 从令牌中提取用户名
        String username = jwtConfig.extractUsername(token);
        if (username == null) {
            log.warn("令牌中缺少用户名: {}", requestUri);
            sendErrorResponse(response, "INVALID_TOKEN", "令牌格式错误");
            return false;
        }
        
        // 确定请求对应的子系统代码
        String subsystemCode = determineSubsystemCode(requestUri);
        if (subsystemCode != null) {
            // 检查用户对子系统的访问权限
            if (!authService.hasSubsystemAccess(username, subsystemCode)) {
                log.warn("用户[{}]无权访问子系统[{}]: {}", username, subsystemCode, requestUri);
                sendErrorResponse(response, "ACCESS_DENIED", "无权访问该子系统");
                return false;
            }
            
            // 对于需要写入权限的操作，检查是否有写入权限
            if (isWriteOperation(method) && !authService.hasSubsystemWriteAccess(username, subsystemCode)) {
                log.warn("用户[{}]对子系统[{}]无写入权限: {}", username, subsystemCode, requestUri);
                sendErrorResponse(response, "WRITE_ACCESS_DENIED", "对子系统无写入权限");
                return false;
            }
            
            log.debug("用户[{}]对子系统[{}]的访问权限验证通过", username, subsystemCode);
        }
        
        return true;
    }

    /**
     * 检查路径是否应该被排除在拦截之外
     */
    private boolean shouldExclude(String requestUri) {
        // 排除认证相关的公开API
        for (String excludedPath : EXCLUDED_PATHS) {
            if (requestUri.startsWith(excludedPath)) {
                return true;
            }
        }
        
        // 排除OPTIONS请求（CORS预检）
        return requestUri.contains("/swagger-ui/") || requestUri.contains("/v3/api-docs");
    }

    /**
     * 从请求头中提取令牌
     */
    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 根据请求路径确定对应的子系统代码
     */
    private String determineSubsystemCode(String requestUri) {
        // 这里实现一个简单的映射逻辑
        // 实际应用中可能需要更复杂的映射规则或配置
        if (requestUri.startsWith("/api/subsystems/")) {
            return "AUTH";
        } else if (requestUri.startsWith("/api/users/")) {
            return "USER_MANAGEMENT";
        } else if (requestUri.startsWith("/api/roles/")) {
            return "ROLE_MANAGEMENT";
        } else if (requestUri.startsWith("/api/permissions/")) {
            return "PERMISSION_MANAGEMENT";
        }
        
        // 尝试从请求参数中获取子系统代码
        // 例如: ?subsystemCode=ERP
        return null;
    }

    /**
     * 判断是否为写入操作
     */
    private boolean isWriteOperation(String method) {
        return method.equals("POST") || method.equals("PUT") || method.equals("DELETE");
    }

    /**
     * 发送错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String errorCode, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        ApiResponse<?> apiResponse = ApiResponse.error(errorCode, errorMessage);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}