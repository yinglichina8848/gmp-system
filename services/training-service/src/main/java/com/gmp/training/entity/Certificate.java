package com.gmp.training.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 证书实体类，对应t_certificate表
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_certificate")
@EntityListeners(AuditingEntityListener.class)
public class Certificate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 证书ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id", nullable = false)
    private Long id;

    /**
     * 关联的培训记录
     */
    @OneToOne
    @JoinColumn(name = "record_id", unique = true)
    private TrainingRecord trainingRecord;

    /**
     * 证书编号
     */
    @NotBlank(message = "证书编号不能为空")
    @Size(max = 100, message = "证书编号长度不能超过100个字符")
    @Column(name = "certificate_number", length = 100, nullable = false, unique = true)
    private String certificateNumber;

    /**
     * 证书名称
     */
    @NotBlank(message = "证书名称不能为空")
    @Size(max = 200, message = "证书名称长度不能超过200个字符")
    @Column(name = "certificate_name", length = 200, nullable = false)
    private String certificateName;

    /**
     * 颁发日期
     */
    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    /**
     * 有效期至
     */
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    /**
     * 颁发机构
     */
    @Size(max = 200, message = "颁发机构长度不能超过200个字符")
    @Column(name = "issuing_authority", length = 200)
    private String issuingAuthority;

    /**
     * 证书状态：有效/已过期/已吊销
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private CertificateStatus status = CertificateStatus.VALID;

    /**
     * 证书描述
     */
    @Size(max = 500, message = "证书描述长度不能超过500个字符")
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 证书文件路径
     */
    @Size(max = 500, message = "证书文件路径长度不能超过500个字符")
    @Column(name = "file_path", length = 500)
    private String filePath;

    /**
     * 是否需要复训
     */
    @Column(name = "requires_refresher")
    private boolean requiresRefresher = false;

    /**
     * 复训提醒提前天数
     */
    @Column(name = "reminder_days")
    private Integer reminderDays;

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
     * 证书状态枚举
     */
    public enum CertificateStatus {
        VALID,         // 有效
        EXPIRED,       // 已过期
        REVOKED        // 已吊销
    }

    /**
     * 检查证书是否即将过期（提前reminderDays天）
     * 
     * @return 是否即将过期
     */
    public boolean isExpiringSoon() {
        if (this.expiryDate == null || this.reminderDays == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderDate = this.expiryDate.minusDays(this.reminderDays);
        return now.isAfter(reminderDate) && now.isBefore(this.expiryDate);
    }

    /**
     * 检查证书是否已过期
     * 
     * @return 是否已过期
     */
    public boolean isExpired() {
        return this.expiryDate != null && this.expiryDate.isBefore(LocalDateTime.now());
    }
}
