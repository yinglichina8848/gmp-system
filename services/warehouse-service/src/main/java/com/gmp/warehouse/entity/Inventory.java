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

/**
 * 库存实体类
 * <p>
 * 表示物料的库存信息，用于库存管理。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_inventory")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 库存ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 物料ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    /**
     * 仓库ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    /**
     * 库位ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * 批次号
     */
    @Column(name = "batch_no", length = 50)
    private String batchNo;

    /**
     * 当前库存数量
     */
    @Column(name = "current_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentQuantity;

    /**
     * 锁定数量
     */
    @Column(name = "locked_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal lockedQuantity;

    /**
     * 可用数量
     */
    @Column(name = "available_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal availableQuantity;

    /**
     * 有效期
     */
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    /**
     * 入库日期
     */
    @Column(name = "stock_in_date")
    private LocalDateTime stockInDate;

    /**
     * 最小安全库存
     */
    @Column(name = "min_safety_stock", precision = 10, scale = 2)
    private BigDecimal minSafetyStock;

    /**
     * 最大安全库存
     */
    @Column(name = "max_safety_stock", precision = 10, scale = 2)
    private BigDecimal maxSafetyStock;

    /**
     * 备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;

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