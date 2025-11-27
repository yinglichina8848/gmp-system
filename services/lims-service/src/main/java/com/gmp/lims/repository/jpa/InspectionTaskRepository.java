package com.gmp.lims.repository.jpa;

import com.gmp.lims.entity.InspectionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 检测任务Repository接口
 * 提供检测任务数据的CRUD操作和查询功能
 */
@Repository
public interface InspectionTaskRepository
        extends JpaRepository<InspectionTask, Long>, JpaSpecificationExecutor<InspectionTask> {

    /**
     * 根据任务编号查询任务
     * 
     * @param taskCode 任务编号
     * @return 检测任务对象
     */
    Optional<InspectionTask> findByTaskCode(String taskCode);

    /**
     * 根据样品ID查询任务列表
     * 
     * @param sampleId 样品ID
     * @return 检测任务列表
     */
    List<InspectionTask> findBySampleId(Long sampleId);

    /**
     * 根据任务状态查询任务列表
     * 
     * @param status 任务状态
     * @return 检测任务列表
     */
    List<InspectionTask> findByStatus(String status);

    /**
     * 根据检测类型查询任务列表
     * 
     * @param inspectionType 检测类型
     * @return 检测任务列表
     */
    List<InspectionTask> findByInspectionType(String inspectionType);

    /**
     * 根据负责人查询任务列表
     * 
     * @param responsiblePerson 负责人
     * @return 检测任务列表
     */
    List<InspectionTask> findByResponsiblePerson(String responsiblePerson);

    /**
     * 根据创建日期范围查询任务列表
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 检测任务列表
     */
    List<InspectionTask> findByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据计划完成日期范围查询任务列表
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 检测任务列表
     */
    List<InspectionTask> findByPlanCompleteDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据实际完成日期范围查询任务列表
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 检测任务列表
     */
    List<InspectionTask> findByActualCompleteDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询包含指纹图谱检测的任务（中药特色查询）
     * 
     * @return 检测任务列表
     */
    List<InspectionTask> findByIncludeFingerprintTrue();

    /**
     * 查询包含传统鉴别检测的任务（中药特色查询）
     * 
     * @return 检测任务列表
     */
    List<InspectionTask> findByIncludeTraditionalIdentifyTrue();

    /**
     * 查询需要道地性评估的任务（中药特色查询）
     * 
     * @return 检测任务列表
     */
    List<InspectionTask> findByNeedAuthenticityAssessmentTrue();

    /**
     * 根据任务状态统计任务数量
     * 
     * @param status 任务状态
     * @return 任务数量
     */
    long countByStatus(String status);

    /**
     * 查询任务统计信息
     * 
     * @return 统计结果列表
     */
    @Query("SELECT t.inspectionType, COUNT(t.id) FROM InspectionTask t GROUP BY t.inspectionType")
    List<Object[]> countByInspectionType();

    /**
     * 查询逾期未完成的任务
     * 
     * @param currentTime 当前时间
     * @return 检测任务列表
     */
    List<InspectionTask> findByPlanCompleteDateBeforeAndStatusNot(LocalDateTime currentTime, String completedStatus);
}