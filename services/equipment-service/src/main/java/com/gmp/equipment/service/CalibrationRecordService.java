package com.gmp.equipment.service;

import com.gmp.equipment.dto.CalibrationRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;

import java.util.Date;
import java.util.List;

/**
 * 设备校准记录服务接口
 * 定义校准记录相关的业务方法
 */
public interface CalibrationRecordService {

    /**
     * 根据ID查询校准记录
     * @param id 校准记录ID
     * @return 校准记录DTO
     */
    CalibrationRecordDTO getById(Long id);

    /**
     * 根据校准编号查询校准记录
     * @param calibrationNumber 校准编号
     * @return 校准记录DTO
     */
    CalibrationRecordDTO getByCalibrationNumber(String calibrationNumber);

    /**
     * 分页查询校准记录
     * @param equipmentId 设备ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 校准记录分页结果
     */
    PageResultDTO<CalibrationRecordDTO> listPage(Long equipmentId, Integer pageNum, Integer pageSize);

    /**
     * 根据设备ID查询校准记录列表
     * @param equipmentId 设备ID
     * @return 校准记录DTO列表
     */
    List<CalibrationRecordDTO> listByEquipmentId(Long equipmentId);

    /**
     * 根据校准日期范围查询校准记录
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 校准记录DTO列表
     */
    List<CalibrationRecordDTO> listByDateRange(Date startDate, Date endDate);

    /**
     * 根据校准结果查询校准记录
     * @param calibrationResult 校准结果
     * @return 校准记录DTO列表
     */
    List<CalibrationRecordDTO> listByResult(String calibrationResult);

    /**
     * 查询设备的最新校准记录
     * @param equipmentId 设备ID
     * @return 校准记录DTO
     */
    CalibrationRecordDTO getLatestByEquipmentId(Long equipmentId);

    /**
     * 新增校准记录
     * @param calibrationRecordDTO 校准记录DTO
     * @return 新增后的校准记录DTO
     */
    CalibrationRecordDTO create(CalibrationRecordDTO calibrationRecordDTO);

    /**
     * 更新校准记录
     * @param id 校准记录ID
     * @param calibrationRecordDTO 校准记录DTO
     * @return 更新后的校准记录DTO
     */
    CalibrationRecordDTO update(Long id, CalibrationRecordDTO calibrationRecordDTO);

    /**
     * 删除校准记录
     * @param id 校准记录ID
     */
    void delete(Long id);

    /**
     * 批量删除校准记录
     * @param ids 校准记录ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 检查校准编号是否存在
     * @param calibrationNumber 校准编号
     * @param id 排除的ID，用于更新时检查
     * @return 是否存在
     */
    boolean existsByCalibrationNumber(String calibrationNumber, Long id);
}