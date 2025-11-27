package com.gmp.lims.repository.mongo;

import com.gmp.lims.entity.mongo.AuthenticityAssessment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 道地性评估MongoDB Repository接口
 * 提供道地性评估数据的CRUD操作和查询功能
 */
@Repository
public interface AuthenticityAssessmentRepository extends MongoRepository<AuthenticityAssessment, String> {

    /**
     * 根据任务ID查询道地性评估记录
     * 
     * @param taskId 任务ID
     * @return 道地性评估记录
     */
    Optional<AuthenticityAssessment> findByTaskId(Long taskId);

    /**
     * 根据样品ID查询道地性评估记录
     * 
     * @param sampleId 样品ID
     * @return 道地性评估记录列表
     */
    List<AuthenticityAssessment> findBySampleId(Long sampleId);

    /**
     * 根据评估等级查询道地性评估记录
     * 
     * @param assessmentLevel 评估等级
     * @return 道地性评估记录列表
     */
    List<AuthenticityAssessment> findByAssessmentLevel(String assessmentLevel);

    /**
     * 根据产地查询道地性评估记录
     * 
     * @param originPlace 产地
     * @return 道地性评估记录列表
     */
    List<AuthenticityAssessment> findByOriginPlace(String originPlace);

    /**
     * 根据评估人员查询道地性评估记录
     * 
     * @param assessor 评估人员
     * @return 道地性评估记录列表
     */
    List<AuthenticityAssessment> findByAssessor(String assessor);

    /**
     * 根据综合评分阈值查询道地性评估记录
     * 
     * @param scoreThreshold 评分阈值
     * @return 道地性评估记录列表
     */
    @Query("{ 'overallScore': { $gte: ?0 } }")
    List<AuthenticityAssessment> findByOverallScoreGreaterThanEqual(Double scoreThreshold);

    /**
     * 查询评分低于阈值的道地性评估记录
     * 
     * @param scoreThreshold 评分阈值
     * @return 道地性评估记录列表
     */
    @Query("{ 'overallScore': { $lt: ?0 } }")
    List<AuthenticityAssessment> findByOverallScoreLessThan(Double scoreThreshold);

    /**
     * 根据创建日期范围查询道地性评估记录
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 道地性评估记录列表
     */
    List<AuthenticityAssessment> findByCreateDateBetween(Long startDate, Long endDate);

    /**
     * 删除指定任务ID的道地性评估记录
     * 
     * @param taskId 任务ID
     * @return 删除的记录数量
     */
    long deleteByTaskId(Long taskId);

    /**
     * 删除指定样品ID的道地性评估记录
     * 
     * @param sampleId 样品ID
     * @return 删除的记录数量
     */
    long deleteBySampleId(Long sampleId);

    /**
     * 统计不同评估等级的记录数量
     * 
     * @return 统计结果列表
     */
    <T> List<T> countByAssessmentLevel(Class<T> type);

    /**
     * 根据关键词模糊查询道地性评估记录
     * 
     * @param keyword 关键词
     * @return 道地性评估记录列表
     */
    @Query("{ $or: [ { 'originPlace': { $regex: ?0, $options: 'i' } }, { 'assessmentLevel': { $regex: ?0, $options: 'i' } }, { 'conclusion': { $regex: ?0, $options: 'i' } } ] }")
    List<AuthenticityAssessment> findByKeyword(String keyword);
}