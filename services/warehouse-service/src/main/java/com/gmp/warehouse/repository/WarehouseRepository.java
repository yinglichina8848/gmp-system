package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 仓库数据访问层
 * <p>
 * 提供仓库相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {

    /**
     * 根据仓库编码查询仓库
     *
     * @param code 仓库编码
     * @return 仓库对象
     */
    Optional<Warehouse> findByCode(String code);

    /**
     * 根据仓库名称查询仓库
     *
     * @param name 仓库名称
     * @return 仓库对象
     */
    Optional<Warehouse> findByName(String name);

    /**
     * 根据仓库名称模糊查询仓库列表
     *
     * @param name 仓库名称
     * @return 仓库列表
     */
    List<Warehouse> findByNameLike(String name);

    /**
     * 根据状态查询仓库列表
     *
     * @param status 状态：0-停用，1-启用
     * @return 仓库列表
     */
    List<Warehouse> findByStatus(Integer status);

    /**
     * 根据类型查询仓库列表
     *
     * @param type 类型
     * @return 仓库列表
     */
    List<Warehouse> findByType(String type);

    /**
     * 检查仓库编码是否已存在
     *
     * @param code 仓库编码
     * @param id   排除的仓库ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 检查仓库名称是否已存在
     *
     * @param name 仓库名称
     * @param id   排除的仓库ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByNameAndIdNot(String name, Long id);

}