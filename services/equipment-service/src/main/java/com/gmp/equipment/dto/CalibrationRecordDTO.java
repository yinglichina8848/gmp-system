package com.gmp.equipment.dto;

import lombok.Data;
import java.util.Date;

/**
 * 设备校准记录DTO类
 * 用于校准记录信息的数据传输
 */
@Data
public class CalibrationRecordDTO {

    /**
     * 校准记录ID
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
     * 校准单号
     */
    private String calibrationNumber;

    /**
     * 校准日期
     */
    private Date calibrationDate;

    /**
     * 下次校准日期
     */
    private Date nextCalibrationDate;

    /**
     * 校准结果
     */
    private String calibrationResult;

    /**
     * 校准结果描述
     */
    private String calibrationResultDescription;

    /**
     * 校准机构
     */
    private String calibrationAgency;

    /**
     * 校准人员
     */
    private String calibrationPerson;

    /**
     * 校准证书编号
     */
    private String certificateNumber;

    /**
     * 校准周期（天）
     */
    private Integer calibrationCycleDays;

    /**
     * 校准说明
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