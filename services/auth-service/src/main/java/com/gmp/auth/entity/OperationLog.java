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
 * @brief 用户操作审计日志实体类
 * 
 * @details 该类用于记录用户在系统中的所有操作，包括认证操作、用户管理、角色管理和系统管理等。
 * 支持审计追踪、操作监控和安全分析等功能。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 */
@Entity
@Table(name = "user_operation_logs")
public class OperationLog {

    /**
     * @brief 日志唯一标识符
     * 
     * @details 主键，自增生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief 用户ID
     * 
     * @details 执行操作的用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * @brief 用户名
     * 
     * @details 执行操作的用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * @brief 操作类型
     * 
     * @details 操作的类型，对应OperationType枚举
     */
    @NotBlank(message = "操作类型不能为空")
    @Column(nullable = false)
    private String operation;

    /**
     * @brief 操作模块
     * 
     * @details 操作所属的模块，对应Module枚举
     */
    @NotBlank(message = "模块不能为空")
    @Column(nullable = false)
    private String module;

    /**
     * @brief 操作动作
     * 
     * @details 具体的操作动作描述
     */
    @NotBlank(message = "操作动作不能为空")
    @Column(nullable = false)
    private String action;

    /**
     * @brief 操作结果
     * 
     * @details 操作的执行结果，成功或失败
     */
    @NotNull(message = "结果不能为空")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Result result = Result.SUCCESS;

    /**
     * @brief IP地址
     * 
     * @details 执行操作的客户端IP地址
     */
    @Column(name = "ip_address")
    private String ipAddress;

    /**
     * @brief 用户代理
     * 
     * @details 执行操作的客户端浏览器信息
     */
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    /**
     * @brief 请求数据
     * 
     * @details 操作的请求数据，JSON格式
     */
    @Column(name = "request_data", columnDefinition = "TEXT")
    private String requestData;

    /**
     * @brief 响应数据
     * 
     * @details 操作的响应数据，JSON格式
     */
    @Column(name = "response_data", columnDefinition = "TEXT")
    private String responseData;

    /**
     * @brief 操作时间
     * 
     * @details 操作执行的时间
     */
    @NotNull(message = "操作时间不能为空")
    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    /**
     * @brief 操作耗时
     * 
     * @details 操作执行的耗时，单位为毫秒
     */
    @Column(name = "duration_ms")
    private Integer durationMs;

    /**
     * @brief 元数据
     * 
     * @details 操作的额外元数据，JSON格式
     */
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
     * @brief 操作结果枚举
     * 
     * @details 定义了操作的执行结果
     */
    public enum Result {
        /**
         * @brief 成功
         * 
         * @details 操作执行成功
         */
        SUCCESS("成功"),
        
        /**
         * @brief 失败
         * 
         * @details 操作执行失败
         */
        FAILED("失败");

        /**
         * @brief 结果描述
         */
        private final String description;

        /**
         * @brief 构造函数
         * 
         * @param description 结果描述
         */
        Result(String description) {
            this.description = description;
        }

        /**
         * @brief 获取结果描述
         * 
         * @return String 结果描述
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * @brief 操作类型枚举
     * 
     * @details 定义了系统支持的各种操作类型
     */
    public enum OperationType {
        /**
         * @brief 用户登录
         * 
         * @details 用户登录系统
         */
        LOGIN("用户登录"),
        
        /**
         * @brief 用户登出
         * 
         * @details 用户退出系统
         */
        LOGOUT("用户登出"),

        /**
         * @brief 创建用户
         * 
         * @details 创建新用户
         */
        USER_CREATE("创建用户"),
        
        /**
         * @brief 更新用户
         * 
         * @details 更新现有用户信息
         */
        USER_UPDATE("更新用户"),
        
        /**
         * @brief 删除用户
         * 
         * @details 删除用户
         */
        USER_DELETE("删除用户"),
        
        /**
         * @brief 用户状态变更
         * 
         * @details 变更用户状态
         */
        USER_STATUS_CHANGE("用户状态变更"),

        /**
         * @brief 分配角色
         * 
         * @details 为用户分配角色
         */
        ROLE_ASSIGN("分配角色"),
        
        /**
         * @brief 撤销角色
         * 
         * @details 撤销用户的角色
         */
        ROLE_REVOKE("撤销角色"),
        
        /**
         * @brief 角色权限变更
         * 
         * @details 变更角色的权限
         */
        ROLE_PERMISSION_CHANGE("角色权限变更"),

        /**
         * @brief 权限变更
         * 
         * @details 变更权限设置
         */
        PERMISSION_CHANGE("权限变更"),
        
        /**
         * @brief 系统配置
         * 
         * @details 变更系统配置
         */
        SYSTEM_CONFIG("系统配置"),
        
        /**
         * @brief 审计审查
         * 
         * @details 审查系统审计日志
         */
        AUDIT_REVIEW("审计审查");

        /**
         * @brief 操作类型描述
         */
        private final String description;

        /**
         * @brief 构造函数
         * 
         * @param description 操作类型描述
         */
        OperationType(String description) {
            this.description = description;
        }

        /**
         * @brief 获取操作类型描述
         * 
         * @return String 操作类型描述
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * @brief 模块枚举
     * 
     * @details 定义了系统的各个模块
     */
    public enum Module {
        /**
         * @brief 认证模块
         * 
         * @details 负责用户认证和授权
         */
        AUTH("认证模块"),
        
        /**
         * @brief 用户管理
         * 
         * @details 负责用户信息管理
         */
        USER("用户管理"),
        
        /**
         * @brief 角色管理
         * 
         * @details 负责角色信息管理
         */
        ROLE("角色管理"),
        
        /**
         * @brief 权限管理
         * 
         * @details 负责权限信息管理
         */
        PERMISSION("权限管理"),
        
        /**
         * @brief 系统管理
         * 
         * @details 负责系统配置和管理
         */
        SYSTEM("系统管理");

        /**
         * @brief 模块描述
         */
        private final String description;

        /**
         * @brief 构造函数
         * 
         * @param description 模块描述
         */
        Module(String description) {
            this.description = description;
        }

        /**
         * @brief 获取模块描述
         * 
         * @return String 模块描述
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * @brief 预持久化处理
     * 
     * @details 在实体持久化之前执行，设置默认值
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
     * @brief 创建登录日志
     * 
     * @details 创建用户登录操作的日志记录
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     * @param userAgent 用户代理
     * @param success 是否登录成功
     * @return OperationLog 登录日志实体
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
     * @brief 创建登出日志
     * 
     * @details 创建用户登出操作的日志记录
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param ipAddress IP地址
     * @return OperationLog 登出日志实体
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
     * @brief 创建用户操作日志
     * 
     * @details 创建用户管理操作的日志记录
     * 
     * @param userId 用户ID
     * @param username 用户名
     * @param operationType 操作类型
     * @param action 操作动作
     * @param success 是否操作成功
     * @return OperationLog 用户操作日志实体
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
