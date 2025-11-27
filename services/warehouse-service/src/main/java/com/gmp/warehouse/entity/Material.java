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
 * 物料实体类
 * <p>
 * 表示仓库中管理的物料信息，是仓库管理系统的核心实体之一。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_material")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物料ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 物料编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 物料名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 物料描述
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 物料分类ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private MaterialCategory category;

    /**
     * 计量单位
     */
    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    /**
     * 规格型号
     */
    @Column(name = "specification", length = 200)
    private String specification;

    /**
     * 供应商ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /**
     * 安全库存
     */
    @Column(name = "safety_stock", precision = 10, scale = 2)
    private BigDecimal safetyStock;

    /**
     * 预警库存
     */
    @Column(name = "warning_stock", precision = 10, scale = 2)
    private BigDecimal warningStock;

    /**
     * 批次管理标志
     */
    @Column(name = "batch_managed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean batchManaged;

    /**
     * 有效期管理标志
     */
    @Column(name = "expiry_managed", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean expiryManaged;

    /**
     * 存储条件
     */
    @Column(name = "storage_condition", length = 200)
    private String storageCondition;

    /**
     * 备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;

    /**
     * 状态：0-停用，1-启用
     */
    @Column(name = "status", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer status;

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