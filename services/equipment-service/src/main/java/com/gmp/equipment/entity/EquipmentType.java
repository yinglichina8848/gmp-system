package com.gmp.equipment.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * 设备类型实体类
 * 表示设备的分类信息，如测量仪器、生产设备等
 */
@Data
@Entity
@Table(name = "equipment_type")
public class EquipmentType {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备类型编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 设备类型名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 设备类型描述
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