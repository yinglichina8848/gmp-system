package com.gmp.equipment.entity;

/**
 * 校准结果枚举
 * 定义设备校准的各种结果状态
 */
public enum CalibrationResult {
    
    /**
     * 合格 - 校准结果符合要求
     */
    PASS("合格"),
    
    /**
     * 不合格 - 校准结果不符合要求
     */
    FAIL("不合格"),
    
    /**
     * 偏差 - 存在偏差但在允许范围内
     */
    DEVIATION("偏差"),
    
    /**
     * 待确认 - 校准结果需要进一步确认
     */
    PENDING_CONFIRMATION("待确认");
    
    private final String description;
    
    /**
     * 构造函数
     * @param description 结果描述
     */
    CalibrationResult(String description) {
        this.description = description;
    }
    
    /**
     * 获取结果描述
     * @return 结果描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据描述获取结果枚举值
     * @param description 结果描述
     * @return 对应的结果枚举值
     * @throws IllegalArgumentException 如果没有对应的结果
     */
    public static CalibrationResult fromDescription(String description) {
        for (CalibrationResult result : CalibrationResult.values()) {
            if (result.getDescription().equals(description)) {
                return result;
            }
        }
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}