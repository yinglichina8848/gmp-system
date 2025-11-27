package com.gmp.qms.dto;

import com.gmp.qms.entity.ChangeControl;
import lombok.Data;

import java.time.LocalDate;

/**
 * 变更控制搜索条件
 * 
 * @author GMP系统开发团队
 */
@Data
public class ChangeControlSearchCriteria {

    /**
     * 变更编号或标题关键字
     */
    private String keyword;

    /**
     * 变更状态
     */
    private ChangeControl.ChangeStatus status;

    /**
     * 变更类型
     */
    private String changeType;

    /**
     * 风险级别
     */
    private ChangeControl.RiskLevel riskLevel;

    /**
     * 变更负责人ID
     */
    private Long changeOwnerId;

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
     * 计划实施日期范围-开始
     */
    private LocalDate plannedImplementationFrom;

    /**
     * 计划实施日期范围-结束
     */
    private LocalDate plannedImplementationTo;

    /**
     * 是否查询逾期变更控制
     */
    private boolean overdueOnly;
}
