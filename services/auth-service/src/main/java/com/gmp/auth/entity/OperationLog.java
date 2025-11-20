package com.gmp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 用户操作审计日志实体
 *
 * @author GMP系统开发团队
 */
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

    @Column(name = "request_data", columnDefinition = "TEXT")
    private String requestData;

    @Column(name = "response_data", columnDefinition = "TEXT")
    private String responseData;

    @NotNull(message = "操作时间不能为空")
    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    /**
     * 无参构造函数
     */
    public OperationLog() {
        this.operationTime = LocalDateTime.now();
        this.result = Result.SUCCESS;
    }
    
    /**
     * 全参构造函数
     */
    public OperationLog(Long id, Long userId, String username, String operation, String module, 
                       String action, Result result, String ipAddress, String userAgent, 
                       String requestData, String responseData, LocalDateTime operationTime, 
                       Integer durationMs, String metadata) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.operation = operation;
        this.module = module;
        this.action = action;
        this.result = result;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.requestData = requestData;
        this.responseData = responseData;
        this.operationTime = operationTime;
        this.durationMs = durationMs;
        this.metadata = metadata;
    }
    
    /**
     * Builder方法实现
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * Builder内部类
     */
    public static class Builder {
        private Long id;
        private Long userId;
        private String username;
        private String operation;
        private String module;
        private String action;
        private Result result = Result.SUCCESS;
        private String ipAddress;
        private String userAgent;
        private String requestData;
        private String responseData;
        private LocalDateTime operationTime = LocalDateTime.now();
        private Integer durationMs;
        private String metadata;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder operation(String operation) {
            this.operation = operation;
            return this;
        }
        
        public Builder module(String module) {
            this.module = module;
            return this;
        }
        
        public Builder action(String action) {
            this.action = action;
            return this;
        }
        
        public Builder result(Result result) {
            this.result = result;
            return this;
        }
        
        public Builder ipAddress(String ipAddress) {
            this.ipAddress = ipAddress;
            return this;
        }
        
        public Builder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        
        public Builder requestData(String requestData) {
            this.requestData = requestData;
            return this;
        }
        
        public Builder responseData(String responseData) {
            this.responseData = responseData;
            return this;
        }
        
        public Builder operationTime(LocalDateTime operationTime) {
            this.operationTime = operationTime;
            return this;
        }
        
        public Builder durationMs(Integer durationMs) {
            this.durationMs = durationMs;
            return this;
        }
        
        public Builder metadata(String metadata) {
            this.metadata = metadata;
            return this;
        }
        
        public OperationLog build() {
            return new OperationLog(id, userId, username, operation, module, action, result, 
                                  ipAddress, userAgent, requestData, responseData, operationTime, 
                                  durationMs, metadata);
        }
    }
    
    // Getter and Setter methods
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Result getResult() {
        return result;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
    
    public String getRequestData() {
        return requestData;
    }
    
    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }
    
    public String getResponseData() {
        return responseData;
    }
    
    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }
    
    public LocalDateTime getOperationTime() {
        return operationTime;
    }
    
    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }
    
    public Integer getDurationMs() {
        return durationMs;
    }
    
    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }
    
    public String getMetadata() {
        return metadata;
    }
    
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

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
