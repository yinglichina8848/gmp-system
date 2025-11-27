package com.gmp.equipment.service;

import com.gmp.equipment.dto.EquipmentDTO;
import com.gmp.equipment.dto.EquipmentQueryDTO;
import com.gmp.equipment.dto.PageResultDTO;

import java.util.Date;
import java.util.List;

/**
 * 设备服务接口
 * 定义设备相关的业务方法
 */
public interface EquipmentService {

    /**
     * 根据ID查询设备
     * @param id 设备ID
     * @return 设备DTO
     */
    EquipmentDTO getById(Long id);

    /**
     * 根据编码查询设备
     * @param code 设备编码
     * @return 设备DTO
     */
    EquipmentDTO getByCode(String code);

    /**
     * 根据序列号查询设备
     * @param serialNumber 设备序列号
     * @return 设备DTO
     */
    EquipmentDTO getBySerialNumber(String serialNumber);

    /**
     * 分页查询设备
     * @param queryDTO 查询条件
     * @return 设备分页结果
     */
    PageResultDTO<EquipmentDTO> listPage(EquipmentQueryDTO queryDTO);

    /**
     * 根据设备类型ID查询设备列表
     * @param equipmentTypeId 设备类型ID
     * @return 设备DTO列表
     */
    List<EquipmentDTO> listByTypeId(Long equipmentTypeId);

    /**
     * 根据设备状态查询设备列表
     * @param status 设备状态
     * @return 设备DTO列表
     */
    List<EquipmentDTO> listByStatus(String status);

    /**
     * 查询需要校准的设备列表
     * @param date 查询日期
     * @return 设备DTO列表
     */
    List<EquipmentDTO> listNeedCalibration(Date date);

    /**
     * 查询需要维护的设备列表
     * @param date 查询日期
     * @return 设备DTO列表
     */
    List<EquipmentDTO> listNeedMaintenance(Date date);

    /**
     * 新增设备
     * @param equipmentDTO 设备DTO
     * @return 新增后的设备DTO
     */
    EquipmentDTO create(EquipmentDTO equipmentDTO);

    /**
     * 更新设备
     * @param id 设备ID
     * @param equipmentDTO 设备DTO
     * @return 更新后的设备DTO
     */
    EquipmentDTO update(Long id, EquipmentDTO equipmentDTO);

    /**
     * 删除设备
     * @param id 设备ID
     */
    void delete(Long id);

    /**
     * 批量删除设备
     * @param ids 设备ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 更新设备状态
     * @param id 设备ID
     * @param status 新状态
     */
    void updateStatus(Long id, String status);

    /**
     * 更新设备下次校准日期
     * @param id 设备ID
     * @param nextCalibrationDate 下次校准日期
     */
    void updateNextCalibrationDate(Long id, Date nextCalibrationDate);

    /**
     * 更新设备下次维护日期
     * @param id 设备ID
     * @param nextMaintenanceDate 下次维护日期
     */
    void updateNextMaintenanceDate(Long id, Date nextMaintenanceDate);

    /**
     * 检查设备编码是否存在
     * @param code 设备编码
     * @param id 排除的ID，用于更新时检查
     * @return 是否存在
     */
    boolean existsByCode(String code, Long id);

    /**
     * 检查设备序列号是否存在
     * @param serialNumber 设备序列号
     * @param id 排除的ID，用于更新时检查
     * @return 是否存在
     */
    boolean existsBySerialNumber(String serialNumber, Long id);
}