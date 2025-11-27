package com.gmp.qms.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * CAPA数据传输对象
 * 
 * @author GMP系统开发团队
 */
@Data
public class CapaDTO {

    /**
     * CAPA标题
     */
    private String title;

    /**
     * CAPA描述
     */
    private String description;

    /**
     * CAPA优先级
     */
    private String priorityLevel;

    /**
     * CAPA责任人ID
     */
    private Long responsiblePersonId;

    /**
     * 目标完成日期
     */
    private LocalDate targetCompletionDate;

    /**
     * CAPA来源类型
     */
    private String sourceType;

    /**
     * CAPA来源ID
     */
    private Long sourceId;

    /**
     * 问题描述
     */
    private String problemDescription;

    /**
     * 根本原因分析
     */
    private String rootCauseAnalysis;

    /**
     * 纠正措施
     */
    private String correctiveAction;

    /**
     * 预防措施
     */
    private String preventiveAction;

    /**
     * 验证方法
     */
    private String verificationMethod;
}
