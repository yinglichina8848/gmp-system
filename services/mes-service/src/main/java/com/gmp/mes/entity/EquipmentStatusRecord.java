package com.gmp.mes.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 设备状态记录实体 - 用于历史记录设备状态变化
 * 
 * @author gmp-system
 */
@Entity
@Table(name = "equipment_status_records")
@Data
@EntityListeners(AuditingEntityListener.class)
public class EquipmentStatusRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String equipmentCode;
    
    @Column(nullable = false)
    private String equipmentName; // 添加设备名称字段

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    // 状态变化时的设备指标
    private Double temperature;
    private Double pressure;
    private Double humidity;
    private Double vibration; // 修改为Double类型以兼容测试

    private String notes;
    private String operator;
    private String remark; // 添加备注字段

    // 记录创建时间即状态变化时间
    @CreatedDate
    private LocalDateTime timestamp;

    /**
     * 设备状态枚举 - 与EquipmentMonitor保持一致
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
    
    public LocalDateTime getRecordTime() {
        return timestamp;
    }
    
    public void setRecordTime(LocalDateTime recordTime) {
        this.timestamp = recordTime;
    }
}