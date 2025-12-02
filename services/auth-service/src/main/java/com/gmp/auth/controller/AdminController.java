package com.gmp.auth.controller;

import com.gmp.auth.entity.OperationLog;
import com.gmp.auth.service.AuditLogService;
import com.gmp.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 管理控制器
 * 提供管理员专用的API接口，包括审计日志查询和权限管理
 * 
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/auth/admin")
@CrossOrigin("*")
public class AdminController {
    
    @Autowired
    private AuditLogService auditLogService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * 获取审计日志列表
     * 
     * @param pageable 分页参数
     * @return 审计日志分页数据
     */
    @GetMapping("/audit-logs")
    public ResponseEntity<Page<OperationLog>> getAuditLogs(Pageable pageable) {
        // 获取所有审计日志（简化实现，实际应该支持过滤条件）
        Page<OperationLog> logs = auditLogService.findLogsByTimeRange(null, null, pageable);
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 获取用户权限信息
     * 
     * @param username 用户名
     * @return 用户权限信息
     */
    @GetMapping("/permissions/{username}")
    public ResponseEntity<Map<String, Object>> getUserPermissions(@PathVariable String username) {
        // 获取用户权限信息（简化实现）
        Set<String> roles = authService.getUserRoles(username);
        List<String> permissions = authService.getUserPermissions(username);
        
        Map<String, Object> result = Map.of(
            "username", username,
            "roles", roles,
            "permissions", permissions,
            "status", "active"
        );
        return ResponseEntity.ok(result);
    }
}