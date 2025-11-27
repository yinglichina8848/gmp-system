package com.gmp.mes.service;

import com.gmp.mes.entity.BatchOperation;
import com.gmp.mes.entity.ProductionBatch;
import com.gmp.mes.repository.BatchOperationRepository;
import com.gmp.mes.repository.ProductionBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 批操作服务 - 实现批次执行过程中的操作管理和记录功能
 * 
 * @author gmp-system
 */
@Service
public class BatchOperationService {

    @Autowired
    private BatchOperationRepository batchOperationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductionBatchRepository productionBatchRepository;

    /**
     * 创建批操作
     * 
     * @param batchOperation 批操作对象
     * @return 创建的批操作
     */
    @Transactional
    public BatchOperation createBatchOperation(BatchOperation batchOperation) {
        // 验证批次是否存在
        if (batchOperation.getBatch() == null || batchOperation.getBatch().getId() == null) {
            throw new IllegalArgumentException("Production batch not found or invalid");
        }

        Optional<ProductionBatch> optionalBatch = productionBatchRepository.findById(batchOperation.getBatch().getId());
        if (optionalBatch.isEmpty()) {
            throw new IllegalArgumentException("Production batch not found: " + batchOperation.getBatch().getId());
        }

        // 设置批次对象
        batchOperation.setBatch(optionalBatch.get());

        // 设置默认状态
        if (batchOperation.getStatus() == null) {
            batchOperation.setStatus(BatchOperation.OperationStatus.PENDING);
        }

        return batchOperationRepository.save(batchOperation);
    }

    /**
     * 获取批操作
     * 
     * @param id 批操作ID
     * @return 批操作对象
     */
    public Optional<BatchOperation> getBatchOperationById(Long id) {
        return batchOperationRepository.findById(id);
    }

    /**
     * 根据操作编号获取批操作
     * 
     * @param operationNumber 操作编号
     * @return 批操作对象
     */
    public Optional<BatchOperation> getBatchOperationByNumber(String operationNumber) {
        // 使用操作名称来查找，因为实体类没有operationNumber字段
        return batchOperationRepository.findAll().stream()
                .filter(op -> op.getOperationName() != null && op.getOperationName().contains(operationNumber))
                .findFirst();
    }

    /**
     * 获取批次的所有操作
     * 
     * @param batchId 批次ID
     * @return 批操作列表
     */
    public List<BatchOperation> getBatchOperationsByBatchId(Long batchId) {
        return batchOperationRepository.findByProductionBatchId(batchId);
    }

    /**
     * 获取批次的特定类型操作
     * 
     * @param batchId       批次ID
     * @param operationType 操作类型
     * @return 批操作列表
     */
    public List<BatchOperation> getBatchOperationsByType(Long batchId, String operationType) {
        // 使用现有方法并手动排序
        List<BatchOperation> operations = batchOperationRepository.findByProductionBatchId(batchId);
        return operations.stream()
                .filter(op -> op.getOperationType().equals(operationType))
                .sorted((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 更新批操作
     * 
     * @param batchOperation 批操作对象
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation updateBatchOperation(BatchOperation batchOperation) {
        return batchOperationRepository.save(batchOperation);
    }

    /**
     * 开始执行批操作 - 兼容版本
     * 为了兼容测试代码中使用的方法签名
     * 
     * @param id       批操作ID
     * @param operator 操作人员
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation startBatchOperation(long id, String operator) {
        // 调用原有的三参数版本，传入空的参数Map
        return startBatchOperation((Long) id, java.util.Collections.emptyMap(), operator);
    }

    /**
     * 开始执行批操作
     * 
     * @param id         批操作ID
     * @param parameters 操作参数
     * @param operator   操作人员
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation startBatchOperation(Long id, Map<String, Object> parameters, String operator) {
        Optional<BatchOperation> optionalOperation = batchOperationRepository.findById(id);
        if (optionalOperation.isPresent()) {
            BatchOperation operation = optionalOperation.get();

            // 检查操作状态
            if (operation.getStatus() != BatchOperation.OperationStatus.PENDING) {
                throw new IllegalStateException("Cannot start operation, current status: " + operation.getStatus());
            }

            // 更新操作状态和参数
            operation.setStatus(BatchOperation.OperationStatus.IN_PROGRESS);
            try {
                operation.setParameters(objectMapper.writeValueAsString(parameters));
            } catch (Exception e) {
                operation.setParameters("{}");
            }
            // 使用重命名后的方法设置Map形式的parameters（兼容测试）
            operation.setParametersMap(parameters);
            operation.setStartTime(LocalDateTime.now());
            operation.setOperator(operator);

            // 生成操作名称 (如果没有)
            if (operation.getOperationName() == null || operation.getOperationName().isEmpty()) {
                operation.setOperationName("OP-" + generateOperationNumber());
            }

            return batchOperationRepository.save(operation);
        } else {
            throw new IllegalArgumentException("Batch operation not found: " + id);
        }
    }

    /**
     * 完成批操作
     * 
     * @param id       批操作ID
     * @param results  操作结果
     * @param operator 操作人员
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation completeBatchOperation(Long id, Map<String, Object> results, String operator) {
        Optional<BatchOperation> optionalOperation = batchOperationRepository.findById(id);
        if (optionalOperation.isPresent()) {
            BatchOperation operation = optionalOperation.get();

            // 检查操作状态
            if (operation.getStatus() != BatchOperation.OperationStatus.IN_PROGRESS) {
                throw new IllegalStateException("Cannot complete operation, current status: " + operation.getStatus());
            }

            // 更新操作状态和结果
            operation.setStatus(BatchOperation.OperationStatus.COMPLETED);
            try {
                operation.setResultValues(objectMapper.writeValueAsString(results));
            } catch (Exception e) {
                operation.setResultValues("{}");
            }
            operation.setEndTime(LocalDateTime.now());
            operation.setOperator(operator);

            return batchOperationRepository.save(operation);
        } else {
            throw new IllegalArgumentException("Batch operation not found: " + id);
        }
    }

    /**
     * 取消批操作
     * 
     * @param id       批操作ID
     * @param reason   取消原因
     * @param operator 操作人员
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation cancelBatchOperation(Long id, String reason, String operator) {
        Optional<BatchOperation> optionalOperation = batchOperationRepository.findById(id);
        if (optionalOperation.isPresent()) {
            BatchOperation operation = optionalOperation.get();

            // 检查操作状态，只有待执行或执行中的操作可以取消
            if (operation.getStatus() != BatchOperation.OperationStatus.PENDING &&
                    operation.getStatus() != BatchOperation.OperationStatus.IN_PROGRESS) {
                throw new IllegalStateException("Cannot cancel operation, current status: " + operation.getStatus());
            }

            // 更新操作状态和备注
            operation.setStatus(BatchOperation.OperationStatus.CANCELLED);
            operation.setEndTime(LocalDateTime.now());
            operation.setNotes(reason);
            operation.setOperator(operator);

            return batchOperationRepository.save(operation);
        } else {
            throw new IllegalArgumentException("Batch operation not found: " + id);
        }
    }

    /**
     * 标记批操作为失败
     * 
     * @param id           批操作ID
     * @param errorDetails 错误详情
     * @param operator     操作人员
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation failBatchOperation(Long id, Map<String, Object> errorDetails, String operator) {
        Optional<BatchOperation> optionalOperation = batchOperationRepository.findById(id);
        if (optionalOperation.isPresent()) {
            BatchOperation operation = optionalOperation.get();

            // 检查操作状态，只有执行中的操作可以标记为失败
            if (operation.getStatus() != BatchOperation.OperationStatus.IN_PROGRESS) {
                throw new IllegalStateException(
                        "Cannot mark operation as failed, current status: " + operation.getStatus());
            }

            // 更新操作状态和错误信息
            operation.setStatus(BatchOperation.OperationStatus.FAILED);
            try {
                operation.setResultValues(objectMapper.writeValueAsString(errorDetails)); // 使用resultValues字段存储错误详情
            } catch (Exception e) {
                operation.setResultValues("{}");
            }
            operation.setEndTime(LocalDateTime.now());
            operation.setNotes("Operation failed");
            operation.setOperator(operator);

            return batchOperationRepository.save(operation);
        } else {
            throw new IllegalArgumentException("Batch operation not found: " + id);
        }
    }

    /**
     * 删除批操作
     * 
     * @param id 批操作ID
     */
    @Transactional
    public void deleteBatchOperation(Long id) {
        batchOperationRepository.deleteById(id);
    }

    /**
     * 根据操作编号获取批操作 - 兼容方法
     * 为了兼容测试代码中使用的方法名
     * 
     * @param operationNumber 操作编号
     * @return 批操作对象
     */
    public BatchOperation getBatchOperationByOperationNumber(String operationNumber) {
        // 使用现有的getBatchOperationByNumber方法并转换为非Optional返回
        Optional<BatchOperation> optionalOperation = getBatchOperationByNumber(operationNumber);
        return optionalOperation.orElse(null);
    }

    /**
     * 标记批操作为失败 - 兼容方法
     * 为了兼容测试代码中使用的方法签名
     * 
     * @param id       批操作ID
     * @param reason   失败原因
     * @param operator 操作人员
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation markBatchOperationFailed(Long id, String reason, String operator) {
        // 创建错误详情map并调用现有的failBatchOperation方法
        Map<String, Object> errorDetails = new java.util.HashMap<>();
        errorDetails.put("reason", reason);
        BatchOperation failedOperation = failBatchOperation(id, errorDetails, operator);

        // 同时设置remark字段以满足测试要求
        failedOperation.setRemark(failedOperation.getRemark() + " " + reason);
        return batchOperationRepository.save(failedOperation);
    }

    /**
     * 根据状态查找批操作列表
     * 
     * @param status 操作状态
     * @return 批操作列表
     */
    public List<BatchOperation> findBatchOperationsByStatus(BatchOperation.OperationStatus status) {
        // 使用repository的findByStatus方法获取特定状态的操作列表
        return batchOperationRepository.findByStatus(status);
    }

    /**
     * 获取所有批操作 - 兼容方法
     * 为了兼容测试代码中的方法调用
     * 
     * @return 所有批操作列表
     */
    public List<BatchOperation> getAllBatchOperations() {
        // 直接使用JpaRepository提供的findAll方法
        return batchOperationRepository.findAll();
    }

    /**
     * 生成唯一的操作编号
     * 
     * @return 操作编号
     */
    public String generateOperationNumber() {
        // 生成基于UUID的唯一操作编号
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
        return LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + "-"
                + uuid;
    }

    /**
     * 更新操作参数
     * 
     * @param id         批操作ID
     * @param parameters 新的操作参数
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation updateOperationParameters(Long id, Map<String, Object> parameters) {
        Optional<BatchOperation> optionalOperation = batchOperationRepository.findById(id);
        if (optionalOperation.isPresent()) {
            BatchOperation operation = optionalOperation.get();

            // 更新操作参数
            try {
                operation.setParameters(objectMapper.writeValueAsString(parameters));
            } catch (Exception e) {
                operation.setParameters("{}");
            }
            // 使用重命名后的方法设置Map形式的parameters（兼容测试）
            operation.setParametersMap(parameters);

            return batchOperationRepository.save(operation);
        } else {
            throw new IllegalArgumentException("Batch operation not found: " + id);
        }
    }

    /**
     * 添加操作备注
     * 
     * @param id     批操作ID
     * @param remark 备注内容
     * @return 更新后的批操作
     */
    @Transactional
    public BatchOperation addOperationRemark(Long id, String remark) {
        Optional<BatchOperation> optionalOperation = batchOperationRepository.findById(id);
        if (optionalOperation.isPresent()) {
            BatchOperation operation = optionalOperation.get();

            // 追加备注
            String currentRemark = operation.getRemark();
            if (currentRemark == null) {
                currentRemark = "";
            }
            operation.setRemark(currentRemark + " " + remark);

            return batchOperationRepository.save(operation);
        } else {
            throw new IllegalArgumentException("Batch operation not found: " + id);
        }
    }
}