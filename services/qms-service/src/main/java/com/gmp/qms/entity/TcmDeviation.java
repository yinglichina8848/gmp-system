package com.gmp.qms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 中药特色偏差实体类
 * 用于管理中药材生产过程中特有的偏差类型，如道地性不符、炮制工艺偏差等
 *
 * @author GMP系统开发团队
 */
@Entity
@Table(name = "tcm_deviations")
@Data
public class TcmDeviation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 偏差编号
     */
    @Column(name = "deviation_code", nullable = false, unique = true)
    private String deviationCode;

    /**
     * 偏差标题
     */
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 偏差描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 发生日期
     */
    @Column(name = "occurrence_date", nullable = false)
    private LocalDateTime occurrenceDate;

    /**
     * 发现日期
     */
    @Column(name = "discovery_date", nullable = false)
    private LocalDateTime discoveryDate;

    /**
     * 中药偏差类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tcm_deviation_type", nullable = false)
    private TcmDeviationType tcmDeviationType;

    /**
     * 道地性信息（适用于道地性不符类型的偏差）
     */
    @Column(name = "authenticity_info", columnDefinition = "TEXT")
    private String authenticityInfo;

    /**
     * 炮制工艺信息（适用于炮制工艺偏差类型的偏差）
     */
    @Column(name = "processing_info", columnDefinition = "TEXT")
    private String processingInfo;

    /**
     * 检验相关信息（适用于检验偏差类型的偏差）
     */
    @Column(name = "inspection_info", columnDefinition = "TEXT")
    private String inspectionInfo;

    /**
     * 仓储相关信息（适用于仓储偏差类型的偏差）
     */
    @Column(name = "storage_info", columnDefinition = "TEXT")
    private String storageInfo;

    /**
     * 严重程度级别
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "severity_level", nullable = false)
    private SeverityLevel severityLevel;

    /**
     * 偏差状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeviationStatus status;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 影响评估
     */
    @Column(name = "impact_assessment", columnDefinition = "TEXT")
    private String impactAssessment;

    /**
     * 根本原因分析
     */
    @Column(name = "root_cause_analysis", columnDefinition = "TEXT")
    private String rootCauseAnalysis;

    /**
     * 纠正措施
     */
    @Column(name = "corrective_action", columnDefinition = "TEXT")
    private String correctiveAction;

    /**
     * 预防措施
     */
    @Column(name = "preventive_action", columnDefinition = "TEXT")
    private String preventiveAction;

    /**
     * 负责人ID
     */
    @Column(name = "responsible_person_id", nullable = false)
    private Long responsiblePersonId;

    /**
     * 审核人ID
     */
    @Column(name = "reviewer_id")
    private Long reviewerId;

    /**
     * 批准人ID
     */
    @Column(name = "approver_id")
    private Long approverId;

    /**
     * 关闭日期
     */
    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    /**
     * 关闭人
     */
    @Column(name = "closed_by")
    private Long closedBy;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    /**
     * 更新人
     */
    @Column(name = "updated_by")
    private Long updatedBy;

    /**
     * 中药偏差类型枚举
     */
    public enum TcmDeviationType {
        /**
         * 道地性不符
         */
        AUTHENTICITY_NON_COMPLIANCE,

        /**
         * 炮制工艺偏差
         */
        PROCESSING_DEVIATION,

        /**
         * 检验偏差
         */
        INSPECTION_DEVIATION,

        /**
         * 仓储偏差
         */
        STORAGE_DEVIATION,

        /**
         * 其他中药相关偏差
         */
        OTHER_TCM_RELATED
    }

    /**
     * 偏差严重程度级别枚举
     */
    public enum SeverityLevel {
        /**
         * 轻微
         */
        MINOR,

        /**
         * 中等
         */
        MODERATE,

        /**
         * 严重
         */
        MAJOR,

        /**
         * 关键
         */
        CRITICAL
    }

    /**
     * 偏差状态枚举
     */
    public enum DeviationStatus {
        /**
         * 草稿
         */
        DRAFT,

        /**
         * 待审核
         */
        PENDING_REVIEW,

        /**
         * 待批准
         */
        PENDING_APPROVAL,

        /**
         * 已批准
         */
        APPROVED,

        /**
         * 已关闭
         */
        CLOSED,

        /**
         * 已取消
         */
        CANCELLED
    }
}