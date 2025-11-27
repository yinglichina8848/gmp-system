package com.gmp.warehouse.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 入库单DTO类
 * <p>
 * 用于入库单信息的数据传输对象。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class StockInOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 入库单ID
     */
    private Long id;

    /**
     * 入库单号
     */
    private String orderNo;

    /**
     * 入库类型：采购入库、生产入库、其他入库
     */
    private String orderType;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 关联单据号
     */
    private String relatedOrderNo;

    /**
     * 入库总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 入库总金额
     */
    private BigDecimal totalAmount;

    /**
     * 入库状态：0-草稿，1-待审核，2-已审核，3-已入库，4-已取消
     */
    private Integer status;

    /**
     * 入库日期
     */
    private LocalDateTime stockInDate;

    /**
     * 制单人
     */
    private String creator;

    /**
     * 审核人
     */
    private String reviewer;

    /**
     * 审核日期
     */
    private LocalDateTime reviewDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 入库单明细列表
     */
    private List<StockInOrderItemDTO> items;

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