package com.gmp.mes.controller;

import com.gmp.mes.entity.EquipmentMonitor;
import com.gmp.mes.entity.EquipmentStatusRecord;
import com.gmp.mes.service.EquipmentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 设备监控控制器 - 提供设备监控相关的RESTful API接口
 * 
 * @author gmp-system
 */
@RestController
@RequestMapping("/api/equipment")
public class EquipmentMonitorController {

    @Autowired
    private EquipmentMonitorService equipmentMonitorService;

    /**
     * 创建设备
     * 
     * @param equipment 设备信息
     * @return 创建的设备
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<EquipmentMonitor> createEquipment(@RequestBody EquipmentMonitor equipment) {
        EquipmentMonitor createdEquipment = equipmentMonitorService.createEquipment(equipment);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    /**
     * 获取所有设备
     * 
     * @return 设备列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<List<EquipmentMonitor>> getAllEquipment() {
        List<EquipmentMonitor> equipmentList = equipmentMonitorService.getAllEquipment();
        return ResponseEntity.ok(equipmentList);
    }

    /**
     * 根据ID获取设备
     * 
     * @param id 设备ID
     * @return 设备信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<EquipmentMonitor> getEquipmentById(@PathVariable Long id) {
        Optional<EquipmentMonitor> equipment = equipmentMonitorService.getEquipmentById(id);
        return equipment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据设备编码获取设备
     * 
     * @param code 设备编码
     * @return 设备信息
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<EquipmentMonitor> getEquipmentByCode(@PathVariable String code) {
        Optional<EquipmentMonitor> equipment = equipmentMonitorService.getEquipmentByCode(code);
        return equipment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据状态获取设备
     * 
     * @param status 设备状态
     * @return 设备列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<List<EquipmentMonitor>> getEquipmentByStatus(@PathVariable EquipmentMonitor.EquipmentStatus status) {
        List<EquipmentMonitor> equipmentList = equipmentMonitorService.getEquipmentByStatus(status);
        return ResponseEntity.ok(equipmentList);
    }

    /**
     * 更新设备信息
     * 
     * @param id 设备ID
     * @param equipment 设备信息
     * @return 更新后的设备
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<EquipmentMonitor> updateEquipment(@PathVariable Long id, @RequestBody EquipmentMonitor equipment) {
        // 确保ID一致
        if (!equipment.getId().equals(id)) {
            equipment.setId(id);
        }
        try {
            EquipmentMonitor updatedEquipment = equipmentMonitorService.updateEquipment(equipment);
            return ResponseEntity.ok(updatedEquipment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 更新设备状态
     * 
     * @param code 设备编码
     * @param request 更新请求，包含状态和操作员
     * @return 更新后的设备
     */
    @PutMapping("/code/{code}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('MAINTENANCE')")
    public ResponseEntity<EquipmentMonitor> updateEquipmentStatus(@PathVariable String code, @RequestBody Map<String, Object> request) {
        try {
            EquipmentMonitor.EquipmentStatus status = EquipmentMonitor.EquipmentStatus.valueOf((String) request.get("status"));
            String operator = (String) request.get("operator");
            EquipmentMonitor updatedEquipment = equipmentMonitorService.updateEquipmentStatus(code, status, operator);
            return ResponseEntity.ok(updatedEquipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 更新设备运行参数
     * 
     * @param code 设备编码
     * @param request 参数更新请求
     * @return 更新后的设备
     */
    @PutMapping("/code/{code}/parameters")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<EquipmentMonitor> updateEquipmentParameters(@PathVariable String code, @RequestBody Map<String, Object> request) {
        try {
            Double temperature = request.containsKey("temperature") ? (Double) request.get("temperature") : null;
            Double pressure = request.containsKey("pressure") ? (Double) request.get("pressure") : null;
            Double humidity = request.containsKey("humidity") ? (Double) request.get("humidity") : null;
            Double vibration = request.containsKey("vibration") ? ((Number) request.get("vibration")).doubleValue() : null;
            
            EquipmentMonitor updatedEquipment = equipmentMonitorService.updateEquipmentParameters(code, temperature, pressure, humidity, vibration);
            return ResponseEntity.ok(updatedEquipment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取设备状态记录
     * 
     * @param code 设备编码
     * @param limit 记录数量限制
     * @return 状态记录列表
     */
    @GetMapping("/code/{code}/status-records")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('MAINTENANCE')")
    public ResponseEntity<List<EquipmentStatusRecord>> getEquipmentStatusRecords(@PathVariable String code, @RequestParam(defaultValue = "100") int limit) {
        List<EquipmentStatusRecord> records = equipmentMonitorService.getEquipmentStatusRecords(code, limit);
        return ResponseEntity.ok(records);
    }

    /**
     * 删除设备
     * 
     * @param id 设备ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        try {
            equipmentMonitorService.deleteEquipment(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}