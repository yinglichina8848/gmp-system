package com.gmp.equipment.repository;

import com.gmp.equipment.entity.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 设备类型仓库接口
 * 提供设备类型相关的数据访问方法
 */
@Repository
public interface EquipmentTypeRepository extends JpaRepository<EquipmentType, Long>, JpaSpecificationExecutor<EquipmentType> {

    /**
     * 根据设备类型编码查询设备类型
     * @param code 设备类型编码
     * @return 设备类型对象
     */
    Optional<EquipmentType> findByCode(String code);

    /**
     * 根据设备类型名称查询设备类型
     * @param name 设备类型名称
     * @return 设备类型对象
     */
    Optional<EquipmentType> findByName(String name);

    /**
     * 判断设备类型编码是否存在（排除指定ID）
     * @param code 设备类型编码
     * @param id 排除的设备类型ID
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 判断设备类型名称是否存在（排除指定ID）
     * @param name 设备类型名称
     * @param id 排除的设备类型ID
     * @return 是否存在
     */
    boolean existsByNameAndIdNot(String name, Long id);
}