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
import java.time.LocalDateTime;
import java.util.List;

/**
 * 仓库实体类
 * <p>
 * 表示仓库信息，用于仓库管理。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_warehouse")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 仓库ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 仓库编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 仓库名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 仓库地址
     */
    @Column(name = "address", length = 500)
    private String address;

    /**
     * 负责人
     */
    @Column(name = "manager", length = 50)
    private String manager;

    /**
     * 联系电话
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 仓库类型
     */
    @Column(name = "type", nullable = false, length = 50)
    private String type;

    /**
     * 存储条件
     */
    @Column(name = "storage_condition", length = 200)
    private String storageCondition;

    /**
     * 库位列表
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    /**
     * 描述
     */
    @Column(name = "description", length = 500)
    private String description;

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