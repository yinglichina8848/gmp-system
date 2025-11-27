package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 库存数据访问层
 * <p>
 * 提供库存相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, JpaSpecificationExecutor<Inventory> {

    /**
     * 根据物料ID查询库存列表
     *
     * @param materialId 物料ID
     * @return 库存列表
     */
    List<Inventory> findByMaterialId(Long materialId);

    /**
     * 根据仓库ID查询库存列表
     *
     * @param warehouseId 仓库ID
     * @return 库存列表
     */
    List<Inventory> findByWarehouseId(Long warehouseId);

    /**
     * 根据库位ID查询库存列表
     *
     * @param locationId 库位ID
     * @return 库存列表
     */
    List<Inventory> findByLocationId(Long locationId);

    /**
     * 根据物料ID和仓库ID查询库存列表
     *
     * @param materialId  物料ID
     * @param warehouseId 仓库ID
     * @return 库存列表
     */
    List<Inventory> findByMaterialIdAndWarehouseId(Long materialId, Long warehouseId);

    /**
     * 根据物料ID、仓库ID和批次号查询库存
     *
     * @param materialId  物料ID
     * @param warehouseId 仓库ID
     * @param batchNo     批次号
     * @return 库存对象
     */
    Optional<Inventory> findByMaterialIdAndWarehouseIdAndBatchNo(Long materialId, Long warehouseId, String batchNo);

    /**
     * 根据物料ID和库位ID查询库存
     *
     * @param materialId 物料ID
     * @param locationId 库位ID
     * @return 库存对象
     */
    Optional<Inventory> findByMaterialIdAndLocationId(Long materialId, Long locationId);

    /**
     * 查询库存不足的物料
     *
     * @return 库存不足的物料列表
     */
    @Query("SELECT i FROM Inventory i WHERE i.availableQuantity < i.minSafetyStock")
    List<Inventory> findInventoryShortages();

    /**
     * 查询即将过期的物料
     *
     * @param days 天数
     * @return 即将过期的物料列表
     */
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate IS NOT NULL AND i.expiryDate <= CURRENT_DATE + :days")
    List<Inventory> findExpiringMaterials(@Param("days") int days);

    /**
     * 查询仓库的物料总数
     *
     * @param warehouseId 仓库ID
     * @return 物料总数
     */
    @Query("SELECT COUNT(DISTINCT i.materialId) FROM Inventory i WHERE i.warehouseId = :warehouseId")
    Long countMaterialsInWarehouse(@Param("warehouseId") Long warehouseId);

    /**
     * 查询仓库的库存总量
     *
     * @param warehouseId 仓库ID
     * @return 库存总量
     */
    @Query("SELECT SUM(i.currentQuantity) FROM Inventory i WHERE i.warehouseId = :warehouseId")
    Double getTotalInventoryInWarehouse(@Param("warehouseId") Long warehouseId);

}