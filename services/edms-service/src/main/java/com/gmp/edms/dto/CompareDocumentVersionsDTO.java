package com.gmp.edms.dto;

import lombok.Data;

/**
 * 文档版本对比DTO类
 * 用于文档版本对比操作的数据传输对象
 */
@Data
public class CompareDocumentVersionsDTO {
    
    /**
     * 文档ID
     */
    private Long documentId;
    
    /**
     * 起始版本ID
     */
    private Long fromVersionId;
    
    /**
     * 目标版本ID
     */
    private Long toVersionId;
}