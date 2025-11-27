package com.gmp.mes.controller;

import com.gmp.mes.entity.ProductionBatch;
import com.gmp.mes.service.ProductionBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 生产批次控制器 - 提供生产批次相关的RESTful API接口
 * 
 * @author gmp-system
 */
@RestController
@RequestMapping("/api/production-batches")
public class ProductionBatchController {

    @Autowired
    private ProductionBatchService productionBatchService;

    /**
     * 创建生产批次
     * 
     * @param productionBatch 生产批次信息
     * @return 创建的生产批次
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> createProductionBatch(@RequestBody ProductionBatch productionBatch) {
        ProductionBatch createdBatch = productionBatchService.createProductionBatch(productionBatch);
        return new ResponseEntity<>(createdBatch, HttpStatus.CREATED);
    }

    /**
     * 获取所有生产批次
     * 
     * @return 生产批次列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<List<ProductionBatch>> getAllProductionBatches() {
        List<ProductionBatch> batches = productionBatchService.getAllProductionBatches();
        return ResponseEntity.ok(batches);
    }

    /**
     * 根据ID获取生产批次
     * 
     * @param id 批次ID
     * @return 生产批次
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> getProductionBatchById(@PathVariable Long id) {
        Optional<ProductionBatch> batch = productionBatchService.getProductionBatchById(id);
        return batch.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据批次编号获取生产批次
     * 
     * @param batchNumber 批次编号
     * @return 生产批次
     */
    @GetMapping("/number/{batchNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> getProductionBatchByNumber(@PathVariable String batchNumber) {
        Optional<ProductionBatch> batch = productionBatchService.getProductionBatchByBatchNumber(batchNumber);
        return batch.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据订单ID获取生产批次
     * 
     * @param orderId 订单ID
     * @return 生产批次列表
     */
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<List<ProductionBatch>> getProductionBatchesByOrderId(@PathVariable Long orderId) {
        List<ProductionBatch> batches = productionBatchService.getProductionBatchesByOrderId(orderId);
        return ResponseEntity.ok(batches);
    }

    /**
     * 根据状态获取生产批次
     * 
     * @param status 批次状态
     * @return 生产批次列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<List<ProductionBatch>> getProductionBatchesByStatus(@PathVariable ProductionBatch.BatchStatus status) {
        List<ProductionBatch> batches = productionBatchService.getProductionBatchesByStatus(status);
        return ResponseEntity.ok(batches);
    }

    /**
     * 根据设备ID获取生产批次
     * 
     * @param equipmentId 设备ID
     * @return 生产批次列表
     */
    @GetMapping("/equipment/{equipmentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<List<ProductionBatch>> getProductionBatchesByEquipmentId(@PathVariable String equipmentId) {
        List<ProductionBatch> batches = productionBatchService.getProductionBatchesByEquipmentId(equipmentId);
        return ResponseEntity.ok(batches);
    }

    /**
     * 根据日期范围获取生产批次
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 生产批次列表
     */
    @GetMapping("/time-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<List<ProductionBatch>> getProductionBatchesByTimeRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<ProductionBatch> batches = productionBatchService.getProductionBatchesByTimeRange(startTime, endTime);
        return ResponseEntity.ok(batches);
    }

    /**
     * 获取所有活跃的生产批次
     * 
     * @return 活跃批次列表
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<List<ProductionBatch>> getActiveProductionBatches() {
        List<ProductionBatch> batches = productionBatchService.getActiveProductionBatches();
        return ResponseEntity.ok(batches);
    }

    /**
     * 更新生产批次
     * 
     * @param id 批次ID
     * @param productionBatch 批次信息
     * @return 更新后的批次
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<ProductionBatch> updateProductionBatch(@PathVariable Long id, @RequestBody ProductionBatch productionBatch) {
        // 确保ID一致
        if (!productionBatch.getId().equals(id)) {
            productionBatch.setId(id);
        }
        try {
            ProductionBatch updatedBatch = productionBatchService.updateProductionBatch(productionBatch);
            return ResponseEntity.ok(updatedBatch);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 开始生产批次
     * 
     * @param id 批次ID
     * @param request 开始请求，包含操作员
     * @return 更新后的批次
     */
    @PutMapping("/{id}/start")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> startProductionBatch(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String operator = (String) request.get("operator");
            ProductionBatch updatedBatch = productionBatchService.startProductionBatch(id, operator);
            return ResponseEntity.ok(updatedBatch);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 暂停生产批次
     * 
     * @param id 批次ID
     * @param request 暂停请求，包含操作员和暂停原因
     * @return 更新后的批次
     */
    @PutMapping("/{id}/pause")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> pauseProductionBatch(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String operator = (String) request.get("operator");
            String reason = (String) request.get("reason");
            ProductionBatch updatedBatch = productionBatchService.pauseProductionBatch(id, operator, reason);
            return ResponseEntity.ok(updatedBatch);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 恢复生产批次
     * 
     * @param id 批次ID
     * @param request 恢复请求，包含操作员
     * @return 更新后的批次
     */
    @PutMapping("/{id}/resume")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> resumeProductionBatch(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String operator = (String) request.get("operator");
            ProductionBatch updatedBatch = productionBatchService.resumeProductionBatch(id, operator);
            return ResponseEntity.ok(updatedBatch);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 完成生产批次
     * 
     * @param id 批次ID
     * @param request 完成请求，包含操作员和完成数量
     * @return 更新后的批次
     */
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> completeProductionBatch(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String operator = (String) request.get("operator");
            Integer completedQuantity = request.containsKey("completedQuantity") ? ((Number) request.get("completedQuantity")).intValue() : null;
            ProductionBatch updatedBatch = productionBatchService.completeProductionBatch(id, operator, completedQuantity);
            return ResponseEntity.ok(updatedBatch);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 标记生产批次为失败
     * 
     * @param id 批次ID
     * @param request 失败请求，包含操作员和失败原因
     * @return 更新后的批次
     */
    @PutMapping("/{id}/fail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<ProductionBatch> failProductionBatch(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String operator = (String) request.get("operator");
            String reason = (String) request.get("reason");
            ProductionBatch updatedBatch = productionBatchService.failProductionBatch(id, operator, reason);
            return ResponseEntity.ok(updatedBatch);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除生产批次
     * 
     * @param id 批次ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProductionBatch(@PathVariable Long id) {
        try {
            productionBatchService.deleteProductionBatch(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}