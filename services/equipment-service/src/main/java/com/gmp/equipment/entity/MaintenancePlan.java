package com.gmp.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 维护计划实体类
 * 记录设备的维护计划信息
 */
@Data
@Entity
@Table(name = "maintenance_plan")
public class MaintenancePlan {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    /**
     * 计划单号
     */
    @Column(name = "plan_no", length = 50)
    private String planNo;

    /**
     * 维护类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_type", nullable = false, length = 50)
    private MaintenanceType maintenanceType;

    /**
     * 维护周期类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_type", length = 50)
    private MaintenanceCycleType cycleType;

    /**
     * 计划开始日期
     */
    @Column(name = "planned_start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date plannedStartDate;

    /**
     * 计划结束日期
     */
    @Column(name = "planned_end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date plannedEndDate;

    /**
     * 实际开始日期
     */
    @Column(name = "actual_start_date")
    @Temporal(TemporalType.DATE)
    private Date actualStartDate;

    /**
     * 实际结束日期
     */
    @Column(name = "actual_end_date")
    @Temporal(TemporalType.DATE)
    private Date actualEndDate;

    /**
     * 计划状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MaintenancePlanStatus status;

    /**
     * 维护负责人
     */
    @Column(name = "responsible_person", length = 50)
    private String responsiblePerson;

    /**
     * 维护内容
     */
    @Column(name = "maintenance_content", length = 1000)
    private String maintenanceContent;

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