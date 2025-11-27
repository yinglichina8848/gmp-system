package com.gmp.edms.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新分类请求DTO
 */
@Data
public class DocumentCategoryUpdateDTO {
    
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String categoryName;
    
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;
    
    private Long parentId;
    private Integer sortOrder;
    private String status;
}
