package com.gmp.crm.repository;

import com.gmp.crm.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单明细数据访问接口
 * 
 * @author TRAE AI
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * 根据订单ID查找订单项
     * 
     * @param orderId 订单ID
     * @return 订单项列表
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * 根据产品编号查找订单项
     * 
     * @param productCode 产品编号
     * @return 订单项列表
     */
    List<OrderItem> findByProductCode(String productCode);

    /**
     * 删除订单的所有订单项
     * 
     * @param orderId 订单ID
     */
    void deleteByOrderId(Long orderId);

    /**
     * 统计订单的订单项数量
     * 
     * @param orderId 订单ID
     * @return 订单项数量
     */
    int countByOrderId(Long orderId);

}