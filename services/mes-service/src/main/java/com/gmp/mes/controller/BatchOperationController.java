package com.gmp.mes.controller;

import com.gmp.mes.entity.BatchOperation;
import com.gmp.mes.service.BatchOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 批操作控制器 - 提供批操作相关的RESTful API接口
 * 
 * @author gmp-system
 */
@RestController
@RequestMapping("/api/batch-operations")
public class BatchOperationController {

    @Autowired
    private BatchOperationService batchOperationService;

    /**
     * 创建批操作
     * 
     * @param batchOperation 批操作信息
     * @return 创建的批操作
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<BatchOperation> createBatchOperation(@RequestBody BatchOperation batchOperation) {
        BatchOperation createdOperation = batchOperationService.createBatchOperation(batchOperation);
        return new ResponseEntity<>(createdOperation, HttpStatus.CREATED);
    }

    /**
     * 获取批操作
     * 
     * @param id 批操作ID
     * @return 批操作
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<BatchOperation> getBatchOperationById(@PathVariable Long id) {
        Optional<BatchOperation> operation = batchOperationService.getBatchOperationById(id);
        return operation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 根据操作编号获取批操作
     * 
     * @param operationNumber 操作编号
     * @return 批操作
     */
    @GetMapping("/number/{operationNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<BatchOperation> getBatchOperationByNumber(@PathVariable String operationNumber) {
        Optional<BatchOperation> operation = batchOperationService.getBatchOperationByNumber(operationNumber);
        return operation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 获取批次的所有操作
     * 
     * @param batchId 批次ID
     * @return 批操作列表
     */
    @GetMapping("/batch/{batchId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<List<BatchOperation>> getBatchOperationsByBatchId(@PathVariable Long batchId) {
        List<BatchOperation> operations = batchOperationService.getBatchOperationsByBatchId(batchId);
        return ResponseEntity.ok(operations);
    }

    /**
     * 获取批次的特定类型操作
     * 
     * @param batchId 批次ID
     * @param operationType 操作类型
     * @return 批操作列表
     */
    @GetMapping("/batch/{batchId}/type/{operationType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<List<BatchOperation>> getBatchOperationsByType(@PathVariable Long batchId, @PathVariable String operationType) {
        List<BatchOperation> operations = batchOperationService.getBatchOperationsByType(batchId, operationType);
        return ResponseEntity.ok(operations);
    }

    /**
     * 更新批操作
     * 
     * @param id 操作ID
     * @param batchOperation 操作信息
     * @return 更新后的操作
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<BatchOperation> updateBatchOperation(@PathVariable Long id, @RequestBody BatchOperation batchOperation) {
        // 确保ID一致
        if (!batchOperation.getId().equals(id)) {
            batchOperation.setId(id);
        }
        try {
            BatchOperation updatedOperation = batchOperationService.updateBatchOperation(batchOperation);
            return ResponseEntity.ok(updatedOperation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 开始执行批操作
     * 
     * @param id 操作ID
     * @param request 开始请求，包含操作参数和操作员
     * @return 更新后的操作
     */
    @PutMapping("/{id}/start")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<BatchOperation> startBatchOperation(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> parameters = (Map<String, Object>) request.getOrDefault("parameters", Map.of());
            String operator = (String) request.get("operator");
            BatchOperation updatedOperation = batchOperationService.startBatchOperation(id, parameters, operator);
            return ResponseEntity.ok(updatedOperation);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 完成批操作
     * 
     * @param id 操作ID
     * @param request 完成请求，包含操作结果和操作员
     * @return 更新后的操作
     */
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<BatchOperation> completeBatchOperation(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> results = (Map<String, Object>) request.getOrDefault("results", Map.of());
            String operator = (String) request.get("operator");
            BatchOperation updatedOperation = batchOperationService.completeBatchOperation(id, results, operator);
            return ResponseEntity.ok(updatedOperation);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 取消批操作
     * 
     * @param id 操作ID
     * @param request 取消请求，包含取消原因和操作员
     * @return 更新后的操作
     */
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER')")
    public ResponseEntity<BatchOperation> cancelBatchOperation(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            String reason = (String) request.get("reason");
            String operator = (String) request.get("operator");
            BatchOperation updatedOperation = batchOperationService.cancelBatchOperation(id, reason, operator);
            return ResponseEntity.ok(updatedOperation);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 标记批操作为失败
     * 
     * @param id 操作ID
     * @param request 失败请求，包含错误详情和操作员
     * @return 更新后的操作
     */
    @PutMapping("/{id}/fail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCTION_MANAGER') or hasRole('OPERATOR')")
    public ResponseEntity<BatchOperation> failBatchOperation(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> errorDetails = (Map<String, Object>) request.getOrDefault("errorDetails", Map.of());
            String operator = (String) request.get("operator");
            BatchOperation updatedOperation = batchOperationService.failBatchOperation(id, errorDetails, operator);
            return ResponseEntity.ok(updatedOperation);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除批操作
     * 
     * @param id 操作ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBatchOperation(@PathVariable Long id) {
        try {
            batchOperationService.deleteBatchOperation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}