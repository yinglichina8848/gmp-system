package com.gmp.mes.service;

import com.gmp.mes.entity.ProductionOrder;
import com.gmp.mes.repository.ProductionOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 生产订单服务类的单元测试
 * 
 * @author gmp-system
 */
class ProductionOrderServiceTest {

    @Mock
    private ProductionOrderRepository productionOrderRepository;

    @InjectMocks
    private ProductionOrderService productionOrderService;

    private ProductionOrder testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testOrder = new ProductionOrder();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-2024-001");
        testOrder.setProductId("PROD-001");
        testOrder.setProductName("Test Product");
        testOrder.setQuantity(100);
        testOrder.setUnit("pcs");
        testOrder.setPlanStartTime(LocalDateTime.now());
        testOrder.setPlanEndTime(LocalDateTime.now().plusDays(1));
        testOrder.setStatus(ProductionOrder.OrderStatus.PENDING);
        testOrder.setPriority(ProductionOrder.OrderPriority.MEDIUM);
        testOrder.setApplicant("张三");
        testOrder.setDepartment("生产部");
    }

    @Test
    void testCreateProductionOrder() {
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(testOrder);
        
        ProductionOrder createdOrder = productionOrderService.createProductionOrder(testOrder);
        
        assertNotNull(createdOrder);
        assertEquals("ORD-2024-001", createdOrder.getOrderNumber());
        assertEquals(ProductionOrder.OrderStatus.PENDING, createdOrder.getStatus());
        verify(productionOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testGetProductionOrderById() {
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        
        Optional<ProductionOrder> foundOrder = productionOrderService.getProductionOrderById(1L);
        
        assertTrue(foundOrder.isPresent());
        assertEquals("ORD-2024-001", foundOrder.get().getOrderNumber());
        verify(productionOrderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductionOrderByIdNotFound() {
        when(productionOrderRepository.findById(99L)).thenReturn(Optional.empty());
        
        Optional<ProductionOrder> foundOrder = productionOrderService.getProductionOrderById(99L);
        
        assertFalse(foundOrder.isPresent());
        verify(productionOrderRepository, times(1)).findById(99L);
    }

    @Test
    void testGetProductionOrderByOrderNumber() {
        when(productionOrderRepository.findByOrderNumber("ORD-2024-001")).thenReturn(Optional.of(testOrder));
        
        Optional<ProductionOrder> foundOrder = productionOrderService.getProductionOrderByOrderNumber("ORD-2024-001");
        
        assertTrue(foundOrder.isPresent());
        assertEquals(1L, foundOrder.get().getId());
        verify(productionOrderRepository, times(1)).findByOrderNumber("ORD-2024-001");
    }

    @Test
    void testGetAllProductionOrders() {
        List<ProductionOrder> orders = Arrays.asList(testOrder);
        when(productionOrderRepository.findAll()).thenReturn(orders);
        
        List<ProductionOrder> allOrders = productionOrderService.getAllProductionOrders();
        
        assertNotNull(allOrders);
        assertEquals(1, allOrders.size());
        verify(productionOrderRepository, times(1)).findAll();
    }

    @Test
    void testUpdateProductionOrder() {
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(testOrder);
        
        testOrder.setQuantity(150);
        testOrder.setRemark("Updated remark");
        
        ProductionOrder updatedOrder = productionOrderService.updateProductionOrder(testOrder);
        
        assertNotNull(updatedOrder);
        assertEquals(150, updatedOrder.getQuantity());
        assertEquals("Updated remark", updatedOrder.getRemark());
        verify(productionOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testUpdateProductionOrderNotFound() {
        when(productionOrderRepository.findById(99L)).thenReturn(Optional.empty());
        
        testOrder.setId(99L);
        
        assertThrows(RuntimeException.class, () -> {
            productionOrderService.updateProductionOrder(testOrder);
        });
        
        verify(productionOrderRepository, times(1)).findById(99L);
    }

    @Test
    void testDeleteProductionOrder() {
        when(productionOrderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productionOrderRepository).deleteById(1L);
        
        productionOrderService.deleteProductionOrder(1L);
        
        verify(productionOrderRepository, times(1)).existsById(1L);
        verify(productionOrderRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProductionOrderNotFound() {
        when(productionOrderRepository.existsById(99L)).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> {
            productionOrderService.deleteProductionOrder(99L);
        });
        
        verify(productionOrderRepository, times(1)).existsById(99L);
    }

    @Test
    void testSubmitProductionOrder() {
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(testOrder);
        
        ProductionOrder submittedOrder = productionOrderService.submitProductionOrder(1L);
        
        assertEquals(ProductionOrder.OrderStatus.SUBMITTED, submittedOrder.getStatus());
        verify(productionOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testApproveProductionOrder() {
        testOrder.setStatus(ProductionOrder.OrderStatus.SUBMITTED);
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(testOrder);
        
        ProductionOrder approvedOrder = productionOrderService.approveProductionOrder(1L, "李四");
        
        assertEquals(ProductionOrder.OrderStatus.APPROVED, approvedOrder.getStatus());
        assertEquals("李四", approvedOrder.getApprover());
        verify(productionOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testRejectProductionOrder() {
        testOrder.setStatus(ProductionOrder.OrderStatus.SUBMITTED);
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(testOrder);
        
        ProductionOrder rejectedOrder = productionOrderService.rejectProductionOrder(1L, "李四", "理由：库存不足");
        
        assertEquals(ProductionOrder.OrderStatus.REJECTED, rejectedOrder.getStatus());
        assertEquals("李四", rejectedOrder.getApprover());
        assertTrue(rejectedOrder.getRemark().contains("理由：库存不足"));
        verify(productionOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testCancelProductionOrder() {
        testOrder.setStatus(ProductionOrder.OrderStatus.APPROVED);
        when(productionOrderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(productionOrderRepository.save(any(ProductionOrder.class))).thenReturn(testOrder);
        
        ProductionOrder cancelledOrder = productionOrderService.cancelProductionOrder(1L, "取消原因：计划变更");
        
        assertEquals(ProductionOrder.OrderStatus.CANCELLED, cancelledOrder.getStatus());
        assertTrue(cancelledOrder.getRemark().contains("取消原因：计划变更"));
        verify(productionOrderRepository, times(1)).save(testOrder);
    }

    @Test
    void testFindProductionOrdersByStatus() {
        List<ProductionOrder> orders = Arrays.asList(testOrder);
        when(productionOrderRepository.findByStatus(ProductionOrder.OrderStatus.PENDING)).thenReturn(orders);
        
        List<ProductionOrder> foundOrders = productionOrderService.findProductionOrdersByStatus(ProductionOrder.OrderStatus.PENDING);
        
        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
        verify(productionOrderRepository, times(1)).findByStatus(ProductionOrder.OrderStatus.PENDING);
    }

    @Test
    void testGenerateOrderNumber() {
        String orderNumber = productionOrderService.generateOrderNumber();
        
        assertNotNull(orderNumber);
        assertTrue(orderNumber.startsWith("ORD-"));
        assertTrue(orderNumber.length() > 10);
    }
}