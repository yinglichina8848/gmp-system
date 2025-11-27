package com.gmp.crm.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细实体类
 * 
 * @author TRAE AI
 */
@Data
@Entity
@Table(name = "crm_order_items")
public class OrderItem {

    /**
     * 明细ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属订单
     */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private SalesOrder order;

    /**
     * 产品编号
     */
    @Column(nullable = false, length = 50)
    private String productCode;

    /**
     * 产品名称
     */
    @Column(nullable = false, length = 255)
    private String productName;

    /**
     * 单价
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 数量
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 小计金额
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subTotal;

    /**
     * 备注
     */
    @Column(length = 200)
    private String remarks;

}