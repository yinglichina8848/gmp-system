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
 * 供应商实体类
 * <p>
 * 表示物料的供应商信息，用于供应商管理。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "gmp_supplier")
@Data
@EqualsAndHashCode(callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    /**
     * 供应商编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    /**
     * 供应商名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 联系人
     */
    @Column(name = "contact_person", length = 50)
    private String contactPerson;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    /**
     * 地址
     */
    @Column(name = "address", length = 500)
    private String address;

    /**
     * 供应商评级（1-5星）
     */
    @Column(name = "rating", nullable = false, columnDefinition = "INTEGER DEFAULT 3")
    private Integer rating;

    /**
     * 供应商资质
     */
    @Column(name = "qualification", length = 500)
    private String qualification;

    /**
     * 备注
     */
    @Column(name = "remark", length = 1000)
    private String remark;

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