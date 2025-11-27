package com.gmp.qms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 偏差实体类
 * 
 * @author GMP系统开发团队
 */
@Entity
@Table(name = "deviations")
@Data
public class Deviation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deviation_code", nullable = false, unique = true)
    private String deviationCode;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "occurrence_date", nullable = false)
    private LocalDateTime occurrenceDate;

    @Column(name = "discovery_date", nullable = false)
    private LocalDateTime discoveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity_level", nullable = false)
    private SeverityLevel severityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeviationStatus status;

    @Column(name = "impact_assessment", columnDefinition = "TEXT")
    private String impactAssessment;

    @Column(name = "root_cause_analysis", columnDefinition = "TEXT")
    private String rootCauseAnalysis;

    @Column(name = "corrective_action", columnDefinition = "TEXT")
    private String correctiveAction;

    @Column(name = "preventive_action", columnDefinition = "TEXT")
    private String preventiveAction;

    @Column(name = "responsible_person_id", nullable = false)
    private Long responsiblePersonId;

    @Column(name = "reviewer_id")
    private Long reviewerId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    @Column(name = "closed_by")
    private Long closedBy;

    @OneToMany(mappedBy = "deviation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviationAttachment> attachments;

    @OneToMany(mappedBy = "deviation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviationAction> actions;

    @OneToMany(mappedBy = "deviation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviationHistory> history;

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
     * 偏差严重程度级别枚举
     */
    public enum SeverityLevel {
        CRITICAL,     // 关键
        HIGH,         // 高
        MEDIUM,       // 中
        LOW           // 低
    }

    /**
     * 偏差状态枚举
     */
    public enum DeviationStatus {
        IDENTIFIED,   // 已识别
        IN_INVESTIGATION, // 调查中
        IN_CORRECTION,    // 纠正中
        PENDING_REVIEW,   // 待审核
        PENDING_APPROVAL, // 待批准
        CLOSED,       // 已关闭
        REJECTED      // 已拒绝
    }
}
