package com.gmp.mes.service;

import com.gmp.mes.entity.ProductionBatch;
import com.gmp.mes.entity.ProductionOrder;
import com.gmp.mes.repository.ProductionBatchRepository;
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
 * 生产批次服务类的单元测试
 * 
 * @author gmp-system
 */
class ProductionBatchServiceTest {

    @Mock
    private ProductionBatchRepository productionBatchRepository;

    @Mock
    private ProductionOrderService productionOrderService;

    @InjectMocks
    private ProductionBatchService productionBatchService;

    private ProductionBatch testBatch;
    private ProductionOrder testOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 创建测试订单
        testOrder = new ProductionOrder();
        testOrder.setId(1L);
        testOrder.setOrderNumber("ORD-2024-001");
        
        // 创建测试批次
        testBatch = new ProductionBatch();
        testBatch.setId(1L);
        testBatch.setBatchNumber("BATCH-2024-001");
        testBatch.setOrderId(1L);
        testBatch.setOrder(testOrder);
        testBatch.setProductId("PROD-001");
        testBatch.setProductName("Test Product");
        testBatch.setEquipmentId("EQP-001");
        testBatch.setEquipmentName("Test Equipment");
        testBatch.setPlanQuantity(100);
        testBatch.setActualQuantity(0);
        testBatch.setStatus(ProductionBatch.BatchStatus.PENDING);
        testBatch.setOperator("操作员1");
    }

    @Test
    void testCreateProductionBatch() {
        when(productionOrderService.getProductionOrderById(1L)).thenReturn(Optional.of(testOrder));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        ProductionBatch createdBatch = productionBatchService.createProductionBatch(testBatch);
        
        assertNotNull(createdBatch);
        assertEquals("BATCH-2024-001", createdBatch.getBatchNumber());
        assertEquals(ProductionBatch.BatchStatus.PENDING, createdBatch.getStatus());
        verify(productionBatchRepository, times(1)).save(testBatch);
    }

    @Test
    void testGetProductionBatchById() {
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        
        Optional<ProductionBatch> foundBatch = productionBatchService.getProductionBatchById(1L);
        
        assertTrue(foundBatch.isPresent());
        assertEquals("BATCH-2024-001", foundBatch.get().getBatchNumber());
        verify(productionBatchRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductionBatchByBatchNumber() {
        when(productionBatchRepository.findByBatchNumber("BATCH-2024-001")).thenReturn(Optional.of(testBatch));
        
        Optional<ProductionBatch> foundBatch = productionBatchService.getProductionBatchByBatchNumber("BATCH-2024-001");
        
        assertTrue(foundBatch.isPresent());
        assertEquals(1L, foundBatch.get().getId());
        verify(productionBatchRepository, times(1)).findByBatchNumber("BATCH-2024-001");
    }

    @Test
    void testGetAllProductionBatches() {
        List<ProductionBatch> batches = Arrays.asList(testBatch);
        when(productionBatchRepository.findAll()).thenReturn(batches);
        
        List<ProductionBatch> allBatches = productionBatchService.getAllProductionBatches();
        
        assertNotNull(allBatches);
        assertEquals(1, allBatches.size());
        verify(productionBatchRepository, times(1)).findAll();
    }

    @Test
    void testGetProductionBatchesByOrderId() {
        List<ProductionBatch> batches = Arrays.asList(testBatch);
        when(productionBatchRepository.findByOrderId(1L)).thenReturn(batches);
        
        List<ProductionBatch> orderBatches = productionBatchService.getProductionBatchesByOrderId(1L);
        
        assertNotNull(orderBatches);
        assertEquals(1, orderBatches.size());
        verify(productionBatchRepository, times(1)).findByOrderId(1L);
    }

    @Test
    void testUpdateProductionBatch() {
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        testBatch.setPlanQuantity(150);
        testBatch.setRemark("Updated batch");
        
        ProductionBatch updatedBatch = productionBatchService.updateProductionBatch(testBatch);
        
        assertNotNull(updatedBatch);
        assertEquals(150, updatedBatch.getPlanQuantity());
        assertEquals("Updated batch", updatedBatch.getRemark());
        verify(productionBatchRepository, times(1)).save(testBatch);
    }

    @Test
    void testDeleteProductionBatch() {
        when(productionBatchRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productionBatchRepository).deleteById(1L);
        
        productionBatchService.deleteProductionBatch(1L);
        
        verify(productionBatchRepository, times(1)).existsById(1L);
        verify(productionBatchRepository, times(1)).deleteById(1L);
    }

    @Test
    void testStartProductionBatch() {
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        ProductionBatch startedBatch = productionBatchService.startProductionBatch(1L);
        
        assertEquals(ProductionBatch.BatchStatus.IN_PROGRESS, startedBatch.getStatus());
        assertNotNull(startedBatch.getActualStartTime());
        verify(productionBatchRepository, times(1)).save(testBatch);
    }

    @Test
    void testCompleteProductionBatch() {
        testBatch.setStatus(ProductionBatch.BatchStatus.IN_PROGRESS);
        testBatch.setPlanQuantity(100);
        testBatch.setActualQuantity(100);
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        ProductionBatch completedBatch = productionBatchService.completeProductionBatch(1L);
        
        assertEquals(ProductionBatch.BatchStatus.COMPLETED, completedBatch.getStatus());
        assertNotNull(completedBatch.getActualEndTime());
        verify(productionBatchRepository, times(1)).save(testBatch);
    }

    @Test
    void testPauseProductionBatch() {
        testBatch.setStatus(ProductionBatch.BatchStatus.IN_PROGRESS);
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        ProductionBatch pausedBatch = productionBatchService.pauseProductionBatch(1L, "设备维护");
        
        assertEquals(ProductionBatch.BatchStatus.PAUSED, pausedBatch.getStatus());
        assertTrue(pausedBatch.getRemark().contains("设备维护"));
        verify(productionBatchRepository, times(1)).save(testBatch);
    }

    @Test
    void testResumeProductionBatch() {
        testBatch.setStatus(ProductionBatch.BatchStatus.PAUSED);
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        ProductionBatch resumedBatch = productionBatchService.resumeProductionBatch(1L);
        
        assertEquals(ProductionBatch.BatchStatus.IN_PROGRESS, resumedBatch.getStatus());
        verify(productionBatchRepository, times(1)).save(testBatch);
    }

    @Test
    void testMarkProductionBatchFailed() {
        testBatch.setStatus(ProductionBatch.BatchStatus.IN_PROGRESS);
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        ProductionBatch failedBatch = productionBatchService.markProductionBatchFailed(1L, "质量问题", "操作员2");
        
        assertEquals(ProductionBatch.BatchStatus.FAILED, failedBatch.getStatus());
        assertTrue(failedBatch.getRemark().contains("质量问题"));
        assertEquals("操作员2", failedBatch.getLastOperator());
        verify(productionBatchRepository, times(1)).save(testBatch);
    }

    @Test
    void testFindProductionBatchesByStatus() {
        List<ProductionBatch> batches = Arrays.asList(testBatch);
        when(productionBatchRepository.findByStatus(ProductionBatch.BatchStatus.PENDING)).thenReturn(batches);
        
        List<ProductionBatch> foundBatches = productionBatchService.findProductionBatchesByStatus(ProductionBatch.BatchStatus.PENDING);
        
        assertNotNull(foundBatches);
        assertEquals(1, foundBatches.size());
        verify(productionBatchRepository, times(1)).findByStatus(ProductionBatch.BatchStatus.PENDING);
    }

    @Test
    void testGenerateBatchNumber() {
        String batchNumber = productionBatchService.generateBatchNumber();
        
        assertNotNull(batchNumber);
        assertTrue(batchNumber.startsWith("BATCH-"));
        assertTrue(batchNumber.length() > 12);
    }

    @Test
    void testUpdateBatchQuantity() {
        when(productionBatchRepository.findById(1L)).thenReturn(Optional.of(testBatch));
        when(productionBatchRepository.save(any(ProductionBatch.class))).thenReturn(testBatch);
        
        ProductionBatch updatedBatch = productionBatchService.updateBatchQuantity(1L, 50);
        
        assertEquals(50, updatedBatch.getActualQuantity());
        verify(productionBatchRepository, times(1)).save(testBatch);
    }
}