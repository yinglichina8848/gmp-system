package com.gmp.training.repository;

import com.gmp.training.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 岗位数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface PositionRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {

    /**
     * 根据岗位编码查找岗位
     * 
     * @param code 岗位编码
     * @return 岗位信息
     */
    Optional<Position> findByCode(String code);

    /**
     * 根据岗位名称查找岗位
     * 
     * @param name 岗位名称
     * @return 岗位信息
     */
    Optional<Position> findByName(String name);

    /**
     * 检查岗位编码是否存在
     * 
     * @param code 岗位编码
     * @return 是否存在
     */
    boolean existsByCode(String code);

    /**
     * 检查岗位名称是否存在
     * 
     * @param name 岗位名称
     * @return 是否存在
     */
    boolean existsByName(String name);

    /**
     * 查找需要GMP资质的岗位
     * 
     * @param requiresGmp 是否需要GMP资质
     * @return 岗位列表
     */
    java.util.List<Position> findByRequiresGmp(boolean requiresGmp);

    /**
     * 查找部门下的岗位
     * 
     * @param departmentId 部门ID
     * @return 岗位列表
     */
    java.util.List<Position> findByDepartmentId(Long departmentId);

    /**
     * 查找活跃的岗位
     * 
     * @param active 是否活跃
     * @return 岗位列表
     */
    java.util.List<Position> findByActive(boolean active);

    /**
     * 查找特定部门中需要GMP资质的岗位
     * 
     * @param departmentId 部门ID
     * @param requiresGmp 是否需要GMP资质
     * @return 岗位列表
     */
    java.util.List<Position> findByDepartmentIdAndRequiresGmp(Long departmentId, boolean requiresGmp);
}
