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
import java.util.Date;

/**
 * 培训计划实体类，对应t_training_plan表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_training_plan")
@EntityListeners(AuditingEntityListener.class)
public class TrainingPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 计划ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false)
    private Long id;

    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空")
    @Size(max = 200, message = "计划名称长度不能超过200个字符")
    @Column(name = "plan_name", length = 200, nullable = false)
    private String planName;

    /**
     * 计划描述
     */
    @Size(max = 1000, message = "计划描述长度不能超过1000个字符")
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 计划年度
     */
    @NotNull(message = "计划年度不能为空")
    @Column(name = "plan_year", nullable = false)
    private Integer planYear;

    /**
     * 计划开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 计划结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 计划状态：草稿/审核中/已发布/已完成/已取消
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private Status status = Status.DRAFT;

    /**
     * 总预算
     */
    @Column(name = "total_budget", precision = 10, scale = 2)
    private Double totalBudget;

    /**
     * 负责部门
     */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * 负责人
     */
    @ManyToOne
    @JoinColumn(name = "responsible_person_id")
    private User responsiblePerson;

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
     * 计划状态枚举
     */
    public enum Status {
        DRAFT,          // 草稿
        UNDER_REVIEW,   // 审核中
        PUBLISHED,      // 已发布
        COMPLETED,      // 已完成
        CANCELLED       // 已取消
    }
}