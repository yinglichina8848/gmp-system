package com.gmp.edms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档实体类
 * 对应数据库中的documents表
 */
@Data
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_number", unique = true, nullable = false, length = 50)
    private String documentNumber; // DOC-YYYY-MMDD-001

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType; // SOP, SPECIFICATION, RECORD, MANUAL

    @Column(name = "category", length = 100)
    private String category; // 分类路径，用/分隔

    @Column(name = "category_id")
    private Long categoryId; // 分类ID

    @Column(name = "status", length = 20)
    private String status = "DRAFT"; // DRAFT, IN_REVIEW, APPROVED, EFFECTIVE, WITHDRAWN

    @Column(name = "current_version", length = 20)
    private String currentVersion = "1.0";

    @Column(name = "template_id")
    private Long templateId; // 关联的模板ID

    @Column(name = "owner_department", length = 100)
    private String ownerDepartment;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "approver", length = 100)
    private String approver;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "retention_period")
    private Integer retentionPeriod; // 保留期(月)

    @Column(name = "confidentiality_level", length = 20)
    private String confidentialityLevel = "NORMAL"; // NORMAL, CONFIDENTIAL, RESTRICTED

    @Column(name = "file_path", length = 500)
    private String filePath; // 存储路径

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType; // MIME类型

    @Column(name = "checksum", length = 128)
    private String checksum; // SHA256校验和

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 关联关系
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentVersion> versions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private DocumentCategory documentCategory;

    // 额外添加的方法以支持业务逻辑
    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setCurrentVersionNumber(String currentVersionNumber) {
        this.currentVersionNumber = currentVersionNumber;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCurrentVersionId(Long currentVersionId) {
        this.currentVersionId = currentVersionId;
    }
    
    public Long getCurrentVersionId() {
        return currentVersionId;
    }
}
