package com.gmp.qms.dto;

import com.gmp.qms.entity.TcmDeviation;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 中药特色偏差DTO类
 * 用于传输中药特色偏差相关数据
 *
 * @author GMP系统开发团队
 */
@Data
public class TcmDeviationDTO {

    /**
     * 偏差ID
     */
    private Long id;

    /**
     * 偏差编号
     */
    private String deviationCode;

    /**
     * 偏差标题
     */
    private String title;

    /**
     * 偏差描述
     */
    private String description;

    /**
     * 发生日期
     */
    private LocalDateTime occurrenceDate;

    /**
     * 发现日期
     */
    private LocalDateTime discoveryDate;

    /**
     * 中药偏差类型
     */
    private TcmDeviation.TcmDeviationType tcmDeviationType;

    /**
     * 道地性信息（适用于道地性不符类型的偏差）
     */
    private String authenticityInfo;

    /**
     * 炮制工艺信息（适用于炮制工艺偏差类型的偏差）
     */
    private String processingInfo;

    /**
     * 检验相关信息（适用于检验偏差类型的偏差）
     */
    private String inspectionInfo;

    /**
     * 仓储相关信息（适用于仓储偏差类型的偏差）
     */
    private String storageInfo;

    /**
     * 严重程度级别
     */
    private TcmDeviation.SeverityLevel severityLevel;

    /**
     * 偏差状态
     */
    private TcmDeviation.DeviationStatus status;

    /**
     * 影响评估
     */
    private String impactAssessment;

    /**
     * 根本原因分析
     */
    private String rootCauseAnalysis;

    /**
     * 纠正措施
     */
    private String correctiveAction;

    /**
     * 预防措施
     */
    private String preventiveAction;

    /**
     * 负责人ID
     */
    private Long responsiblePersonId;

    /**
     * 审核人ID
     */
    private Long reviewerId;

    /**
     * 批准人ID
     */
    private Long approverId;

    /**
     * 关闭日期
     */
    private LocalDateTime closedDate;

    /**
     * 关闭人
     */
    private Long closedBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 更新人
     */
    private Long updatedBy;
}