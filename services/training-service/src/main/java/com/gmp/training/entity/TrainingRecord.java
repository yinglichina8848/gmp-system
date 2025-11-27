package com.gmp.training.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 培训记录实体类，对应t_training_record表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_training_record")
@EntityListeners(AuditingEntityListener.class)
public class TrainingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id", nullable = false)
    private Long id;

    /**
     * 培训场次
     */
    @NotNull(message = "培训场次不能为空")
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private TrainingSession session;

    /**
     * 参训人员
     */
    @NotNull(message = "参训人员不能为空")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 签到时间
     */
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    /**
     * 签退时间
     */
    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    /**
     * 考勤状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "attendance_status", length = 20, nullable = false)
    private AttendanceStatus attendanceStatus = AttendanceStatus.NOT_CHECKED;

    /**
     * 考核成绩
     */
    @Column(name = "score", precision = 5, scale = 2)
    private BigDecimal score;

    /**
     * 考核结果
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_result", length = 20)
    private AssessmentResult assessmentResult;

    /**
     * 考核时间
     */
    @Column(name = "assessment_time")
    private LocalDateTime assessmentTime;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

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
     * 考勤状态枚举
     */
    public enum AttendanceStatus {
        NOT_CHECKED,  // 未签到
        PRESENT,      // 出席
        LATE,         // 迟到
        ABSENT,       // 缺席
        LEAVE         // 请假
    }

    /**
     * 考核结果枚举
     */
    public enum AssessmentResult {
        PASSED,       // 合格
        FAILED,       // 不合格
        NOT_ASSESSED  // 未考核
    }
    
    /**
     * 记录状态枚举
     */
    public enum Status {
        ACTIVE,       // 有效
        INACTIVE,     // 无效
        DELETED       // 已删除
    }
}