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
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 资质实体类，用于存储员工的具体资质证书信息
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hr_qualification")
@EntityListeners(AuditingEntityListener.class)
public class Qualification {
    
    /**
     * 资质ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 证书编号
     */
    @Column(name = "certificate_number", nullable = false, length = 100)
    @NotBlank(message = "证书编号不能为空")
    private String certificateNumber;
    
    /**
     * 发证机构
     */
    @Column(name = "issuing_authority", length = 200)
    private String issuingAuthority;
    
    /**
     * 获得日期
     */
    @Column(name = "issue_date", nullable = false)
    @NotNull(message = "获得日期不能为空")
    private LocalDate issueDate;
    
    /**
     * 有效期至
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    /**
     * 资质状态
     */
    @Column(name = "status", nullable = false, length = 20)
    @NotBlank(message = "资质状态不能为空")
    private String status; // ACTIVE, EXPIRED, PENDING_RENEWAL
    
    /**
     * 证书文件路径
     */
    @Column(name = "certificate_file_path", length = 500)
    private String certificateFilePath;
    
    /**
     * 备注
     */
    @Column(name = "remarks", length = 1000)
    private String remarks;
    
    /**
     * 所属员工
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Employee employee;
    
    /**
     * 资质类型
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualification_type_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private QualificationType qualificationType;
    
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