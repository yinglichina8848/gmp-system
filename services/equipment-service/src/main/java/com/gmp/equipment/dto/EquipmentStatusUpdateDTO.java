package com.gmp.equipment.dto;

import lombok.Data;

/**
 * 设备状态更新数据传输对象
 * 用于更新设备状态的API请求
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-07-01
 */
@Data
public class EquipmentStatusUpdateDTO {
    
    /**
     * 设备状态
     */
    private String status;
    
    /**
     * 更新原因
     */
    private String reason;
    
    /**
     * 操作人备注
     */
    private String remark;
    
    /**
     * 电子签名信息
     * 符合21 CFR Part 11要求的电子签名数据
     */
    private String electronicSignature;
}
