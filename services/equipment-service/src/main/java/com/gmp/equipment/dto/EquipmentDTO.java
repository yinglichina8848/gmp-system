package com.gmp.equipment.dto;

import lombok.Data;
import java.util.Date;

/**
 * 设备DTO类
 * 用于设备信息的数据传输
 */
@Data
public class EquipmentDTO {

    /**
     * 设备ID
     */
    private Long id;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备序列号
     */
    private String serialNumber;

    /**
     * 设备类型ID
     */
    private Long equipmentTypeId;

    /**
     * 设备类型编码
     */
    private String equipmentTypeCode;

    /**
     * 设备类型名称
     */
    private String equipmentTypeName;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 设备状态描述
     */
    private String statusDescription;

    /**
     * 放置位置
     */
    private String location;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 购买日期
     */
    private Date purchaseDate;

    /**
     * 验收日期
     */
    private Date acceptanceDate;

    /**
     * 启用日期
     */
    private Date commissioningDate;

    /**
     * 下次校准日期
     */
    private Date nextCalibrationDate;

    /**
     * 下次维护日期
     */
    private Date nextMaintenanceDate;

    /**
     * 设备描述
     */
    private String description;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;
}