package com.gmp.warehouse.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 出库单DTO类
 * <p>
 * 用于出库单信息的数据传输对象。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class StockOutOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 出库单ID
     */
    private Long id;

    /**
     * 出库单号
     */
    private String orderNo;

    /**
     * 出库类型：生产领料、销售出库、其他出库
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
     * 关联单据号
     */
    private String relatedOrderNo;

    /**
     * 出库总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 出库总金额
     */
    private BigDecimal totalAmount;

    /**
     * 出库状态：0-草稿，1-待审核，2-已审核，3-已出库，4-已取消
     */
    private Integer status;

    /**
     * 出库日期
     */
    private LocalDateTime stockOutDate;

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
     * 出库单明细列表
     */
    private List<StockOutOrderItemDTO> items;

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