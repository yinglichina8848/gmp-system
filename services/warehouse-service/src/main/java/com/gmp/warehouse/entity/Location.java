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

/**
 * 库位实体类
 * <p>
 * 表示仓库中的库位信息，用于库位管理。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_location")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 库位ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 库位编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 库位名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 仓库ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    /**
     * 区域
     */
    @Column(name = "area", length = 50)
    private String area;

    /**
     * 行号
     */
    @Column(name = "row_no", length = 20)
    private String rowNo;

    /**
     * 列号
     */
    @Column(name = "column_no", length = 20)
    private String columnNo;

    /**
     * 层号
     */
    @Column(name = "level_no", length = 20)
    private String levelNo;

    /**
     * 库位类型
     */
    @Column(name = "type", length = 50)
    private String type;

    /**
     * 最大容量
     */
    @Column(name = "max_capacity", precision = 10, scale = 2)
    private Double maxCapacity;

    /**
     * 可用容量
     */
    @Column(name = "available_capacity", precision = 10, scale = 2)
    private Double availableCapacity;

    /**
     * 描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 状态：0-停用，1-启用，2-占用
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