package com.gmp.qms.dto;

import lombok.Data;

import java.util.Date;

/**
 * 培训记录数据传输对象，用于系统间培训数据交换
 * 
 * @author GMP系统开发团队
 */
@Data
public class TrainingRecordDTO {
    
    /**
     * 培训记录ID
     */
    private String id;
    
    /**
     * 员工ID
     */
    private String employeeId;
    
    /**
     * 员工姓名
     */
    private String employeeName;
    
    /**
     * 培训代码
     */
    private String trainingCode;
    
    /**
     * 培训名称
     */
    private String trainingName;
    
    /**
     * 培训类型
     */
    private String trainingType;
    
    /**
     * 培训状态
     */
    private String status;
    
    /**
     * 培训开始时间
     */
    private Date startTime;
    
    /**
     * 培训结束时间
     */
    private Date endTime;
    
    /**
     * 培训成绩/评估结果
     */
    private String result;
    
    /**
     * 培训师ID
     */
    private String trainerId;
    
    /**
     * 培训师姓名
     */
    private String trainerName;
    
    /**
     * 有效期至
     */
    private Date validUntil;
    
    /**
     * 是否需要重新培训
     */
    private boolean needRetraining;
}
