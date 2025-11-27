package com.gmp.mes.repository;

import com.gmp.mes.entity.ProductionBatch;
import com.gmp.mes.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 生产批次数据访问接口
 * 
 * @author gmp-system
 */
@Repository
public interface ProductionBatchRepository extends JpaRepository<ProductionBatch, Long> {

    /**
     * 根据批次编号查询批次
     * 
     * @param batchNumber 批次编号
     * @return 生产批次对象
     */
    Optional<ProductionBatch> findByBatchNumber(String batchNumber);

    /**
     * 根据订单查询批次列表
     * 
     * @param order 生产订单
     * @return 批次列表
     */
    List<ProductionBatch> findByOrder(ProductionOrder order);

    /**
     * 根据订单ID查询批次列表
     * 
     * @param orderId 订单ID
     * @return 批次列表
     */
    List<ProductionBatch> findByOrderId(Long orderId);

    /**
     * 根据状态查询批次列表
     * 
     * @param status 批次状态
     * @return 批次列表
     */
    List<ProductionBatch> findByStatus(ProductionBatch.BatchStatus status);

    /**
     * 根据设备ID查询批次列表
     * 
     * @param equipmentId 设备ID
     * @return 批次列表
     */
    List<ProductionBatch> findByEquipmentId(String equipmentId);

    /**
     * 查询指定时间范围内的批次
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 批次列表
     */
    List<ProductionBatch> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询正在进行的批次
     * 
     * @return 正在进行的批次列表
     */
    @Query("SELECT b FROM ProductionBatch b WHERE b.status = :status ORDER BY b.startTime ASC")
    List<ProductionBatch> findActiveBatches(@Param("status") ProductionBatch.BatchStatus status);

    /**
     * 统计指定状态的批次数量
     * 
     * @param status 批次状态
     * @return 批次数量
     */
    long countByStatus(ProductionBatch.BatchStatus status);
}