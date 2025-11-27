package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 库位数据访问层
 * <p>
 * 提供库位相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    /**
     * 根据库位编码查询库位
     *
     * @param code 库位编码
     * @return 库位对象
     */
    Optional<Location> findByCode(String code);

    /**
     * 根据库位名称查询库位
     *
     * @param name 库位名称
     * @return 库位对象
     */
    Optional<Location> findByName(String name);

    /**
     * 根据仓库ID查询库位列表
     *
     * @param warehouseId 仓库ID
     * @return 库位列表
     */
    List<Location> findByWarehouseId(Long warehouseId);

    /**
     * 根据仓库ID和状态查询库位列表
     *
     * @param warehouseId 仓库ID
     * @param status      状态：0-停用，1-启用
     * @return 库位列表
     */
    List<Location> findByWarehouseIdAndStatus(Long warehouseId, Integer status);

    /**
     * 根据状态查询库位列表
     *
     * @param status 状态：0-停用，1-启用
     * @return 库位列表
     */
    List<Location> findByStatus(Integer status);

    /**
     * 检查库位编码是否已存在
     *
     * @param code        库位编码
     * @param warehouseId 仓库ID
     * @param id          排除的库位ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByCodeAndWarehouseIdAndIdNot(String code, Long warehouseId, Long id);

    /**
     * 检查库位名称是否已存在
     *
     * @param name        库位名称
     * @param warehouseId 仓库ID
     * @param id          排除的库位ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByNameAndWarehouseIdAndIdNot(String name, Long warehouseId, Long id);

}