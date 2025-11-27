package com.gmp.equipment.dto;

import lombok.Data;

/**
 * 电子签名请求数据传输对象
 * 用于符合21 CFR Part 11要求的电子签名操作
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-07-01
 */
@Data
public class ElectronicSignatureRequestDTO {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 操作原因
     */
    private String reason;
    
    /**
     * 操作描述
     */
    private String actionDescription;
    
    /**
     * 关联的记录ID
     */
    private Long recordId;
    
    /**
     * 记录类型
     * - CALIBRATION: 校准记录
     * - MAINTENANCE: 维护记录
     * - EQUIPMENT_STATUS: 设备状态变更
     */
    private String recordType;
}
