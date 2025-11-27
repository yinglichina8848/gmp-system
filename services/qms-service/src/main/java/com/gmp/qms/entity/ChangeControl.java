package com.gmp.qms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 变更控制实体类
 * 
 * @author GMP系统开发团队
 */
@Entity
@Table(name = "change_controls")
@Data
public class ChangeControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "change_code", nullable = false, unique = true)
    private String changeCode;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "change_type", nullable = false)
    private String changeType;

    @Column(name = "affected_areas", columnDefinition = "TEXT")
    private String affectedAreas;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChangeStatus status;

    @Column(name = "proposed_date", nullable = false)
    private LocalDateTime proposedDate;

    @Column(name = "planned_implementation_date")
    private LocalDateTime plannedImplementationDate;

    @Column(name = "actual_implementation_date")
    private LocalDateTime actualImplementationDate;

    @Column(name = "risk_assessment", columnDefinition = "TEXT")
    private String riskAssessment;

    @Column(name = "implementation_plan", columnDefinition = "TEXT")
    private String implementationPlan;

    @Column(name = "verification_results", columnDefinition = "TEXT")
    private String verificationResults;

    @Column(name = "change_owner_id", nullable = false)
    private Long changeOwnerId;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @Column(name = "closed_by")
    private Long closedBy;

    @OneToMany(mappedBy = "changeControl", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangeControlAttachment> attachments;

    @OneToMany(mappedBy = "changeControl", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangeControlAction> actions;

    @OneToMany(mappedBy = "changeControl", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChangeControlHistory> history;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 风险级别枚举
     */
    public enum RiskLevel {
        CRITICAL,     // 关键
        HIGH,         // 高
        MEDIUM,       // 中
        LOW           // 低
    }

    /**
     * 变更状态枚举
     */
    public enum ChangeStatus {
        PROPOSED,     // 已提议
        IN_REVIEW,    // 审核中
        IN_APPROVAL,  // 批准中
        SCHEDULED,    // 已计划
        IN_IMPLEMENTATION, // 实施中
        IN_VERIFICATION, // 验证中
        COMPLETED,    // 已完成
        REJECTED,     // 已拒绝
        CANCELLED     // 已取消
    }
}
