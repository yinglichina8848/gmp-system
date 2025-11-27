package com.gmp.hr.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 资质类型实体类，用于定义各类资质证书的类型
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hr_qualification_type")
@EntityListeners(AuditingEntityListener.class)
public class QualificationType {
    
    /**
     * 资质类型ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 资质代码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    @NotBlank(message = "资质代码不能为空")
    private String code;
    
    /**
     * 资质名称
     */
    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "资质名称不能为空")
    private String name;
    
    /**
     * 资质描述
     */
    @Column(name = "description", length = 500)
    private String description;
    
    /**
     * 是否需要定期更新
     */
    @Column(name = "requires_renewal", nullable = false)
    private Boolean requiresRenewal = false;
    
    /**
     * 有效期（月）
     */
    @Column(name = "validity_period_months")
    private Integer validityPeriodMonths;
    
    /**
     * 预警提前期（月）
     */
    @Column(name = "alert_period_months")
    private Integer alertPeriodMonths;
    
    /**
     * 关联的职位要求
     */
    @ManyToMany(mappedBy = "requiredQualifications")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Position> requiredPositions = new HashSet<>();
    
    /**
     * 属于该类型的资质证书
     */
    @OneToMany(mappedBy = "qualificationType", cascade = CascadeType.ALL, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Qualification> qualifications = new HashSet<>();
    
    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false, length = 50)
    private String createdBy;
    
    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * 最后修改人
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    /**
     * 记录是否被删除
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
}