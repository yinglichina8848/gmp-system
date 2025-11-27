package com.gmp.lims.dto;

import com.gmp.lims.entity.TcmInspection;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 中药特色检验DTO类
 * 用于传输中药特色检验相关数据
 */
@Data
public class TcmInspectionDTO {

    /**
     * 检验ID
     */
    private Long id;

    /**
     * 检验编号
     */
    @NotBlank(message = "检验编号不能为空")
    @Size(max = 50, message = "检验编号长度不能超过50个字符")
    private String inspectionCode;

    /**
     * 关联的检验任务ID
     */
    private Long taskId;

    /**
     * 中药材名称
     */
    @NotBlank(message = "中药材名称不能为空")
    @Size(max = 100, message = "中药材名称长度不能超过100个字符")
    private String herbName;

    /**
     * 检验方法类型
     */
    private TcmInspection.InspectionMethod inspectionMethod;

    /**
     * 性状鉴别结果
     */
    private String appearanceResult;

    /**
     * 显微鉴别结果
     */
    private String microscopicResult;

    /**
     * 薄层色谱结果
     */
    private String tlcResult;

    /**
     * 高效液相色谱结果
     */
    private String hplcResult;

    /**
     * 检验结果判定
     */
    private TcmInspection.ResultJudgment resultJudgment;

    /**
     * 检验员ID
     */
    private Long inspectorId;

    /**
     * 审核员ID
     */
    private Long reviewerId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建日期
     */
    private LocalDateTime createDate;

    /**
     * 更新日期
     */
    private LocalDateTime updateDate;
}