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

/**
 * 检测结果实体类
 * 表示检测任务的具体检测数据和结论
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "inspectionTask" })
@Entity
@Table(name = "lims_inspection_result", indexes = {
        @Index(name = "idx_result_task_id", columnList = "task_id"),
        @Index(name = "idx_result_item_name", columnList = "item_name"),
        @Index(name = "idx_result_test_date", columnList = "test_date")
})
public class InspectionResult {

    /**
     * 结果ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的检测任务
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private InspectionTask inspectionTask;

    /**
     * 检测项目名称
     */
    @NotBlank(message = "检测项目名称不能为空")
    @Size(max = 100, message = "检测项目名称长度不能超过100个字符")
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    /**
     * 检测方法
     */
    @Size(max = 200, message = "检测方法长度不能超过200个字符")
    @Column(name = "test_method", length = 200)
    private String testMethod;

    /**
     * 检测结果值
     */
    @Size(max = 200, message = "检测结果值长度不能超过200个字符")
    @Column(name = "test_value", length = 200)
    private String testValue;

    /**
     * 计量单位
     */
    @Size(max = 20, message = "计量单位长度不能超过20个字符")
    @Column(name = "measurement_unit", length = 20)
    private String measurementUnit;

    /**
     * 标准值
     */
    @Size(max = 200, message = "标准值长度不能超过200个字符")
    @Column(name = "standard_value", length = 200)
    private String standardValue;

    /**
     * 标准范围
     */
    @Size(max = 200, message = "标准范围长度不能超过200个字符")
    @Column(name = "standard_range", length = 200)
    private String standardRange;

    /**
     * 判定结果
     */
    @Size(max = 20, message = "判定结果长度不能超过20个字符")
    @Column(name = "judgment_result", length = 20)
    private String judgmentResult;

    /**
     * 检测日期
     */
    @Column(name = "test_date")
    private LocalDateTime testDate;

    /**
     * 检测人员
     */
    @Size(max = 50, message = "检测人员长度不能超过50个字符")
    @Column(name = "test_person", length = 50)
    private String testPerson;

    /**
     * 审核人员
     */
    @Size(max = 50, message = "审核人员长度不能超过50个字符")
    @Column(name = "review_person", length = 50)
    private String reviewPerson;

    /**
     * 审核日期
     */
    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    /**
     * 审核状态
     */
    @Column(name = "review_status")
    private String reviewStatus;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Column(length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    /**
     * 中药特有属性：检测结果描述
     */
    @Size(max = 1000, message = "检测结果描述长度不能超过1000个字符")
    @Column(name = "result_description", length = 1000)
    private String resultDescription;

    /**
     * 中药特有属性：是否为关键指标
     */
    @Column(name = "is_key_indicator", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isKeyIndicator = false;
}