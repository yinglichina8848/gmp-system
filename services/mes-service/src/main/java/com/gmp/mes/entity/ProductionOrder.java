package com.gmp.mes.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 生产订单实体
 * 
 * @author gmp-system
 */
@Entity
@Table(name = "production_orders")
@Data
@EntityListeners(AuditingEntityListener.class)
public class ProductionOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Column(nullable = false)
    private String productId;

    private String productName;
    private BigDecimal batchSize;
    private String unit; // 单位
    private String department; // 部门
    private String remark; // 备注

    @Column(nullable = false, name = "planned_start_date")
    private LocalDateTime plannedStartDate;

    @Column(nullable = false, name = "planned_end_date")
    private LocalDateTime plannedEndDate;

    // 兼容方法 - 计划开始时间
    public LocalDateTime getPlanStartTime() {
        return this.plannedStartDate;
    }

    public void setPlanStartTime(LocalDateTime planStartTime) {
        this.plannedStartDate = planStartTime;
    }

    // 兼容方法 - 计划结束时间
    public LocalDateTime getPlanEndTime() {
        return this.plannedEndDate;
    }

    public void setPlanEndTime(LocalDateTime planEndTime) {
        this.plannedEndDate = planEndTime;
    }

    // 兼容方法 - 实际开始时间
    public LocalDateTime getActualStartTime() {
        // 在当前类中没有实际开始时间字段，返回null
        return null;
    }

    public void setActualStartTime(LocalDateTime actualStartTime) {
        // 在当前类中没有实际开始时间字段，不做操作
    }

    // 兼容方法 - 实际结束时间
    public LocalDateTime getActualEndTime() {
        // 在当前类中没有实际结束时间字段，返回null
        return null;
    }

    public void setActualEndTime(LocalDateTime actualEndTime) {
        // 在当前类中没有实际结束时间字段，不做操作
    }

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    private OrderPriority priority = OrderPriority.NORMAL;

    @Column(nullable = false)
    private String requester; // 请求人

    private String approver;
    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "productionOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductionBatch> batches = new ArrayList<>();

    // 显式添加getter和setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    // 审计字段
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 订单状态枚举
     */
    public enum OrderStatus {
        DRAFT, // 草稿
        SUBMITTED, // 已提交
        APPROVED, // 已批准
        IN_PROGRESS, // 进行中
        COMPLETED, // 已完成
        CANCELLED // 已取消
    }

    /**
     * 订单优先级枚举
     */
    public enum OrderPriority {
        LOW, // 低
        NORMAL, // 正常
        HIGH, // 高
        URGENT // 紧急
    }

    // 兼容方法 - 单位
    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // 兼容方法 - 优先级（字符串类型）
    public String getPriority() {
        return this.priority != null ? this.priority.name() : null;
    }

    public void setPriority(String priority) {
        try {
            this.priority = OrderPriority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            // 如果传入的字符串不是有效枚举值，保持默认值
        }
    }

    // 兼容方法 - 申请人（对应requester）
    public String getApplicant() {
        return this.requester;
    }

    public void setApplicant(String applicant) {
        this.requester = applicant;
    }

    // 兼容方法 - 部门
    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // 兼容方法 - 备注
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // 兼容方法 - 生产批次集合（Set类型）
    public Set<ProductionBatch> getProductionBatches() {
        return new HashSet<>(this.batches);
    }

    public void setProductionBatches(Set<ProductionBatch> productionBatches) {
        if (productionBatches != null) {
            this.batches = new ArrayList<>(productionBatches);
        }
    }

    // 兼容方法 - 添加生产批次
    public void addProductionBatch(ProductionBatch batch) {
        if (batch != null) {
            this.batches.add(batch);
            // 设置关联关系
            if (batch.getProductionOrder() != this) {
                batch.setProductionOrder(this);
            }
        }
    }

    // 兼容方法 - 移除生产批次
    public void removeProductionBatch(ProductionBatch batch) {
        if (batch != null) {
            this.batches.remove(batch);
            // 清除关联关系
            if (batch.getProductionOrder() == this) {
                batch.setProductionOrder(null);
            }
        }
    }
}