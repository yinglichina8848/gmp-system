package com.gmp.training.service;

import com.gmp.training.entity.Position;

import java.util.List;
import java.util.Optional;

/**
 * 岗位服务接口
 * 
 * @author GMP系统开发团队
 */
public interface PositionService extends BaseService<Position, Long> {

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
     * 查找部门下的岗位
     * 
     * @param departmentId 部门ID
     * @return 岗位列表
     */
    List<Position> findByDepartmentId(Long departmentId);

    /**
     * 查找GMP相关岗位
     * 
     * @param gmpQualification 是否需要GMP资质
     * @return 岗位列表
     */
    List<Position> findByGmpQualification(boolean gmpQualification);

    /**
     * 查找所有活跃的岗位
     * 
     * @return 活跃岗位列表
     */
    List<Position> findActivePositions();

    /**
     * 更新岗位状态
     * 
     * @param id 岗位ID
     * @param active 状态
     * @return 更新后的岗位
     */
    Position updateStatus(Long id, boolean active);

    /**
     * 更新岗位所属部门
     * 
     * @param id 岗位ID
     * @param departmentId 部门ID
     * @return 更新后的岗位
     */
    Position updateDepartment(Long id, Long departmentId);

    /**
     * 批量禁用岗位
     * 
     * @param ids 岗位ID列表
     * @return 影响的行数
     */
    int batchDisable(List<Long> ids);

    /**
     * 批量启用岗位
     * 
     * @param ids 岗位ID列表
     * @return 影响的行数
     */
    int batchEnable(List<Long> ids);

    /**
     * 查找需要特定培训的岗位
     * 
     * @param trainingTypeId 培训类型ID
     * @return 岗位列表
     */
    List<Position> findPositionsByRequiredTraining(Long trainingTypeId);
}
