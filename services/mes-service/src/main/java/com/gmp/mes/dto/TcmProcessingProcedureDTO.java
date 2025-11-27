package com.gmp.mes.dto;

import com.gmp.mes.entity.TcmProcessingProcedure;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 中药炮制工艺DTO
 * 用于传输中药炮制工艺相关数据
 *
 * @author GMP系统开发团队
 */
@Data
public class TcmProcessingProcedureDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 工艺编号
     */
    private String procedureNumber;

    /**
     * 关联的生产批次ID
     */
    private Long batchId;

    /**
     * 中药材名称
     */
    private String herbName;

    /**
     * 炮制方法
     */
    private TcmProcessingProcedure.ProcessingMethod processingMethod;

    /**
     * 辅料信息
     */
    private String auxiliaryMaterials;

    /**
     * 辅料用量
     */
    private BigDecimal auxiliaryAmount;

    /**
     * 辅料单位
     */
    private String auxiliaryUnit;

    /**
     * 炮制温度(℃)
     */
    private Integer processingTemperature;

    /**
     * 炮制时间(分钟)
     */
    private Integer processingTime;

    /**
     * 火力控制
     */
    private String fireControl;

    /**
     * 搅拌速度
     */
    private String stirringSpeed;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 操作员ID
     */
    private Long operatorId;

    /**
     * 工艺状态
     */
    private TcmProcessingProcedure.ProcedureStatus status;

    /**
     * 质量评估结果
     */
    private String qualityEvaluation;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}