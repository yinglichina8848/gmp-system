package com.gmp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

/**
 * 用户操作审计日志实体
 *
 * @author GMP系统开发团队
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_operation_logs")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @NotBlank(message = "操作类型不能为空")
    @Column(nullable = false)
    private String operation;

    @NotBlank(message = "模块不能为空")
    @Column(nullable = false)
    private String module;

    @NotBlank(message = "操作动作不能为空")
    @Column(nullable = false)
    private String action;

    @NotNull(message = "结果不能为空")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Result result = Result.SUCCESS;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "request_data", columnDefinition = "JSONB")
    private JsonNode requestData;

    @Column(name = "response_data", columnDefinition = "JSONB")
    private JsonNode responseData;

    @NotNull(message = "操作时间不能为空")
    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(columnDefinition = "JSONB")
    private JsonNode metadata;

    /**
     * 操作结果枚举
     */
    public enum Result {
        SUCCESS("成功"),
        FAILED("失败");

        private final String description;

        Result(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 操作类型枚举
     */
    public enum OperationType {
        // 认证操作
        LOGIN("用户登录"),
        LOGOUT("用户登出"),

        // 用户管理
        USER_CREATE("创建用户"),
        USER_UPDATE("更新用户"),
        USER_DELETE("删除用户"),
        USER_STATUS_CHANGE("用户状态变更"),

        // 角色管理
        ROLE_ASSIGN("分配角色"),
        ROLE_REVOKE("撤销角色"),
        ROLE_PERMISSION_CHANGE("角色权限变更"),

        // 系统管理
        PERMISSION_CHANGE("权限变更"),
        SYSTEM_CONFIG("系统配置"),
        AUDIT_REVIEW("审计审查");

        private final String description;

        OperationType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 模块枚举
     */
    public enum Module {
        AUTH("认证模块"),
        USER("用户管理"),
        ROLE("角色管理"),
        PERMISSION("权限管理"),
        SYSTEM("系统管理");

        private final String description;

        Module(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * 预持久化处理
     */
    @PrePersist
    protected void onCreate() {
        if (operationTime == null) {
            operationTime = LocalDateTime.now();
        }
        if (result == null) {
            result = Result.SUCCESS;
        }
    }

    /**
     * 创建登录日志
     */
    public static OperationLog createLoginLog(Long userId, String username, String ipAddress,
            String userAgent, boolean success) {
        return OperationLog.builder()
                .userId(userId)
                .username(username)
                .operation(OperationType.LOGIN.name())
                .module(Module.AUTH.name())
                .action(success ? "用户登录成功" : "用户登录失败")
                .result(success ? Result.SUCCESS : Result.FAILED)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .build();
    }

    /**
     * 创建登出日志
     */
    public static OperationLog createLogoutLog(Long userId, String username, String ipAddress) {
        return OperationLog.builder()
                .userId(userId)
                .username(username)
                .operation(OperationType.LOGOUT.name())
                .module(Module.AUTH.name())
                .action("用户登出")
                .result(Result.SUCCESS)
                .ipAddress(ipAddress)
                .build();
    }

    /**
     * 创建用户操作日志
     */
    public static OperationLog createUserOperationLog(Long userId, String username,
            OperationType operationType,
            String action, boolean success) {
        return OperationLog.builder()
                .userId(userId)
                .username(username)
                .operation(operationType.name())
                .module(Module.USER.name())
                .action(action)
                .result(success ? Result.SUCCESS : Result.FAILED)
                .build();
    }
}
