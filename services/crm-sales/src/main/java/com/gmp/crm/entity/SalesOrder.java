package com.gmp.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单实体
 * 
 * @author TRAE AI
 */
@Data
@Entity
@Table(name = "sales_order")
public class SalesOrder {

    /**
     * 订单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单编号
     */
    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    /**
     * 客户ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * 订单状态
     */
    @Column(name = "status", nullable = false, length = 50)
    private String status = "PENDING";

    /**
     * 总金额
     */
    @Column(name = "total_amount", precision = 19, scale = 4)
    private BigDecimal totalAmount;

    /**
     * 备注
     */
    @Column(name = "remarks", length = 500)
    private String remarks;

    /**
     * 创建人
     */
    @Column(name = "created_by", length = 100)
    private String createdBy;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 订单明细列表
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    /**
     * 添加订单项
     */
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    /**
     * 移除订单项
     */
    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    /**
     * 计算总金额
     */
    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}