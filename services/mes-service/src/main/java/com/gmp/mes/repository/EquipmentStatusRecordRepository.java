package com.gmp.mes.repository;

import com.gmp.mes.entity.EquipmentStatusRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备状态记录数据访问接口
 * 
 * @author gmp-system
 */
@Repository
public interface EquipmentStatusRecordRepository extends JpaRepository<EquipmentStatusRecord, Long> {

    /**
     * 根据设备编码查询状态记录
     * 
     * @param equipmentCode 设备编码
     * @return 状态记录列表
     */
    List<EquipmentStatusRecord> findByEquipmentCodeOrderByTimestampDesc(String equipmentCode);

    /**
     * 根据设备编码和时间范围查询状态记录
     * 
     * @param equipmentCode 设备编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 状态记录列表
     */
    List<EquipmentStatusRecord> findByEquipmentCodeAndTimestampBetweenOrderByTimestampDesc(
            String equipmentCode, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询指定设备的最新状态记录
     * 
     * @param equipmentCode 设备编码
     * @return 最新的状态记录
     */
    @Query("SELECT e FROM EquipmentStatusRecord e WHERE e.equipmentCode = :code ORDER BY e.timestamp DESC LIMIT 1")
    EquipmentStatusRecord findLatestByEquipmentCode(@Param("code") String equipmentCode);

    /**
     * 统计指定状态的记录数量
     * 
     * @param status 设备状态
     * @return 记录数量
     */
    long countByStatus(EquipmentStatusRecord.EquipmentStatus status);

    /**
     * 清理指定时间之前的历史记录
     * 
     * @param date 截止日期
     * @return 清理的记录数
     */
    @org.springframework.data.jpa.repository.Modifying
    int deleteByTimestampBefore(LocalDateTime date);
}