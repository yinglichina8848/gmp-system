package com.gmp.training.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 培训需求实体类，对应t_training_need表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_training_need")
@EntityListeners(AuditingEntityListener.class)
public class TrainingNeed implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 需求ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "need_id", nullable = false)
    private Long id;

    /**
     * 需求名称
     */
    @NotBlank(message = "需求名称不能为空")
    @Size(max = 200, message = "需求名称长度不能超过200个字符")
    @Column(name = "need_name", length = 200, nullable = false)
    private String needName;

    /**
     * 需求描述
     */
    @Size(max = 1000, message = "需求描述长度不能超过1000个字符")
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 需求来源：部门/个人/岗位/合规要求
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 50, nullable = false)
    private NeedSource source;

    /**
     * 关联部门
     */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * 关联岗位
     */
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    /**
     * 优先级：高/中/低
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20, nullable = false)
    private PriorityLevel priority = PriorityLevel.MEDIUM;

    /**
     * 预计培训人数
     */
    @Column(name = "estimated_participants")
    private Integer estimatedParticipants;

    /**
     * 期望完成时间
     */
    @Column(name = "expected_completion_time")
    private LocalDateTime expectedCompletionTime;

    /**
     * 需求提出人
     */
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    /**
     * 审核人
     */
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    /**
     * 需求状态：待审核/已审核/已拒绝/已纳入计划
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private NeedStatus status = NeedStatus.PENDING_REVIEW;

    /**
     * 审核意见
     */
    @Size(max = 500, message = "审核意见长度不能超过500个字符")
    @Column(name = "review_comment", length = 500)
    private String reviewComment;

    /**
     * 是否GMP相关培训需求
     */
    @Column(name = "is_gmp_related")
    private boolean gmpRelated = false;

    /**
     * 创建者
     */
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 最后修改者
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    /**
     * 需求来源枚举
     */
    public enum NeedSource {
        DEPARTMENT, // 部门
        INDIVIDUAL, // 个人
        POSITION, // 岗位
        COMPLIANCE // 合规要求
    }

    /**
     * 需求状态枚举 - 兼容服务层使用的Status枚举
     */
    public enum Status {
        PENDING, // 待处理 (对应PENDING_REVIEW)
        APPROVED, // 已批准
        REJECTED, // 已拒绝
        IMPLEMENTED // 已实施
    }

    // 兼容现有代码的别名
    public enum NeedStatus {
        PENDING_REVIEW, // 待审核
        APPROVED, // 已审核
        REJECTED, // 已拒绝
        IN_PLAN // 已纳入计划
    }

    /**
     * 优先级枚举 - 兼容服务层使用的Priority枚举
     */
    public enum Priority {
        LOW, // 低
        MEDIUM, // 中
        HIGH // 高
    }

    // 兼容现有代码的别名
    public enum PriorityLevel {
        HIGH, // 高
        MEDIUM, // 中
        LOW // 低
    }
}
