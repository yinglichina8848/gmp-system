package com.gmp.mes.service;

import com.gmp.mes.entity.ProductionBatch;
import com.gmp.mes.entity.ProductionOrder;
import com.gmp.mes.repository.ProductionBatchRepository;
import com.gmp.mes.repository.ProductionOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 生产批次服务 - 实现批次管理的核心业务逻辑
 * 
 * @author gmp-system
 */
@Service
public class ProductionBatchService {

    @Autowired
    private ProductionBatchRepository productionBatchRepository;

    @Autowired
    private ProductionOrderRepository productionOrderRepository;

    /**
     * 创建生产批次
     * 
     * @param batch 生产批次对象
     * @return 创建的生产批次
     */
    @Transactional
    public ProductionBatch createBatch(ProductionBatch batch) {
        // 生成批次编号
        if (batch.getBatchNumber() == null) {
            batch.setBatchNumber(generateBatchNumber());
        }
        // 设置初始状态
        if (batch.getStatus() == null) {
            batch.setStatus(ProductionBatch.BatchStatus.PENDING);
        }
        return productionBatchRepository.save(batch);
    }

    /**
     * 根据ID获取批次
     * 
     * @param id 批次ID
     * @return 批次对象
     */
    public Optional<ProductionBatch> getBatchById(Long id) {
        return productionBatchRepository.findById(id);
    }

    /**
     * 根据批次编号获取批次
     * 
     * @param batchNumber 批次编号
     * @return 批次对象
     */
    public Optional<ProductionBatch> getBatchByNumber(String batchNumber) {
        return productionBatchRepository.findByBatchNumber(batchNumber);
    }

    /**
     * 获取所有批次
     * 
     * @return 批次列表
     */
    public List<ProductionBatch> getAllBatches() {
        return productionBatchRepository.findAll();
    }

    /**
     * 根据状态获取批次
     * 
     * @param status 批次状态
     * @return 批次列表
     */
    public List<ProductionBatch> getBatchesByStatus(ProductionBatch.BatchStatus status) {
        return productionBatchRepository.findByStatus(status);
    }

    /**
     * 根据订单ID获取批次
     * 
     * @param orderId 订单ID
     * @return 批次列表
     */
    public List<ProductionBatch> getBatchesByOrderId(Long orderId) {
        return productionBatchRepository.findByOrderId(orderId);
    }

    /**
     * 更新批次
     * 
     * @param batch 批次对象
     * @return 更新后的批次
     */
    @Transactional
    public ProductionBatch updateBatch(ProductionBatch batch) {
        return productionBatchRepository.save(batch);
    }

    /**
     * 开始批次生产
     * 
     * @param batchNumber 批次编号
     * @param equipmentId 设备ID
     * @return 开始生产的批次
     */
    @Transactional
    public ProductionBatch startBatch(String batchNumber, String equipmentId) {
        Optional<ProductionBatch> optionalBatch = productionBatchRepository.findByBatchNumber(batchNumber);
        if (optionalBatch.isPresent()) {
            ProductionBatch batch = optionalBatch.get();
            if (batch.getStatus() == ProductionBatch.BatchStatus.PENDING) {
                batch.setStatus(ProductionBatch.BatchStatus.IN_PROGRESS);
                batch.setStartTime(LocalDateTime.now());
                batch.setEquipmentId(equipmentId);
                return productionBatchRepository.save(batch);
            } else {
                throw new IllegalStateException("Only pending batches can be started");
            }
        } else {
            throw new IllegalArgumentException("Batch not found: " + batchNumber);
        }
    }

    /**
     * 完成批次生产
     * 
     * @param batchNumber 批次编号
     * @return 完成生产的批次
     */
    @Transactional
    public ProductionBatch completeBatch(String batchNumber) {
        Optional<ProductionBatch> optionalBatch = productionBatchRepository.findByBatchNumber(batchNumber);
        if (optionalBatch.isPresent()) {
            ProductionBatch batch = optionalBatch.get();
            if (batch.getStatus() == ProductionBatch.BatchStatus.IN_PROGRESS) {
                batch.setStatus(ProductionBatch.BatchStatus.COMPLETED);
                batch.setEndTime(LocalDateTime.now());

                // 更新关联的订单状态
                updateOrderStatusIfNeeded(batch);

                return productionBatchRepository.save(batch);
            } else {
                throw new IllegalStateException("Only in-progress batches can be completed");
            }
        } else {
            throw new IllegalArgumentException("Batch not found: " + batchNumber);
        }
    }

    /**
     * 暂停批次生产
     * 
     * @param batchNumber 批次编号
     * @return 暂停的批次
     */
    @Transactional
    public ProductionBatch suspendBatch(String batchNumber) {
        Optional<ProductionBatch> optionalBatch = productionBatchRepository.findByBatchNumber(batchNumber);
        if (optionalBatch.isPresent()) {
            ProductionBatch batch = optionalBatch.get();
            if (batch.getStatus() == ProductionBatch.BatchStatus.IN_PROGRESS) {
                batch.setStatus(ProductionBatch.BatchStatus.SUSPENDED);
                return productionBatchRepository.save(batch);
            } else {
                throw new IllegalStateException("Only in-progress batches can be suspended");
            }
        } else {
            throw new IllegalArgumentException("Batch not found: " + batchNumber);
        }
    }

    /**
     * 恢复暂停的批次
     * 
     * @param batchNumber 批次编号
     * @return 恢复的批次
     */
    @Transactional
    public ProductionBatch resumeBatch(String batchNumber) {
        Optional<ProductionBatch> optionalBatch = productionBatchRepository.findByBatchNumber(batchNumber);
        if (optionalBatch.isPresent()) {
            ProductionBatch batch = optionalBatch.get();
            if (batch.getStatus() == ProductionBatch.BatchStatus.SUSPENDED) {
                batch.setStatus(ProductionBatch.BatchStatus.IN_PROGRESS);
                return productionBatchRepository.save(batch);
            } else {
                throw new IllegalStateException("Only suspended batches can be resumed");
            }
        } else {
            throw new IllegalArgumentException("Batch not found: " + batchNumber);
        }
    }

    /**
     * 标记批次失败
     * 
     * @param batchNumber 批次编号
     * @param notes       失败原因
     * @return 标记为失败的批次
     */
    @Transactional
    public ProductionBatch failBatch(String batchNumber, String notes) {
        Optional<ProductionBatch> optionalBatch = productionBatchRepository.findByBatchNumber(batchNumber);
        if (optionalBatch.isPresent()) {
            ProductionBatch batch = optionalBatch.get();
            if (batch.getStatus() == ProductionBatch.BatchStatus.IN_PROGRESS ||
                    batch.getStatus() == ProductionBatch.BatchStatus.SUSPENDED) {
                batch.setStatus(ProductionBatch.BatchStatus.FAILED);
                batch.setEndTime(LocalDateTime.now());
                // 这里可以添加失败原因的字段到BatchOperation实体
                return productionBatchRepository.save(batch);
            } else {
                throw new IllegalStateException("Only in-progress or suspended batches can be marked as failed");
            }
        } else {
            throw new IllegalArgumentException("Batch not found: " + batchNumber);
        }
    }

    /**
     * 删除批次
     * 
     * @param id 批次ID
     */
    @Transactional
    public void deleteBatch(Long id) {
        productionBatchRepository.deleteById(id);
    }

    /**
     * 完成生产批次（按ID）
     * 
     * @param batchId           批次ID
     * @param operator          操作员
     * @param completedQuantity 完成数量
     * @return 完成的批次
     */
    @Transactional
    public ProductionBatch completeProductionBatch(Long batchId, String operator, Integer completedQuantity) {
        ProductionBatch batch = getBatchById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("批次不存在: " + batchId));

        if (batch.getStatus() != ProductionBatch.BatchStatus.IN_PROGRESS) {
            throw new IllegalStateException("只能完成进行中的批次");
        }

        batch.setStatus(ProductionBatch.BatchStatus.COMPLETED);
        batch.setEndTime(LocalDateTime.now());

        // 更新关联的订单状态
        updateOrderStatusIfNeeded(batch);

        return productionBatchRepository.save(batch);
    }

    /**
     * 标记生产批次为失败（按ID）
     * 
     * @param batchId  批次ID
     * @param operator 操作员
     * @param reason   失败原因
     * @return 失败的批次
     */
    @Transactional
    public ProductionBatch failProductionBatch(Long batchId, String operator, String reason) {
        ProductionBatch batch = getBatchById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("批次不存在: " + batchId));

        if (batch.getStatus() != ProductionBatch.BatchStatus.IN_PROGRESS &&
                batch.getStatus() != ProductionBatch.BatchStatus.SUSPENDED) {
            throw new IllegalStateException("只能标记进行中或已暂停的批次为失败");
        }

        batch.setStatus(ProductionBatch.BatchStatus.FAILED);
        batch.setEndTime(LocalDateTime.now());

        return productionBatchRepository.save(batch);
    }

    /**
     * 恢复暂停的生产批次（按ID）
     * 
     * @param batchId  批次ID
     * @param operator 操作员
     * @return 恢复的批次
     */
    @Transactional
    public ProductionBatch resumeProductionBatch(Long batchId, String operator) {
        ProductionBatch batch = getBatchById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("批次不存在: " + batchId));

        if (batch.getStatus() != ProductionBatch.BatchStatus.SUSPENDED) {
            throw new IllegalStateException("只能恢复已暂停的批次");
        }

        batch.setStatus(ProductionBatch.BatchStatus.IN_PROGRESS);

        return productionBatchRepository.save(batch);
    }

    /**
     * 删除生产批次（兼容方法）
     * 
     * @param batchId 批次ID
     */
    @Transactional
    public void deleteProductionBatch(Long batchId) {
        deleteBatch(batchId);
    }

    /**
     * 根据时间范围获取生产批次
     * 
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 批次列表
     */
    public List<ProductionBatch> getProductionBatchesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        // 这里可以实现按时间范围查询的逻辑
        // 暂时返回空列表，实际应该使用repository查询
        return new ArrayList<>();
    }

    /**
     * 获取活跃的生产批次（进行中或已暂停）
     * 
     * @return 活跃批次列表
     */
    public List<ProductionBatch> getActiveProductionBatches() {
        List<ProductionBatch> activeBatches = new ArrayList<>();
        activeBatches.addAll(productionBatchRepository.findByStatus(ProductionBatch.BatchStatus.IN_PROGRESS));
        activeBatches.addAll(productionBatchRepository.findByStatus(ProductionBatch.BatchStatus.SUSPENDED));
        return activeBatches;
    }

    /**
     * 更新生产批次（兼容方法）
     * 
     * @param batch 批次对象
     * @return 更新后的批次
     */
    @Transactional
    public ProductionBatch updateProductionBatch(ProductionBatch batch) {
        return updateBatch(batch);
    }

    /**
     * 开始生产批次（按ID）
     * 
     * @param batchId     批次ID
     * @param equipmentId 设备ID
     * @return 开始生产的批次
     */
    @Transactional
    public ProductionBatch startProductionBatch(Long batchId, String equipmentId) {
        ProductionBatch batch = getBatchById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("批次不存在: " + batchId));

        if (batch.getStatus() != ProductionBatch.BatchStatus.PENDING) {
            throw new IllegalStateException("只能开始待执行的批次");
        }

        batch.setStatus(ProductionBatch.BatchStatus.IN_PROGRESS);
        batch.setStartTime(LocalDateTime.now());
        batch.setEquipmentId(equipmentId);

        return productionBatchRepository.save(batch);
    }

    /**
     * 暂停生产批次（按ID）
     * 
     * @param batchId  批次ID
     * @param operator 操作员
     * @param reason   暂停原因
     * @return 暂停的批次
     */
    @Transactional
    public ProductionBatch pauseProductionBatch(Long batchId, String operator, String reason) {
        ProductionBatch batch = getBatchById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("批次不存在: " + batchId));

        if (batch.getStatus() != ProductionBatch.BatchStatus.IN_PROGRESS) {
            throw new IllegalStateException("只能暂停进行中的批次");
        }

        batch.setStatus(ProductionBatch.BatchStatus.SUSPENDED);

        return productionBatchRepository.save(batch);
    }

    /**
     * 根据订单ID获取生产批次列表
     * 
     * @param orderId 订单ID
     * @return 批次列表
     */
    public List<ProductionBatch> getProductionBatchesByOrderId(Long orderId) {
        return productionBatchRepository.findByOrderId(orderId);
    }

    /**
     * 根据状态获取生产批次列表
     * 
     * @param status 批次状态
     * @return 批次列表
     */
    public List<ProductionBatch> getProductionBatchesByStatus(ProductionBatch.BatchStatus status) {
        return productionBatchRepository.findByStatus(status);
    }

    /**
     * 根据设备ID获取生产批次列表
     * 
     * @param equipmentId 设备ID
     * @return 批次列表
     */
    public List<ProductionBatch> getProductionBatchesByEquipmentId(String equipmentId) {
        return productionBatchRepository.findByEquipmentId(equipmentId);
    }

    /**
     * 创建生产批次（兼容方法）
     * 
     * @param batch 批次对象
     * @return 创建的批次
     */
    @Transactional
    public ProductionBatch createProductionBatch(ProductionBatch batch) {
        return createBatch(batch);
    }

    /**
     * 获取所有生产批次（兼容方法）
     * 
     * @return 所有批次列表
     */
    public List<ProductionBatch> getAllProductionBatches() {
        return productionBatchRepository.findAll();
    }

    /**
     * 根据ID获取生产批次（兼容方法）
     * 
     * @param batchId 批次ID
     * @return 批次对象
     */
    public Optional<ProductionBatch> getProductionBatchById(Long batchId) {
        return getBatchById(batchId);
    }

    /**
     * 根据批次号获取生产批次
     * 
     * @param batchNumber 批次号
     * @return 批次对象
     */
    public Optional<ProductionBatch> getProductionBatchByBatchNumber(String batchNumber) {
        return productionBatchRepository.findByBatchNumber(batchNumber);
    }

    /**
     * 如果订单的所有批次都完成，则更新订单状态
     * 
     * @param batch 批次对象
     */
    private void updateOrderStatusIfNeeded(ProductionBatch batch) {
        ProductionOrder order = batch.getOrder();
        if (order != null) {
            List<ProductionBatch> batches = productionBatchRepository.findByOrder(order);
            boolean allCompleted = batches.stream()
                    .allMatch(b -> b.getStatus() == ProductionBatch.BatchStatus.COMPLETED);

            if (allCompleted && order.getStatus() == ProductionOrder.OrderStatus.IN_PROGRESS) {
                order.setStatus(ProductionOrder.OrderStatus.COMPLETED);
                productionOrderRepository.save(order);
            }
        }
    }

    /**
     * 生成批次编号
     * 
     * @return 批次编号
     */
    private String generateBatchNumber() {
        return "BATCH-" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + "-"
                + UUID.randomUUID().toString().substring(0, 8);
    }
}