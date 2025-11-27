package com.gmp.qms.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 工艺参数数据传输对象，用于系统间工艺数据交换
 * 
 * @author GMP系统开发团队
 */
@Data
public class ProcessParameterDTO {
    
    /**
     * 参数记录ID
     */
    private String id;
    
    /**
     * 批次ID
     */
    private String batchId;
    
    /**
     * 批次号
     */
    private String batchNumber;
    
    /**
     * 工序代码
     */
    private String processCode;
    
    /**
     * 工序名称
     */
    private String processName;
    
    /**
     * 参数名称
     */
    private String parameterName;
    
    /**
     * 参数代码
     */
    private String parameterCode;
    
    /**
     * 参数值
     */
    private String parameterValue;
    
    /**
     * 参数类型
     */
    private String parameterType;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 标准下限
     */
    private String standardLowerLimit;
    
    /**
     * 标准上限
     */
    private String standardUpperLimit;
    
    /**
     * 是否在范围内
     */
    private boolean inRange;
    
    /**
     * 记录时间
     */
    private Date recordTime;
    
    /**
     * 记录人ID
     */
    private String recorderId;
    
    /**
     * 记录人姓名
     */
    private String recorderName;
    
    /**
     * 额外属性
     */
    private Map<String, Object> extraAttributes;
}
