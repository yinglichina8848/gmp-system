package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.StockOutOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 出库单数据访问层
 * <p>
 * 提供出库单相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface StockOutOrderRepository extends JpaRepository<StockOutOrder, Long>, JpaSpecificationExecutor<StockOutOrder> {

    /**
     * 根据出库单号查询出库单
     *
     * @param orderNo 出库单号
     * @return 出库单对象
     */
    Optional<StockOutOrder> findByOrderNo(String orderNo);

    /**
     * 根据仓库ID查询出库单列表
     *
     * @param warehouseId 仓库ID
     * @return 出库单列表
     */
    List<StockOutOrder> findByWarehouseId(Long warehouseId);

    /**
     * 根据状态查询出库单列表
     *
     * @param status 状态：0-草稿，1-待审核，2-已审核，3-已出库，4-已取消
     * @return 出库单列表
     */
    List<StockOutOrder> findByStatus(Integer status);

    /**
     * 根据出库类型查询出库单列表
     *
     * @param orderType 出库类型
     * @return 出库单列表
     */
    List<StockOutOrder> findByOrderType(String orderType);

    /**
     * 根据关联单据号查询出库单
     *
     * @param relatedOrderNo 关联单据号
     * @return 出库单列表
     */
    List<StockOutOrder> findByRelatedOrderNo(String relatedOrderNo);

    /**
     * 根据出库日期范围查询出库单
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 出库单列表
     */
    List<StockOutOrder> findByStockOutDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询待审核的出库单
     *
     * @return 待审核的出库单列表
     */
    List<StockOutOrder> findByStatusOrderByCreatedAtDesc(Integer status);

    /**
     * 查询出库单总金额统计
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 总金额
     */
    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM StockOutOrder s WHERE s.stockOutDate BETWEEN :startDate AND :endDate AND s.status = 3")
    Double getTotalAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}