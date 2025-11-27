package com.gmp.edms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 创建分类请求DTO
 */
@Data
public class DocumentCategoryCreateDTO {
    
    @NotBlank(message = "分类编码不能为空")
    @Size(max = 50, message = "分类编码长度不能超过50个字符")
    private String categoryCode;
    
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String categoryName;
    
    @Size(max = 500, message = "分类描述长度不能超过500个字符")
    private String description;
    
    private Long parentId;
    private Integer sortOrder;
    private String createdBy;
}
