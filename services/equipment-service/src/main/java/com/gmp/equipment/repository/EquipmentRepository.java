package com.gmp.equipment.repository;

import com.gmp.equipment.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 设备仓库接口
 * 提供设备相关的数据访问方法
 */
@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {

    /**
     * 根据设备编码查询设备
     * @param code 设备编码
     * @return 设备对象
     */
    Optional<Equipment> findByCode(String code);

    /**
     * 根据设备序列号查询设备
     * @param serialNumber 设备序列号
     * @return 设备对象
     */
    Optional<Equipment> findBySerialNumber(String serialNumber);

    /**
     * 根据设备类型ID查询设备列表
     * @param equipmentTypeId 设备类型ID
     * @return 设备列表
     */
    List<Equipment> findByEquipmentTypeId(Long equipmentTypeId);

    /**
     * 根据设备状态查询设备列表
     * @param status 设备状态
     * @return 设备列表
     */
    List<Equipment> findByStatus(String status);

    /**
     * 根据放置位置查询设备列表
     * @param location 放置位置
     * @return 设备列表
     */
    List<Equipment> findByLocation(String location);

    /**
     * 根据负责人查询设备列表
     * @param responsiblePerson 负责人
     * @return 设备列表
     */
    List<Equipment> findByResponsiblePerson(String responsiblePerson);

    /**
     * 查询需要校准的设备列表（下次校准日期在指定日期之前）
     * @param date 指定日期
     * @return 设备列表
     */
    List<Equipment> findByNextCalibrationDateBefore(Date date);

    /**
     * 查询需要维护的设备列表（下次维护日期在指定日期之前）
     * @param date 指定日期
     * @return 设备列表
     */
    List<Equipment> findByNextMaintenanceDateBefore(Date date);

    /**
     * 判断设备编码是否存在（排除指定ID）
     * @param code 设备编码
     * @param id 排除的设备ID
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 判断设备序列号是否存在（排除指定ID）
     * @param serialNumber 设备序列号
     * @param id 排除的设备ID
     * @return 是否存在
     */
    boolean existsBySerialNumberAndIdNot(String serialNumber, Long id);

    /**
     * 根据设备名称模糊查询设备列表
     * @param name 设备名称（部分匹配）
     * @return 设备列表
     */
    @Query("select e from Equipment e where e.name like %:name%")
    List<Equipment> findByNameLike(String name);

    /**
     * 根据设备型号模糊查询设备列表
     * @param model 设备型号（部分匹配）
     * @return 设备列表
     */
    @Query("select e from Equipment e where e.model like %:model%")
    List<Equipment> findByModelLike(String model);
}