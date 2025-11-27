package com.gmp.qms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 偏差数据传输对象
 * 
 * @author GMP系统开发团队
 */
@Data
public class DeviationDTO {

    /**
     * 偏差标题
     */
    private String title;

    /**
     * 偏差描述
     */
    private String description;

    /**
     * 偏差发生地点
     */
    private String location;

    /**
     * 偏差发生日期
     */
    private LocalDate occurrenceDate;

    /**
     * 偏差严重程度
     */
    private String severityLevel;

    /**
     * 偏差责任人ID
     */
    private Long responsiblePersonId;

    /**
     * 目标完成日期
     */
    private LocalDate targetCompletionDate;

    /**
     * 偏差来源类型
     */
    private String sourceType;

    /**
     * 偏差来源ID
     */
    private Long sourceId;

    /**
     * 受影响产品/流程
     */
    private String affectedProducts;

    /**
     * 初步评估
     */
    private String initialAssessment;
}
