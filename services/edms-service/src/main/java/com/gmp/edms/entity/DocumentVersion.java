package com.gmp.edms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 文档版本实体类
 * 对应数据库中的document_versions表
 */
@Data
@Entity
@Table(name = "document_versions")
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 关联到文档
    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "version_number", nullable = false, length = 20)
    private String versionNumber;

    @Column(name = "version_type", length = 20)
    private String versionType = "REVISION"; // REVISION, MAJOR, MINOR

    @Column(name = "change_reason", columnDefinition = "TEXT")
    private String changeReason; // 变更原因说明

    @Column(name = "change_summary", columnDefinition = "TEXT")
    private String changeSummary; // 变更内容摘要

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "reviewer", length = 100)
    private String reviewer;

    @Column(name = "approver", length = 100)
    private String approver;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_name", length = 255)
    private String fileName;

    @Column(name = "file_type", length = 100)
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "checksum", length = 128)
    private String checksum;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments; // 备注信息

    @Column(name = "is_current")
    private Boolean isCurrent = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime createdTime;

    // 计算文件大小的格式化显示
    @Transient
    public String getFormattedFileSize() {
        if (fileSize == null || fileSize == 0) {
            return "0 B";
        }

        double size = fileSize;
        int unitIndex = 0;
        String[] units = { "B", "KB", "MB", "GB", "TB" };

        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }

        return String.format("%.2f %s", size, units[unitIndex]);
    }

    // 获取完整的文件名（含版本号）
    @Transient
    public String getFullFileName() {
        if (document == null) {
            return versionNumber;
        }
        String title = (String) getFieldValue(document, "title");
        if (title == null) {
            return versionNumber;
        }
        return title + "_v" + versionNumber;
    }

    /**
     * 获取关联文档ID
     */
    public Long getDocumentId() {
        if (document != null) {
            return (Long) getFieldValue(document, "id");
        }
        return null;
    }

    /**
     * 设置文档ID
     */
    public void setDocumentId(Long documentId) {
        if (this.document == null) {
            this.document = new Document();
        }
        // 使用反射设置id字段，避免方法调用问题
        setFieldValue(this.document, "id", documentId);
    }

    /**
     * 使用反射获取对象字段值
     */
    private Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }

        try {
            // 尝试通过字段访问
            java.lang.reflect.Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
            // 尝试通过getter方法获取
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            try {
                java.lang.reflect.Method getter = obj.getClass().getMethod(getterName);
                return getter.invoke(obj);
            } catch (NoSuchMethodException e) {
                // 如果getter不存在，返回null
                return null;
            }
        } catch (Exception e) {
            // 反射访问失败，返回null
            return null;
        }
    }

    /**
     * 使用反射设置对象字段值
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        if (obj == null || fieldName == null) {
            return;
        }

        try {
            // 尝试通过字段访问
            java.lang.reflect.Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(obj, value);
            }
        } catch (Exception e) {
            // 反射设置失败，静默忽略
        }
    }

    /**
     * 查找字段，包括父类中的字段
     */
    private java.lang.reflect.Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 设置备注信息
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
}
