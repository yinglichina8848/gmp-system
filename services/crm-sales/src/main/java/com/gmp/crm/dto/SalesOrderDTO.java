package com.gmp.crm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 销售订单数据传输对象
 * 
 * @author TRAE AI
 */
@Data
public class SalesOrderDTO {

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 客户信息
     */
    private CustomerDTO customer;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 订单明细
     */
    private List<OrderItemDTO> items;

}