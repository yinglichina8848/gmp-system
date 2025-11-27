package com.gmp.lims.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 样品DTO类
 * 用于样品信息的传输和展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SampleDTO {

    /**
     * 样品ID
     */
    private Long id;

    /**
     * 样品编号
     */
    @NotBlank(message = "样品编号不能为空")
    @Size(max = 50, message = "样品编号长度不能超过50个字符")
    private String sampleCode;

    /**
     * 样品名称
     */
    @NotBlank(message = "样品名称不能为空")
    @Size(max = 200, message = "样品名称长度不能超过200个字符")
    private String sampleName;

    /**
     * 样品类型
     */
    @NotBlank(message = "样品类型不能为空")
    @Size(max = 50, message = "样品类型长度不能超过50个字符")
    private String sampleType;

    /**
     * 样品来源
     */
    @Size(max = 200, message = "样品来源长度不能超过200个字符")
    private String sampleSource;

    /**
     * 批次号
     */
    @Size(max = 100, message = "批次号长度不能超过100个字符")
    private String batchNumber;

    /**
     * 规格
     */
    @Size(max = 100, message = "规格长度不能超过100个字符")
    private String specification;

    /**
     * 数量
     */
    private Integer sampleQuantity;

    /**
     * 单位
     */
    @Size(max = 20, message = "单位长度不能超过20个字符")
    private String unit;

    /**
     * 接收日期
     */
    private LocalDateTime receiptDate;

    /**
     * 样品状态
     */
    private String status;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 中药特有属性：药材名称
     */
    @Size(max = 100, message = "药材名称长度不能超过100个字符")
    private String chineseMedicineName;

    /**
     * 中药特有属性：道地产区
     */
    @Size(max = 100, message = "道地产区长度不能超过100个字符")
    private String authenticProductionArea;

    /**
     * 中药特有属性：采收时间
     */
    private LocalDateTime harvestTime;

    /**
     * 中药特有属性：加工方法
     */
    @Size(max = 200, message = "加工方法长度不能超过200个字符")
    private String processingMethod;
}