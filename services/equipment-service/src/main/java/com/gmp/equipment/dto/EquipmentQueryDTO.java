package com.gmp.equipment.dto;

import lombok.Data;
import java.util.Date;

/**
 * 设备查询DTO类
 * 用于设备查询参数的数据传输
 */
@Data
public class EquipmentQueryDTO {

    /**
     * 设备编码（模糊查询）
     */
    private String code;

    /**
     * 设备名称（模糊查询）
     */
    private String name;

    /**
     * 设备型号（模糊查询）
     */
    private String model;

    /**
     * 设备类型ID
     */
    private Long equipmentTypeId;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 放置位置（模糊查询）
     */
    private String location;

    /**
     * 负责人（模糊查询）
     */
    private String responsiblePerson;

    /**
     * 供应商（模糊查询）
     */
    private String supplier;

    /**
     * 购买日期开始
     */
    private Date purchaseDateStart;

    /**
     * 购买日期结束
     */
    private Date purchaseDateEnd;

    /**
     * 启用日期开始
     */
    private Date commissioningDateStart;

    /**
     * 启用日期结束
     */
    private Date commissioningDateEnd;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 页码，从1开始
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;
}