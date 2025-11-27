package com.gmp.qms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Map;

/**
 * MCP权限评估器，用于评估MCP请求对特定工具或资源的访问权限
 * 
 * @author GMP系统开发团队
 */
@Component
public class McpPermissionEvaluator implements PermissionEvaluator {
    
    @Autowired
    private McpToolRegistry toolRegistry;
    
    @Autowired
    private McpResourceRegistry resourceRegistry;
    
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        // 获取当前认证的MCP主体
        if (!(authentication.getPrincipal() instanceof McpPrincipal)) {
            return false;
        }
        
        McpPrincipal principal = (McpPrincipal) authentication.getPrincipal();
        
        // 根据目标对象类型评估权限
        if (targetDomainObject instanceof String) {
            String target = (String) targetDomainObject;
            String perm = (String) permission;
            
            // 检查是工具调用还是资源访问
            if (target.startsWith("tool:")) {
                String toolName = target.substring(5); // 去掉 "tool:" 前缀
                return hasToolPermission(principal, toolName, perm);
            } else if (target.startsWith("resource:")) {
                String resourceName = target.substring(9); // 去掉 "resource:" 前缀
                return hasResourcePermission(principal, resourceName, perm);
            }
        }
        
        return false;
    }
    
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        // 获取当前认证的MCP主体
        if (!(authentication.getPrincipal() instanceof McpPrincipal)) {
            return false;
        }
        
        McpPrincipal principal = (McpPrincipal) authentication.getPrincipal();
        String perm = (String) permission;
        
        // 根据目标类型评估权限
        switch (targetType) {
            case "tool":
                return hasToolPermission(principal, targetId.toString(), perm);
            case "resource":
                return hasResourcePermission(principal, targetId.toString(), perm);
            default:
                return false;
        }
    }
    
    /**
     * 检查主体是否有调用特定工具的权限
     */
    private boolean hasToolPermission(McpPrincipal principal, String toolName, String permission) {
        // 检查工具是否存在
        if (!toolRegistry.isToolRegistered(toolName)) {
            return false;
        }
        
        // 获取工具元数据
        Map<String, Object> toolMetadata = toolRegistry.getToolMetadata(toolName);
        
        // 检查是否需要特定角色
        if (toolMetadata.containsKey("requiredRoles")) {
            @SuppressWarnings("unchecked")
            java.util.List<String> requiredRoles = (java.util.List<String>) toolMetadata.get("requiredRoles");
            for (String role : requiredRoles) {
                if (principal.hasRole(role)) {
                    return true;
                }
            }
            return false; // 未找到所需角色
        }
        
        // 检查是否需要特定权限
        if (toolMetadata.containsKey("requiredPermissions")) {
            @SuppressWarnings("unchecked")
            java.util.List<String> requiredPermissions = (java.util.List<String>) toolMetadata.get("requiredPermissions");
            for (String reqPermission : requiredPermissions) {
                if (principal.hasPermission(reqPermission)) {
                    return true;
                }
            }
            return false; // 未找到所需权限
        }
        
        // 默认权限检查逻辑
        // 根据不同的操作类型检查不同的权限
        switch (permission) {
            case "execute":
                // 执行权限：检查工具特定的执行权限或默认的工具执行权限
                return principal.hasPermission("MCP_TOOL_EXECUTE_" + toolName.toUpperCase()) ||
                       principal.hasPermission("MCP_TOOL_EXECUTE_ALL");
            case "read":
                // 读取权限：检查工具特定的读取权限或默认的工具读取权限
                return principal.hasPermission("MCP_TOOL_READ_" + toolName.toUpperCase()) ||
                       principal.hasPermission("MCP_TOOL_READ_ALL");
            default:
                return false;
        }
    }
    
    /**
     * 检查主体是否有访问特定资源的权限
     */
    private boolean hasResourcePermission(McpPrincipal principal, String resourceName, String permission) {
        // 检查资源是否存在
        if (!resourceRegistry.isResourceRegistered(resourceName)) {
            return false;
        }
        
        // 获取资源元数据
        Map<String, Object> resourceMetadata = resourceRegistry.getResourceMetadata(resourceName);
        
        // 检查是否需要特定角色
        if (resourceMetadata.containsKey("requiredRoles")) {
            @SuppressWarnings("unchecked")
            java.util.List<String> requiredRoles = (java.util.List<String>) resourceMetadata.get("requiredRoles");
            for (String role : requiredRoles) {
                if (principal.hasRole(role)) {
                    return true;
                }
            }
            return false; // 未找到所需角色
        }
        
        // 检查是否需要特定权限
        if (resourceMetadata.containsKey("requiredPermissions")) {
            @SuppressWarnings("unchecked")
            java.util.List<String> requiredPermissions = (java.util.List<String>) resourceMetadata.get("requiredPermissions");
            for (String reqPermission : requiredPermissions) {
                if (principal.hasPermission(reqPermission)) {
                    return true;
                }
            }
            return false; // 未找到所需权限
        }
        
        // 默认权限检查逻辑
        // 根据不同的操作类型检查不同的权限
        switch (permission) {
            case "read":
                // 读取权限：检查资源特定的读取权限或默认的资源读取权限
                return principal.hasPermission("MCP_RESOURCE_READ_" + resourceName.toUpperCase()) ||
                       principal.hasPermission("MCP_RESOURCE_READ_ALL");
            case "write":
                // 写入权限：检查资源特定的写入权限或默认的资源写入权限
                return principal.hasPermission("MCP_RESOURCE_WRITE_" + resourceName.toUpperCase()) ||
                       principal.hasPermission("MCP_RESOURCE_WRITE_ALL");
            case "admin":
                // 管理权限：检查资源特定的管理权限或默认的资源管理权限
                return principal.hasPermission("MCP_RESOURCE_ADMIN_" + resourceName.toUpperCase()) ||
                       principal.hasPermission("MCP_RESOURCE_ADMIN_ALL");
            default:
                return false;
        }
    }
}
