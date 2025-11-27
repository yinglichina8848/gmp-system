package com.gmp.edms.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档分类实体类
 */
@Data
@Entity
@Table(name = "document_categories")
public class DocumentCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "category_code", unique = true, nullable = false, length = 50)
    private String categoryCode;
    
    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "category_path", length = 500)
    private String categoryPath; // 完整分类路径，用/分隔
    
    @Column(name = "level")
    private Integer level; // 分类层级
    
    @Column(name = "status", length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE
    
    @Column(name = "sort_order")
    private Integer sortOrder;
    
    // 创建者信息
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 自关联关系 - 父分类
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DocumentCategory parent;
    
    // 自关联关系 - 子分类
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentCategory> children;
    
    // 关联的文档
    @OneToMany(mappedBy = "documentCategory", cascade = CascadeType.PERSIST)
    private List<Document> documents;
}
