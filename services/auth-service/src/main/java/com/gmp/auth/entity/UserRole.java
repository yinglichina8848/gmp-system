package com.gmp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 用户角色关联实体
 *
 * @author GMP系统开发团队
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "角色ID不能为空")
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "assigned_by")
    private Long assignedBy;

    @Column(name = "assigned_at_audit")
    private LocalDateTime assignedAtAudit;

    /**
     * 检查关联是否有效
     */
    @Transient
    public boolean isValid() {
        if (!isActive) {
            return false;
        }
        return expiredAt == null || LocalDateTime.now().isBefore(expiredAt);
    }

    /**
     * 预持久化处理
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (assignedAt == null) {
            assignedAt = now;
        }
        if (assignedAtAudit == null) {
            assignedAtAudit = now;
        }
    }
    
    // Setter方法
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    
    public void setActive(Boolean active) {
        isActive = active;
    }
    
    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
    
    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
    
    public void setAssignedBy(Long assignedBy) {
        this.assignedBy = assignedBy;
    }
    
    public void setAssignedAtAudit(LocalDateTime assignedAtAudit) {
        this.assignedAtAudit = assignedAtAudit;
    }
    
    // Getter方法
    public Long getId() {
        return id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public Boolean getActive() {
        return isActive;
    }
    
    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
    
    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
    
    public Long getAssignedBy() {
        return assignedBy;
    }
    
    public LocalDateTime getAssignedAtAudit() {
        return assignedAtAudit;
    }
}
