package com.gmp.qms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 纠正和预防措施(CAPA)实体类
 * 
 * @author GMP系统开发团队
 */
@Entity
@Table(name = "capa")
@Data
public class Capa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "capa_code", nullable = false, unique = true)
    private String capaCode;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "source_type", nullable = false)
    private String sourceType;

    @Column(name = "source_id")
    private Long sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level", nullable = false)
    private PriorityLevel priorityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CapaStatus status;

    @Column(name = "target_completion_date", nullable = false)
    private LocalDateTime targetCompletionDate;

    @Column(name = "actual_completion_date")
    private LocalDateTime actualCompletionDate;

    @Column(name = "root_cause_analysis", columnDefinition = "TEXT")
    private String rootCauseAnalysis;

    @Column(name = "implementation_summary", columnDefinition = "TEXT")
    private String implementationSummary;

    @Column(name = "effectiveness_assessment", columnDefinition = "TEXT")
    private String effectivenessAssessment;

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

    @OneToMany(mappedBy = "capa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CapaAttachment> attachments;

    @OneToMany(mappedBy = "capa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CapaAction> actions;

    @OneToMany(mappedBy = "capa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CapaHistory> history;

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
     * 优先级级别枚举
     */
    public enum PriorityLevel {
        URGENT,       // 紧急
        HIGH,         // 高
        MEDIUM,       // 中
        LOW           // 低
    }

    /**
     * CAPA状态枚举
     */
    public enum CapaStatus {
        IDENTIFIED,   // 已识别
        IN_ANALYSIS,  // 分析中
        IN_IMPLEMENTATION, // 实施中
        IN_EFFECTIVENESS_CHECK, // 有效性检查中
        PENDING_REVIEW,   // 待审核
        PENDING_APPROVAL, // 待批准
        CLOSED,       // 已关闭
        REJECTED      // 已拒绝
    }
}
