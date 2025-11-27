package com.gmp.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检测任务实体类
 * 表示针对样品的检测工作任务
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "sample", "inspectionResults" })
@Entity
@Table(name = "lims_inspection_task", indexes = {
        @Index(name = "idx_task_code", columnList = "task_code", unique = true),
        @Index(name = "idx_task_status", columnList = "status"),
        @Index(name = "idx_task_sample_id", columnList = "sample_id"),
        @Index(name = "idx_task_create_date", columnList = "create_date")
})
public class InspectionTask {

    /**
     * 任务ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 任务编号
     */
    @NotBlank(message = "任务编号不能为空")
    @Size(max = 50, message = "任务编号长度不能超过50个字符")
    @Column(name = "task_code", nullable = false, unique = true, length = 50)
    private String taskCode;

    /**
     * 关联的样品
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id", nullable = false)
    private Sample sample;

    /**
     * 检测目的
     */
    @Size(max = 200, message = "检测目的长度不能超过200个字符")
    @Column(name = "inspection_purpose", length = 200)
    private String inspectionPurpose;

    /**
     * 检测类型
     */
    @NotBlank(message = "检测类型不能为空")
    @Size(max = 50, message = "检测类型长度不能超过50个字符")
    @Column(name = "inspection_type", nullable = false, length = 50)
    private String inspectionType;

    /**
     * 检测项目
     */
    @Size(max = 500, message = "检测项目长度不能超过500个字符")
    @Column(name = "inspection_items", length = 500)
    private String inspectionItems;

    /**
     * 检测依据
     */
    @Size(max = 500, message = "检测依据长度不能超过500个字符")
    @Column(name = "inspection_basis", length = 500)
    private String inspectionBasis;

    /**
     * 任务状态
     */
    @NotBlank(message = "任务状态不能为空")
    @Column(nullable = false)
    private String status;

    /**
     * 负责人
     */
    @Size(max = 50, message = "负责人长度不能超过50个字符")
    @Column(name = "responsible_person", length = 50)
    private String responsiblePerson;

    /**
     * 计划开始日期
     */
    @Column(name = "plan_start_date")
    private LocalDateTime planStartDate;

    /**
     * 计划完成日期
     */
    @Column(name = "plan_complete_date")
    private LocalDateTime planCompleteDate;

    /**
     * 实际开始日期
     */
    @Column(name = "actual_start_date")
    private LocalDateTime actualStartDate;

    /**
     * 实际完成日期
     */
    @Column(name = "actual_complete_date")
    private LocalDateTime actualCompleteDate;

    /**
     * 创建日期
     */
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    /**
     * 创建人
     */
    @Size(max = 50, message = "创建人长度不能超过50个字符")
    @Column(name = "created_by", length = 50)
    private String createdBy;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    /**
     * 更新人
     */
    @Size(max = 50, message = "更新人长度不能超过50个字符")
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Column(length = 500)
    private String remark;

    /**
     * 关联的检测结果
     */
    @OneToMany(mappedBy = "inspectionTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionResult> inspectionResults;

    /**
     * 中药特有属性：是否包含指纹图谱检测
     */
    @Column(name = "include_fingerprint", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean includeFingerprint = false;

    /**
     * 中药特有属性：是否包含传统鉴别
     */
    @Column(name = "include_traditional_identify", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean includeTraditionalIdentify = false;

    /**
     * 中药特有属性：是否需要道地性评估
     */
    @Column(name = "need_authenticity_assessment", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean needAuthenticityAssessment = false;
}