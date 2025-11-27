package com.gmp.equipment.entity;

/**
 * 维护周期类型枚举
 * 定义设备维护的周期类型
 */
public enum MaintenanceCycleType {
    
    /**
     * 每日维护
     */
    DAILY("每日"),
    
    /**
     * 每周维护
     */
    WEEKLY("每周"),
    
    /**
     * 每月维护
     */
    MONTHLY("每月"),
    
    /**
     * 每季度维护
     */
    QUARTERLY("每季度"),
    
    /**
     * 每半年维护
     */
    BIANNUALLY("每半年"),
    
    /**
     * 每年维护
     */
    ANNUALLY("每年"),
    
    /**
     * 不定期维护
     */
    IRREGULAR("不定期");
    
    private final String description;
    
    /**
     * 构造函数
     * @param description 周期描述
     */
    MaintenanceCycleType(String description) {
        this.description = description;
    }
    
    /**
     * 获取周期描述
     * @return 周期描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据描述获取周期类型枚举值
     * @param description 周期描述
     * @return 对应的周期类型枚举值
     * @throws IllegalArgumentException 如果没有对应的周期类型
     */
    public static MaintenanceCycleType fromDescription(String description) {
        for (MaintenanceCycleType type : MaintenanceCycleType.values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}