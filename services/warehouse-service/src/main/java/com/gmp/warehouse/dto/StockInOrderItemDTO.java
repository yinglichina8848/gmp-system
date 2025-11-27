package com.gmp.warehouse.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 入库单明细DTO类
 * <p>
 * 用于入库单明细信息的数据传输对象。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class StockInOrderItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
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
     * 入库数量
     */
    private BigDecimal quantity;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 有效期
     */
    private LocalDateTime expiryDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 行号
     */
    private Integer lineNo;

}