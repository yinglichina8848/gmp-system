package com.gmp.mes.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 中药炮制工艺实体
 * 用于记录中药材炮制过程中的工艺参数和操作信息
 *
 * @author GMP系统开发团队
 */
@Entity
@Table(name = "tcm_processing_procedures")
@Data
@EntityListeners(AuditingEntityListener.class)
public class TcmProcessingProcedure {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工艺编号
     */
    @Column(unique = true, nullable = false)
    private String procedureNumber;

    /**
     * 关联的生产批次ID
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 中药材名称
     */
    @Column(name = "herb_name", nullable = false)
    private String herbName;

    /**
     * 炮制方法
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_method", nullable = false)
    private ProcessingMethod processingMethod;

    /**
     * 辅料信息
     */
    @Column(name = "auxiliary_materials")
    private String auxiliaryMaterials;

    /**
     * 辅料用量
     */
    @Column(name = "auxiliary_amount")
    private BigDecimal auxiliaryAmount;

    /**
     * 辅料单位
     */
    @Column(name = "auxiliary_unit")
    private String auxiliaryUnit;

    /**
     * 炮制温度(℃)
     */
    @Column(name = "processing_temperature")
    private Integer processingTemperature;

    /**
     * 炮制时间(分钟)
     */
    @Column(name = "processing_time")
    private Integer processingTime;

    /**
     * 火力控制
     */
    @Column(name = "fire_control")
    private String fireControl;

    /**
     * 搅拌速度
     */
    @Column(name = "stirring_speed")
    private String stirringSpeed;

    /**
     * 设备ID
     */
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 操作员ID
     */
    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    /**
     * 工艺状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProcedureStatus status;

    /**
     * 质量评估结果
     */
    @Column(name = "quality_evaluation")
    private String qualityEvaluation;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 炮制方法枚举
     */
    public enum ProcessingMethod {
        /**
         * 清炒
         */
        DRY_FRYING,

        /**
         * 加辅料炒
         */
        FRYING_WITH_AUXILIARIES,

        /**
         * 炙制
         */
        ROASTING,

        /**
         * 煅制
         */
        CALCINATION,

        /**
         * 蒸制
         */
        STEAMING,

        /**
         * 煮制
         */
        BOILING,

        /**
         * 烊制
         */
        STewing,

        /**
         * 其他
         */
        OTHER
    }

    /**
     * 工艺状态枚举
     */
    public enum ProcedureStatus {
        /**
         * 待执行
         */
        PENDING,

        /**
         * 执行中
         */
        IN_PROGRESS,

        /**
         * 已完成
         */
        COMPLETED,

        /**
         * 已取消
         */
        CANCELLED,

        /**
         * 异常
         */
        EXCEPTION
    }
}