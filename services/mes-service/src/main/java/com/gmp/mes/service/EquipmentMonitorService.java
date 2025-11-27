package com.gmp.mes.service;

import com.gmp.mes.entity.EquipmentMonitor;
import com.gmp.mes.entity.EquipmentStatusRecord;
import com.gmp.mes.repository.EquipmentMonitorRepository;
import com.gmp.mes.repository.EquipmentStatusRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 设备监控服务 - 实现设备监控和状态管理的核心业务逻辑
 * 
 * @author gmp-system
 */
@Service
public class EquipmentMonitorService {

    @Autowired
    private EquipmentMonitorRepository equipmentMonitorRepository;

    @Autowired
    private EquipmentStatusRecordRepository equipmentStatusRecordRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建设备监控记录
     * 
     * @param equipment 设备监控对象
     * @return 创建的设备监控记录
     */
    @Transactional
    public EquipmentMonitor createEquipment(EquipmentMonitor equipment) {
        return equipmentMonitorRepository.save(equipment);
    }

    /**
     * 根据ID获取设备
     * 
     * @param id 设备ID
     * @return 设备对象
     */
    public Optional<EquipmentMonitor> getEquipmentById(Long id) {
        return equipmentMonitorRepository.findById(id);
    }

    /**
     * 根据设备编码获取设备
     * 
     * @param equipmentCode 设备编码
     * @return 设备对象
     */
    public Optional<EquipmentMonitor> getEquipmentByCode(String equipmentCode) {
        // 尝试从Redis缓存获取
        String cacheKey = "equipment:" + equipmentCode;
        EquipmentMonitor equipment = (EquipmentMonitor) redisTemplate.opsForValue().get(cacheKey);
        
        if (equipment == null) {
            // 缓存未命中，从数据库查询
            Optional<EquipmentMonitor> optionalEquipment = equipmentMonitorRepository.findByEquipmentCode(equipmentCode);
            if (optionalEquipment.isPresent()) {
                equipment = optionalEquipment.get();
                // 更新缓存
                redisTemplate.opsForValue().set(cacheKey, equipment);
            }
        }
        
        return Optional.ofNullable(equipment);
    }

    /**
     * 获取所有设备
     * 
     * @return 设备列表
     */
    public List<EquipmentMonitor> getAllEquipment() {
        return equipmentMonitorRepository.findAll();
    }

    /**
     * 根据状态获取设备
     * 
     * @param status 设备状态
     * @return 设备列表
     */
    public List<EquipmentMonitor> getEquipmentByStatus(EquipmentMonitor.EquipmentStatus status) {
        return equipmentMonitorRepository.findByStatus(status);
    }

    /**
     * 更新设备信息
     * 
     * @param equipment 设备对象
     * @return 更新后的设备
     */
    @Transactional
    public EquipmentMonitor updateEquipment(EquipmentMonitor equipment) {
        // 更新数据库
        EquipmentMonitor updatedEquipment = equipmentMonitorRepository.save(equipment);
        
        // 更新缓存
        String cacheKey = "equipment:" + equipment.getEquipmentCode();
        redisTemplate.opsForValue().set(cacheKey, updatedEquipment);
        
        return updatedEquipment;
    }

    /**
     * 更新设备状态
     * 
     * @param equipmentCode 设备编码
     * @param status 新状态
     * @param operator 操作人员
     * @return 更新后的设备
     */
    @Transactional
    public EquipmentMonitor updateEquipmentStatus(String equipmentCode, EquipmentMonitor.EquipmentStatus status, String operator) {
        Optional<EquipmentMonitor> optionalEquipment = equipmentMonitorRepository.findByEquipmentCode(equipmentCode);
        if (optionalEquipment.isPresent()) {
            EquipmentMonitor equipment = optionalEquipment.get();
            EquipmentMonitor.EquipmentStatus oldStatus = equipment.getStatus();
            
            // 如果状态发生变化，记录状态变更
            if (oldStatus != status) {
                equipment.setStatus(status);
                EquipmentMonitor updatedEquipment = equipmentMonitorRepository.save(equipment);
                
                // 记录状态变更历史
                recordStatusChange(equipmentCode, status, operator);
                
                // 更新缓存
                String cacheKey = "equipment:" + equipmentCode;
                redisTemplate.opsForValue().set(cacheKey, updatedEquipment);
                
                return updatedEquipment;
            }
            return equipment;
        } else {
            throw new IllegalArgumentException("Equipment not found: " + equipmentCode);
        }
    }

    /**
     * 更新设备运行参数
     * 
     * @param equipmentCode 设备编码
     * @param temperature 温度
     * @param pressure 压力
     * @param humidity 湿度
     * @param vibration 振动
     * @return 更新后的设备
     */
    @Transactional
    public EquipmentMonitor updateEquipmentParameters(String equipmentCode, Double temperature, Double pressure, Double humidity, Double vibration) {
        Optional<EquipmentMonitor> optionalEquipment = equipmentMonitorRepository.findByEquipmentCode(equipmentCode);
        if (optionalEquipment.isPresent()) {
            EquipmentMonitor equipment = optionalEquipment.get();
            
            // 更新参数
            if (temperature != null) equipment.setTemperature(temperature);
            if (pressure != null) equipment.setPressure(pressure);
            if (humidity != null) equipment.setHumidity(humidity);
            if (vibration != null) equipment.setVibration(vibration);
            
            // 检查是否需要更新状态（如温度超过阈值）
            checkAndUpdateStatusForThresholds(equipment);
            
            EquipmentMonitor updatedEquipment = equipmentMonitorRepository.save(equipment);
            
            // 更新缓存
            String cacheKey = "equipment:" + equipmentCode;
            redisTemplate.opsForValue().set(cacheKey, updatedEquipment);
            
            return updatedEquipment;
        } else {
            throw new IllegalArgumentException("Equipment not found: " + equipmentCode);
        }
    }

    /**
     * 获取设备状态记录
     * 
     * @param equipmentCode 设备编码
     * @param limit 记录数量
     * @return 状态记录列表
     */
    public List<EquipmentStatusRecord> getEquipmentStatusRecords(String equipmentCode, int limit) {
        return equipmentStatusRecordRepository.findByEquipmentCodeOrderByTimestampDesc(equipmentCode)
                .stream()
                .limit(limit)
                .toList();
    }

    /**
     * 删除设备
     * 
     * @param id 设备ID
     */
    @Transactional
    public void deleteEquipment(Long id) {
        Optional<EquipmentMonitor> optionalEquipment = equipmentMonitorRepository.findById(id);
        if (optionalEquipment.isPresent()) {
            // 删除缓存
            String cacheKey = "equipment:" + optionalEquipment.get().getEquipmentCode();
            redisTemplate.delete(cacheKey);
            
            // 删除设备
            equipmentMonitorRepository.deleteById(id);
        }
    }

    /**
     * 检查并根据阈值更新设备状态
     * 
     * @param equipment 设备对象
     */
    private void checkAndUpdateStatusForThresholds(EquipmentMonitor equipment) {
        boolean isError = false;
        String errorReason = "";
        
        // 检查温度阈值
        if (equipment.getTemperatureThreshold() != null && equipment.getTemperature() != null && 
            equipment.getTemperature() > equipment.getTemperatureThreshold()) {
            isError = true;
            errorReason = "Temperature exceeded threshold";
        }
        
        // 检查压力阈值
        if (!isError && equipment.getPressureThreshold() != null && equipment.getPressure() != null && 
            equipment.getPressure() > equipment.getPressureThreshold()) {
            isError = true;
            errorReason = "Pressure exceeded threshold";
        }
        
        // 检查振动阈值
        if (!isError && equipment.getVibrationThreshold() != null && equipment.getVibration() != null && 
            equipment.getVibration() > equipment.getVibrationThreshold()) {
            isError = true;
            errorReason = "Vibration exceeded threshold";
        }
        
        // 如果有错误，则更新状态
        if (isError && equipment.getStatus() != EquipmentMonitor.EquipmentStatus.ERROR) {
            equipment.setStatus(EquipmentMonitor.EquipmentStatus.ERROR);
            // 记录状态变更
            recordStatusChange(equipment.getEquipmentCode(), EquipmentMonitor.EquipmentStatus.ERROR, "SYSTEM");
        } else if (!isError && equipment.getStatus() == EquipmentMonitor.EquipmentStatus.ERROR &&
                   equipment.getStatus() != EquipmentMonitor.EquipmentStatus.MAINTENANCE) {
            // 如果错误恢复且不是维护状态，则恢复到在线状态
            equipment.setStatus(EquipmentMonitor.EquipmentStatus.ONLINE);
            // 记录状态变更
            recordStatusChange(equipment.getEquipmentCode(), EquipmentMonitor.EquipmentStatus.ONLINE, "SYSTEM");
        }
    }

    /**
     * 记录设备状态变更
     * 
     * @param equipmentCode 设备编码
     * @param status 新状态
     * @param operator 操作人员
     */
    private void recordStatusChange(String equipmentCode, EquipmentMonitor.EquipmentStatus status, String operator) {
        EquipmentStatusRecord record = new EquipmentStatusRecord();
        record.setEquipmentCode(equipmentCode);
        record.setStatus(convertStatus(status));
        record.setOperator(operator);
        
        // 设置状态变更时的设备指标
            Optional<EquipmentMonitor> optionalEquipment = equipmentMonitorRepository.findByEquipmentCode(equipmentCode);
            if (optionalEquipment.isPresent()) {
                EquipmentMonitor equipment = optionalEquipment.get();
                record.setTemperature(equipment.getTemperature());
                record.setPressure(equipment.getPressure());
                record.setHumidity(equipment.getHumidity());
                // 进行类型转换：Double -> Integer
                if (equipment.getVibration() != null) {
                    record.setVibration(equipment.getVibration().intValue());
                }
            }
        
        equipmentStatusRecordRepository.save(record);
    }

    /**
     * 转换状态枚举，从EquipmentMonitor.Status转换为EquipmentStatusRecord.Status
     * 
     * @param status 设备监控状态
     * @return 设备状态记录状态
     */
    private EquipmentStatusRecord.EquipmentStatus convertStatus(EquipmentMonitor.EquipmentStatus status) {
        switch (status) {
            case ONLINE: return EquipmentStatusRecord.EquipmentStatus.ONLINE;
            case OFFLINE: return EquipmentStatusRecord.EquipmentStatus.OFFLINE;
            case MAINTENANCE: return EquipmentStatusRecord.EquipmentStatus.MAINTENANCE;
            case ERROR: return EquipmentStatusRecord.EquipmentStatus.ERROR;
            case IDLE: return EquipmentStatusRecord.EquipmentStatus.IDLE;
            default: return EquipmentStatusRecord.EquipmentStatus.OFFLINE;
        }
    }
}