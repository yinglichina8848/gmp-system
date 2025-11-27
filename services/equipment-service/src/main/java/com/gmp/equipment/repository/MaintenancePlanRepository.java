package com.gmp.equipment.repository;

import com.gmp.equipment.entity.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 设备维护计划仓库接口
 * 提供维护计划相关的数据访问方法
 */
@Repository
public interface MaintenancePlanRepository extends JpaRepository<MaintenancePlan, Long>, JpaSpecificationExecutor<MaintenancePlan> {

    /**
     * 根据计划单号查询维护计划
     * @param planNumber 计划单号
     * @return 维护计划对象
     */
    Optional<MaintenancePlan> findByPlanNumber(String planNumber);

    /**
     * 根据设备ID查询维护计划列表
     * @param equipmentId 设备ID
     * @return 维护计划列表
     */
    List<MaintenancePlan> findByEquipmentId(Long equipmentId);

    /**
     * 根据维护计划状态查询维护计划列表
     * @param status 维护计划状态
     * @return 维护计划列表
     */
    List<MaintenancePlan> findByStatus(String status);

    /**
     * 根据维护类型查询维护计划列表
     * @param maintenanceType 维护类型
     * @return 维护计划列表
     */
    List<MaintenancePlan> findByMaintenanceType(String maintenanceType);

    /**
     * 查询计划开始日期在指定日期之前且未完成的维护计划
     * @param date 指定日期
     * @return 维护计划列表
     */
    List<MaintenancePlan> findByPlannedStartDateBeforeAndStatusNot(Date date, String status);

    /**
     * 根据计划开始日期范围查询维护计划列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 维护计划列表
     */
    List<MaintenancePlan> findByPlannedStartDateBetween(Date startDate, Date endDate);

    /**
     * 根据设备ID和维护计划状态查询维护计划列表
     * @param equipmentId 设备ID
     * @param status 维护计划状态
     * @return 维护计划列表
     */
    List<MaintenancePlan> findByEquipmentIdAndStatus(Long equipmentId, String status);

    /**
     * 判断计划单号是否存在（排除指定ID）
     * @param planNumber 计划单号
     * @param id 排除的维护计划ID
     * @return 是否存在
     */
    boolean existsByPlanNumberAndIdNot(String planNumber, Long id);
}