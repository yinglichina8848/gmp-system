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

/**
 * GMP系统用户实体
 *
 * @author GMP系统开发团队
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
     * 获取用户角色
     * 注：实际项目中应从用户-角色关联表获取
     */
    public Set<String> getRoles() {
        // 临时实现，实际应通过UserRepository或UserService获取角色信息
        Set<String> roles = new HashSet<>();
        roles.add("USER"); // 默认角色
        return roles;
    }
    
    /**
     * 获取用户权限
     * 注：实际项目中应从用户-角色-权限关联表获取
     */
    public List<String> getPermissions() {
        // 临时实现，实际应通过UserRepository或UserService获取权限信息
        List<String> permissions = new ArrayList<>();
        permissions.add("user:read"); // 默认权限
        return permissions;
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
}
