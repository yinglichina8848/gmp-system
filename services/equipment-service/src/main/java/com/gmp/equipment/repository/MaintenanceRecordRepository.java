package com.gmp.equipment.repository;

import com.gmp.equipment.entity.MaintenanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 设备维护记录仓库接口
 * 提供维护记录相关的数据访问方法
 */
@Repository
public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long>, JpaSpecificationExecutor<MaintenanceRecord> {

    /**
     * 根据维护计划ID查询维护记录列表
     * @param maintenancePlanId 维护计划ID
     * @return 维护记录列表
     */
    List<MaintenanceRecord> findByMaintenancePlanId(Long maintenancePlanId);

    /**
     * 根据设备ID查询维护记录列表
     * @param equipmentId 设备ID
     * @return 维护记录列表
     */
    List<MaintenanceRecord> findByEquipmentId(Long equipmentId);

    /**
     * 根据设备ID查询最新的维护记录
     * @param equipmentId 设备ID
     * @return 最新的维护记录
     */
    MaintenanceRecord findFirstByEquipmentIdOrderByMaintenanceEndTimeDesc(Long equipmentId);

    /**
     * 根据维护类型查询维护记录列表
     * @param maintenanceType 维护类型
     * @return 维护记录列表
     */
    List<MaintenanceRecord> findByMaintenanceType(String maintenanceType);

    /**
     * 根据维护人员查询维护记录列表
     * @param maintenancePerson 维护人员
     * @return 维护记录列表
     */
    List<MaintenanceRecord> findByMaintenancePerson(String maintenancePerson);

    /**
     * 根据维护开始时间范围查询维护记录列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 维护记录列表
     */
    List<MaintenanceRecord> findByMaintenanceStartTimeBetween(Date startDate, Date endDate);

    /**
     * 根据设备ID和维护类型查询维护记录列表
     * @param equipmentId 设备ID
     * @param maintenanceType 维护类型
     * @return 维护记录列表
     */
    List<MaintenanceRecord> findByEquipmentIdAndMaintenanceType(Long equipmentId, String maintenanceType);

    /**
     * 根据设备ID和维护时间范围查询维护记录列表
     * @param equipmentId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 维护记录列表
     */
    List<MaintenanceRecord> findByEquipmentIdAndMaintenanceStartTimeBetween(Long equipmentId, Date startDate, Date endDate);
}