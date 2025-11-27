package com.gmp.equipment.dto;

import lombok.Data;
import java.util.Date;

/**
 * 设备维护计划DTO类
 * 用于维护计划信息的数据传输
 */
@Data
public class MaintenancePlanDTO {

    /**
     * 维护计划ID
     */
    private Long id;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 设备编码
     */
    private String equipmentCode;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 计划单号
     */
    private String planNumber;

    /**
     * 维护类型
     */
    private String maintenanceType;

    /**
     * 维护类型描述
     */
    private String maintenanceTypeDescription;

    /**
     * 周期类型
     */
    private String cycleType;

    /**
     * 周期类型描述
     */
    private String cycleTypeDescription;

    /**
     * 维护周期（天数）
     */
    private Integer maintenanceCycleDays;

    /**
     * 计划开始日期
     */
    private Date plannedStartDate;

    /**
     * 计划结束日期
     */
    private Date plannedEndDate;

    /**
     * 实际开始日期
     */
    private Date actualStartDate;

    /**
     * 实际结束日期
     */
    private Date actualEndDate;

    /**
     * 维护状态
     */
    private String status;

    /**
     * 维护状态描述
     */
    private String statusDescription;

    /**
     * 维护负责人
     */
    private String maintenancePerson;

    /**
     * 计划说明
     */
    private String remarks;

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