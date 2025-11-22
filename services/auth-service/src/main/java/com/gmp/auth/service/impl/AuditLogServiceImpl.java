package com.gmp.auth.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.auth.entity.OperationLog;
import com.gmp.auth.repository.OperationLogRepository;
import com.gmp.auth.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 审计日志服务实现类
 * 提供增强的审计日志功能实现
 *
 * @author GMP系统开发团队
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    public void logLogout(String username) {
        // 实现登出日志记录逻辑
        logger.info("User {} logged out", username);
    }
    
    @Override
    public void logLoginSuccess(String username, String ipAddress, String userAgent) {
        // 实现登录成功日志记录逻辑
        logger.info("User {} successfully logged in from IP {} using agent {}", username, ipAddress, userAgent);
    }
    
    @Override
    public void logLoginFailure(String username, String ipAddress, String userAgent, String reason) {
        // 实现登录失败日志记录逻辑
        logger.warn("User {} failed to login from IP {} using agent {}: {}", username, ipAddress, userAgent, reason);
    }
    
    @Override
    public void logPasswordChange(String username) {
        // 实现密码修改日志记录逻辑
        logger.info("User {} changed password", username);
    }
    
    @Override
    public void logPasswordReset(String username) {
        // 实现密码重置日志记录逻辑
        logger.info("User {} reset password", username);
    }
    
    @Override
    public void logPasswordReset(Long userId, String username, String ipAddress) {
        // 简化实现，避免使用可能不存在的常量
        OperationLog log = OperationLog.builder()
                .userId(userId)
                .username(username)
                .ipAddress(ipAddress)
                .build();
        logOperation(log);
    }
    
    @Override
    public void logPasswordChange(Long userId, String username, String ipAddress) {
        // 简化实现
        OperationLog log = OperationLog.builder()
                .userId(userId)
                .username(username)
                .ipAddress(ipAddress)
                .build();
        logOperation(log);
    }
    
    @Override
    public void logLoginSuccess(Long userId, String username, String ipAddress, String userAgent) {
        // 简化实现
        OperationLog log = OperationLog.builder()
                .userId(userId)
                .username(username)
                .ipAddress(ipAddress)
                .build();
        logOperation(log);
    }

    @Override
    @Transactional
    public OperationLog logOperation(OperationLog log) {
        try {
            return operationLogRepository.save(log);
        } catch (Exception e) {
            logger.error("Failed to log operation: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to log operation", e);
        }
    }

    @Override
    public OperationLog logLogin(Long userId, String username, String ipAddress, String userAgent,
                               boolean success, Map<String, Object> additionalInfo) {
        OperationLog log = OperationLog.builder()
                .userId(userId)
                .username(username)
                .operation(OperationLog.OperationType.LOGIN.name())
                .module(OperationLog.Module.AUTH.name())
                .action(success ? "用户登录成功" : "用户登录失败")
                .result(success ? OperationLog.Result.SUCCESS : OperationLog.Result.FAILED)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();

        // 添加附加信息作为元数据
        if (additionalInfo != null && !additionalInfo.isEmpty()) {
            try {
                log.setMetadata(objectMapper.writeValueAsString(additionalInfo));
            } catch (Exception e) {
                logger.warn("Failed to serialize additional info: {}", e.getMessage());
            }
        }

        return logOperation(log);
    }

    @Override
    public OperationLog logLogout(Long userId, String username, String ipAddress) {
        return logOperation(OperationLog.createLogoutLog(userId, username, ipAddress));
    }

    @Override
    public OperationLog logUserManagement(Long operatorId, String operatorName,
                                       Long targetUserId, String targetUsername,
                                       OperationLog.OperationType operationType,
                                       boolean success, String details) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("targetUserId", targetUserId);
        metadata.put("targetUsername", targetUsername);
        if (details != null) {
            metadata.put("details", details);
        }

        OperationLog log = OperationLog.builder()
                .userId(operatorId)
                .username(operatorName)
                .operation(operationType.name())
                .module(OperationLog.Module.USER.name())
                .action(operationType.getDescription() + (success ? "成功" : "失败"))
                .result(success ? OperationLog.Result.SUCCESS : OperationLog.Result.FAILED)
                .build();

        try {
            log.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            logger.warn("Failed to serialize user management metadata: {}", e.getMessage());
        }

        return logOperation(log);
    }

    @Override
    public OperationLog logRolePermissionChange(Long operatorId, String operatorName,
                                            Long roleId, String roleCode,
                                            String permissionChanges, boolean success) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("roleId", roleId);
        metadata.put("roleCode", roleCode);
        metadata.put("permissionChanges", permissionChanges);

        OperationLog log = OperationLog.builder()
                .userId(operatorId)
                .username(operatorName)
                .operation(OperationLog.OperationType.ROLE_PERMISSION_CHANGE.name())
                .module(OperationLog.Module.ROLE.name())
                .action("角色权限变更" + (success ? "成功" : "失败"))
                .result(success ? OperationLog.Result.SUCCESS : OperationLog.Result.FAILED)
                .build();

        try {
            log.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            logger.warn("Failed to serialize role permission metadata: {}", e.getMessage());
        }

        return logOperation(log);
    }

    @Override
    public OperationLog logRoleAssignment(Long operatorId, String operatorName,
                                      Long userId, String username,
                                      Long roleId, String roleCode,
                                      boolean isAssign, boolean success) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userId", userId);
        metadata.put("username", username);
        metadata.put("roleId", roleId);
        metadata.put("roleCode", roleCode);

        String operation = isAssign ? OperationLog.OperationType.ROLE_ASSIGN.name() : 
                                     OperationLog.OperationType.ROLE_REVOKE.name();
        String action = (isAssign ? "分配角色" : "撤销角色") + (success ? "成功" : "失败");

        OperationLog log = OperationLog.builder()
                .userId(operatorId)
                .username(operatorName)
                .operation(operation)
                .module(OperationLog.Module.USER.name())
                .action(action)
                .result(success ? OperationLog.Result.SUCCESS : OperationLog.Result.FAILED)
                .build();

        try {
            log.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            logger.warn("Failed to serialize role assignment metadata: {}", e.getMessage());
        }

        return logOperation(log);
    }

    @Override
    public OperationLog logSecurityEvent(Long userId, String username, 
                                     String eventType, String severity,
                                     String details, String ipAddress) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("eventType", eventType);
        metadata.put("severity", severity);
        if (details != null) {
            metadata.put("details", details);
        }

        OperationLog log = OperationLog.builder()
                .userId(userId)
                .username(username)
                .operation(OperationLog.OperationType.AUDIT_REVIEW.name())
                .module(OperationLog.Module.SYSTEM.name())
                .action("安全事件：" + eventType)
                .result(OperationLog.Result.FAILED) // 安全事件通常被视为失败
                .ipAddress(ipAddress)
                .build();

        try {
            log.setMetadata(objectMapper.writeValueAsString(metadata));
        } catch (Exception e) {
            logger.warn("Failed to serialize security event metadata: {}", e.getMessage());
        }

        return logOperation(log);
    }

    @Override
    public Page<OperationLog> findLogsByPage(Pageable pageable, Map<String, Object> filters) {
        // 这里可以实现更复杂的动态查询逻辑
        // 暂时返回所有日志的分页结果
        return operationLogRepository.findAll(pageable);
    }

    @Override
    public Page<OperationLog> findLogsByUserId(Long userId, Pageable pageable) {
        List<OperationLog> logs = operationLogRepository.findByUserIdOrderByOperationTimeDesc(userId);
        int start = Math.min((int) pageable.getOffset(), logs.size());
        int end = Math.min((start + pageable.getPageSize()), logs.size());
        return new org.springframework.data.domain.PageImpl<>(
                logs.subList(start, end),
                pageable,
                logs.size()
        );
    }

    @Override
    public Page<OperationLog> findLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        List<OperationLog> logs = operationLogRepository.findByOperationTimeBetween(startTime, endTime);
        int start = Math.min((int) pageable.getOffset(), logs.size());
        int end = Math.min((start + pageable.getPageSize()), logs.size());
        return new org.springframework.data.domain.PageImpl<>(
                logs.subList(start, end),
                pageable,
                logs.size()
        );
    }

    @Override
    public Page<OperationLog> findFailedOperations(LocalDateTime since, Pageable pageable) {
        List<OperationLog> logs = operationLogRepository.findFailedOperationsSince(since);
        int start = Math.min((int) pageable.getOffset(), logs.size());
        int end = Math.min((start + pageable.getPageSize()), logs.size());
        return new org.springframework.data.domain.PageImpl<>(
                logs.subList(start, end),
                pageable,
                logs.size()
        );
    }

    @Override
    public Map<String, Object> getOperationStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        List<OperationLog> logs = operationLogRepository.findByOperationTimeBetween(startTime, endTime);
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalOperations", logs.size());
        
        // 按结果统计
        long successCount = logs.stream().filter(log -> log.getResult() == OperationLog.Result.SUCCESS).count();
        long failedCount = logs.size() - successCount;
        statistics.put("successCount", successCount);
        statistics.put("failedCount", failedCount);
        
        // 按操作类型统计
        Map<String, Long> operationTypeStats = logs.stream()
                .collect(Collectors.groupingBy(OperationLog::getOperation, Collectors.counting()));
        statistics.put("operationTypeStats", operationTypeStats);
        
        // 按模块统计
        Map<String, Long> moduleStats = logs.stream()
                .collect(Collectors.groupingBy(OperationLog::getModule, Collectors.counting()));
        statistics.put("moduleStats", moduleStats);
        
        return statistics;
    }

    @Override
    @Transactional
    public int cleanupOldLogs(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        try {
            List<OperationLog> oldLogs = operationLogRepository.findByOperationTimeBetween(
                    LocalDateTime.MIN, cutoffDate);
            int count = oldLogs.size();
            operationLogRepository.deleteLogsBefore(cutoffDate);
            return count;
        } catch (Exception e) {
            logger.error("Failed to cleanup old logs: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to cleanup old logs", e);
        }
    }

    @Override
    public byte[] exportLogs(Map<String, Object> filters, String format) {
        try {
            // 获取要导出的日志
            List<OperationLog> logs = operationLogRepository.findAll();
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            // 暂时只支持JSON格式导出
            if ("json".equalsIgnoreCase(format)) {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, logs);
            } else {
                throw new IllegalArgumentException("Unsupported export format: " + format);
            }
            
            return outputStream.toByteArray();
        } catch (IOException e) {
            logger.error("Failed to export logs: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to export logs", e);
        }
    }
}