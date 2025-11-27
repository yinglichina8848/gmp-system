package com.gmp.mes.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 设备监控实体 - 用于跟踪设备状态和性能指标
 * 
 * @author gmp-system
 */
@Entity
@Table(name = "equipment_monitor")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentMonitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String equipmentCode;

    @Column(nullable = false)
    private String equipmentName;

    @Column(nullable = false)
    private String equipmentType;
    
    // 兼容测试的字段
    private Integer port; // 端口号

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status = EquipmentStatus.OFFLINE;

    private String location;
    private String ipAddress;
    private String firmwareVersion;
    private String manufacturer;
    private String model;

    // 设备指标
    private Double temperature;
    private Double pressure;
    private Double humidity;
    private Double vibration; // 修改为Double类型以匹配测试
    private Integer runtimeHours;
    private Integer cycles;
    private LocalDateTime lastUpdatedTime; // 添加测试所需字段

    // 告警阈值 - 使用测试期望的字段名
    private Double alertThresholdTemperature;
    private Double alertThresholdPressure;
    private Double alertThresholdHumidity; // 添加湿度阈值
    private Double alertThresholdVibration; // 修改为Double类型

    // 维护相关字段
    private String maintenanceSchedule;
    private LocalDateTime lastMaintenance;
    private LocalDateTime nextMaintenance;
    private LocalDateTime maintenanceDate; // 添加测试所需字段
    private String maintenancePerson; // 添加测试所需字段
    private String remark; // 添加备注字段

    // 审计字段
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 设备状态枚举
     */
    public enum EquipmentStatus {
        ONLINE, // 在线
        OFFLINE, // 离线
        MAINTENANCE, // 维护中
        ERROR, // 错误
        IDLE, // 空闲
        RUNNING // 运行中（兼容测试）
    }
    
    // 兼容测试的getter/setter方法
    public String getEquipmentId() {
        return equipmentCode;
    }
    
    public void setEquipmentId(String equipmentId) {
        this.equipmentCode = equipmentId;
    }
    
    public String getType() {
        return equipmentType;
    }
    
    public void setType(String type) {
        this.equipmentType = type;
    }
    
    public Double getTempThreshold() {
        return alertThresholdTemperature;
    }
    
    public void setTempThreshold(Double tempThreshold) {
        this.alertThresholdTemperature = tempThreshold;
    }
    
    public Double getPressThreshold() {
        return alertThresholdPressure;
    }
    
    public void setPressThreshold(Double pressThreshold) {
        this.alertThresholdPressure = pressThreshold;
    }
    
    public Double getHumidityThreshold() {
        return alertThresholdHumidity;
    }
    
    public void setHumidityThreshold(Double humidityThreshold) {
        this.alertThresholdHumidity = humidityThreshold;
    }
    
    public Double getVibrationThreshold() {
        return alertThresholdVibration;
    }
    
    public void setVibrationThreshold(Double vibrationThreshold) {
        this.alertThresholdVibration = vibrationThreshold;
    }

    // 业务方法 - 温度是否超出范围
    public boolean isTemperatureOutOfRange() {
        return temperature != null && alertThresholdTemperature != null &&
                temperature > alertThresholdTemperature;
    }

    // 业务方法 - 压力是否超出范围
    public boolean isPressureOutOfRange() {
        return pressure != null && alertThresholdPressure != null &&
                pressure > alertThresholdPressure;
    }

    // 业务方法 - 湿度是否超出范围
    public boolean isHumidityOutOfRange() {
        return humidity != null && alertThresholdHumidity != null &&
                humidity > alertThresholdHumidity;
    }

    // 业务方法 - 振动是否超出范围
    public boolean isVibrationOutOfRange() {
        return vibration != null && alertThresholdVibration != null &&
                vibration > alertThresholdVibration;
    }

    // 业务方法 - 是否有任何参数超出范围
    public boolean isAnyParameterOutOfRange() {
        return isTemperatureOutOfRange() || isPressureOutOfRange() ||
                isHumidityOutOfRange() || isVibrationOutOfRange();
    }

    // 为了兼容，添加getter和setter方法映射到原有的threshold字段
    public Double getTemperatureThreshold() {
        return alertThresholdTemperature;
    }

    public void setTemperatureThreshold(Double temperatureThreshold) {
        this.alertThresholdTemperature = temperatureThreshold;
    }

    public Double getPressureThreshold() {
        return alertThresholdPressure;
    }

    public void setPressureThreshold(Double pressureThreshold) {
        this.alertThresholdPressure = pressureThreshold;
    }

    public Double getVibrationThreshold() {
        return alertThresholdVibration;
    }

    public void setVibrationThreshold(Double vibrationThreshold) {
        this.alertThresholdVibration = vibrationThreshold;
    }
}