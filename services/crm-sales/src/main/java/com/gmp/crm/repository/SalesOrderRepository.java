package com.gmp.crm.repository;

import com.gmp.crm.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 销售订单数据访问接口
 * 
 * @author TRAE AI
 */
@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {

    /**
     * 根据订单编号查找订单
     * 
     * @param orderNumber 订单编号
     * @return 销售订单
     */
    Optional<SalesOrder> findByOrderNumber(String orderNumber);

    /**
     * 查找客户的所有订单
     * 
     * @param customerId 客户ID
     * @param pageable 分页参数
     * @return 订单分页列表
     */
    Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable);

    /**
     * 查找客户的特定状态订单
     * 
     * @param customerId 客户ID
     * @param status 订单状态
     * @param pageable 分页参数
     * @return 订单分页列表
     */
    Page<SalesOrder> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);

    /**
     * 根据状态查找订单
     * 
     * @param status 订单状态
     * @param pageable 分页参数
     * @return 订单分页列表
     */
    Page<SalesOrder> findByStatus(String status, Pageable pageable);

    /**
     * 统计客户的订单数量
     * 
     * @param customerId 客户ID
     * @return 订单数量
     */
    long countByCustomerId(Long customerId);

    /**
     * 查询最近的订单
     * 
     * @param limit 数量限制
     * @return 订单列表
     */
    List<SalesOrder> findTopByOrderByCreatedAtDesc(Pageable limit);

}