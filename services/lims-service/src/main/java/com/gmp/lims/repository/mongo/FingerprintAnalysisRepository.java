package com.gmp.lims.repository.mongo;

import com.gmp.lims.entity.mongo.FingerprintAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 指纹图谱分析MongoDB Repository接口
 * 提供指纹图谱分析数据的CRUD操作和查询功能
 */
@Repository
public interface FingerprintAnalysisRepository extends MongoRepository<FingerprintAnalysis, String> {

    /**
     * 根据任务ID查询指纹图谱分析记录
     * 
     * @param taskId 任务ID
     * @return 指纹图谱分析记录
     */
    Optional<FingerprintAnalysis> findByTaskId(Long taskId);

    /**
     * 根据样品ID查询指纹图谱分析记录
     * 
     * @param sampleId 样品ID
     * @return 指纹图谱分析记录列表
     */
    List<FingerprintAnalysis> findBySampleId(Long sampleId);

    /**
     * 根据图谱类型查询指纹图谱分析记录
     * 
     * @param fingerprintType 图谱类型
     * @return 指纹图谱分析记录列表
     */
    List<FingerprintAnalysis> findByFingerprintType(String fingerprintType);

    /**
     * 根据分析方法查询指纹图谱分析记录
     * 
     * @param analysisMethod 分析方法
     * @return 指纹图谱分析记录列表
     */
    List<FingerprintAnalysis> findByAnalysisMethod(String analysisMethod);

    /**
     * 根据相似度阈值查询指纹图谱分析记录
     * 
     * @param similarityThreshold 相似度阈值
     * @return 指纹图谱分析记录列表
     */
    @Query("{ 'similarityScore': { $gte: ?0 } }")
    List<FingerprintAnalysis> findBySimilarityScoreGreaterThanEqual(Double similarityThreshold);

    /**
     * 查询相似度低于阈值的指纹图谱分析记录
     * 
     * @param similarityThreshold 相似度阈值
     * @return 指纹图谱分析记录列表
     */
    @Query("{ 'similarityScore': { $lt: ?0 } }")
    List<FingerprintAnalysis> findBySimilarityScoreLessThan(Double similarityThreshold);

    /**
     * 根据分析人员查询指纹图谱分析记录
     * 
     * @param analyst 分析人员
     * @return 指纹图谱分析记录列表
     */
    List<FingerprintAnalysis> findByAnalyst(String analyst);

    /**
     * 根据创建日期范围查询指纹图谱分析记录
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 指纹图谱分析记录列表
     */
    List<FingerprintAnalysis> findByCreateDateBetween(Long startDate, Long endDate);

    /**
     * 删除指定任务ID的指纹图谱分析记录
     * 
     * @param taskId 任务ID
     * @return 删除的记录数量
     */
    long deleteByTaskId(Long taskId);

    /**
     * 删除指定样品ID的指纹图谱分析记录
     * 
     * @param sampleId 样品ID
     * @return 删除的记录数量
     */
    long deleteBySampleId(Long sampleId);

    /**
     * 统计不同图谱类型的分析记录数量
     * 
     * @return 统计结果列表
     */
    <T> List<T> countByFingerprintType(Class<T> type);

    /**
     * 根据关键词模糊查询指纹图谱分析记录
     * 
     * @param keyword 关键词
     * @return 指纹图谱分析记录列表
     */
    @Query("{ $or: [ { 'fingerprintType': { $regex: ?0, $options: 'i' } }, { 'analysisMethod': { $regex: ?0, $options: 'i' } }, { 'conclusion': { $regex: ?0, $options: 'i' } } ] }")
    List<FingerprintAnalysis> findByKeyword(String keyword);
}