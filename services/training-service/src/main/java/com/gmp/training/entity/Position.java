package com.gmp.training.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 职位实体类，对应t_position表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_position")
@EntityListeners(AuditingEntityListener.class)
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职位ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id", nullable = false)
    private Long id;
    
    /**
     * 获取职位ID
     * 
     * @return 职位ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 职位名称
     */
    @NotBlank(message = "职位名称不能为空")
    @Size(max = 100, message = "职位名称长度不能超过100个字符")
    @Column(name = "position_name", length = 100, nullable = false, unique = true)
    private String positionName;

    /**
     * 职位编码
     */
    @NotBlank(message = "职位编码不能为空")
    @Size(max = 50, message = "职位编码长度不能超过50个字符")
    @Column(name = "position_code", length = 50, nullable = false, unique = true)
    private String positionCode;

    /**
     * 职位描述
     */
    @Size(max = 500, message = "职位描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 职位等级
     */
    @Size(max = 50, message = "职位等级长度不能超过50个字符")
    @Column(name = "position_level", length = 50)
    private String positionLevel;

    /**
     * 职位状态：启用/禁用
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private Status status = Status.ACTIVE;

    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 创建者
     */
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 最后修改者
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_time")
    private LocalDateTime lastModifiedTime;

    /**
     * 职位状态枚举
     */
    public enum Status {
        ACTIVE, // 启用
        INACTIVE // 禁用
    }
}