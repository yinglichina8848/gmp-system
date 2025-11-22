package com.gmp.auth.controller;

import com.gmp.auth.dto.PasswordChangeRequest;
import com.gmp.auth.service.AuthService;
    // AuditLogService不再直接在Controller中使用
    import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// HashMap不再直接在Controller中使用
import java.util.Map;

/**
 * 密码管理控制器
 */
@RestController
@RequestMapping("/api/auth/password")
public class PasswordController {

    private static final Logger log = LoggerFactory.getLogger(PasswordController.class);

    @Autowired
    private AuthService authService;
    
    // AuditLogService不再直接在Controller中使用

    /**
     * 修改密码
     */
    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            authService.changePassword(username, request);
            return ResponseEntity.ok(Map.of("message", "密码修改成功"));
        } catch (RuntimeException e) {
            log.error("修改密码失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 管理员重置密码
     * 需要适当的权限控制
     */
    @PostMapping("/reset/{username}")
    public ResponseEntity<?> resetPassword(@PathVariable String username, @RequestBody Map<String, String> request) {
        try {
            // 这里应该添加权限检查，确保只有管理员可以重置密码
            String newPassword = request.get("newPassword");
            if (newPassword == null || newPassword.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "新密码不能为空"));
            }
            
            authService.resetPassword(username, newPassword);
            return ResponseEntity.ok(Map.of("message", "密码重置成功"));
        } catch (RuntimeException e) {
            log.error("重置密码失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 获取密码复杂度要求
     */
    @GetMapping("/requirements")
    public ResponseEntity<java.util.Map<String, Object>> getPasswordRequirements() {
        try {
            // 确保返回Map类型，处理可能的类型转换问题
            Object result = authService.getPasswordComplexityRequirements();
            java.util.Map<String, Object> requirements;
            
            if (result instanceof java.util.Map) {
                requirements = (java.util.Map<String, Object>) result;
            } else {
                // 如果返回的不是Map，创建一个新的Map包装结果
                requirements = new java.util.HashMap<>();
                if (result != null) {
                    requirements.put("message", result.toString());
                } else {
                    requirements.put("message", "无密码复杂度要求");
                }
            }
            
            return ResponseEntity.ok(requirements);
        } catch (Exception e) {
            log.error("获取密码复杂度要求失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}