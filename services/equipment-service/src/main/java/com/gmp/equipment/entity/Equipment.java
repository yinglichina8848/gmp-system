package com.gmp.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 设备实体类
 * 表示设备的基本信息和状态
 */
@Data
@Entity
@Table(name = "equipment")
public class Equipment {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备编码，唯一标识
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 设备名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 设备型号
     */
    @Column(name = "model", length = 100)
    private String model;

    /**
     * 设备序列号
     */
    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    /**
     * 设备类型ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_type_id", nullable = false)
    private EquipmentType equipmentType;

    /**
     * 设备状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EquipmentStatus status;

    /**
     * 放置位置
     */
    @Column(name = "location", length = 200)
    private String location;

    /**
     * 负责人
     */
    @Column(name = "responsible_person", length = 50)
    private String responsiblePerson;

    /**
     * 供应商
     */
    @Column(name = "supplier", length = 100)
    private String supplier;

    /**
     * 购买日期
     */
    @Column(name = "purchase_date")
    @Temporal(TemporalType.DATE)
    private Date purchaseDate;

    /**
     * 验收日期
     */
    @Column(name = "acceptance_date")
    @Temporal(TemporalType.DATE)
    private Date acceptanceDate;

    /**
     * 启用日期
     */
    @Column(name = "commissioning_date")
    @Temporal(TemporalType.DATE)
    private Date commissioningDate;

    /**
     * 下次校准日期
     */
    @Column(name = "next_calibration_date")
    @Temporal(TemporalType.DATE)
    private Date nextCalibrationDate;

    /**
     * 下次维护日期
     */
    @Column(name = "next_maintenance_date")
    @Temporal(TemporalType.DATE)
    private Date nextMaintenanceDate;

    /**
     * 设备描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /**
     * 创建人
     */
    @Column(name = "created_by", length = 50)
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name = "created_time", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    /**
     * 更新人
     */
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    /**
     * 更新时间
     */
    @Column(name = "updated_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;

    /**
     * 版本号，用于乐观锁
     */
    @Version
    private Integer version;

    /**
     * 实体创建前的钩子方法，设置创建时间和更新时间
     */
    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createdTime = now;
        this.updatedTime = now;
    }

    /**
     * 实体更新前的钩子方法，更新最后修改时间
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedTime = new Date();
    }
}