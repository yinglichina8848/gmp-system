package com.gmp.equipment.entity;

/**
 * 维护计划状态枚举
 * 定义维护计划的各种状态
 */
public enum MaintenancePlanStatus {
    
    /**
     * 待执行 - 维护计划已创建但尚未执行
     */
    PENDING("待执行"),
    
    /**
     * 执行中 - 维护计划正在执行
     */
    IN_PROGRESS("执行中"),
    
    /**
     * 已完成 - 维护计划已完成
     */
    COMPLETED("已完成"),
    
    /**
     * 已取消 - 维护计划已取消
     */
    CANCELLED("已取消"),
    
    /**
     * 已逾期 - 维护计划已超过预定执行时间
     */
    OVERDUE("已逾期");
    
    private final String description;
    
    /**
     * 构造函数
     * @param description 状态描述
     */
    MaintenancePlanStatus(String description) {
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
    public static MaintenancePlanStatus fromDescription(String description) {
        for (MaintenancePlanStatus status : MaintenancePlanStatus.values()) {
            if (status.getDescription().equals(description)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}