package com.gmp.qms.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * EDMS文档数据传输对象，用于系统间文档数据交换
 * 
 * @author GMP系统开发团队
 */
@Data
public class EdmsDocumentDTO {
    
    /**
     * 文档ID
     */
    private String id;
    
    /**
     * 文档标题
     */
    private String title;
    
    /**
     * 文档内容
     */
    private String content;
    
    /**
     * 文档类型
     */
    private String type;
    
    /**
     * 文档状态
     */
    private String status;
    
    /**
     * 文档版本
     */
    private String version;
    
    /**
     * 作者ID
     */
    private String authorId;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 最后修改时间
     */
    private Date updateTime;
    
    /**
     * 相关实体ID（如偏差ID、CAPA ID等）
     */
    private String relatedEntityId;
    
    /**
     * 相关实体类型
     */
    private String relatedEntityType;
    
    /**
     * 额外属性
     */
    private Map<String, Object> extraAttributes;
}
