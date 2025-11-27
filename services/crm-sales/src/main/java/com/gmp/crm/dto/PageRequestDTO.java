package com.gmp.crm.dto;

import lombok.Data;

/**
 * 分页请求DTO
 * 
 * @author TRAE AI
 */
@Data
public class PageRequestDTO {

    /**
     * 当前页码，默认1
     */
    private int page = 1;

    /**
     * 每页大小，默认10
     */
    private int size = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向：asc/desc
     */
    private String sortOrder = "asc";

    /**
     * 获取分页开始索引
     * 
     * @return 开始索引
     */
    public int getStartIndex() {
        return (page - 1) * size;
    }

}