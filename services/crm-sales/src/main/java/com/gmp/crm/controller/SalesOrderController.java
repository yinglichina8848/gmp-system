package com.gmp.crm.controller;

import com.gmp.crm.dto.ApiResponse;
import com.gmp.crm.dto.PageRequestDTO;
import com.gmp.crm.dto.SalesOrderDTO;
import com.gmp.crm.dto.SalesOrderRequestDTO;
import com.gmp.crm.service.SalesOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * 销售订单控制器
 * 
 * @author TRAE AI
 */
@RestController
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    /**
     * 创建销售订单
     * 
     * @param request   订单请求DTO
     * @param principal 当前用户信息
     * @return 创建的订单DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SalesOrderDTO>> createSalesOrder(
            @Valid @RequestBody SalesOrderRequestDTO request,
            Principal principal) {
        // 获取当前用户名，用于记录创建人
        String createdBy = principal != null ? principal.getName() : "SYSTEM";

        SalesOrderDTO salesOrderDTO = salesOrderService.createSalesOrder(request, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(salesOrderDTO));
    }

    /**
     * 更新订单状态
     * 
     * @param id     订单ID
     * @param status 新状态
     * @return 更新后的订单DTO
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<SalesOrderDTO>> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        SalesOrderDTO salesOrderDTO = salesOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success(salesOrderDTO));
    }

    /**
     * 获取订单详情
     * 
     * @param id 订单ID
     * @return 订单DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalesOrderDTO>> getSalesOrder(@PathVariable Long id) {
        return salesOrderService.getSalesOrderById(id)
                .map(orderDTO -> ResponseEntity.ok(ApiResponse.success(orderDTO)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "订单不存在")));
    }

    /**
     * 根据订单号获取订单
     * 
     * @param orderNumber 订单编号
     * @return 订单DTO
     */
    @GetMapping("/by-number/{orderNumber}")
    public ResponseEntity<ApiResponse<SalesOrderDTO>> getSalesOrderByNumber(
            @PathVariable String orderNumber) {
        return salesOrderService.getSalesOrderByOrderNumber(orderNumber)
                .map(orderDTO -> ResponseEntity.ok(ApiResponse.success(orderDTO)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "订单不存在")));
    }

    /**
     * 分页查询订单列表
     * 
     * @param pageRequest 分页请求
     * @return 订单分页列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getSalesOrders(
            PageRequestDTO pageRequest) {
        var orderPage = salesOrderService.getSalesOrders(pageRequest);
        ApiResponse.PageResponse<List<SalesOrderDTO>> pageResponse = new ApiResponse.PageResponse<>();
        pageResponse.setPage(pageRequest.getPage());
        pageResponse.setSize(pageRequest.getSize());
        pageResponse.setTotal(orderPage.getTotalElements());
        pageResponse.setTotalPages(orderPage.getTotalPages());
        pageResponse.setList(orderPage.getContent());
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 查询客户的订单列表
     * 
     * @param customerId  客户ID
     * @param pageRequest 分页请求
     * @return 订单分页列表
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<?>> getSalesOrdersByCustomer(
            @PathVariable Long customerId,
            PageRequestDTO pageRequest) {
        var orderPage = salesOrderService.getSalesOrdersByCustomer(customerId, pageRequest);
        ApiResponse.PageResponse<List<SalesOrderDTO>> pageResponse = new ApiResponse.PageResponse<>();
        pageResponse.setPage(pageRequest.getPage());
        pageResponse.setSize(pageRequest.getSize());
        pageResponse.setTotal(orderPage.getTotalElements());
        pageResponse.setTotalPages(orderPage.getTotalPages());
        pageResponse.setList(orderPage.getContent());
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 搜索订单
     * 
     * @param keyword     搜索关键字
     * @param status      订单状态
     * @param pageRequest 分页请求
     * @return 订单分页列表
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchSalesOrders(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            PageRequestDTO pageRequest) {
        var orderPage = salesOrderService.searchSalesOrders(keyword, status, pageRequest);
        ApiResponse.PageResponse<List<SalesOrderDTO>> pageResponse = new ApiResponse.PageResponse<>();
        pageResponse.setPage(pageRequest.getPage());
        pageResponse.setSize(pageRequest.getSize());
        pageResponse.setTotal(orderPage.getTotalElements());
        pageResponse.setTotalPages(orderPage.getTotalPages());
        pageResponse.setList(orderPage.getContent());
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 取消订单
     * 
     * @param id 订单ID
     * @return 操作结果
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Boolean>> cancelOrder(@PathVariable Long id) {
        boolean cancelled = salesOrderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success(cancelled));
    }

}