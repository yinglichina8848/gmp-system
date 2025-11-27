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
 * 出库单明细实体类
 * <p>
 * 表示出库单中的具体物料明细信息。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_stock_out_order_item")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class StockOutOrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 明细ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 出库单ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private StockOutOrder stockOutOrder;

    /**
     * 物料ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    /**
     * 库位ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    /**
     * 物料编码
     */
    @Column(name = "material_code", length = 50)
    private String materialCode;

    /**
     * 物料名称
     */
    @Column(name = "material_name", length = 200)
    private String materialName;

    /**
     * 规格型号
     */
    @Column(name = "specification", length = 200)
    private String specification;

    /**
     * 单位
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 批次号
     */
    @Column(name = "batch_no", length = 50)
    private String batchNo;

    /**
     * 出库数量
     */
    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    /**
     * 单价
     */
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 行号
     */
    @Column(name = "line_no", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer lineNo;

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