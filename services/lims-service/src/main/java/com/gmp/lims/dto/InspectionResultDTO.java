package com.gmp.lims.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 检测结果DTO类
 * 用于检测结果信息的传输和展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class InspectionResultDTO {

    /**
     * 结果ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 检测项目名称
     */
    @NotBlank(message = "检测项目名称不能为空")
    @Size(max = 100, message = "检测项目名称长度不能超过100个字符")
    private String itemName;

    /**
     * 检测方法
     */
    @Size(max = 200, message = "检测方法长度不能超过200个字符")
    private String testMethod;

    /**
     * 检测结果值
     */
    @Size(max = 200, message = "检测结果值长度不能超过200个字符")
    private String testValue;

    /**
     * 计量单位
     */
    @Size(max = 20, message = "计量单位长度不能超过20个字符")
    private String measurementUnit;

    /**
     * 标准值
     */
    @Size(max = 200, message = "标准值长度不能超过200个字符")
    private String standardValue;

    /**
     * 标准范围
     */
    @Size(max = 200, message = "标准范围长度不能超过200个字符")
    private String standardRange;

    /**
     * 判定结果
     */
    @Size(max = 20, message = "判定结果长度不能超过20个字符")
    private String judgmentResult;

    /**
     * 检测日期
     */
    private LocalDateTime testDate;

    /**
     * 检测人员
     */
    @Size(max = 50, message = "检测人员长度不能超过50个字符")
    private String testPerson;

    /**
     * 审核人员
     */
    @Size(max = 50, message = "审核人员长度不能超过50个字符")
    private String reviewPerson;

    /**
     * 审核日期
     */
    private LocalDateTime reviewDate;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 中药特有属性：检测结果描述
     */
    @Size(max = 1000, message = "检测结果描述长度不能超过1000个字符")
    private String resultDescription;

    /**
     * 中药特有属性：是否为关键指标
     */
    private Boolean isKeyIndicator = false;
}