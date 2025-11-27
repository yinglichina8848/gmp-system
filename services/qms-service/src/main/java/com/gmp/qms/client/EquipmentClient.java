package com.gmp.qms.client;

import com.gmp.qms.dto.EquipmentDTO;
import com.gmp.qms.dto.MaintenanceRecordDTO;
import com.gmp.qms.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理系统Feign客户端，定义与设备管理系统的通信接口
 * 
 * @author GMP系统开发团队
 */
@FeignClient(name = "equipment-service", url = "${feign.client.equipment.url}", fallback = EquipmentClientFallback.class)
public interface EquipmentClient {
    
    /**
     * 获取设备信息
     * 
     * @param equipmentId 设备ID
     * @return 设备信息
     */
    @GetMapping("/api/equipment/{equipmentId}")
    EquipmentDTO getEquipment(@PathVariable("equipmentId") String equipmentId);
    
    /**
     * 获取设备维护记录
     * 
     * @param equipmentId 设备ID
     * @return 维护记录列表
     */
    @GetMapping("/api/equipment/{equipmentId}/maintenance-records")
    List<MaintenanceRecordDTO> getMaintenanceRecords(@PathVariable("equipmentId") String equipmentId);
    
    /**
     * 发送通知到设备管理系统
     * 
     * @param notificationDTO 通知信息
     */
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDTO notificationDTO);
    
    /**
     * 获取设备校准记录
     * 
     * @param equipmentId 设备ID
     * @return 校准记录列表
     */
    @GetMapping("/api/equipment/{equipmentId}/calibration-records")
    List<MaintenanceRecordDTO> getCalibrationRecords(@PathVariable("equipmentId") String equipmentId);
    
    /**
     * 检查设备是否可用
     * 
     * @param equipmentId 设备ID
     * @return 设备是否可用
     */
    @GetMapping("/api/equipment/{equipmentId}/availability")
    boolean isEquipmentAvailable(@PathVariable("equipmentId") String equipmentId);
}
