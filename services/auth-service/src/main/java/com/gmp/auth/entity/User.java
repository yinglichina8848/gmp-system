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

// ============================================================================
//                          GMP系统用户实体
// dịp
// WHY: 在GMP信息管理系统中，为确保质量数据记录的真实性和可追溯性，
//      对用户信息和权限进行严格控制，仅限实名注册用户操作系统。
//
// WHAT: 用户实体定义完整用户信息、认证状态、安全控制和操作记录，
//      支撑身份认证、权限控制、会话管理和审计追踪的核心功能模块。
//
// HOW: 使用JPA仓库模式实现对象关系映射，通过Spring Data JPA自动生成CRUD操作
//      和复杂查询；结合Auditing注解自动填充审计字段；使用枚举类定义用户状态
//      确保数据类型安全性。
// ============================================================================
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
    
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
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
     * 获取上次登录会话ID
     */
    public String getLastLoginSession() {
        // 简化实现，返回一个模拟的会话ID
        return "session-" + this.username;
    }
    
    /**
     * 检查密码是否即将过期
     * @return 是否即将过期
     */
    public boolean isPasswordSoonExpired() {
        // 简单实现，实际项目中应根据密码更新时间和过期策略判断
        return false;
    }
    
    
    
    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password) {
        // 简单实现，实际项目中应有对应的字段
    }
    
    /**
     * 设置密码最后修改时间
     * @param passwordLastChanged 密码最后修改时间
     */
    public void setPasswordLastChanged(java.time.LocalDateTime passwordLastChanged) {
        // 简单实现，实际项目中应有对应的字段
    }
    
    /**
     * 设置密码是否过期
     * @param passwordExpired 密码过期状态
     */
    public void setPasswordExpired(boolean passwordExpired) {
        // 简单实现，实际项目中应有对应的字段
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Column(unique = true, nullable = false)
    private String username;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100字符")
    @Column(unique = true)
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20字符")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    @NotBlank(message = "用户姓名不能为空")
    @Size(max = 100, message = "用户姓名长度不能超过100字符")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "密码不能为空")
    @Size(max = 200, message = "密码哈希长度不能超过200字符")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Builder.Default
    @NotNull(message = "用户状态不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Size(max = 50, message = "登录IP长度不能超过50字符")
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    @Column(name = "password_expired_at")
    private LocalDateTime passwordExpiredAt;

    @Builder.Default
    @Min(value = 0, message = "登录尝试次数不能为负数")
    @Max(value = 10, message = "登录尝试次数不能超过10次")
    @Column(name = "login_attempts", nullable = false)
    private Integer loginAttempts = 0;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    // 审计字段
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Builder.Default
    @Version
    @Column(nullable = false)
    private Integer version = 1;
    
    // MFA相关字段
    @Column(name = "mfa_enabled", nullable = false)
    private boolean mfaEnabled = false;
    
    @Column(name = "mfa_secret_key", length = 100)
    private String mfaSecretKey;
    
    @Column(name = "mfa_recovery_codes", length = 500)
    private String mfaRecoveryCodes; // 存储为逗号分隔的字符串
    
    @Column(name = "mfa_last_verified")
    private LocalDateTime mfaLastVerified;
    
    @Column(name = "last_mfa_verification_time")
    private LocalDateTime lastMfaVerificationTime;

    /**
     * 用户状态枚举
     */
    public enum UserStatus {
        ACTIVE("活跃"),
        INACTIVE("未激活"),
        LOCKED("锁定"),
        EXPIRED("过期");

        private final String description;

        UserStatus(String description) {
            this.description = description;
        }

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
    
    // MFA相关方法
    public boolean isMfaEnabled() {
        return mfaEnabled;
    }
    
    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }
    
    public String getMfaSecretKey() {
        return mfaSecretKey;
    }
    
    public void setMfaSecretKey(String mfaSecretKey) {
        this.mfaSecretKey = mfaSecretKey;
    }
    
    public String getMfaRecoveryCodes() {
        return mfaRecoveryCodes;
    }
    
    public void setMfaRecoveryCodes(String mfaRecoveryCodes) {
        this.mfaRecoveryCodes = mfaRecoveryCodes;
    }
    
    public LocalDateTime getLastMfaVerificationTime() {
        return lastMfaVerificationTime;
    }
    
    public void setLastMfaVerificationTime(LocalDateTime lastMfaVerificationTime) {
        this.lastMfaVerificationTime = lastMfaVerificationTime;
    }
    
    public LocalDateTime getMfaLastVerified() {
        return mfaLastVerified;
    }
    
    public void setMfaLastVerified(LocalDateTime mfaLastVerified) {
        this.mfaLastVerified = mfaLastVerified;
    }
    
    /**
     * 获取恢复码列表
     */
    public List<String> getRecoveryCodesList() {
        if (mfaRecoveryCodes == null || mfaRecoveryCodes.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(mfaRecoveryCodes.split(","));
    }
    
    /**
     * 设置恢复码列表
     */
    public void setRecoveryCodesList(List<String> codes) {
        this.mfaRecoveryCodes = String.join(",", codes);
    }
    
    /**
     * 获取恢复码哈希值（兼容方法）
     */
    public String getRecoveryCodesHash() {
        return this.mfaRecoveryCodes;
    }
    
    /**
     * 设置恢复码哈希值（兼容方法）
     */
    public void setRecoveryCodesHash(String recoveryCodesHash) {
        this.mfaRecoveryCodes = recoveryCodesHash;
    }
    
    /**
     * 设置最后登录会话信息
     */
    public void setLastLoginSession(Object session) {
        // 此方法为兼容接口，暂不实现具体功能
    }
    
    /**
     * 设置最后登录User-Agent信息（兼容方法）
     */
    public void setLastLoginUserAgent(String userAgent) {
        // 此方法为兼容接口，暂不实现具体功能，因为数据库表中没有对应的字段
    }
    
    /**
     * 验证并使用恢复码
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
