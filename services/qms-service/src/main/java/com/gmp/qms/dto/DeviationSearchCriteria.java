package com.gmp.qms.dto;

import com.gmp.qms.entity.Deviation;
import lombok.Data;

import java.time.LocalDate;

/**
 * 偏差搜索条件
 * 
 * @author GMP系统开发团队
 */
@Data
public class DeviationSearchCriteria {

    /**
     * 偏差编号或标题关键字
     */
    private String keyword;

    /**
     * 偏差状态
     */
    private Deviation.DeviationStatus status;

    /**
     * 偏差严重程度
     */
    private Deviation.SeverityLevel severityLevel;

    /**
     * 偏差责任人ID
     */
    private Long responsiblePersonId;

    /**
     * 创建人ID
     */
    private Long createdById;

    /**
     * 发生日期范围-开始
     */
    private LocalDate occurrenceDateFrom;

    /**
     * 发生日期范围-结束
     */
    private LocalDate occurrenceDateTo;

    /**
     * 创建日期范围-开始
     */
    private LocalDate createdAtFrom;

    /**
     * 创建日期范围-结束
     */
    private LocalDate createdAtTo;

    /**
     * 是否查询逾期偏差
     */
    private boolean overdueOnly;

    /**
     * 偏差来源类型
     */
    private String sourceType;

    /**
     * 偏差来源ID
     */
    private Long sourceId;
}
