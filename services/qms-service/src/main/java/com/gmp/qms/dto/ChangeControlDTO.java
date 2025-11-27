package com.gmp.qms.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * 变更控制数据传输对象
 * 
 * @author GMP系统开发团队
 */
@Data
public class ChangeControlDTO {

    /**
     * 变更标题
     */
    private String title;

    /**
     * 变更描述
     */
    private String description;

    /**
     * 变更类型
     */
    private String changeType;

    /**
     * 风险级别
     */
    private String riskLevel;

    /**
     * 变更负责人ID
     */
    private Long changeOwnerId;

    /**
     * 计划实施日期
     */
    private LocalDate plannedImplementationDate;

    /**
     * 计划完成日期
     */
    private LocalDate plannedCompletionDate;

    /**
     * 变更原因
     */
    private String changeReason;

    /**
     * 变更内容
     */
    private String changeContent;

    /**
     * 受影响的系统/产品
     */
    private String affectedSystems;

    /**
     * 变更验证方法
     */
    private String validationMethod;

    /**
     * 变更审批人ID列表
     */
    private Long[] approverIds;
}
