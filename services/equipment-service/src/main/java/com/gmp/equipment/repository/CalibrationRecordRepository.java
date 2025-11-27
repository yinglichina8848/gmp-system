package com.gmp.equipment.repository;

import com.gmp.equipment.entity.CalibrationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 设备校准记录仓库接口
 * 提供校准记录相关的数据访问方法
 */
@Repository
public interface CalibrationRecordRepository extends JpaRepository<CalibrationRecord, Long>, JpaSpecificationExecutor<CalibrationRecord> {

    /**
     * 根据校准单号查询校准记录
     * @param calibrationNumber 校准单号
     * @return 校准记录对象
     */
    Optional<CalibrationRecord> findByCalibrationNumber(String calibrationNumber);

    /**
     * 根据设备ID查询校准记录列表
     * @param equipmentId 设备ID
     * @return 校准记录列表
     */
    List<CalibrationRecord> findByEquipmentId(Long equipmentId);

    /**
     * 根据设备ID查询最新的校准记录
     * @param equipmentId 设备ID
     * @return 最新的校准记录
     */
    Optional<CalibrationRecord> findFirstByEquipmentIdOrderByCalibrationDateDesc(Long equipmentId);

    /**
     * 根据校准日期范围查询校准记录列表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 校准记录列表
     */
    List<CalibrationRecord> findByCalibrationDateBetween(Date startDate, Date endDate);

    /**
     * 根据校准结果查询校准记录列表
     * @param calibrationResult 校准结果
     * @return 校准记录列表
     */
    List<CalibrationRecord> findByCalibrationResult(String calibrationResult);

    /**
     * 根据校准机构查询校准记录列表
     * @param calibrationAgency 校准机构
     * @return 校准记录列表
     */
    List<CalibrationRecord> findByCalibrationAgency(String calibrationAgency);

    /**
     * 根据设备ID和校准日期范围查询校准记录列表
     * @param equipmentId 设备ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 校准记录列表
     */
    List<CalibrationRecord> findByEquipmentIdAndCalibrationDateBetween(Long equipmentId, Date startDate, Date endDate);

    /**
     * 判断校准单号是否存在（排除指定ID）
     * @param calibrationNumber 校准单号
     * @param id 排除的校准记录ID
     * @return 是否存在
     */
    boolean existsByCalibrationNumberAndIdNot(String calibrationNumber, Long id);
}