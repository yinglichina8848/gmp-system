package com.gmp.edms.dto;

import lombok.Data;

/**
 * 分页请求DTO
 */
@Data
public class PageRequestDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private String sortBy;
    private String sortOrder = "asc";
}
