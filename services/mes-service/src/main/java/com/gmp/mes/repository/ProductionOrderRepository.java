package com.gmp.mes.repository;

import com.gmp.mes.entity.ProductionOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 生产订单数据访问接口
 * 
 * @author gmp-system
 */
@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {

    /**
     * 根据订单编号查询订单
     * 
     * @param orderNumber 订单编号
     * @return 生产订单对象
     */
    Optional<ProductionOrder> findByOrderNumber(String orderNumber);

    /**
     * 根据状态查询订单列表
     * 
     * @param status 订单状态
     * @return 订单列表
     */
    List<ProductionOrder> findByStatus(ProductionOrder.OrderStatus status);

    /**
     * 根据申请人查询订单列表
     * 
     * @param requester 申请人
     * @return 订单列表
     */
    List<ProductionOrder> findByRequester(String requester);

    /**
     * 根据计划开始日期范围查询订单
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 订单列表
     */
    List<ProductionOrder> findByPlannedStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据产品ID查询订单
     * 
     * @param productId 产品ID
     * @return 订单列表
     */
    List<ProductionOrder> findByProductId(Long productId);

    /**
     * 查询优先级高于或等于指定优先级的订单
     * 
     * @param priority 优先级
     * @return 订单列表
     */
    @Query("SELECT p FROM ProductionOrder p WHERE p.priority >= :priority ORDER BY p.priority DESC")
    List<ProductionOrder> findByPriorityGreaterThanEqualOrderByPriorityDesc(@Param("priority") ProductionOrder.OrderPriority priority);

    /**
     * 统计指定状态的订单数量
     * 
     * @param status 订单状态
     * @return 订单数量
     */
    long countByStatus(ProductionOrder.OrderStatus status);
}