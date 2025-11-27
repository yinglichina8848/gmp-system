package com.gmp.lims.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 检测任务DTO类
 * 用于检测任务信息的传输和展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class InspectionTaskDTO {

    /**
     * 任务ID
     */
    private Long id;

    /**
     * 任务编号
     */
    @NotBlank(message = "任务编号不能为空")
    @Size(max = 50, message = "任务编号长度不能超过50个字符")
    private String taskCode;

    /**
     * 样品ID
     */
    private Long sampleId;

    /**
     * 样品编号
     */
    private String sampleCode;

    /**
     * 样品名称
     */
    private String sampleName;

    /**
     * 检测目的
     */
    @Size(max = 200, message = "检测目的长度不能超过200个字符")
    private String inspectionPurpose;

    /**
     * 检测类型
     */
    @NotBlank(message = "检测类型不能为空")
    @Size(max = 50, message = "检测类型长度不能超过50个字符")
    private String inspectionType;

    /**
     * 检测项目
     */
    @Size(max = 500, message = "检测项目长度不能超过500个字符")
    private String inspectionItems;

    /**
     * 检测依据
     */
    @Size(max = 500, message = "检测依据长度不能超过500个字符")
    private String inspectionBasis;

    /**
     * 任务状态
     */
    @NotBlank(message = "任务状态不能为空")
    private String status;

    /**
     * 负责人
     */
    @Size(max = 50, message = "负责人长度不能超过50个字符")
    private String responsiblePerson;

    /**
     * 计划开始日期
     */
    private LocalDateTime planStartDate;

    /**
     * 计划完成日期
     */
    private LocalDateTime planCompleteDate;

    /**
     * 实际开始日期
     */
    private LocalDateTime actualStartDate;

    /**
     * 实际完成日期
     */
    private LocalDateTime actualCompleteDate;

    /**
     * 创建日期
     */
    private LocalDateTime createDate;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

    /**
     * 中药特有属性：是否包含指纹图谱检测
     */
    private Boolean includeFingerprint = false;

    /**
     * 中药特有属性：是否包含传统鉴别
     */
    private Boolean includeTraditionalIdentify = false;

    /**
     * 中药特有属性：是否需要道地性评估
     */
    private Boolean needAuthenticityAssessment = false;
}