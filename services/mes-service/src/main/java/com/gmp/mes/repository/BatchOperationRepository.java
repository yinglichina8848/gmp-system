package com.gmp.mes.repository;

import com.gmp.mes.entity.BatchOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 批操作Repository接口
 * 提供批操作实体的数据库访问功能
 *
 * @author gmp-system
 */
@Repository
public interface BatchOperationRepository
        extends JpaRepository<BatchOperation, Long>, JpaSpecificationExecutor<BatchOperation> {

    /**
     * 根据生产批次ID查询批操作列表
     *
     * @param productionBatchId 生产批次ID
     * @return 批操作列表
     */
    List<BatchOperation> findByProductionBatchId(Long productionBatchId);

    /**
     * 根据操作状态查询批操作列表
     *
     * @param status 操作状态
     * @return 批操作列表
     */
    List<BatchOperation> findByStatus(BatchOperation.OperationStatus status);

    /**
     * 根据操作类型查询批操作列表
     *
     * @param operationType 操作类型
     * @return 批操作列表
     */
    List<BatchOperation> findByOperationType(String operationType);

    /**
     * 根据设备ID查询批操作列表
     *
     * @param equipmentId 设备ID
     * @return 批操作列表
     */
    List<BatchOperation> findByEquipmentId(String equipmentId);

    /**
     * 根据操作编号查询批操作
     * 兼容方法，实际上查询的是operationName字段
     *
     * @param operationNumber 操作编号
     * @return 批操作对象
     */
    BatchOperation findByOperationName(String operationNumber);

    /**
     * 根据操作编号查询批操作的别名方法
     * 为了兼容测试代码
     *
     * @param operationNumber 操作编号
     * @return 批操作对象
     */
    default BatchOperation findByOperationNumber(String operationNumber) {
        return findByOperationName(operationNumber);
    }

    /**
     * 根据批次ID查询批操作 - 兼容方法
     * 为了兼容测试代码中使用的方法名
     *
     * @param batchId 批次ID
     * @return 批操作列表
     */
    default List<BatchOperation> findByBatchId(long batchId) {
        return findByProductionBatchId(batchId);
    }
}