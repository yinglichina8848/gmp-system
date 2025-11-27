package com.gmp.lims.repository.jpa;

import com.gmp.lims.entity.InspectionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 检测结果Repository接口
 * 提供检测结果数据的CRUD操作和查询功能
 */
@Repository
public interface InspectionResultRepository
        extends JpaRepository<InspectionResult, Long>, JpaSpecificationExecutor<InspectionResult> {

    /**
     * 根据任务ID查询检测结果列表
     * 
     * @param taskId 任务ID
     * @return 检测结果列表
     */
    List<InspectionResult> findByTaskId(Long taskId);

    /**
     * 根据任务编号查询检测结果列表
     * 
     * @param taskCode 任务编号
     * @return 检测结果列表
     */
    List<InspectionResult> findByTaskCode(String taskCode);

    /**
     * 根据检测项目名称查询检测结果列表
     * 
     * @param itemName 检测项目名称
     * @return 检测结果列表
     */
    List<InspectionResult> findByItemName(String itemName);

    /**
     * 根据判定结果查询检测结果列表
     * 
     * @param judgmentResult 判定结果
     * @return 检测结果列表
     */
    List<InspectionResult> findByJudgmentResult(String judgmentResult);

    /**
     * 根据审核状态查询检测结果列表
     * 
     * @param reviewStatus 审核状态
     * @return 检测结果列表
     */
    List<InspectionResult> findByReviewStatus(String reviewStatus);

    /**
     * 根据检测人员查询检测结果列表
     * 
     * @param testPerson 检测人员
     * @return 检测结果列表
     */
    List<InspectionResult> findByTestPerson(String testPerson);

    /**
     * 根据审核人员查询检测结果列表
     * 
     * @param reviewPerson 审核人员
     * @return 检测结果列表
     */
    List<InspectionResult> findByReviewPerson(String reviewPerson);

    /**
     * 根据检测日期范围查询检测结果列表
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 检测结果列表
     */
    List<InspectionResult> findByTestDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查询关键指标的检测结果（中药特色查询）
     * 
     * @return 检测结果列表
     */
    List<InspectionResult> findByIsKeyIndicatorTrue();

    /**
     * 根据任务ID和项目名称查询检测结果
     * 
     * @param taskId   任务ID
     * @param itemName 项目名称
     * @return 检测结果
     */
    Optional<InspectionResult> findByTaskIdAndItemName(Long taskId, String itemName);

    /**
     * 统计不同判定结果的数量
     * 
     * @return 统计结果列表
     */
    @Query("SELECT r.judgmentResult, COUNT(r.id) FROM InspectionResult r GROUP BY r.judgmentResult")
    List<Object[]> countByJudgmentResult();

    /**
     * 查询任务的所有检测结果是否已全部审核
     * 
     * @param taskId 任务ID
     * @return 是否全部已审核
     */
    @Query("SELECT COUNT(r) = 0 FROM InspectionResult r WHERE r.taskId = :taskId AND r.reviewStatus != 'APPROVED'")
    boolean isAllResultsApproved(@Param("taskId") Long taskId);

    /**
     * 根据关键词模糊查询检测结果
     * 
     * @param keyword 关键词
     * @return 检测结果列表
     */
    @Query("SELECT r FROM InspectionResult r WHERE r.taskCode LIKE %:keyword% OR r.itemName LIKE %:keyword% OR r.testMethod LIKE %:keyword%")
    List<InspectionResult> findByKeyword(@Param("keyword") String keyword);
}