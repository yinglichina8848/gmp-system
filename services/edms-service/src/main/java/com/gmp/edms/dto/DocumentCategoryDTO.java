package com.gmp.edms.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档分类DTO
 */
@Data
public class DocumentCategoryDTO {
    private Long id;
    private String categoryCode;
    private String categoryName;
    private String description;
    private String categoryPath;
    private Integer level;
    private String status;
    private Integer sortOrder;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentId;
    private String parentName;
    private List<DocumentCategoryDTO> children;
    private int documentCount;
}
