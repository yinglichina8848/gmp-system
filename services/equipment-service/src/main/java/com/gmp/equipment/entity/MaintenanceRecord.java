package com.gmp.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 维护记录实体类
 * 记录设备的维护历史信息
 */
@Data
@Entity
@Table(name = "maintenance_record")
public class MaintenanceRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 维护计划ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_plan_id")
    private MaintenancePlan maintenancePlan;

    /**
     * 设备ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    /**
     * 维护单号
     */
    @Column(name = "maintenance_no", length = 50)
    private String maintenanceNo;

    /**
     * 维护类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_type", nullable = false, length = 50)
    private MaintenanceType maintenanceType;

    /**
     * 维护开始时间
     */
    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    /**
     * 维护结束时间
     */
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    /**
     * 维护人员
     */
    @Column(name = "maintenance_by", length = 50)
    private String maintenanceBy;

    /**
     * 维护内容
     */
    @Column(name = "maintenance_content", length = 1000)
    private String maintenanceContent;

    /**
     * 故障描述
     */
    @Column(name = "fault_description", length = 1000)
    private String faultDescription;

    /**
     * 维修措施
     */
    @Column(name = "repair_measures", length = 1000)
    private String repairMeasures;

    /**
     * 更换部件
     */
    @Column(name = "replaced_parts", length = 1000)
    private String replacedParts;

    /**
     * 维护结果
     */
    @Column(name = "maintenance_result", length = 500)
    private String maintenanceResult;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

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