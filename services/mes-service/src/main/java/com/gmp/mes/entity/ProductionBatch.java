package com.gmp.mes.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 生产批次实体
 * 
 * @author gmp-system
 */
@Entity
@Table(name = "production_batches")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProductionBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String batchNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private ProductionOrder productionOrder;

    // 兼容字段，保持向后兼容
    public ProductionOrder getOrder() {
        return productionOrder;
    }

    public void setOrder(ProductionOrder order) {
        this.productionOrder = order;
    }

    @Column(nullable = false)
    private String productId;

    private String productName;
    private Integer batchSize;
    private String equipmentId;
    private String equipmentName;

    private Integer goodQuantity;
    private Integer rejectQuantity;
    private String operator;
    private String remark;

    @Enumerated(EnumType.STRING)
    private BatchStatus status = BatchStatus.PENDING;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "batch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BatchOperation> batchOperations = new HashSet<>();

    // 兼容字段，保持向后兼容
    public List<BatchOperation> getOperations() {
        return new ArrayList<>(batchOperations);
    }

    public void setOperations(List<BatchOperation> operations) {
        this.batchOperations = new HashSet<>(operations);
        for (BatchOperation operation : operations) {
            operation.setBatch(this);
        }
    }

    // 审计字段
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 业务方法
    public void addBatchOperation(BatchOperation operation) {
        batchOperations.add(operation);
        operation.setBatch(this);
    }

    public void removeBatchOperation(BatchOperation operation) {
        batchOperations.remove(operation);
        operation.setBatch(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProductionBatch that = (ProductionBatch) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * 批次状态枚举
     */
    public enum BatchStatus {
        PENDING, // 待执行
        IN_PROGRESS, // 进行中
        RUNNING, // 运行中（兼容状态，等同于IN_PROGRESS）
        COMPLETED, // 已完成
        FAILED, // 失败
        SUSPENDED // 已暂停
    }
    
    // 兼容方法
    public BatchStatus getStatus() {
        return status;
    }
    
    public void setStatus(BatchStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}