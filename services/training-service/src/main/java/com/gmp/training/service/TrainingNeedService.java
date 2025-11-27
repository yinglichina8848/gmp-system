package com.gmp.training.service;

import com.gmp.training.entity.TrainingNeed;
import com.gmp.training.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 培训需求服务接口
 * 
 * @author GMP系统开发团队
 */
public interface TrainingNeedService extends BaseService<TrainingNeed, Long> {

    /**
     * 根据需求名称查找培训需求
     * 
     * @param name 需求名称
     * @return 培训需求信息
     */
    Optional<TrainingNeed> findByName(String name);

    /**
     * 查找部门的培训需求
     * 
     * @param departmentId 部门ID
     * @return 培训需求列表
     */
    List<TrainingNeed> findByDepartmentId(Long departmentId);

    /**
     * 查找岗位的培训需求
     * 
     * @param positionId 岗位ID
     * @return 培训需求列表
     */
    List<TrainingNeed> findByPositionId(Long positionId);

    /**
     * 查找特定状态的培训需求
     * 
     * @param status 状态
     * @return 培训需求列表
     */
    List<TrainingNeed> findByStatus(TrainingNeed.Status status);

    /**
     * 查找创建者的培训需求
     * 
     * @param creator 创建者
     * @return 培训需求列表
     */
    List<TrainingNeed> findByCreator(User creator);

    /**
     * 查找特定时间段内提交的培训需求
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 培训需求列表
     */
    List<TrainingNeed> findBySubmitTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据优先级查找培训需求
     * 
     * @param priority 优先级
     * @return 培训需求列表
     */
    List<TrainingNeed> findByPriority(TrainingNeed.Priority priority);

    /**
     * 查找已批准的培训需求
     * 
     * @return 培训需求列表
     */
    List<TrainingNeed> findApprovedNeeds();

    /**
     * 查找未处理的培训需求
     * 
     * @return 培训需求列表
     */
    List<TrainingNeed> findPendingNeeds();

    /**
     * 更新培训需求状态
     * 
     * @param id 需求ID
     * @param status 状态
     * @param remark 备注
     * @return 更新后的培训需求
     */
    TrainingNeed updateStatus(Long id, TrainingNeed.Status status, String remark);

    /**
     * 批量更新培训需求状态
     * 
     * @param ids 需求ID列表
     * @param status 状态
     * @param remark 备注
     * @return 影响的行数
     */
    int batchUpdateStatus(List<Long> ids, TrainingNeed.Status status, String remark);

    /**
     * 搜索培训需求
     * 
     * @param keyword 关键词
     * @return 培训需求列表
     */
    List<TrainingNeed> search(String keyword);
}
