package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 物料数据访问层
 * <p>
 * 提供物料相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long>, JpaSpecificationExecutor<Material> {

    /**
     * 根据物料编码查询物料
     *
     * @param code 物料编码
     * @return 物料对象
     */
    Optional<Material> findByCode(String code);

    /**
     * 根据物料名称模糊查询物料列表
     *
     * @param name 物料名称
     * @return 物料列表
     */
    List<Material> findByNameLike(String name);

    /**
     * 根据物料分类ID查询物料列表
     *
     * @param categoryId 物料分类ID
     * @return 物料列表
     */
    List<Material> findByCategoryId(Long categoryId);

    /**
     * 根据供应商ID查询物料列表
     *
     * @param supplierId 供应商ID
     * @return 物料列表
     */
    List<Material> findBySupplierId(Long supplierId);

    /**
     * 根据状态查询物料列表
     *
     * @param status 状态：0-停用，1-启用
     * @return 物料列表
     */
    List<Material> findByStatus(Integer status);

    /**
     * 检查物料编码是否已存在
     *
     * @param code 物料编码
     * @param id   排除的物料ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 查询需要库存预警的物料
     *
     * @return 需要预警的物料列表
     */
    @Query("SELECT m FROM Material m WHERE m.safetyStock IS NOT NULL AND m.warningStock IS NOT NULL")
    List<Material> findMaterialsForWarning();

}