package com.gmp.equipment.service;

import com.gmp.equipment.dto.MaintenancePlanDTO;
import com.gmp.equipment.dto.PageResultDTO;

import java.util.Date;
import java.util.List;

/**
 * 设备维护计划服务接口
 * 定义维护计划相关的业务方法
 */
public interface MaintenancePlanService {

    /**
     * 根据ID查询维护计划
     * @param id 维护计划ID
     * @return 维护计划DTO
     */
    MaintenancePlanDTO getById(Long id);

    /**
     * 根据计划编号查询维护计划
     * @param planNumber 计划编号
     * @return 维护计划DTO
     */
    MaintenancePlanDTO getByPlanNumber(String planNumber);

    /**
     * 分页查询维护计划
     * @param equipmentId 设备ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 维护计划分页结果
     */
    PageResultDTO<MaintenancePlanDTO> listPage(Long equipmentId, Integer pageNum, Integer pageSize);

    /**
     * 根据设备ID查询维护计划列表
     * @param equipmentId 设备ID
     * @return 维护计划DTO列表
     */
    List<MaintenancePlanDTO> listByEquipmentId(Long equipmentId);

    /**
     * 根据计划日期范围查询维护计划
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 维护计划DTO列表
     */
    List<MaintenancePlanDTO> listByDateRange(Date startDate, Date endDate);

    /**
     * 根据计划状态查询维护计划
     * @param status 计划状态
     * @return 维护计划DTO列表
     */
    List<MaintenancePlanDTO> listByStatus(String status);

    /**
     * 查询设备的最新维护计划
     * @param equipmentId 设备ID
     * @return 维护计划DTO
     */
    MaintenancePlanDTO getLatestByEquipmentId(Long equipmentId);

    /**
     * 新增维护计划
     * @param maintenancePlanDTO 维护计划DTO
     * @return 新增后的维护计划DTO
     */
    MaintenancePlanDTO create(MaintenancePlanDTO maintenancePlanDTO);

    /**
     * 更新维护计划
     * @param id 维护计划ID
     * @param maintenancePlanDTO 维护计划DTO
     * @return 更新后的维护计划DTO
     */
    MaintenancePlanDTO update(Long id, MaintenancePlanDTO maintenancePlanDTO);

    /**
     * 删除维护计划
     * @param id 维护计划ID
     */
    void delete(Long id);

    /**
     * 批量删除维护计划
     * @param ids 维护计划ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 更新维护计划状态
     * @param id 维护计划ID
     * @param status 新状态
     */
    void updateStatus(Long id, String status);

    /**
     * 检查计划编号是否存在
     * @param planNumber 计划编号
     * @param id 排除的ID，用于更新时检查
     * @return 是否存在
     */
    boolean existsByPlanNumber(String planNumber, Long id);
}