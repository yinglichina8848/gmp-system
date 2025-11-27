package com.gmp.warehouse.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 中药特色库存批次DTO类
 * <p>
 * 用于传输中药材库存批次信息的数据传输对象
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class TcmInventoryBatchDTO {

    /**
     * 批次ID
     */
    private Long id;

    /**
     * 关联的库存ID
     */
    private Long inventoryId;

    /**
     * 批号
     */
    private String batchNumber;

    /**
     * 生产日期
     */
    private LocalDateTime productionDate;

    /**
     * 入库日期
     */
    private LocalDateTime storageDate;

    /**
     * 有效期至
     */
    private LocalDateTime expirationDate;

    /**
     * 库存数量
     */
    private BigDecimal quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 产地信息
     */
    private String originPlace;

    /**
     * 采收期
     */
    private String harvestingPeriod;

    /**
     * 炮制方法
     */
    private String processingMethod;

    /**
     * 炮制日期
     */
    private LocalDateTime processingDate;

    /**
     * 炮制批号
     */
    private String processingBatchNumber;

    /**
     * 存储条件
     */
    private String storageCondition;

    /**
     * 质量状态
     */
    private String qualityStatus;

    /**
     * 状态
     */
    private String status;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 采购订单号
     */
    private String purchaseOrderNo;

    /**
     * 检验报告编号
     */
    private String inspectionReportNo;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 最后修改人
     */
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifiedTime;
}