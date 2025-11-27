package com.gmp.equipment.service;

import com.gmp.equipment.dto.EquipmentTypeDTO;
import com.gmp.equipment.dto.PageResultDTO;

import java.util.List;

/**
 * 设备类型服务接口
 * 定义设备类型相关的业务方法
 */
public interface EquipmentTypeService {

    /**
     * 根据ID查询设备类型
     * @param id 设备类型ID
     * @return 设备类型DTO
     */
    EquipmentTypeDTO getById(Long id);

    /**
     * 根据编码查询设备类型
     * @param code 设备类型编码
     * @return 设备类型DTO
     */
    EquipmentTypeDTO getByCode(String code);

    /**
     * 查询所有设备类型
     * @return 设备类型DTO列表
     */
    List<EquipmentTypeDTO> listAll();

    /**
     * 查询所有启用的设备类型
     * @return 启用的设备类型DTO列表
     */
    List<EquipmentTypeDTO> listEnabled();

    /**
     * 分页查询设备类型
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 设备类型分页结果
     */
    PageResultDTO<EquipmentTypeDTO> listPage(Integer pageNum, Integer pageSize);

    /**
     * 新增设备类型
     * @param equipmentTypeDTO 设备类型DTO
     * @return 新增后的设备类型DTO
     */
    EquipmentTypeDTO create(EquipmentTypeDTO equipmentTypeDTO);

    /**
     * 更新设备类型
     * @param id 设备类型ID
     * @param equipmentTypeDTO 设备类型DTO
     * @return 更新后的设备类型DTO
     */
    EquipmentTypeDTO update(Long id, EquipmentTypeDTO equipmentTypeDTO);

    /**
     * 删除设备类型
     * @param id 设备类型ID
     */
    void delete(Long id);

    /**
     * 批量删除设备类型
     * @param ids 设备类型ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 启用/禁用设备类型
     * @param id 设备类型ID
     * @param enabled 是否启用
     */
    void enable(Long id, Boolean enabled);

    /**
     * 检查设备类型编码是否存在
     * @param code 设备类型编码
     * @param id 排除的ID，用于更新时检查
     * @return 是否存在
     */
    boolean existsByCode(String code, Long id);

    /**
     * 检查设备类型名称是否存在
     * @param name 设备类型名称
     * @param id 排除的ID，用于更新时检查
     * @return 是否存在
     */
    boolean existsByName(String name, Long id);
}