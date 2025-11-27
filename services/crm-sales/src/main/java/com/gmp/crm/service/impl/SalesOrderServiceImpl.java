package com.gmp.crm.service.impl;

import com.gmp.crm.dto.PageRequestDTO;
import com.gmp.crm.dto.SalesOrderDTO;
import com.gmp.crm.dto.SalesOrderRequestDTO;
import com.gmp.crm.entity.Customer;
import com.gmp.crm.entity.OrderItem;
import com.gmp.crm.entity.SalesOrder;
import com.gmp.crm.repository.CustomerRepository;
import com.gmp.crm.repository.SalesOrderRepository;
import com.gmp.crm.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 销售订单服务实现类
 * 
 * @author TRAE AI
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public SalesOrderDTO createSalesOrder(SalesOrderRequestDTO request, String createdBy) {
        // 验证客户是否存在
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("客户不存在: " + request.getCustomerId()));

        // 创建销售订单
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomer(customer);
        salesOrder.setOrderNumber(generateOrderNumber());
        salesOrder.setStatus("PENDING");
        salesOrder.setRemarks(request.getRemarks());
        salesOrder.setCreatedBy(createdBy);

        // 添加订单项
        for (SalesOrderRequestDTO.OrderItemRequestDTO itemRequest : request.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductCode(itemRequest.getProductCode());
            orderItem.setProductName(itemRequest.getProductName());
            orderItem.setUnitPrice(new BigDecimal(itemRequest.getUnitPrice()));
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setRemarks(itemRequest.getRemarks());
            
            // 计算小计
            BigDecimal quantity = new BigDecimal(orderItem.getQuantity());
            orderItem.setSubTotal(orderItem.getUnitPrice().multiply(quantity));
            
            // 添加到订单
            salesOrder.addItem(orderItem);
        }

        // 保存订单
        SalesOrder savedOrder = salesOrderRepository.save(salesOrder);

        // 转换为DTO
        return modelMapper.map(savedOrder, SalesOrderDTO.class);
    }

    @Override
    @Transactional
    public SalesOrderDTO updateOrderStatus(Long id, String status) {
        // 查找订单
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在: " + id));

        // 验证状态转换
        validateStatusTransition(salesOrder.getStatus(), status);

        // 更新状态
        salesOrder.setStatus(status);

        // 保存更新
        SalesOrder updatedOrder = salesOrderRepository.save(salesOrder);

        // 转换为DTO
        return modelMapper.map(updatedOrder, SalesOrderDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalesOrderDTO> getSalesOrderById(Long id) {
        return salesOrderRepository.findById(id)
                .map(order -> modelMapper.map(order, SalesOrderDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalesOrderDTO> getSalesOrderByOrderNumber(String orderNumber) {
        return salesOrderRepository.findByOrderNumber(orderNumber)
                .map(order -> modelMapper.map(order, SalesOrderDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrderDTO> getSalesOrders(PageRequestDTO pageRequest) {
        // 构建排序
        Sort.Direction direction = "desc".equalsIgnoreCase(pageRequest.getSortOrder()) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = pageRequest.getSortField() != null ? pageRequest.getSortField() : "createdAt";
        Sort sort = Sort.by(direction, sortField);

        // 构建分页请求
        PageRequest pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        // 查询订单
        Page<SalesOrder> orderPage = salesOrderRepository.findAll(pageable);

        // 转换为DTO
        return orderPage.map(order -> modelMapper.map(order, SalesOrderDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrderDTO> getSalesOrdersByCustomer(Long customerId, PageRequestDTO pageRequest) {
        // 构建排序
        Sort.Direction direction = "desc".equalsIgnoreCase(pageRequest.getSortOrder()) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = pageRequest.getSortField() != null ? pageRequest.getSortField() : "createdAt";
        Sort sort = Sort.by(direction, sortField);

        // 构建分页请求
        PageRequest pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        // 查询客户订单
        Page<SalesOrder> orderPage = salesOrderRepository.findByCustomerId(customerId, pageable);

        // 转换为DTO
        return orderPage.map(order -> modelMapper.map(order, SalesOrderDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalesOrderDTO> searchSalesOrders(String keyword, String status, PageRequestDTO pageRequest) {
        // 构建排序
        Sort.Direction direction = "desc".equalsIgnoreCase(pageRequest.getSortOrder()) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        String sortField = pageRequest.getSortField() != null ? pageRequest.getSortField() : "createdAt";
        Sort sort = Sort.by(direction, sortField);

        // 构建分页请求
        PageRequest pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        // 这里简化处理，实际可以使用Specification进行复杂查询
        Page<SalesOrder> orderPage;
        if (status != null) {
            orderPage = salesOrderRepository.findByStatus(status, pageable);
        } else {
            orderPage = salesOrderRepository.findAll(pageable);
        }

        // 转换为DTO
        return orderPage.map(order -> modelMapper.map(order, SalesOrderDTO.class));
    }

    @Override
    @Transactional
    public boolean cancelOrder(Long id) {
        // 查找订单
        SalesOrder salesOrder = salesOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在: " + id));

        // 验证是否可以取消
        if (!"PENDING".equals(salesOrder.getStatus())) {
            throw new IllegalStateException("只有待处理的订单可以取消: " + salesOrder.getStatus());
        }

        // 更新状态为已取消
        salesOrder.setStatus("CANCELLED");
        salesOrderRepository.save(salesOrder);

        return true;
    }

    /**
     * 生成订单编号
     * 格式: SO+日期(YYYYMMDD)+随机数
     */
    private String generateOrderNumber() {
        String datePrefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "SO" + datePrefix + randomSuffix;
    }

    /**
     * 验证状态转换的合法性
     */
    private void validateStatusTransition(String currentStatus, String newStatus) {
        // 这里实现状态转换的业务规则验证
        // 例如: PENDING -> CONFIRMED -> SHIPPED -> DELIVERED
        // 或: PENDING -> CANCELLED
        // 实际业务中可能有更复杂的状态流转规则
        List<String> validStatuses = List.of("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");
        if (!validStatuses.contains(newStatus)) {
            throw new IllegalArgumentException("无效的订单状态: " + newStatus);
        }
    }

}