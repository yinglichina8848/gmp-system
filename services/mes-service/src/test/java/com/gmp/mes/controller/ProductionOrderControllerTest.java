package com.gmp.mes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.mes.entity.ProductionOrder;
import com.gmp.mes.service.ProductionOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 生产订单控制器的单元测试
 * 
 * @author gmp-system
 */
@WebMvcTest(ProductionOrderController.class)
class ProductionOrderControllerTest {

    @Mock
    private ProductionOrderService productionOrderService;

    @InjectMocks
    private ProductionOrderController productionOrderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ProductionOrder testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productionOrderController).build();
        objectMapper = new ObjectMapper();
        
        // 创建测试订单对象
        testOrder = new ProductionOrder();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-2024-001");
        testOrder.setProductCode("PRD-001");
        testOrder.setProductName("产品A");
        testOrder.setQuantity(1000);
        testOrder.setPriority(ProductionOrder.Priority.HIGH);
        testOrder.setStatus(ProductionOrder.Status.PENDING);
        testOrder.setCreator("张三");
        testOrder.setCreateTime(LocalDateTime.now());
        testOrder.setEstimatedStartTime(LocalDateTime.now().plusDays(1));
        testOrder.setEstimatedEndTime(LocalDateTime.now().plusDays(5));
    }

    @Test
    void testCreateProductionOrder() throws Exception {
        when(productionOrderService.createProductionOrder(any(ProductionOrder.class))).thenReturn(testOrder);
        
        mockMvc.perform(post("/mes/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value("ORD-2024-001"))
                .andExpect(jsonPath("$.productName").value("产品A"));
    }

    @Test
    void testGetProductionOrderById() throws Exception {
        when(productionOrderService.getProductionOrderById(1L)).thenReturn(Optional.of(testOrder));
        
        mockMvc.perform(get("/mes/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value("ORD-2024-001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetProductionOrderByOrderNumber() throws Exception {
        when(productionOrderService.getProductionOrderByOrderNumber("ORD-2024-001")).thenReturn(Optional.of(testOrder));
        
        mockMvc.perform(get("/mes/api/orders/by-number/ORD-2024-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("产品A"));
    }

    @Test
    void testGetAllProductionOrders() throws Exception {
        List<ProductionOrder> orders = Arrays.asList(testOrder);
        Page<ProductionOrder> page = new PageImpl<>(orders, PageRequest.of(0, 10), orders.size());
        
        when(productionOrderService.getAllProductionOrders(any(PageRequest.class))).thenReturn(page);
        
        mockMvc.perform(get("/mes/api/orders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].orderNumber").value("ORD-2024-001"));
    }

    @Test
    void testUpdateProductionOrder() throws Exception {
        testOrder.setProductName("产品A-更新");
        when(productionOrderService.updateProductionOrder(any(ProductionOrder.class))).thenReturn(testOrder);
        
        mockMvc.perform(put("/mes/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("产品A-更新"));
    }

    @Test
    void testDeleteProductionOrder() throws Exception {
        mockMvc.perform(delete("/mes/api/orders/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSubmitOrder() throws Exception {
        when(productionOrderService.submitOrder(anyLong())).thenReturn(testOrder);
        
        mockMvc.perform(post("/mes/api/orders/1/submit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    void testApproveOrder() throws Exception {
        when(productionOrderService.approveOrder(anyLong(), anyString())).thenReturn(testOrder);
        
        Map<String, String> approvalInfo = new HashMap<>();
        approvalInfo.put("approver", "李四");
        
        mockMvc.perform(post("/mes/api/orders/1/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(approvalInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testRejectOrder() throws Exception {
        when(productionOrderService.rejectOrder(anyLong(), anyString(), anyString())).thenReturn(testOrder);
        
        Map<String, String> rejectionInfo = new HashMap<>();
        rejectionInfo.put("approver", "李四");
        rejectionInfo.put("reason", "产品信息不完整");
        
        mockMvc.perform(post("/mes/api/orders/1/reject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rejectionInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void testCancelOrder() throws Exception {
        when(productionOrderService.cancelOrder(anyLong(), anyString())).thenReturn(testOrder);
        
        Map<String, String> cancelInfo = new HashMap<>();
        cancelInfo.put("reason", "客户取消订单");
        
        mockMvc.perform(post("/mes/api/orders/1/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cancelInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @Test
    void testSearchProductionOrders() throws Exception {
        List<ProductionOrder> orders = Arrays.asList(testOrder);
        Page<ProductionOrder> page = new PageImpl<>(orders, PageRequest.of(0, 10), orders.size());
        
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("productCode", "PRD-001");
        searchParams.put("status", "PENDING");
        
        when(productionOrderService.searchProductionOrders(any(Map.class), any(PageRequest.class))).thenReturn(page);
        
        mockMvc.perform(post("/mes/api/orders/search?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void testFindOrdersByProductCode() throws Exception {
        List<ProductionOrder> orders = Arrays.asList(testOrder);
        
        when(productionOrderService.findOrdersByProductCode(anyString())).thenReturn(orders);
        
        mockMvc.perform(get("/mes/api/orders/product/PRD-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindOrdersByStatus() throws Exception {
        List<ProductionOrder> orders = Arrays.asList(testOrder);
        
        when(productionOrderService.findOrdersByStatus(any())).thenReturn(orders);
        
        mockMvc.perform(get("/mes/api/orders/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}