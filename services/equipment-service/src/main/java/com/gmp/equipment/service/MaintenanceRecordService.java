package com.gmp.equipment.service;

import com.gmp.equipment.dto.MaintenanceRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;

import java.util.Date;
import java.util.List;

/**
 * 设备维护记录服务接口
 * 定义维护记录相关的业务方法
 */
public interface MaintenanceRecordService {

    /**
     * 根据ID查询维护记录
     * @param id 维护记录ID
     * @return 维护记录DTO
     */
    MaintenanceRecordDTO getById(Long id);

    /**
     * 根据维护编号查询维护记录
     * @param maintenanceNumber 维护编号
     * @return 维护记录DTO
     */
    MaintenanceRecordDTO getByMaintenanceNumber(String maintenanceNumber);

    /**
     * 分页查询维护记录
     * @param equipmentId 设备ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 维护记录分页结果
     */
    PageResultDTO<MaintenanceRecordDTO> listPage(Long equipmentId, Integer pageNum, Integer pageSize);

    /**
     * 根据设备ID查询维护记录列表
     * @param equipmentId 设备ID
     * @return 维护记录DTO列表
     */
    List<MaintenanceRecordDTO> listByEquipmentId(Long equipmentId);

    /**
     * 根据维护计划ID查询维护记录
     * @param maintenancePlanId 维护计划ID
     * @return 维护记录DTO列表
     */
    List<MaintenanceRecordDTO> listByMaintenancePlanId(Long maintenancePlanId);

    /**
     * 根据维护日期范围查询维护记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 维护记录DTO列表
     */
    List<MaintenanceRecordDTO> listByDateRange(Date startDate, Date endDate);

    /**
     * 查询设备的最新维护记录
     * @param equipmentId 设备ID
     * @return 维护记录DTO
     */
    MaintenanceRecordDTO getLatestByEquipmentId(Long equipmentId);

    /**
     * 新增维护记录
     * @param maintenanceRecordDTO 维护记录DTO
     * @return 新增后的维护记录DTO
     */
    MaintenanceRecordDTO create(MaintenanceRecordDTO maintenanceRecordDTO);

    /**
     * 更新维护记录
     * @param id 维护记录ID
     * @param maintenanceRecordDTO 维护记录DTO
     * @return 更新后的维护记录DTO
     */
    MaintenanceRecordDTO update(Long id, MaintenanceRecordDTO maintenanceRecordDTO);

    /**
     * 删除维护记录
     * @param id 维护记录ID
     */
    void delete(Long id);

    /**
     * 批量删除维护记录
     * @param ids 维护记录ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 检查维护编号是否存在
     * @param maintenanceNumber 维护编号
     * @param id 排除的ID，用于更新时检查
     * @return 是否存在
     */
    boolean existsByMaintenanceNumber(String maintenanceNumber, Long id);
}