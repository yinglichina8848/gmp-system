package com.gmp.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 入库单实体类
 * <p>
 * 表示物料入库单据信息，用于入库管理。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_stock_in_order")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class StockInOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 入库单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 入库单号
     */
    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    /**
     * 入库类型：采购入库、生产入库、退货入库、其他入库
     */
    @Column(name = "order_type", nullable = false, length = 50)
    private String orderType;

    /**
     * 仓库ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    /**
     * 供应商ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /**
     * 关联单据号
     */
    @Column(name = "related_order_no", length = 50)
    private String relatedOrderNo;

    /**
     * 入库总数量
     */
    @Column(name = "total_quantity", precision = 10, scale = 2)
    private BigDecimal totalQuantity;

    /**
     * 入库总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 入库状态：0-草稿，1-待审核，2-已审核，3-已入库，4-已取消
     */
    @Column(name = "status", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer status;

    /**
     * 入库日期
     */
    @Column(name = "stock_in_date")
    private LocalDateTime stockInDate;

    /**
     * 制单人
     */
    @Column(name = "creator", length = 50)
    private String creator;

    /**
     * 审核人
     */
    @Column(name = "reviewer", length = 50)
    private String reviewer;

    /**
     * 审核日期
     */
    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    /**
     * 备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;

    /**
     * 入库单明细列表
     */
    @OneToMany(mappedBy = "stockInOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockInOrderItem> items;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 更新人
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

}