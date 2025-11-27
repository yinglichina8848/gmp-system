package com.gmp.qms.dto;

import com.gmp.qms.entity.Capa;
import lombok.Data;

import java.time.LocalDate;

/**
 * CAPA搜索条件
 * 
 * @author GMP系统开发团队
 */
@Data
public class CapaSearchCriteria {

    /**
     * CAPA编号或标题关键字
     */
    private String keyword;

    /**
     * CAPA状态
     */
    private Capa.CapaStatus status;

    /**
     * CAPA优先级
     */
    private Capa.PriorityLevel priorityLevel;

    /**
     * CAPA责任人ID
     */
    private Long responsiblePersonId;

    /**
     * 创建人ID
     */
    private Long createdById;

    /**
     * 创建日期范围-开始
     */
    private LocalDate createdAtFrom;

    /**
     * 创建日期范围-结束
     */
    private LocalDate createdAtTo;

    /**
     * 是否查询逾期CAPA
     */
    private boolean overdueOnly;

    /**
     * CAPA来源类型
     */
    private String sourceType;

    /**
     * CAPA来源ID
     */
    private Long sourceId;
}
