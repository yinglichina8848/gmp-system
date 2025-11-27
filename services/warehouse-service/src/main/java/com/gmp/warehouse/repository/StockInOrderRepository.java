package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.StockInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 入库单数据访问层
 * <p>
 * 提供入库单相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface StockInOrderRepository extends JpaRepository<StockInOrder, Long>, JpaSpecificationExecutor<StockInOrder> {

    /**
     * 根据入库单号查询入库单
     *
     * @param orderNo 入库单号
     * @return 入库单对象
     */
    Optional<StockInOrder> findByOrderNo(String orderNo);

    /**
     * 根据仓库ID查询入库单列表
     *
     * @param warehouseId 仓库ID
     * @return 入库单列表
     */
    List<StockInOrder> findByWarehouseId(Long warehouseId);

    /**
     * 根据供应商ID查询入库单列表
     *
     * @param supplierId 供应商ID
     * @return 入库单列表
     */
    List<StockInOrder> findBySupplierId(Long supplierId);

    /**
     * 根据状态查询入库单列表
     *
     * @param status 状态：0-草稿，1-待审核，2-已审核，3-已入库，4-已取消
     * @return 入库单列表
     */
    List<StockInOrder> findByStatus(Integer status);

    /**
     * 根据入库类型查询入库单列表
     *
     * @param orderType 入库类型
     * @return 入库单列表
     */
    List<StockInOrder> findByOrderType(String orderType);

    /**
     * 根据关联单据号查询入库单
     *
     * @param relatedOrderNo 关联单据号
     * @return 入库单列表
     */
    List<StockInOrder> findByRelatedOrderNo(String relatedOrderNo);

    /**
     * 根据入库日期范围查询入库单
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 入库单列表
     */
    List<StockInOrder> findByStockInDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询待审核的入库单
     *
     * @return 待审核的入库单列表
     */
    List<StockInOrder> findByStatusOrderByCreatedAtDesc(Integer status);

    /**
     * 查询入库单总金额统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 总金额
     */
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM StockInOrder s WHERE s.stockInDate BETWEEN :startDate AND :endDate AND s.status = 3")
    Double getTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}