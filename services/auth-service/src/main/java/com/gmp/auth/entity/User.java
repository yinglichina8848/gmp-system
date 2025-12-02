package com.gmp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @brief GMP系统用户实体类
 * 
 * @details 该类定义了GMP系统中的用户信息，包括基本信息、认证状态、安全控制和操作记录。
 *          支撑身份认证、权限控制、会话管理和审计追踪的核心功能模块。
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-01-01
 * 
 * @see org.springframework.data.jpa.domain.support.AuditingEntityListener
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    // 添加显式getter方法以确保编译通过，解决可能的Lombok依赖问题
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getLoginIp() {
        return lastLoginIp;
    }

    public Integer getLoginAttempts() {
        return loginAttempts;
    }

    /**
     * 设置登录尝试次数
     */
    public void setLoginAttempts(Integer loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    // 添加更多缺失的方法
    public String getFullName() {
        return fullName;
    }

    /**
     * 获取密码哈希
     * 用于密码验证
     */
    public String getPassword() {
        return passwordHash;
    }

    /**
     * 设置密码哈希
     * 用于密码更新
     */
    public void setPassword(String password) {
        this.passwordHash = password;
    }

    /**
     * 设置密码过期状态
     * 
     * @param expired 是否过期
     */
    public void setPasswordExpired(boolean expired) {
        if (expired) {
            this.passwordExpiredAt = LocalDateTime.now().minusDays(1);
        } else {
            this.passwordExpiredAt = LocalDateTime.now().plusDays(90); // 默认90天后过期
        }
    }

    // 用户角色关联
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    /**
     * 获取用户角色列表
     */
    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    /**
     * 设置用户角色列表
     */
    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    /**
     * 检查用户是否处于活跃状态
     */
    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.userStatus);
    }

    /**
     * 设置用户活跃状态
     */
    public void setActive(boolean active) {
        this.userStatus = active ? UserStatus.ACTIVE : UserStatus.INACTIVE;
    }

    /**
     * 设置用户锁定状态
     */
    public void setLocked(boolean locked) {
        this.userStatus = locked ? UserStatus.LOCKED : UserStatus.ACTIVE;
    }

    /**
     * 设置用户过期状态
     */
    public void setExpired(boolean expired) {
        this.userStatus = expired ? UserStatus.EXPIRED : UserStatus.ACTIVE;
    }

    /**
     * 检查密码是否即将过期
     * 
     * @return 是否即将过期
     */
    public boolean isPasswordSoonExpired() {
        // 简单实现，实际项目中应根据密码更新时间和过期策略判断
        return false;
    }

    /**
     * @brief 用户唯一标识符
     * 
     * @details 主键，自增生成
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @brief 用户名
     * 
     * @details 用于登录系统的唯一标识符，只能包含字母、数字和下划线
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * @brief 用户邮箱
     * 
     * @details 用于系统通知和密码重置
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100字符")
    @Column(unique = true)
    private String email;

    /**
     * @brief 用户手机号
     * 
     * @details 用于系统通知和手机验证码登录
     */
    @Size(max = 20, message = "手机号长度不能超过20字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    /**
     * @brief 用户姓名
     * 
     * @details 用户的真实姓名
     */
    @NotBlank(message = "用户姓名不能为空")
    @Size(max = 100, message = "用户姓名长度不能超过100字符")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    /**
     * @brief 密码哈希值
     * 
     * @details 存储用户密码的加密哈希值
     */
    @NotBlank(message = "密码不能为空")
    @Size(max = 200, message = "密码哈希长度不能超过200字符")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /**
     * @brief 用户状态
     * 
     * @details 枚举类型，表示用户当前状态
     * @see UserStatus
     */
    @Builder.Default
    @NotNull(message = "用户状态不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    /**
     * @brief 最后登录时间
     * 
     * @details 记录用户最后一次成功登录的时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * @brief 最后登录IP
     * 
     * @details 记录用户最后一次成功登录的IP地址
     */
    @Size(max = 50, message = "登录IP长度不能超过50字符")
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    /**
     * @brief 上次登录会话ID
     * 
     * @details 记录用户最后一次登录的会话ID，用于MFA验证等场景
     */
    @Column(name = "last_login_session", length = 100)
    private String lastLoginSession;

    /**
     * @brief 密码过期时间
     * 
     * @details 记录用户密码的过期时间
     */
    @Column(name = "password_expired_at")
    private LocalDateTime passwordExpiredAt;

    /**
     * @brief 登录尝试次数
     * 
     * @details 记录用户连续登录失败的次数，超过阈值会锁定账户
     */
    @Builder.Default
    @Min(value = 0, message = "登录尝试次数不能为负数")
    @Max(value = 10, message = "登录尝试次数不能超过10次")
    @Column(name = "login_attempts", nullable = false)
    private Integer loginAttempts = 0;

    /**
     * @brief 账户锁定截止时间
     * 
     * @details 记录账户被锁定的截止时间
     */
    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    /**
     * @brief 创建时间
     * 
     * @details 记录用户记录的创建时间，由审计机制自动填充
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * @brief 更新时间
     * 
     * @details 记录用户记录的最后更新时间，由审计机制自动填充
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * @brief 创建人ID
     * 
     * @details 记录创建该用户的用户ID
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * @brief 更新人ID
     * 
     * @details 记录最后更新该用户的用户ID
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * @brief 版本号
     * 
     * @details 用于乐观锁机制，防止并发更新冲突
     */
    @Builder.Default
    @Version
    @Column(nullable = false)
    private Integer version = 1;

    /**
     * @brief 是否启用MFA认证
     * 
     * @details 标记用户是否启用了多因素认证
     */
    @Column(name = "mfa_enabled", nullable = false)
    private boolean mfaEnabled = false;

    /**
     * @brief MFA密钥
     * 
     * @details 用于生成和验证MFA验证码的密钥
     */
    @Column(name = "mfa_secret_key", length = 100)
    private String mfaSecretKey;

    /**
     * @brief MFA恢复码
     * 
     * @details 存储为逗号分隔的字符串，用于MFA认证失败时恢复账户访问
     */
    @Column(name = "mfa_recovery_codes", length = 500)
    private String mfaRecoveryCodes;

    /**
     * @brief MFA最后验证时间
     * 
     * @details 记录用户最后一次MFA验证的时间
     */
    @Column(name = "mfa_last_verified")
    private LocalDateTime mfaLastVerified;

    /**
     * @brief 最后MFA验证时间
     * 
     * @details 记录用户最后一次MFA验证的时间（兼容字段）
     */
    @Column(name = "last_mfa_verification_time")
    private LocalDateTime lastMfaVerificationTime;

    /**
     * @brief 用户状态枚举
     * 
     * @details 定义了用户在系统中的各种状态
     */
    public enum UserStatus {
        /**
         * @brief 活跃状态
         * 
         * @details 用户可以正常登录和使用系统
         */
        ACTIVE("活跃"),

        /**
         * @brief 未激活状态
         * 
         * @details 用户尚未激活，无法登录系统
         */
        INACTIVE("未激活"),

        /**
         * @brief 锁定状态
         * 
         * @details 用户因多次登录失败或其他原因被锁定，无法登录系统
         */
        LOCKED("锁定"),

        /**
         * @brief 过期状态
         * 
         * @details 用户账户已过期，无法登录系统
         */
        EXPIRED("过期");

        /**
         * @brief 状态描述
         */
        private final String description;

        /**
         * @brief 构造函数
         * 
         * @param description 状态描述
         */
        UserStatus(String description) {
            this.description = description;
        }

        /**
         * @brief 获取状态描述
         * 
         * @return 状态描述
         */
        public String getDescription() {
            return description;
        }
    }

    /**
     * 判断用户是否被锁定
     */
    @Transient
    public boolean isLocked() {
        return this.userStatus == UserStatus.LOCKED ||
                (this.lockedUntil != null && LocalDateTime.now().isBefore(this.lockedUntil));
    }

    /**
     * 判断密码是否过期
     */
    @Transient
    public boolean isPasswordExpired() {
        return this.passwordExpiredAt != null && LocalDateTime.now().isAfter(this.passwordExpiredAt);
    }

    /**
     * 增加登录尝试次数
     */
    public void incrementLoginAttempts() {
        this.loginAttempts = (this.loginAttempts == null ? 0 : this.loginAttempts) + 1;
    }

    /**
     * 重置登录尝试次数
     */
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.lockedUntil = null;
    }

    /**
     * 锁定用户账户
     */
    public void lockAccount(int lockMinutes) {
        this.userStatus = UserStatus.LOCKED;
        this.lockedUntil = LocalDateTime.now().plusMinutes(lockMinutes);
        this.loginAttempts = 0;
    }

    /**
     * @brief 获取恢复码列表
     * 
     * @details 将逗号分隔的恢复码字符串转换为列表
     * 
     * @return List<String> 恢复码列表
     */
    public List<String> getRecoveryCodesList() {
        if (mfaRecoveryCodes == null || mfaRecoveryCodes.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(mfaRecoveryCodes.split(","));
    }

    /**
     * @brief 设置恢复码列表
     * 
     * @details 将恢复码列表转换为逗号分隔的字符串
     * 
     * @param codes 恢复码列表
     */
    public void setRecoveryCodesList(List<String> codes) {
        this.mfaRecoveryCodes = String.join(",", codes);
    }

    /**
     * @brief 设置最后登录会话信息（兼容方法）
     * 
     * @details 此方法为兼容接口，将Object类型转换为String类型
     * 
     * @param session 会话信息
     */
    public void setLastLoginSession(Object session) {
        this.lastLoginSession = session != null ? session.toString() : null;
    }

    /**
     * @brief 设置最后登录User-Agent信息（兼容方法）
     * 
     * @details 此方法为兼容接口，暂不实现具体功能，因为数据库表中没有对应的字段
     * 
     * @param userAgent User-Agent信息
     */
    public void setLastLoginUserAgent(String userAgent) {
        // 此方法为兼容接口，暂不实现具体功能，因为数据库表中没有对应的字段
    }

    /**
     * @brief 验证并使用恢复码
     * 
     * @details 验证恢复码是否有效，并从列表中移除已使用的恢复码
     * 
     * @param code 恢复码
     * @return boolean 如果恢复码有效返回true，否则返回false
     */
    public boolean useRecoveryCode(String code) {
        List<String> codes = getRecoveryCodesList();
        if (codes.contains(code)) {
            codes.remove(code);
            setRecoveryCodesList(codes);
            return true;
        }
        return false;
    }
}
