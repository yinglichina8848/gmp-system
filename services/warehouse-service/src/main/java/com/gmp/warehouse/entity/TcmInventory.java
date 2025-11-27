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
 * 中药特色库存实体类
 * <p>
 * 表示中药材的库存信息，用于中药材库存管理，包含中药材特有的属性如产地、采收期、炮制信息等。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_tcm_inventory")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class TcmInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 库存ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 中药材名称
     */
    @Column(name = "herb_name", nullable = false, length = 100)
    private String herbName;

    /**
     * 批号
     */
    @Column(name = "batch_number", nullable = false, length = 50)
    private String batchNumber;

    /**
     * 产地信息
     */
    @Column(name = "origin_place", length = 100)
    private String originPlace;

    /**
     * 道地性标识
     */
    @Column(name = "authenticity_flag")
    private Boolean authenticityFlag = false;

    /**
     * 采收日期
     */
    @Column(name = "harvest_date")
    private LocalDateTime harvestDate;

    /**
     * 炮制信息
     */
    @Column(name = "processing_info", length = 200)
    private String processingInfo;

    /**
     * 炮制方法
     */
    @Column(name = "processing_method", length = 50)
    private String processingMethod;

    /**
     * 炮制日期
     */
    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    /**
     * 质量等级
     */
    @Column(name = "quality_grade", length = 20)
    private String qualityGrade;

    /**
     * 入库数量
     */
    @Column(name = "quantity_in", precision = 15, scale = 3)
    private BigDecimal quantityIn;

    /**
     * 出库数量
     */
    @Column(name = "quantity_out", precision = 15, scale = 3)
    private BigDecimal quantityOut;

    /**
     * 当前库存数量
     */
    @Column(name = "current_quantity", precision = 15, scale = 3)
    private BigDecimal currentQuantity;

    /**
     * 库存单位
     */
    @Column(name = "unit", length = 20)
    private String unit;

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
    @JoinColumn(name = "location_id")
    private Location location;

    /**
     * 供应商ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    /**
     * 生产日期
     */
    @Column(name = "manufacture_date")
    private LocalDateTime manufactureDate;

    /**
     * 有效期至
     */
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    /**
     * 养护要求
     */
    @Column(name = "storage_requirement", length = 200)
    private String storageRequirement;

    /**
     * 养护记录
     */
    @Column(name = "maintenance_record", columnDefinition = "TEXT")
    private String maintenanceRecord;

    /**
     * 状态
     */
    @Column(name = "status", length = 20)
    private String status = "NORMAL"; // NORMAL, FROZEN, EXPIRED, DAMAGED

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_time", updatable = false)
    private LocalDateTime createdTime;

    /**
     * 最后修改人
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;
}