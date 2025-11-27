package com.gmp.mes.service;

import com.gmp.mes.entity.ProductionOrder;
import com.gmp.mes.repository.ProductionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 生产订单服务 - 实现订单管理的核心业务逻辑
 * 
 * @author gmp-system
 */
@Service
public class ProductionOrderService {

    @Autowired
    private ProductionOrderRepository productionOrderRepository;

    /**
     * 创建生产订单
     * 
     * @param order 生产订单对象
     * @return 创建的生产订单
     */
    @Transactional
    public ProductionOrder createOrder(ProductionOrder order) {
        // 生成订单编号
        if (order.getOrderNumber() == null) {
            order.setOrderNumber(generateOrderNumber());
        }
        // 设置初始状态
        if (order.getStatus() == null) {
            order.setStatus(ProductionOrder.OrderStatus.DRAFT);
        }
        return productionOrderRepository.save(order);
    }

    /**
     * 创建生产订单（兼容方法）
     * 
     * @param order 生产订单对象
     * @return 创建的生产订单
     */
    @Transactional
    public ProductionOrder createProductionOrder(ProductionOrder order) {
        return createOrder(order);
    }

    /**
     * 根据ID获取订单
     * 
     * @param id 订单ID
     * @return 订单对象
     */
    public Optional<ProductionOrder> getOrderById(Long id) {
        return productionOrderRepository.findById(id);
    }

    /**
     * 根据ID获取订单（兼容方法）
     * 
     * @param id 订单ID
     * @return 订单对象
     */
    public Optional<ProductionOrder> getProductionOrderById(Long id) {
        return getOrderById(id);
    }

    /**
     * 根据订单编号获取订单
     * 
     * @param orderNumber 订单编号
     * @return 订单对象
     */
    public Optional<ProductionOrder> getOrderByNumber(String orderNumber) {
        return productionOrderRepository.findByOrderNumber(orderNumber);
    }

    /**
     * 根据订单编号获取订单（兼容方法）
     * 
     * @param orderNumber 订单编号
     * @return 订单对象
     */
    public Optional<ProductionOrder> getProductionOrderByOrderNumber(String orderNumber) {
        return getOrderByNumber(orderNumber);
    }

    /**
     * 获取所有订单
     * 
     * @return 订单列表
     */
    public List<ProductionOrder> getAllOrders() {
        return productionOrderRepository.findAll();
    }

    /**
     * 获取所有订单（兼容方法）
     * 
     * @return 订单列表
     */
    public List<ProductionOrder> getAllProductionOrders() {
        return getAllOrders();
    }

    /**
     * 根据状态获取订单
     * 
     * @param status 订单状态
     * @return 订单列表
     */
    public List<ProductionOrder> getOrdersByStatus(ProductionOrder.OrderStatus status) {
        return productionOrderRepository.findByStatus(status);
    }

    /**
     * 根据状态获取订单（兼容方法）
     * 
     * @param status 订单状态
     * @return 订单列表
     */
    public List<ProductionOrder> getProductionOrdersByStatus(ProductionOrder.OrderStatus status) {
        return getOrdersByStatus(status);
    }

    /**
     * 根据申请人获取订单
     * 
     * @param applicant 申请人
     * @return 订单列表
     */
    public List<ProductionOrder> getProductionOrdersByApplicant(String applicant) {
        return productionOrderRepository.findByRequester(applicant);
    }

    /**
     * 根据计划日期范围获取订单
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 订单列表
     */
    public List<ProductionOrder> getProductionOrdersByPlanDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        return productionOrderRepository.findByPlannedStartDateBetween(start, end);
    }

    /**
     * 根据产品代码查询订单
     * 
     * @param productCode 产品代码
     * @return 订单列表
     */
    public List<ProductionOrder> findOrdersByProductCode(String productCode) {
        // 简化实现，实际可能需要通过产品ID关联查询
        return new ArrayList<>();
    }

    /**
     * 更新订单
     * 
     * @param order 订单对象
     * @return 更新后的订单
     */
    @Transactional
    public ProductionOrder updateOrder(ProductionOrder order) {
        return productionOrderRepository.save(order);
    }

    /**
     * 提交订单审批
     * 
     * @param orderNumber 订单编号
     * @return 提交后的订单
     */
    @Transactional
    public ProductionOrder submitOrder(String orderNumber) {
        Optional<ProductionOrder> optionalOrder = productionOrderRepository.findByOrderNumber(orderNumber);
        if (optionalOrder.isPresent()) {
            ProductionOrder order = optionalOrder.get();
            if (order.getStatus() == ProductionOrder.OrderStatus.DRAFT) {
                order.setStatus(ProductionOrder.OrderStatus.SUBMITTED);
                return productionOrderRepository.save(order);
            } else {
                throw new IllegalStateException("Only draft orders can be submitted");
            }
        } else {
            throw new IllegalArgumentException("Order not found: " + orderNumber);
        }
    }

    /**
     * 批准订单
     * 
     * @param orderNumber 订单编号
     * @param approver    批准人
     * @return 批准后的订单
     */
    @Transactional
    public ProductionOrder approveOrder(String orderNumber, String approver) {
        Optional<ProductionOrder> optionalOrder = productionOrderRepository.findByOrderNumber(orderNumber);
        if (optionalOrder.isPresent()) {
            ProductionOrder order = optionalOrder.get();
            if (order.getStatus() == ProductionOrder.OrderStatus.SUBMITTED) {
                order.setStatus(ProductionOrder.OrderStatus.APPROVED);
                order.setApprover(approver);
                order.setApprovedAt(LocalDateTime.now());
                return productionOrderRepository.save(order);
            } else {
                throw new IllegalStateException("Only submitted orders can be approved");
            }
        } else {
            throw new IllegalArgumentException("Order not found: " + orderNumber);
        }
    }

    /**
     * 取消订单
     * 
     * @param orderNumber 订单编号
     * @return 取消后的订单
     */
    @Transactional
    public ProductionOrder cancelOrder(String orderNumber) {
        Optional<ProductionOrder> optionalOrder = productionOrderRepository.findByOrderNumber(orderNumber);
        if (optionalOrder.isPresent()) {
            ProductionOrder order = optionalOrder.get();
            // 只有草稿、已提交或已批准的订单可以取消
            if (order.getStatus() == ProductionOrder.OrderStatus.DRAFT ||
                    order.getStatus() == ProductionOrder.OrderStatus.SUBMITTED ||
                    order.getStatus() == ProductionOrder.OrderStatus.APPROVED) {
                order.setStatus(ProductionOrder.OrderStatus.CANCELLED);
                return productionOrderRepository.save(order);
            } else {
                throw new IllegalStateException("Cannot cancel order in current status: " + order.getStatus());
            }
        } else {
            throw new IllegalArgumentException("Order not found: " + orderNumber);
        }
    }

    /**
     * 更新生产订单
     * 
     * @param order 订单对象
     * @return 更新后的订单
     */
    @Transactional
    public ProductionOrder updateProductionOrder(ProductionOrder order) {
        // 验证订单是否存在
        Long orderId = order.getId();
        if (orderId == null) {
            throw new IllegalArgumentException("订单ID不能为空");
        }

        ProductionOrder existingOrder = getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("订单不存在: " + orderId));

        // 检查状态是否允许更新
        if (existingOrder.getStatus() != ProductionOrder.OrderStatus.DRAFT &&
                existingOrder.getStatus() != ProductionOrder.OrderStatus.SUBMITTED) {
            throw new IllegalStateException("只能更新草稿或已提交状态的订单");
        }

        // 更新非状态字段
        existingOrder.setProductId(order.getProductId());
        existingOrder.setProductName(order.getProductName());
        existingOrder.setBatchSize(order.getBatchSize());
        existingOrder.setPlannedStartDate(order.getPlannedStartDate());
        existingOrder.setPlannedEndDate(order.getPlannedEndDate());
        existingOrder.setPriority(order.getPriority());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        return productionOrderRepository.save(existingOrder);
    }

    /**
     * 删除订单
     * 
     * @param id 订单ID
     */
    @Transactional
    public void deleteOrder(Long id) {
        ProductionOrder order = getOrderById(id).orElseThrow(() -> new IllegalArgumentException("订单不存在: " + id));

        if (order.getStatus() != ProductionOrder.OrderStatus.DRAFT &&
                order.getStatus() != ProductionOrder.OrderStatus.CANCELLED) {
            throw new IllegalStateException("只能删除草稿或已取消的订单");
        }

        productionOrderRepository.delete(order);
    }

    /**
     * 删除订单（兼容方法）
     * 
     * @param id 订单ID
     * @return 删除结果
     */
    @Transactional
    public boolean deleteProductionOrder(Long id) {
        productionOrderRepository.deleteById(id);
        return true;
    }

    /**
     * 通过ID提交订单
     * 
     * @param id        订单ID
     * @param submitter 提交人
     * @return 提交后的订单
     */
    @Transactional
    public ProductionOrder submitProductionOrder(Long id, String submitter) {
        Optional<ProductionOrder> optionalOrder = productionOrderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            ProductionOrder order = optionalOrder.get();
            if (order.getStatus() == ProductionOrder.OrderStatus.DRAFT) {
                order.setStatus(ProductionOrder.OrderStatus.SUBMITTED);
                return productionOrderRepository.save(order);
            } else {
                throw new IllegalStateException("Only draft orders can be submitted");
            }
        } else {
            throw new IllegalArgumentException("Order not found: " + id);
        }
    }

    /**
     * 通过ID批准订单
     * 
     * @param id       订单ID
     * @param approver 批准人
     * @return 批准后的订单
     */
    @Transactional
    public ProductionOrder approveProductionOrder(Long id, String approver) {
        Optional<ProductionOrder> optionalOrder = productionOrderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            ProductionOrder order = optionalOrder.get();
            if (order.getStatus() == ProductionOrder.OrderStatus.SUBMITTED) {
                order.setStatus(ProductionOrder.OrderStatus.APPROVED);
                order.setApprover(approver);
                order.setApprovedAt(LocalDateTime.now());
                return productionOrderRepository.save(order);
            } else {
                throw new IllegalStateException("Only submitted orders can be approved");
            }
        } else {
            throw new IllegalArgumentException("Order not found: " + id);
        }
    }

    /**
     * 拒绝订单
     * 
     * @param id       订单ID
     * @param rejecter 拒绝人
     * @param reason   拒绝原因
     * @return 拒绝后的订单
     */
    @Transactional
    public ProductionOrder rejectProductionOrder(Long id, String rejecter, String reason) {
        Optional<ProductionOrder> optionalOrder = productionOrderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            ProductionOrder order = optionalOrder.get();
            if (order.getStatus() == ProductionOrder.OrderStatus.SUBMITTED) {
                order.setStatus(ProductionOrder.OrderStatus.CANCELLED);
                order.setApprover(rejecter);
                // 假设实体有reason字段，否则需要添加
                return productionOrderRepository.save(order);
            } else {
                throw new IllegalStateException("Only submitted orders can be rejected");
            }
        } else {
            throw new IllegalArgumentException("Order not found: " + id);
        }
    }

    /**
     * 通过ID取消订单
     * 
     * @param id        订单ID
     * @param canceller 取消人
     * @param reason    取消原因
     * @return 取消后的订单
     */
    @Transactional
    public ProductionOrder cancelProductionOrder(Long id, String canceller, String reason) {
        Optional<ProductionOrder> optionalOrder = productionOrderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            ProductionOrder order = optionalOrder.get();
            // 只有草稿、已提交或已批准的订单可以取消
            if (order.getStatus() == ProductionOrder.OrderStatus.DRAFT ||
                    order.getStatus() == ProductionOrder.OrderStatus.SUBMITTED ||
                    order.getStatus() == ProductionOrder.OrderStatus.APPROVED) {
                order.setStatus(ProductionOrder.OrderStatus.CANCELLED);
                // 假设实体有cancelledBy和cancelledReason字段，否则需要添加
                return productionOrderRepository.save(order);
            } else {
                throw new IllegalStateException("Cannot cancel order in current status: " + order.getStatus());
            }
        } else {
            throw new IllegalArgumentException("Order not found: " + id);
        }
    }

    /**
     * 生成订单编号
     * 
     * @return 订单编号
     */
    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + "-"
                + UUID.randomUUID().toString().substring(0, 8);
    }
}