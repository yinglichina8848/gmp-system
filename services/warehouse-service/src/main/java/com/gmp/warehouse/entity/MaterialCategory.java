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
 * 物料分类实体类
 * <p>
 * 表示物料的分类信息，用于物料的分类管理。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_material_category")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class MaterialCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 分类编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 分类名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 父分类ID
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private MaterialCategory parent;

    /**
     * 子分类列表
     */
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MaterialCategory> children;

    /**
     * 分类级别
     */
    @Column(name = "level", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer level;

    /**
     * 排序号
     */
    @Column(name = "sort_order", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer sortOrder;

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