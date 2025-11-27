package com.gmp.qms.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 维护记录数据传输对象，用于系统间维护和校准数据交换
 * 
 * @author GMP系统开发团队
 */
@Data
public class MaintenanceRecordDTO {
    
    /**
     * 记录ID
     */
    private String id;
    
    /**
     * 设备ID
     */
    private String equipmentId;
    
    /**
     * 设备名称
     */
    private String equipmentName;
    
    /**
     * 维护/校准类型
     */
    private String type;
    
    /**
     * 维护/校准编号
     */
    private String code;
    
    /**
     * 开始时间
     */
    private Date startTime;
    
    /**
     * 结束时间
     */
    private Date endTime;
    
    /**
     * 执行人员ID
     */
    private String executorId;
    
    /**
     * 执行人员姓名
     */
    private String executorName;
    
    /**
     * 结果
     */
    private String result;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 问题描述
     */
    private String problemDescription;
    
    /**
     * 采取的措施
     */
    private String actionsTaken;
    
    /**
     * 下次维护/校准日期
     */
    private Date nextScheduleDate;
    
    /**
     * 相关文档ID列表
     */
    private List<String> documentIds;
    
    /**
     * 额外属性
     */
    private Map<String, Object> extraAttributes;
}
