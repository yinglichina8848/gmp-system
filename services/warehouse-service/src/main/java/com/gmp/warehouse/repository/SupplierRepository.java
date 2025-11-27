package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 供应商数据访问层
 * <p>
 * 提供供应商相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {

    /**
     * 根据供应商编码查询供应商
     *
     * @param code 供应商编码
     * @return 供应商对象
     */
    Optional<Supplier> findByCode(String code);

    /**
     * 根据供应商名称查询供应商
     *
     * @param name 供应商名称
     * @return 供应商对象
     */
    Optional<Supplier> findByName(String name);

    /**
     * 根据供应商名称模糊查询供应商列表
     *
     * @param name 供应商名称
     * @return 供应商列表
     */
    List<Supplier> findByNameLike(String name);

    /**
     * 根据联系电话查询供应商
     *
     * @param contactPhone 联系电话
     * @return 供应商对象
     */
    Optional<Supplier> findByContactPhone(String contactPhone);

    /**
     * 根据状态查询供应商列表
     *
     * @param status 状态：0-停用，1-启用
     * @return 供应商列表
     */
    List<Supplier> findByStatus(Integer status);

    /**
     * 检查供应商编码是否已存在
     *
     * @param code 供应商编码
     * @param id   排除的供应商ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 检查供应商名称是否已存在
     *
     * @param name 供应商名称
     * @param id   排除的供应商ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByNameAndIdNot(String name, Long id);

}