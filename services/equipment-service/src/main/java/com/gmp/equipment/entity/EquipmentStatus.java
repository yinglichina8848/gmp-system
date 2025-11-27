package com.gmp.equipment.entity;

/**
 * 设备状态枚举
 * 定义设备的各种状态，用于设备生命周期管理
 */
public enum EquipmentStatus {
    
    /**
     * 待验收 - 新购设备尚未验收
     */
    PENDING_ACCEPTANCE("待验收"),
    
    /**
     * 正常 - 设备处于正常使用状态
     */
    NORMAL("正常"),
    
    /**
     * 维护中 - 设备正在进行日常维护
     */
    UNDER_MAINTENANCE("维护中"),
    
    /**
     * 校准中 - 设备正在进行校准
     */
    UNDER_CALIBRATION("校准中"),
    
    /**
     * 故障 - 设备出现故障
     */
    FAULT("故障"),
    
    /**
     * 停用 - 设备暂时停用
     */
    DISABLED("停用"),
    
    /**
     * 报废 - 设备已报废
     */
    SCRAPPED("报废");
    
    private final String description;
    
    /**
     * 构造函数
     * @param description 状态描述
     */
    EquipmentStatus(String description) {
        this.description = description;
    }
    
    /**
     * 获取状态描述
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据描述获取状态枚举值
     * @param description 状态描述
     * @return 对应的状态枚举值
     * @throws IllegalArgumentException 如果没有对应的状态
     */
    public static EquipmentStatus fromDescription(String description) {
        for (EquipmentStatus status : EquipmentStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}