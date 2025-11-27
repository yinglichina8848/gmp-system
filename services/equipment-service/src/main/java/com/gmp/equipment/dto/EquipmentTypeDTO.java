package com.gmp.equipment.dto;

import lombok.Data;
import java.util.Date;

/**
 * 设备类型DTO类
 * 用于设备类型信息的数据传输
 */
@Data
public class EquipmentTypeDTO {

    /**
     * 设备类型ID
     */
    private Long id;

    /**
     * 设备类型编码
     */
    private String code;

    /**
     * 设备类型名称
     */
    private String name;

    /**
     * 设备类型描述
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