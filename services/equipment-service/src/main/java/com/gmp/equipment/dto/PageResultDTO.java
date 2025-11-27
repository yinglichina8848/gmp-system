package com.gmp.equipment.dto;

import lombok.Data;
import java.util.List;

/**
 * 分页响应DTO类
 * 用于分页查询结果的数据传输
 */
@Data
public class PageResultDTO<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 每页数据列表
     */
    private List<T> records;

    /**
     * 页码，从1开始
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 是否有下一页
     */
    private Boolean hasNextPage;

    /**
     * 是否有上一页
     */
    private Boolean hasPreviousPage;
}