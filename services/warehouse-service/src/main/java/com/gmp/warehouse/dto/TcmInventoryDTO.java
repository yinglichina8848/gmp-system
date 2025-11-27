package com.gmp.warehouse.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 中药特色库存DTO类
 * <p>
 * 用于传输中药材库存信息的数据传输对象。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class TcmInventoryDTO {

    /**
     * 库存ID
     */
    private Long id;

    /**
     * 中药材名称
     */
    private String herbName;

    /**
     * 批号
     */
    private String batchNumber;

    /**
     * 产地信息
     */
    private String originPlace;

    /**
     * 道地性标识
     */
    private Boolean authenticityFlag = false;

    /**
     * 采收日期
     */
    private LocalDateTime harvestDate;

    /**
     * 炮制信息
     */
    private String processingInfo;

    /**
     * 炮制方法
     */
    private String processingMethod;

    /**
     * 炮制日期
     */
    private LocalDateTime processingDate;

    /**
     * 质量等级
     */
    private String qualityGrade;

    /**
     * 入库数量
     */
    private BigDecimal quantityIn;

    /**
     * 出库数量
     */
    private BigDecimal quantityOut;

    /**
     * 当前库存数量
     */
    private BigDecimal currentQuantity;

    /**
     * 库存单位
     */
    private String unit;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 库位ID
     */
    private Long locationId;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 生产日期
     */
    private LocalDateTime manufactureDate;

    /**
     * 有效期至
     */
    private LocalDateTime expiryDate;

    /**
     * 养护要求
     */
    private String storageRequirement;

    /**
     * 养护记录
     */
    private String maintenanceRecord;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
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
     * 最后修改人
     */
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifiedTime;
}