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
 * 出库单实体类
 * <p>
 * 表示物料出库单据信息，用于出库管理。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_stock_out_order")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class StockOutOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 出库单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 出库单号
     */
    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    /**
     * 出库类型：生产领料、销售出库、其他出库
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
     * 关联单据号
     */
    @Column(name = "related_order_no", length = 50)
    private String relatedOrderNo;

    /**
     * 出库总数量
     */
    @Column(name = "total_quantity", precision = 10, scale = 2)
    private BigDecimal totalQuantity;

    /**
     * 出库总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 出库状态：0-草稿，1-待审核，2-已审核，3-已出库，4-已取消
     */
    @Column(name = "status", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer status;

    /**
     * 出库日期
     */
    @Column(name = "stock_out_date")
    private LocalDateTime stockOutDate;

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
     * 出库单明细列表
     */
    @OneToMany(mappedBy = "stockOutOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockOutOrderItem> items;

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