package com.gmp.crm.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细数据传输对象
 * 
 * @author TRAE AI
 */
@Data
public class OrderItemDTO {

    /**
     * 明细ID
     */
    private Long id;

    /**
     * 产品编号
     */
    private String productCode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 小计金额
     */
    private BigDecimal subTotal;

    /**
     * 备注
     */
    private String remarks;

}