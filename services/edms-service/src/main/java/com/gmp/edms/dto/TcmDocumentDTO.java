package com.gmp.edms.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 中药特色文档DTO类
 * 用于传输中药特色文档相关数据
 */
@Data
public class TcmDocumentDTO {

    /**
     * 文档ID
     */
    private Long id;

    /**
     * 文档编号
     */
    private String documentNumber;

    /**
     * 文档标题
     */
    private String title;

    /**
     * 文档类型
     */
    private String documentType;

    /**
     * 文档分类路径
     */
    private String category;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 文档状态
     */
    private String status;

    /**
     * 当前版本
     */
    private String currentVersion;

    /**
     * 当前版本ID
     */
    private Long currentVersionId;

    /**
     * 关联的模板ID
     */
    private Long templateId;

    /**
     * 所属部门
     */
    private String ownerDepartment;

    /**
     * 作者
     */
    private String author;

    /**
     * 审批人
     */
    private String approver;

    /**
     * 生效日期
     */
    private LocalDateTime effectiveDate;

    /**
     * 过期日期
     */
    private LocalDateTime expiryDate;

    /**
     * 保留期(月)
     */
    private Integer retentionPeriod;

    /**
     * 保密级别
     */
    private String confidentialityLevel;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 内容类型
     */
    private String contentType;

    /**
     * 校验和
     */
    private String checksum;

    /**
     * 中药材名称（适用于中药材相关文档）
     */
    private String herbName;

    /**
     * 炮制方法（适用于炮制工艺文档）
     */
    private String processingMethod;

    /**
     * 提取方法（适用于提取工艺文档）
     */
    private String extractionMethod;

    /**
     * 质量标准（适用于质量控制文档）
     */
    private String qualityStandard;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}