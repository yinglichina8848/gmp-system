package com.gmp.edms.dto;

import lombok.Data;

/**
 * 文档版本恢复DTO类
 * 用于文档版本恢复操作的数据传输对象
 */
@Data
public class RestoreDocumentDTO {
    
    /**
     * 文档ID
     */
    private Long documentId;
    
    /**
     * 版本ID
     */
    private Long versionId;
}