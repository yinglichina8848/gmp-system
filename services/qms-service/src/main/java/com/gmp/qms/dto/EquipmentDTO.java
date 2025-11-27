package com.gmp.qms.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 设备数据传输对象，用于系统间设备数据交换
 * 
 * @author GMP系统开发团队
 */
@Data
public class EquipmentDTO {
    
    /**
     * 设备ID
     */
    private String id;
    
    /**
     * 设备名称
     */
    private String name;
    
    /**
     * 设备编号
     */
    private String code;
    
    /**
     * 设备类型
     */
    private String type;
    
    /**
     * 设备状态
     */
    private String status;
    
    /**
     * 设备位置
     */
    private String location;
    
    /**
     * 制造商
     */
    private String manufacturer;
    
    /**
     * 型号
     */
    private String model;
    
    /**
     * 序列号
     */
    private String serialNumber;
    
    /**
     * 安装日期
     */
    private Date installationDate;
    
    /**
     * 下次维护日期
     */
    private Date nextMaintenanceDate;
    
    /**
     * 下次校准日期
     */
    private Date nextCalibrationDate;
    
    /**
     * 设备负责人ID
     */
    private String responsiblePersonId;
    
    /**
     * 设备负责人姓名
     */
    private String responsiblePersonName;
    
    /**
     * 额外属性
     */
    private Map<String, Object> extraAttributes;
}
