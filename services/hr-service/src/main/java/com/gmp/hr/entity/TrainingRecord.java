package com.gmp.hr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 培训记录实体类，用于存储员工的培训信息
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hr_training_record")
@EntityListeners(AuditingEntityListener.class)
public class TrainingRecord {
    
    /**
     * 培训记录ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 培训课程名称
     */
    @Column(name = "course_name", nullable = false, length = 200)
    @NotBlank(message = "培训课程名称不能为空")
    private String courseName;
    
    /**
     * 培训内容
     */
    @Column(name = "training_content", length = 1000)
    private String trainingContent;
    
    /**
     * 培训类型
     */
    @Column(name = "training_type", length = 50)
    private String trainingType;
    
    /**
     * 培训开始日期
     */
    @Column(name = "start_date", nullable = false)
    @NotNull(message = "培训开始日期不能为空")
    private LocalDate startDate;
    
    /**
     * 培训结束日期
     */
    @Column(name = "end_date", nullable = false)
    @NotNull(message = "培训结束日期不能为空")
    private LocalDate endDate;
    
    /**
     * 培训时长（小时）
     */
    @Column(name = "duration_hours")
    private Integer durationHours;
    
    /**
     * 培训机构
     */
    @Column(name = "training_provider", length = 200)
    private String trainingProvider;
    
    /**
     * 培训讲师
     */
    @Column(name = "trainer", length = 100)
    private String trainer;
    
    /**
     * 考核成绩
     */
    @Column(name = "score")
    private Integer score;
    
    /**
     * 培训结果
     */
    @Column(name = "result", length = 20)
    private String result; // PASSED, FAILED, ATTENDED
    
    /**
     * 备注
     */
    @Column(name = "remarks", length = 1000)
    private String remarks;
    
    /**
     * 关联员工
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Employee employee;
    
    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;
    
    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * 最后修改人
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    /**
     * 记录是否被删除
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}