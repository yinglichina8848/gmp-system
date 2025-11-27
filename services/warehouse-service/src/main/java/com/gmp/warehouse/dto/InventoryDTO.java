package com.gmp.warehouse.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存DTO类
 * <p>
 * 用于库存信息的数据传输对象。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class InventoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 库存ID
     */
    private Long id;

    /**
     * 物料ID
     */
    private Long materialId;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 单位
     */
    private String unit;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 库位ID
     */
    private Long locationId;

    /**
     * 库位名称
     */
    private String locationName;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 当前库存数量
     */
    private BigDecimal currentQuantity;

    /**
     * 锁定数量
     */
    private BigDecimal lockedQuantity;

    /**
     * 可用数量
     */
    private BigDecimal availableQuantity;

    /**
     * 有效期
     */
    private LocalDateTime expiryDate;

    /**
     * 入库日期
     */
    private LocalDateTime stockInDate;

    /**
     * 最小安全库存
     */
    private BigDecimal minSafetyStock;

    /**
     * 最大安全库存
     */
    private BigDecimal maxSafetyStock;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 更新人
     */
    private String updatedBy;

}