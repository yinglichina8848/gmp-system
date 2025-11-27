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
 * 培训场次实体类，对应t_training_session表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_training_session")
@EntityListeners(AuditingEntityListener.class)
public class TrainingSession implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场次ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", nullable = false)
    private Long id;

    /**
     * 关联培训课程
     */
    @ManyToOne
    @JoinColumn(name = "training_course_id", nullable = false)
    private TrainingCourse trainingCourse;

    /**
     * 培训标题
     */
    @NotBlank(message = "培训标题不能为空")
    @Size(max = 200, message = "培训标题长度不能超过200个字符")
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    /**
     * 培训讲师
     */
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private User trainer;

    /**
     * 培训开始时间
     */
    @NotNull(message = "培训开始时间不能为空")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 培训结束时间
     */
    @NotNull(message = "培训结束时间不能为空")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * 培训地点
     */
    @Size(max = 200, message = "培训地点长度不能超过200个字符")
    @Column(name = "location", length = 200)
    private String location;

    /**
     * 培训方式：线下/线上/混合
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "training_method", length = 50, nullable = false)
    private TrainingMethod trainingMethod;

    /**
     * 培训状态：未开始/进行中/已完成/已取消
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private Status status = Status.NOT_STARTED;

    /**
     * 预计人数
     */
    @Column(name = "expected_participants")
    private Integer expectedParticipants;

    /**
     * 实际参与人数
     */
    @Column(name = "actual_participants")
    private Integer actualParticipants;

    /**
     * 培训资料链接
     */
    @Size(max = 500, message = "培训资料链接长度不能超过500个字符")
    @Column(name = "training_materials", length = 500)
    private String trainingMaterials;

    /**
     * 培训总结
     */
    @Size(max = 2000, message = "培训总结长度不能超过2000个字符")
    @Column(name = "summary", length = 2000)
    private String summary;

    /**
     * 是否GMP相关培训
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
     * 培训方式枚举
     */
    public enum TrainingMethod {
        OFFLINE, // 线下
        ONLINE, // 线上
        MIXED // 混合
    }

    /**
     * 培训方式枚举（兼容旧代码）
     */
    public enum Method {
        OFFLINE, // 线下
        ONLINE, // 线上
        MIXED // 混合
    }

    /**
     * 培训状态枚举
     */
    public enum Status {
        NOT_STARTED, // 未开始
        IN_PROGRESS, // 进行中
        COMPLETED, // 已完成
        CANCELLED // 已取消
    }
}