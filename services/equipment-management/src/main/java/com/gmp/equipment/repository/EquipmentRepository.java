package com.gmp.equipment.repository;

// ============================================================================
//                        设备数据仓库接口
// dịp
//
// WHY: 在GMP设备管理系统中，需要通过多种查询维度来高效访问设备数据，
//      包括按状态、位置、类型等条件检索设备，支持分页和聚合查询，
//      为业务逻辑层提供统一的数据访问接口。
//
// WHAT: EquipmentRepository定义了设备的CRUD操作和各种业务查询方法，
//      包含标准查询、统计查询和特殊业务查询，为设备管理系统提供完整的数据访问能力。
//
// HOW: 继承JpaRepository获得基本CRUD能力，通过@Query注解自定义复杂查询，
//      使用Spring Data JPA命名方法约定优化查询开发效率，确保数据库访问的类型安全。
// ============================================================================

import com.gmp.equipment.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备数据仓库接口
 *
 * 提供设备管理的完整数据访问层功能：
 * - 基础CRUD操作（继承自JpaRepository）
 * - 设备状态查询
 * - 维护计划查询
 * - 统计聚合查询
 * - 业务规则验证方法
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    /**
     * 根据设备编码查找设备
     */
    Equipment findByEquipmentCode(String equipmentCode);

    /**
     * 检查设备编码是否存在
     */
    boolean existsByEquipmentCode(String equipmentCode);

    /**
     * 根据设备状态查询设备列表
     */
    List<Equipment> findByEquipmentStatus(Equipment.EquipmentStatus status);

    /**
     * 根据设备状态分页查询
     */
    Page<Equipment> findByEquipmentStatus(Equipment.EquipmentStatus status, Pageable pageable);

    /**
     * 根据位置查询设备
     */
    List<Equipment> findByLocation(String location);

    /**
     * 根据设备类型查询设备
     */
    List<Equipment> findByEquipmentType(String equipmentType);

    /**
     * 根据制造商查询设备
     */
    List<Equipment> findByManufacturer(String manufacturer);

    /**
     * 查询需要维护的设备（基于下次维护日期）
     */
    @Query("SELECT e FROM Equipment e WHERE e.nextMaintenanceDate <= :currentDate")
    List<Equipment> findEquipmentDueForMaintenance(@Param("currentDate") LocalDateTime currentDate);

    /**
     * 查询校准过期的设备
     */
    @Query("SELECT e FROM Equipment e WHERE e.nextCalibrationDate <= :currentDate AND e.equipmentStatus != 'DECOMMISSIONED'")
    List<Equipment> findCalibrationOverdueEquipment(@Param("currentDate") LocalDateTime currentDate);

    /**
     * 查询即将到期的校准设备（预警查询）
     */
    @Query("SELECT e FROM Equipment e WHERE e.nextCalibrationDate BETWEEN :currentDate AND :futureDate AND e.equipmentStatus = 'ACTIVE'")
    List<Equipment> findEquipmentDueForCalibration(@Param("currentDate") LocalDateTime currentDate,
            @Param("futureDate") LocalDateTime futureDate);

    /**
     * 查询质保期内的设备
     */
    @Query("SELECT e FROM Equipment e WHERE e.warrantyExpiry > :currentDate AND e.equipmentStatus = 'ACTIVE'")
    List<Equipment> findEquipmentUnderWarranty(@Param("currentDate") LocalDateTime currentDate);

    /**
     * 统计按类型分组的设备数量
     */
    @Query("SELECT e.equipmentType, COUNT(e) FROM Equipment e GROUP BY e.equipmentType")
    List<Object[]> countEquipmentByType();

    /**
     * 统计按状态分组的设备数量
     */
    @Query("SELECT e.equipmentStatus, COUNT(e) FROM Equipment e GROUP BY e.equipmentStatus")
    List<Object[]> countEquipmentByStatus();

    /**
     * 统计按运行状态分组的设备数量
     */
    @Query("SELECT e.operationalStatus, COUNT(e) FROM Equipment e GROUP BY e.operationalStatus")
    List<Object[]> countEquipmentByOperationalStatus();

    /**
     * 查找关键设备（GMP合规的关键设备）
     */
    @Query("SELECT e FROM Equipment e WHERE e.criticalEquipment = true AND e.equipmentStatus = 'ACTIVE'")
    List<Equipment> findCriticalEquipment();

    /**
     * 查询指定时间范围内安装的设备
     */
    @Query("SELECT e FROM Equipment e WHERE e.installationDate BETWEEN :startDate AND :endDate")
    List<Equipment> findEquipmentInstalledBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * 查询支持监控的设备
     */
    @Query("SELECT e FROM Equipment e WHERE e.monitoringEnabled = true AND e.equipmentStatus = 'ACTIVE'")
    List<Equipment> findMonitoredEquipment();

    /**
     * 复杂查询：按多个条件组合查询设备
     */
    @Query("""
            SELECT e FROM Equipment e
            WHERE (:equipmentCode IS NULL OR LOWER(e.equipmentCode) LIKE LOWER(CONCAT('%', :equipmentCode, '%')))
              AND (:equipmentName IS NULL OR LOWER(e.equipmentName) LIKE LOWER(CONCAT('%', :equipmentName, '%')))
              AND (:equipmentType IS NULL OR e.equipmentType = :equipmentType)
              AND (:manufacturer IS NULL OR LOWER(e.manufacturer) LIKE LOWER(CONCAT('%', :manufacturer, '%')))
              AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%')))
              AND (:equipmentStatus IS NULL OR e.equipmentStatus = :equipmentStatus)
              AND e.equipmentStatus != 'DECOMMISSIONED'
            """)
    Page<Equipment> findEquipmentWithFilters(@Param("equipmentCode") String equipmentCode,
            @Param("equipmentName") String equipmentName,
            @Param("equipmentType") String equipmentType,
            @Param("manufacturer") String manufacturer,
            @Param("location") String location,
            @Param("equipmentStatus") Equipment.EquipmentStatus equipmentStatus,
            Pageable pageable);

    /**
     * 通过传感器ID查询设备
     */
    Equipment findBySensorId(String sensorId);
}
