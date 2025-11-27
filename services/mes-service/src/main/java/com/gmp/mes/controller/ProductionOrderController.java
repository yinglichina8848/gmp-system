package com.gmp.mes.controller;

import com.gmp.mes.entity.ProductionOrder;
import com.gmp.mes.service.ProductionOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 生产订单控制器 - 提供生产订单相关的RESTful API接口
 * 
 * @author gmp-system
 */
@RestController
@RequestMapping("/api/production-orders")
public class ProductionOrderController {

    @Autowired
    private ProductionOrderService productionOrderService;

    /**
     * 创建生产订单
     * 
     * @param productionOrder 生产订单信息
     * @return 创建的生产订单
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<ProductionOrder> createProductionOrder(@RequestBody ProductionOrder productionOrder) {
        ProductionOrder createdOrder = productionOrderService.createProductionOrder(productionOrder);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * 获取所有生产订单
     * 
     * @return 生产订单列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<List<ProductionOrder>> getAllProductionOrders() {
        List<ProductionOrder> orders = productionOrderService.getAllProductionOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据ID获取生产订单
     * 
     * @param id 订单ID
     * @return 生产订单
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionOrder> getProductionOrderById(@PathVariable Long id) {
        Optional<ProductionOrder> order = productionOrderService.getProductionOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据订单编号获取生产订单
     * 
     * @param orderNumber 订单编号
     * @return 生产订单
     */
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionOrder> getProductionOrderByNumber(@PathVariable String orderNumber) {
        Optional<ProductionOrder> order = productionOrderService.getProductionOrderByOrderNumber(orderNumber);
        return order.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据状态获取生产订单
     * 
     * @param status 订单状态
     * @return 生产订单列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<List<ProductionOrder>> getProductionOrdersByStatus(@PathVariable ProductionOrder.OrderStatus status) {
        List<ProductionOrder> orders = productionOrderService.getProductionOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据申请人获取生产订单
     * 
     * @param applicant 申请人
     * @return 生产订单列表
     */
    @GetMapping("/applicant/{applicant}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or @securityService.isUser(#applicant)")
    public ResponseEntity<List<ProductionOrder>> getProductionOrdersByApplicant(@PathVariable String applicant) {
        List<ProductionOrder> orders = productionOrderService.getProductionOrdersByApplicant(applicant);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据日期范围获取生产订单
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 生产订单列表
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<List<ProductionOrder>> getProductionOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ProductionOrder> orders = productionOrderService.getProductionOrdersByPlanDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    /**
     * 更新生产订单
     * 
     * @param id 订单ID
     * @param productionOrder 订单信息
     * @return 更新后的订单
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<ProductionOrder> updateProductionOrder(@PathVariable Long id, @RequestBody ProductionOrder productionOrder) {
        // 确保ID一致
        if (!productionOrder.getId().equals(id)) {
            productionOrder.setId(id);
        }
        try {
            ProductionOrder updatedOrder = productionOrderService.updateProductionOrder(productionOrder);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 提交生产订单审批
     * 
     * @param id 订单ID
     * @param request 提交请求，包含提交人
     * @return 更新后的订单
     */
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or @securityService.isUser(#request['submitter'])")
    public ResponseEntity<ProductionOrder> submitProductionOrder(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String submitter = (String) request.get("submitter");
            ProductionOrder updatedOrder = productionOrderService.submitProductionOrder(id, submitter);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 批准生产订单
     * 
     * @param id 订单ID
     * @param request 批准请求，包含审批人
     * @return 更新后的订单
     */
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<ProductionOrder> approveProductionOrder(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String approver = (String) request.get("approver");
            ProductionOrder updatedOrder = productionOrderService.approveProductionOrder(id, approver);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 拒绝生产订单
     * 
     * @param id 订单ID
     * @param request 拒绝请求，包含审批人和拒绝原因
     * @return 更新后的订单
     */
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<ProductionOrder> rejectProductionOrder(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String approver = (String) request.get("approver");
            String reason = (String) request.get("reason");
            ProductionOrder updatedOrder = productionOrderService.rejectProductionOrder(id, approver, reason);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 取消生产订单
     * 
     * @param id 订单ID
     * @param request 取消请求，包含操作人和取消原因
     * @return 更新后的订单
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<ProductionOrder> cancelProductionOrder(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String operator = (String) request.get("operator");
            String reason = (String) request.get("reason");
            ProductionOrder updatedOrder = productionOrderService.cancelProductionOrder(id, operator, reason);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除生产订单
     * 
     * @param id 订单ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductionOrder(@PathVariable Long id) {
        try {
            productionOrderService.deleteProductionOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}