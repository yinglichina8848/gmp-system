package com.gmp.warehouse.repository;

import com.gmp.warehouse.entity.MaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 物料分类数据访问层
 * <p>
 * 提供物料分类相关的数据库操作方法。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Long>, JpaSpecificationExecutor<MaterialCategory> {

    /**
     * 根据物料分类编码查询物料分类
     *
     * @param code 物料分类编码
     * @return 物料分类对象
     */
    Optional<MaterialCategory> findByCode(String code);

    /**
     * 根据物料分类名称查询物料分类
     *
     * @param name 物料分类名称
     * @return 物料分类对象
     */
    Optional<MaterialCategory> findByName(String name);

    /**
     * 根据父分类ID查询子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<MaterialCategory> findByParentId(Long parentId);

    /**
     * 查询顶级分类（parentId为null的分类）
     *
     * @return 顶级分类列表
     */
    List<MaterialCategory> findByParentIdIsNull();

    /**
     * 根据状态查询物料分类列表
     *
     * @param status 状态：0-停用，1-启用
     * @return 物料分类列表
     */
    List<MaterialCategory> findByStatus(Integer status);

    /**
     * 检查物料分类编码是否已存在
     *
     * @param code 物料分类编码
     * @param id   排除的分类ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 检查物料分类名称是否已存在
     *
     * @param name 物料分类名称
     * @param id   排除的分类ID（用于更新操作）
     * @return 是否存在
     */
    boolean existsByNameAndIdNot(String name, Long id);

}