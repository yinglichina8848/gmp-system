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
 * 样品实体类
 * 表示实验室接收的待检测样品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "inspectionTasks" })
@Entity
@Table(name = "lims_sample", indexes = {
        @Index(name = "idx_sample_code", columnList = "sample_code", unique = true),
        @Index(name = "idx_sample_status", columnList = "status"),
        @Index(name = "idx_sample_receipt_date", columnList = "receipt_date")
})
public class Sample {

    /**
     * 样品ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 样品编号
     */
    @NotBlank(message = "样品编号不能为空")
    @Size(max = 50, message = "样品编号长度不能超过50个字符")
    @Column(name = "sample_code", nullable = false, unique = true, length = 50)
    private String sampleCode;

    /**
     * 样品名称
     */
    @NotBlank(message = "样品名称不能为空")
    @Size(max = 200, message = "样品名称长度不能超过200个字符")
    @Column(name = "sample_name", nullable = false, length = 200)
    private String sampleName;

    /**
     * 样品类型
     */
    @NotBlank(message = "样品类型不能为空")
    @Size(max = 50, message = "样品类型长度不能超过50个字符")
    @Column(name = "sample_type", nullable = false, length = 50)
    private String sampleType;

    /**
     * 样品来源
     */
    @Size(max = 200, message = "样品来源长度不能超过200个字符")
    @Column(name = "sample_source", length = 200)
    private String sampleSource;

    /**
     * 批次号
     */
    @Size(max = 100, message = "批次号长度不能超过100个字符")
    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    /**
     * 规格
     */
    @Size(max = 100, message = "规格长度不能超过100个字符")
    @Column(length = 100)
    private String specification;

    /**
     * 数量
     */
    @Column(name = "sample_quantity")
    private Integer sampleQuantity;

    /**
     * 单位
     */
    @Size(max = 20, message = "单位长度不能超过20个字符")
    @Column(length = 20)
    private String unit;

    /**
     * 接收日期
     */
    @Column(name = "receipt_date")
    private LocalDateTime receiptDate;

    /**
     * 样品状态
     */
    @Column(nullable = false)
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Column(length = 500)
    private String remark;

    /**
     * 创建人
     */
    @Size(max = 50, message = "创建人长度不能超过50个字符")
    @Column(name = "created_by", length = 50)
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @Size(max = 50, message = "更新人长度不能超过50个字符")
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    /**
     * 关联的检测任务
     */
    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionTask> inspectionTasks;

    /**
     * 中药特有属性：药材名称
     */
    @Size(max = 100, message = "药材名称长度不能超过100个字符")
    @Column(name = "chinese_medicine_name", length = 100)
    private String chineseMedicineName;

    /**
     * 中药特有属性：道地产区
     */
    @Size(max = 100, message = "道地产区长度不能超过100个字符")
    @Column(name = "authentic_production_area", length = 100)
    private String authenticProductionArea;

    /**
     * 中药特有属性：采收时间
     */
    @Column(name = "harvest_time")
    private LocalDateTime harvestTime;

    /**
     * 中药特有属性：加工方法
     */
    @Size(max = 200, message = "加工方法长度不能超过200个字符")
    @Column(name = "processing_method", length = 200)
    private String processingMethod;
}