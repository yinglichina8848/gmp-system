package com.gmp.crm.service;

import com.gmp.crm.dto.PageRequestDTO;
import com.gmp.crm.dto.SalesOrderDTO;
import com.gmp.crm.dto.SalesOrderRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * 销售订单服务接口
 * 
 * @author TRAE AI
 */
public interface SalesOrderService {

    /**
     * 创建销售订单
     * 
     * @param request 订单请求DTO
     * @param createdBy 创建人
     * @return 创建的订单DTO
     */
    SalesOrderDTO createSalesOrder(SalesOrderRequestDTO request, String createdBy);

    /**
     * 更新订单状态
     * 
     * @param id 订单ID
     * @param status 新状态
     * @return 更新后的订单DTO
     */
    SalesOrderDTO updateOrderStatus(Long id, String status);

    /**
     * 根据ID获取订单
     * 
     * @param id 订单ID
     * @return 订单DTO
     */
    Optional<SalesOrderDTO> getSalesOrderById(Long id);

    /**
     * 根据订单号获取订单
     * 
     * @param orderNumber 订单编号
     * @return 订单DTO
     */
    Optional<SalesOrderDTO> getSalesOrderByOrderNumber(String orderNumber);

    /**
     * 分页查询订单列表
     * 
     * @param pageRequest 分页请求
     * @return 订单分页列表
     */
    Page<SalesOrderDTO> getSalesOrders(PageRequestDTO pageRequest);

    /**
     * 查询客户的订单列表
     * 
     * @param customerId 客户ID
     * @param pageRequest 分页请求
     * @return 订单分页列表
     */
    Page<SalesOrderDTO> getSalesOrdersByCustomer(Long customerId, PageRequestDTO pageRequest);

    /**
     * 搜索订单
     * 
     * @param keyword 搜索关键字（订单号、客户名称）
     * @param status 订单状态
     * @param pageRequest 分页请求
     * @return 订单分页列表
     */
    Page<SalesOrderDTO> searchSalesOrders(String keyword, String status, PageRequestDTO pageRequest);

    /**
     * 取消订单
     * 
     * @param id 订单ID
     * @return 是否取消成功
     */
    boolean cancelOrder(Long id);

}