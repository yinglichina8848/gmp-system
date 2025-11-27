package com.gmp.equipment.dto;

import lombok.Data;
import java.util.Date;

/**
 * 设备维护记录DTO类
 * 用于维护记录信息的数据传输
 */
@Data
public class MaintenanceRecordDTO {

    /**
     * 维护记录ID
     */
    private Long id;

    /**
     * 维护计划ID
     */
    private Long maintenancePlanId;

    /**
     * 计划单号
     */
    private String planNumber;

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
     * 维护类型
     */
    private String maintenanceType;

    /**
     * 维护类型描述
     */
    private String maintenanceTypeDescription;

    /**
     * 维护开始时间
     */
    private Date maintenanceStartTime;

    /**
     * 维护结束时间
     */
    private Date maintenanceEndTime;

    /**
     * 维护人员
     */
    private String maintenancePerson;

    /**
     * 维护内容
     */
    private String maintenanceContent;

    /**
     * 维护结果
     */
    private String maintenanceResult;

    /**
     * 维护发现问题
     */
    private String issuesFound;

    /**
     * 维护处理措施
     */
    private String handlingMeasures;

    /**
     * 下次维护建议
     */
    private String nextMaintenanceSuggestion;

    /**
     * 维护记录说明
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