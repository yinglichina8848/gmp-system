package com.gmp.qms.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 批次信息数据传输对象，用于系统间批次数据交换
 * 
 * @author GMP系统开发团队
 */
@Data
public class BatchInfoDTO {
    
    /**
     * 批次ID
     */
    private String id;
    
    /**
     * 批次号
     */
    private String batchNumber;
    
    /**
     * 产品代码
     */
    private String productCode;
    
    /**
     * 产品名称
     */
    private String productName;
    
    /**
     * 生产数量
     */
    private Double quantity;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 生产状态
     */
    private String status;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 生产线
     */
    private String productionLine;
    
    /**
     * 操作员ID
     */
    private String operatorId;
    
    /**
     * 操作员姓名
     */
    private String operatorName;
    
    /**
     * 质量状态
     */
    private String qualityStatus;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 额外属性
     */
    private Map<String, Object> extraAttributes;
}
