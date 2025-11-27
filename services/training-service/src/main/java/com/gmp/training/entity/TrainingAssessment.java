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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 培训考核实体类，对应t_training_assessment表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_training_assessment")
@EntityListeners(AuditingEntityListener.class)
public class TrainingAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考核ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id", nullable = false)
    private Long id;

    /**
     * 关联培训场次
     */
    @ManyToOne
    @JoinColumn(name = "training_session_id", nullable = false)
    private TrainingSession trainingSession;

    /**
     * 关联培训课程
     */
    @ManyToOne
    @JoinColumn(name = "training_course_id", nullable = false)
    private TrainingCourse trainingCourse;

    /**
     * 关联用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 考核类型：考试/测验/项目/演示
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type", length = 50, nullable = false)
    private Type type;

    /**
     * 考核分数
     */
    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    /**
     * 考核状态：未开始/进行中/已完成/未通过/已通过
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private Status status = Status.NOT_STARTED;

    /**
     * 考核时间
     */
    @Column(name = "assessment_time")
    private LocalDateTime assessmentTime;

    /**
     * 考核结果说明
     */
    @Size(max = 1000, message = "考核结果说明长度不能超过1000个字符")
    @Column(name = "result_description", length = 1000)
    private String resultDescription;

    /**
     * 及格分数
     */
    @Column(name = "passing_score", precision = 5, scale = 2)
    private BigDecimal passingScore;

    /**
     * 完成日期
     */
    @Column(name = "completion_date")
    private LocalDate completionDate;

    /**
     * 备注
     */
    @Size(max = 2000, message = "备注长度不能超过2000个字符")
    @Column(name = "notes", length = 2000)
    private String notes;

    /**
     * 获取考核分数
     * 
     * @return 考核分数
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * 设置完成日期
     * 
     * @param completionDate 完成日期
     */
    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    /**
     * 获取备注
     * 
     * @return 备注
     */
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     * 
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * 获取考核ID
     * 
     * @return 考核ID
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置考核状态
     * 
     * @param status 考核状态
     */
    public void setStatus(Status status) {
        this.status = status;
    }

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
     * 考核类型枚举
     */
    public enum Type {
        EXAM, // 考试
        QUIZ, // 测验
        PROJECT, // 项目
        DEMO // 演示
    }

    /**
     * 考核状态枚举
     */
    public enum Status {
        NOT_STARTED, // 未开始
        IN_PROGRESS, // 进行中
        COMPLETED, // 已完成
        FAILED, // 未通过
        PASSED // 已通过
    }
}