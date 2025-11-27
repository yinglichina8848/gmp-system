package com.gmp.mes.repository;

import com.gmp.mes.entity.EquipmentMonitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 设备监控数据访问接口
 * 
 * @author gmp-system
 */
@Repository
public interface EquipmentMonitorRepository extends JpaRepository<EquipmentMonitor, Long> {

    /**
     * 根据设备编码查询设备
     * 
     * @param equipmentCode 设备编码
     * @return 设备监控对象
     */
    Optional<EquipmentMonitor> findByEquipmentCode(String equipmentCode);

    /**
     * 根据设备类型查询设备列表
     * 
     * @param equipmentType 设备类型
     * @return 设备列表
     */
    List<EquipmentMonitor> findByEquipmentType(String equipmentType);

    /**
     * 根据设备状态查询设备列表
     * 
     * @param status 设备状态
     * @return 设备列表
     */
    List<EquipmentMonitor> findByStatus(EquipmentMonitor.EquipmentStatus status);

    /**
     * 根据位置查询设备列表
     * 
     * @param location 设备位置
     * @return 设备列表
     */
    List<EquipmentMonitor> findByLocation(String location);

    /**
     * 查询需要维护的设备（下一次维护日期在指定时间之前）
     * 
     * @param date 截止日期
     * @return 需要维护的设备列表
     */
    List<EquipmentMonitor> findByNextMaintenanceBefore(LocalDateTime date);

    /**
     * 查询温度超过阈值的设备
     * 
     * @return 温度异常设备列表
     */
    @Query("SELECT e FROM EquipmentMonitor e WHERE e.temperature > e.temperatureThreshold")
    List<EquipmentMonitor> findByTemperatureExceedsThreshold();

    /**
     * 批量更新设备状态
     * 
     * @param status 新状态
     * @param equipmentCodes 设备编码列表
     * @return 更新的记录数
     */
    @Modifying
    @Query("UPDATE EquipmentMonitor e SET e.status = :status WHERE e.equipmentCode IN :codes")
    int updateStatus(@Param("status") EquipmentMonitor.EquipmentStatus status, @Param("codes") List<String> equipmentCodes);

    /**
     * 统计各状态的设备数量
     * 
     * @param status 设备状态
     * @return 设备数量
     */
    long countByStatus(EquipmentMonitor.EquipmentStatus status);
}