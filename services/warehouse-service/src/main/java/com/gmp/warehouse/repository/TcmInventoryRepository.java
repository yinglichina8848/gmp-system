package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.TcmInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 中药特色库存仓库接口
 * <p>
 * 提供中药材库存信息的数据访问接口。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface TcmInventoryRepository extends JpaRepository<TcmInventory, Long>, JpaSpecificationExecutor<TcmInventory> {

    /**
     * 根据中药材名称查找库存记录
     *
     * @param herbName 中药材名称
     * @return 库存记录列表
     */
    List<TcmInventory> findByHerbName(String herbName);

    /**
     * 根据批号查找库存记录
     *
     * @param batchNumber 批号
     * @return 库存记录Optional对象
     */
    Optional<TcmInventory> findByBatchNumber(String batchNumber);

    /**
     * 根据中药材名称和仓库ID查找库存记录
     *
     * @param herbName 中药材名称
     * @param warehouseId 仓库ID
     * @return 库存记录列表
     */
    List<TcmInventory> findByHerbNameAndWarehouseId(String herbName, Long warehouseId);
}