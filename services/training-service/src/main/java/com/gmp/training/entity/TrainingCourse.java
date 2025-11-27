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
import java.util.Set;

/**
 * 培训课程实体类，对应t_training_course表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_training_course")
@EntityListeners(AuditingEntityListener.class)
public class TrainingCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id", nullable = false)
    private Long id;

    /**
     * 获取ID
     * 
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    @Size(max = 200, message = "课程名称长度不能超过200个字符")
    @Column(name = "course_name", length = 200, nullable = false)
    private String courseName;

    /**
     * 课程编码
     */
    @NotBlank(message = "课程编码不能为空")
    @Size(max = 50, message = "课程编码长度不能超过50个字符")
    @Column(name = "course_code", length = 50, nullable = false, unique = true)
    private String courseCode;

    /**
     * 课程描述
     */
    @Size(max = 1000, message = "课程描述长度不能超过1000个字符")
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 课程类型：GMP/安全/专业技能/管理技能/其他
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "course_type", length = 50, nullable = false)
    private CourseType courseType;

    /**
     * 课程分类
     */
    @Size(max = 100, message = "课程分类长度不能超过100个字符")
    @Column(name = "category", length = 100)
    private String category;

    /**
     * 课程时长（小时）
     */
    @Column(name = "duration_hours")
    private Integer durationHours;

    /**
     * 课程难度：初级/中级/高级
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", length = 20)
    private DifficultyLevel difficultyLevel = DifficultyLevel.INTERMEDIATE;

    /**
     * 培训难度：初级/中级/高级
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 20, nullable = false)
    private Difficulty difficulty = Difficulty.BEGINNER;

    /**
     * 讲师ID
     */
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private User lecturer;

    /**
     * 关联的培训计划
     */
    @ManyToMany
    @JoinTable(name = "t_plan_course_relation", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "plan_id"))
    private Set<TrainingPlan> relatedPlans;

    /**
     * 课程内容摘要
     */
    @Size(max = 500, message = "课程内容摘要长度不能超过500个字符")
    @Column(name = "content_summary", length = 500)
    private String contentSummary;

    /**
     * 培训目标
     */
    @Size(max = 500, message = "培训目标长度不能超过500个字符")
    @Column(name = "training_objective", length = 500)
    private String trainingObjective;

    /**
     * 考核方式：考试/实操/报告/综合
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_method", length = 50)
    private AssessmentMethod assessmentMethod;

    /**
     * 合格标准
     */
    @Size(max = 200, message = "合格标准长度不能超过200个字符")
    @Column(name = "passing_criteria", length = 200)
    private String passingCriteria;

    /**
     * 参考资料
     */
    @Size(max = 1000, message = "参考资料长度不能超过1000个字符")
    @Column(name = "reference_materials", length = 1000)
    private String referenceMaterials;

    /**
     * 课程状态：草稿/已发布/已归档/已废弃
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private CourseStatus status = CourseStatus.DRAFT;

    /**
     * 是否GMP相关课程
     */
    @Column(name = "is_gmp_related")
    private boolean gmpRelated = false;

    /**
     * 是否需要定期复训
     */
    @Column(name = "requires_refresher")
    private boolean requiresRefresher = false;

    /**
     * 是否必修课程
     */
    @Column(name = "is_compulsory")
    private boolean isCompulsory = false;

    /**
     * 是否循环课程
     */
    @Column(name = "is_recurring")
    private boolean isRecurring = false;

    /**
     * 创建日期
     */
    @Column(name = "creation_date")
    private java.time.LocalDate creationDate;

    /**
     * 复训周期（月）
     */
    @Column(name = "refresher_cycle_months")
    private Integer refresherCycleMonths;

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
     * 课程类型枚举
     */
    public enum CourseType {
        GMP, // GMP相关
        SAFETY, // 安全
        PROFESSIONAL_SKILL, // 专业技能
        MANAGEMENT_SKILL, // 管理技能
        OTHER // 其他
    }

    /**
     * 难度级别枚举
     */
    public enum DifficultyLevel {
        BEGINNER, // 初级
        INTERMEDIATE, // 中级
        ADVANCED // 高级
    }

    /**
     * 考核方式枚举
     */
    public enum AssessmentMethod {
        EXAM, // 考试
        PRACTICAL, // 实操
        REPORT, // 报告
        COMPREHENSIVE // 综合
    }

    /**
     * 课程状态枚举
     */
    public enum CourseStatus {
        DRAFT, // 草稿
        PUBLISHED, // 已发布
        ARCHIVED, // 已归档
        DISCARDED // 已废弃
    }

    /**
     * 培训类型枚举
     */
    public enum Type {
        INTERNAL, // 内部培训
        EXTERNAL, // 外部培训
        ONLINE, // 在线培训
        CLASSROOM // 课堂培训
    }

    /**
     * 通用状态枚举
     */
    public enum Status {
        DRAFT, // 草稿
        PUBLISHED, // 已发布
        ONGOING, // 进行中
        COMPLETED, // 已完成
        CANCELLED // 已取消
    }

    /**
     * 培训方式枚举
     */
    public enum Method {
        INTERNAL, // 内部培训
        EXTERNAL, // 外部培训
        ONLINE, // 在线培训
        CLASSROOM // 课堂培训
    }

    /**
     * 培训难度枚举
     */
    public enum Difficulty {
        BEGINNER, // 初级
        INTERMEDIATE, // 中级
        ADVANCED // 高级
    }

    /**
     * 设置课程状态
     * 
     * @param status 课程状态
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * 设置创建日期
     * 
     * @param creationDate 创建日期
     */
    public void setCreationDate(java.time.LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
