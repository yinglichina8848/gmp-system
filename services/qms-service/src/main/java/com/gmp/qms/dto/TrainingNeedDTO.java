package com.gmp.qms.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 培训需求数据传输对象，用于系统间培训需求数据交换
 * 
 * @author GMP系统开发团队
 */
@Data
public class TrainingNeedDTO {
    
    /**
     * 培训需求ID
     */
    private String id;
    
    /**
     * 需求名称
     */
    private String name;
    
    /**
     * 需求描述
     */
    private String description;
    
    /**
     * 需求来源（如：偏差、CAPA、变更等）
     */
    private String source;
    
    /**
     * 来源ID
     */
    private String sourceId;
    
    /**
     * 培训类型
     */
    private String trainingType;
    
    /**
     * 目标员工ID列表
     */
    private List<String> targetEmployeeIds;
    
    /**
     * 优先级
     */
    private String priority;
    
    /**
     * 计划完成时间
     */
    private Date plannedCompletionTime;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 创建人ID
     */
    private String creatorId;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
