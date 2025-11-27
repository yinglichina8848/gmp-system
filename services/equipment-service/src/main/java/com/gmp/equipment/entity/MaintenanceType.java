package com.gmp.equipment.entity;

/**
 * 维护类型枚举
 * 定义设备维护的各种类型
 */
public enum MaintenanceType {
    
    /**
     * 预防性维护 - 定期计划的维护活动
     */
    PREVENTIVE("预防性维护"),
    
    /**
     * 故障维修 - 设备发生故障后的维修活动
     */
    CORRECTIVE("故障维修"),
    
    /**
     * 预测性维护 - 基于设备状态监控的维护活动
     */
    PREDICTIVE("预测性维护"),
    
    /**
     * 计划性维护 - 有计划的停机维护
     */
    SCHEDULED("计划性维护"),
    
    /**
     * 紧急维护 - 需要立即处理的维护活动
     */
    EMERGENCY("紧急维护");
    
    private final String description;
    
    /**
     * 构造函数
     * @param description 类型描述
     */
    MaintenanceType(String description) {
        this.description = description;
    }
    
    /**
     * 获取类型描述
     * @return 类型描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据描述获取类型枚举值
     * @param description 类型描述
     * @return 对应的类型枚举值
     * @throws IllegalArgumentException 如果没有对应的类型
     */
    public static MaintenanceType fromDescription(String description) {
        for (MaintenanceType type : MaintenanceType.values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}