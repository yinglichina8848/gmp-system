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
 * 中药特色库存批次实体类
 * <p>
 * 表示中药材的库存批次信息，用于中药材库存批次管理，
 * 包含中药材特有的属性如产地、采收期、炮制信息、存储条件等。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_tcm_inventory_batch")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class TcmInventoryBatch implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 批次ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 关联的库存ID
     */
    @Column(name = "inventory_id", nullable = false)
    private Long inventoryId;

    /**
     * 批号
     */
    @Column(name = "batch_number", nullable = false, length = 50)
    private String batchNumber;

    /**
     * 生产日期
     */
    @Column(name = "production_date")
    private LocalDateTime productionDate;

    /**
     * 入库日期
     */
    @Column(name = "storage_date", nullable = false)
    private LocalDateTime storageDate;

    /**
     * 有效期至
     */
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    /**
     * 库存数量
     */
    @Column(name = "quantity", precision = 18, scale = 3, nullable = false)
    private BigDecimal quantity;

    /**
     * 单位
     */
    @Column(name = "unit", length = 20, nullable = false)
    private String unit;

    /**
     * 产地信息
     */
    @Column(name = "origin_place", length = 100)
    private String originPlace;

    /**
     * 采收期
     */
    @Column(name = "harvesting_period", length = 50)
    private String harvestingPeriod;

    /**
     * 炮制方法
     */
    @Column(name = "processing_method", length = 100)
    private String processingMethod;

    /**
     * 炮制日期
     */
    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    /**
     * 炮制批号
     */
    @Column(name = "processing_batch_number", length = 50)
    private String processingBatchNumber;

    /**
     * 存储条件
     */
    @Column(name = "storage_condition", length = 200)
    private String storageCondition;

    /**
     * 质量状态
     */
    @Column(name = "quality_status", length = 20)
    private String qualityStatus; // NORMAL, DAMAGED, EXPIRED, QUARANTINE

    /**
     * 状态
     */
    @Column(name = "status", length = 20, nullable = false)
    private String status; // ACTIVE, INACTIVE, DELETED

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 采购订单号
     */
    @Column(name = "purchase_order_no", length = 50)
    private String purchaseOrderNo;

    /**
     * 检验报告编号
     */
    @Column(name = "inspection_report_no", length = 50)
    private String inspectionReportNo;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by", length = 50)
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    /**
     * 最后修改人
     */
    @LastModifiedBy
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;
}