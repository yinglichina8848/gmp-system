package com.gmp.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 校准记录实体类
 * 记录设备的校准历史信息
 */
@Data
@Entity
@Table(name = "calibration_record")
public class CalibrationRecord {

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
     * 校准单号
     */
    @Column(name = "calibration_no", length = 50)
    private String calibrationNo;

    /**
     * 校准日期
     */
    @Column(name = "calibration_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date calibrationDate;

    /**
     * 下次校准日期
     */
    @Column(name = "next_calibration_date")
    @Temporal(TemporalType.DATE)
    private Date nextCalibrationDate;

    /**
     * 校准结果
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false, length = 50)
    private CalibrationResult result;

    /**
     * 校准机构
     */
    @Column(name = "calibration_organization", length = 100)
    private String calibrationOrganization;

    /**
     * 校准人员
     */
    @Column(name = "calibration_by", length = 50)
    private String calibrationBy;

    /**
     * 校准证书编号
     */
    @Column(name = "certificate_no", length = 50)
    private String certificateNo;

    /**
     * 校准标准
     */
    @Column(name = "calibration_standard", length = 200)
    private String calibrationStandard;

    /**
     * 偏差描述
     */
    @Column(name = "deviation_description", length = 500)
    private String deviationDescription;

    /**
     * 处理措施
     */
    @Column(name = "treatment_measures", length = 500)
    private String treatmentMeasures;

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