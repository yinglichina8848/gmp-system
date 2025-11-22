package com.gmp.auth.entity;

// ============================================================================
//                    GMP系统用户组织角色关联实体定义
// dịp
//
// WHY: 传统的RBAC模型不支持用户在多个组织内拥有不同角色的复杂权限场景，
//      在GMP企业中，一个质量工程师可能同时在质量组织、生产组织、培训组织中
//      担任不同角色，这种多组织多角色的权限需求需要专门的实体进行管理。
//
// WHAT: UserOrganizationRole实体实现用户、组织、角色三者间的关联管理，
//      支持临时权限、权限过期、分配审核等高级权限管理特性，确保权限
//      分配的追溯性和合规性，满足GMP审计要求。
//
// HOW: 使用唯一约束保证用户在同一组织内只能有一个角色，通过枚举约束赋值状态，
//      使用审计字段记录完整的权限变更历史，支持权限的动态激活和自动失效。
// ============================================================================

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

/**
 * GMP系统用户组织角色关联实体
 *
 * 实现用户、组织、角色三者间的关联管理，支持：
 * - 用户可同时拥有多个组织内的不同角色
 * - 临时权限分配和自动过期
 * - 权限分配的审批流程
 * - 完整的权限变更历史记录
 * - GMP合规的权限追溯要求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_user_org_roles",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"user_id", "organization_id", "role_id"},
           name = "uk_user_org_role"
       ),
       indexes = {
           @Index(name = "idx_uor_user", columnList = "user_id"),
           @Index(name = "idx_uor_org", columnList = "organization_id"),
           @Index(name = "idx_uor_role", columnList = "role_id"),
           @Index(name = "idx_uor_status", columnList = "assignment_status"),
           @Index(name = "idx_uor_effective", columnList = "effective_from,effective_until")
       })
@EntityListeners(AuditingEntityListener.class)
public class UserOrganizationRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 核心关联关系
    @NotNull(message = "用户ID不能为空")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull(message = "组织ID不能为空")
    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @NotNull(message = "角色ID不能为空")
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    // 权限分配状态管理
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_status", nullable = false)
    private AssignmentStatus status = AssignmentStatus.ACTIVE;

    @Column(name = "assigned_by")
    private Long assignedBy;  // 分配人ID

    @Builder.Default
    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    // 生效时间控制 - 支持临时权限
    @Builder.Default
    @Column(name = "effective_from", nullable = false)
    private LocalDateTime effectiveFrom = LocalDateTime.now();

    @Column(name = "effective_until")
    private LocalDateTime effectiveUntil;  // 到期时间，为null表示永久有效

    @Column(name = "expires_notified")
    private Boolean expiresNotified = false;  // 到期提醒标志

    // 权限分配理由和备注
    @Size(max = 500, message = "分配理由长度不能超过500字符")
    @Column(name = "assignment_reason", columnDefinition = "TEXT")
    private String assignmentReason;

    @Size(max = 1000, message = "备注信息长度不能超过1000字符")
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // GMP合规相关扩展字段
    @Column(name = "gmp_required")
    private Boolean gmpRequired = false;  // 是否GMP必备权限

    @Column(name = "qualification_required")
    private String qualificationRequired;  // 需要的资质证明

    @Column(name = "training_required")
    private String trainingRequired;      // 需要的培训记录

    @Column(name = "approval_required")
    private Boolean approvalRequired = false;  // 是否需要上级审批

    @Column(name = "approved_by")
    private Long approvedBy;  // 审批人ID

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;      // 审批时间

    @Size(max = 500, message = "审批意见长度不能超过500字符")
    @Column(name = "approval_note")
    private String approvalNote;

    // 审计字段 - GMP合规要求完整审计记录
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
     * 权限分配状态枚举
     * 支持完整的权限生命周期管理
     */
    public enum AssignmentStatus {
        PENDING("待审批"),      // 权限分配申请已提交，等待审批
        APPROVED("已批准"),      // 权限分配已通过审批
        ACTIVE("生效中"),       // 权限已生效，可以正常使用
        SUSPENDED("已暂停"),    // 权限暂时被暂停（非永久）
        EXPIRED("已过期"),      // 权限超过有效期
        REVOKED("已撤销"),      // 权限被主动撤销
        REJECTED("已拒绝");     // 权限分配申请被拒绝

        private final String description;

        AssignmentStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        /**
         * 判断是否为活动状态（可以正常使用的状态）
         */
        public boolean isActiveStatus() {
            return this == ACTIVE || this == APPROVED;
        }
    }

    /**
     * 判断权限分配是否有效（可使用状态）
     */
    @Transient
    public boolean isEffective() {
        // 1. 检查状态是否为活动状态
        if (!status.isActiveStatus()) {
            return false;
        }

        // 2. 检查生效时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(effectiveFrom)) {
            return false;  // 还未到生效时间
        }

        // 3. 检查到期时间
        if (effectiveUntil != null && now.isAfter(effectiveUntil)) {
            return false;  // 已过期
        }

        return true;
    }

    /**
     * 判断权限是否即将过期（需要在指定天数前预警）
     */
    @Transient
    public boolean isExpiringSoon(int warningDays) {
        if (effectiveUntil == null) {
            return false;  // 永久有效，无需警告
        }

        LocalDateTime warningTime = LocalDateTime.now().plusDays(warningDays);
        return LocalDateTime.now().isBefore(effectiveUntil) &&
               effectiveUntil.isBefore(warningTime);
    }

    /**
     * 判断权限是否已经过期
     */
    @Transient
    public boolean isExpired() {
        return effectiveUntil != null && LocalDateTime.now().isAfter(effectiveUntil);
    }

    /**
     * 判断是否需要审批流程
     */
    @Transient
    public boolean requiresApproval() {
        return approvalRequired && (assignedBy == null || assignedBy.equals(createdBy));
    }

    /**
     * 计算权限剩余有效天数
     */
    @Transient
    public Integer getRemainingDays() {
        if (effectiveUntil == null) {
            return null;  // 永久有效
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(effectiveUntil)) {
            return 0;  // 已过期
        }

        return (int) java.time.Duration.between(now, effectiveUntil).toDays();
    }
}
