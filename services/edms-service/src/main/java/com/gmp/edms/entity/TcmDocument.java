package com.gmp.edms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 中药特色文档实体类
 * 用于存储中药材相关的专业文档，如中药材管理、炮制工艺、提取工艺等
 */
@Data
@Entity
@Table(name = "tcm_documents")
public class TcmDocument {

    /**
     * 文档ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文档编号
     */
    @Column(name = "document_number", unique = true, nullable = false, length = 50)
    private String documentNumber; // TCM-DOC-YYYY-MMDD-001

    /**
     * 文档标题
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 文档类型
     */
    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType; // TCM_SOP, TCM_SPECIFICATION, TCM_RECORD, TCM_MANUAL

    /**
     * 文档分类路径
     */
    @Column(name = "category", length = 100)
    private String category; // 分类路径，用/分隔

    /**
     * 分类ID
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 文档状态
     */
    @Column(name = "status", length = 20)
    private String status = "DRAFT"; // DRAFT, IN_REVIEW, APPROVED, EFFECTIVE, WITHDRAWN

    /**
     * 当前版本
     */
    @Column(name = "current_version", length = 20)
    private String currentVersion = "1.0";

    /**
     * 当前版本ID
     */
    @Column(name = "current_version_id")
    private Long currentVersionId;

    /**
     * 关联的模板ID
     */
    @Column(name = "template_id")
    private Long templateId;

    /**
     * 所属部门
     */
    @Column(name = "owner_department", length = 100)
    private String ownerDepartment;

    /**
     * 作者
     */
    @Column(name = "author", nullable = false, length = 100)
    private String author;

    /**
     * 审批人
     */
    @Column(name = "approver", length = 100)
    private String approver;

    /**
     * 生效日期
     */
    @Column(name = "effective_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectiveDate;

    /**
     * 过期日期
     */
    @Column(name = "expiry_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryDate;

    /**
     * 保留期(月)
     */
    @Column(name = "retention_period")
    private Integer retentionPeriod;

    /**
     * 保密级别
     */
    @Column(name = "confidentiality_level", length = 20)
    private String confidentialityLevel = "NORMAL"; // NORMAL, CONFIDENTIAL, RESTRICTED

    /**
     * 文件路径
     */
    @Column(name = "file_path", length = 500)
    private String filePath;

    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * 内容类型
     */
    @Column(name = "content_type", length = 100)
    private String contentType;

    /**
     * 校验和
     */
    @Column(name = "checksum", length = 128)
    private String checksum;

    /**
     * 中药材名称（适用于中药材相关文档）
     */
    @Column(name = "herb_name", length = 100)
    private String herbName;

    /**
     * 炮制方法（适用于炮制工艺文档）
     */
    @Column(name = "processing_method", length = 100)
    private String processingMethod;

    /**
     * 提取方法（适用于提取工艺文档）
     */
    @Column(name = "extraction_method", length = 100)
    private String extractionMethod;

    /**
     * 质量标准（适用于质量控制文档）
     */
    @Column(name = "quality_standard", length = 200)
    private String qualityStandard;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}