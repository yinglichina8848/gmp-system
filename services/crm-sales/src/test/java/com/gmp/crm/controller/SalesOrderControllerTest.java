package com.gmp.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.crm.dto.PageRequestDTO;
import com.gmp.crm.dto.SalesOrderDTO;
import com.gmp.crm.dto.SalesOrderRequestDTO;
import com.gmp.crm.service.SalesOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 销售订单控制器单元测试
 * 
 * @author TRAE AI
 */
@WebMvcTest(controllers = SalesOrderController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class SalesOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalesOrderService salesOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    private SalesOrderDTO salesOrderDTO;
    private SalesOrderRequestDTO salesOrderRequestDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        salesOrderDTO = new SalesOrderDTO();
        salesOrderDTO.setId(1L);
        salesOrderDTO.setOrderNumber("SO20240101ABCD");
        salesOrderDTO.setStatus("PENDING");
        salesOrderDTO.setTotalAmount(BigDecimal.valueOf(1000.00));
        salesOrderDTO.setRemarks("测试订单");
        salesOrderDTO.setCreatedBy("admin");
        salesOrderDTO.setCreatedAt(LocalDateTime.now());
        salesOrderDTO.setUpdatedAt(LocalDateTime.now());

        // 初始化订单项
        SalesOrderRequestDTO.OrderItemRequestDTO orderItemRequest = new SalesOrderRequestDTO.OrderItemRequestDTO();
        orderItemRequest.setProductCode("P001");
        orderItemRequest.setProductName("测试产品");
        orderItemRequest.setUnitPrice("100.00");
        orderItemRequest.setQuantity(10);

        salesOrderRequestDTO = new SalesOrderRequestDTO();
        salesOrderRequestDTO.setCustomerId(1L);
        salesOrderRequestDTO.setRemarks("测试订单");
        salesOrderRequestDTO.setItems(Arrays.asList(orderItemRequest));
    }

    @Test
    void createSalesOrder_Success() throws Exception {
        // 配置mock行为
        when(salesOrderService.createSalesOrder(any(SalesOrderRequestDTO.class), anyString()))
                .thenReturn(salesOrderDTO);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesOrderRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.orderNumber").value("SO20240101ABCD"));
    }

    @Test
    void createSalesOrder_InvalidRequest() throws Exception {
        // 准备无效的请求数据
        salesOrderRequestDTO.setCustomerId(null); // 客户ID为空，应该触发验证错误

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sales-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(salesOrderRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateOrderStatus_Success() throws Exception {
        // 配置mock行为
        salesOrderDTO.setStatus("CONFIRMED");
        when(salesOrderService.updateOrderStatus(anyLong(), anyString())).thenReturn(salesOrderDTO);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.put("/api/sales-orders/1/status")
                .param("status", "CONFIRMED"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value("CONFIRMED"));
    }

    @Test
    void getSalesOrder_Success() throws Exception {
        // 配置mock行为
        when(salesOrderService.getSalesOrderById(1L)).thenReturn(Optional.of(salesOrderDTO));

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sales-orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.orderNumber").value("SO20240101ABCD"));
    }

    @Test
    void getSalesOrder_NotFound() throws Exception {
        // 配置mock行为
        when(salesOrderService.getSalesOrderById(1L)).thenReturn(Optional.empty());

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sales-orders/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("订单不存在"));
    }

    @Test
    void getSalesOrderByNumber_Success() throws Exception {
        // 配置mock行为
        when(salesOrderService.getSalesOrderByOrderNumber("SO20240101ABCD")).thenReturn(Optional.of(salesOrderDTO));

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sales-orders/by-number/SO20240101ABCD"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.orderNumber").value("SO20240101ABCD"));
    }

    @Test
    void getSalesOrderByNumber_NotFound() throws Exception {
        // 配置mock行为
        when(salesOrderService.getSalesOrderByOrderNumber("SO999999999999")).thenReturn(Optional.empty());

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sales-orders/by-number/SO999999999999"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("订单不存在"));
    }

    @Test
    void getSalesOrders_Pagination() throws Exception {
        // 准备分页数据
        List<SalesOrderDTO> orderList = Arrays.asList(salesOrderDTO);
        Page<SalesOrderDTO> orderPage = new PageImpl<>(orderList);

        // 配置mock行为
        when(salesOrderService.getSalesOrders(any(PageRequestDTO.class))).thenReturn(orderPage);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sales-orders")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total").value(1));
    }

    @Test
    void getSalesOrdersByCustomer_Success() throws Exception {
        // 准备分页数据
        List<SalesOrderDTO> orderList = Arrays.asList(salesOrderDTO);
        Page<SalesOrderDTO> orderPage = new PageImpl<>(orderList);

        // 配置mock行为
        when(salesOrderService.getSalesOrdersByCustomer(anyLong(), any(PageRequestDTO.class))).thenReturn(orderPage);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sales-orders/customer/1")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id").value(1L));
    }

    @Test
    void searchSalesOrders_Success() throws Exception {
        // 准备分页数据
        List<SalesOrderDTO> orderList = Arrays.asList(salesOrderDTO);
        Page<SalesOrderDTO> orderPage = new PageImpl<>(orderList);

        // 配置mock行为
        when(salesOrderService.searchSalesOrders(anyString(), anyString(), any(PageRequestDTO.class)))
                .thenReturn(orderPage);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/sales-orders/search")
                .param("keyword", "测试")
                .param("status", "PENDING")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id").value(1L));
    }

    @Test
    void cancelOrder_Success() throws Exception {
        // 配置mock行为
        when(salesOrderService.cancelOrder(anyLong())).thenReturn(true);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.put("/api/sales-orders/1/cancel"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true));
    }

}