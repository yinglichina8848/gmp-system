package com.gmp.edms.dto;

import lombok.Data;

import java.util.List;

/**
 * 分页响应DTO
 */
@Data
public class PageResponseDTO<T> {
    private List<T> records;
    private long total;
    private int pageNo;
    private int pageSize;
    private int pages;
    private boolean hasNext;
    private boolean hasPrevious;
    
    public PageResponseDTO(List<T> records, long total, int pageNo, int pageSize) {
        this.records = records;
        this.total = total;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pages = (int) Math.ceil((double) total / pageSize);
        this.hasNext = pageNo < pages;
        this.hasPrevious = pageNo > 1;
    }
}
