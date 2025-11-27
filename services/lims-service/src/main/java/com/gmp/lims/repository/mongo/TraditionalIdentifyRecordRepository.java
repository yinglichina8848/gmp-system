package com.gmp.lims.repository.mongo;

import com.gmp.lims.entity.mongo.TraditionalIdentifyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 传统鉴别记录MongoDB Repository接口
 * 提供传统鉴别记录数据的CRUD操作和查询功能
 */
@Repository
public interface TraditionalIdentifyRecordRepository extends MongoRepository<TraditionalIdentifyRecord, String> {

    /**
     * 根据任务ID查询传统鉴别记录
     * 
     * @param taskId 任务ID
     * @return 传统鉴别记录列表
     */
    List<TraditionalIdentifyRecord> findByTaskId(Long taskId);

    /**
     * 根据样品ID查询传统鉴别记录
     * 
     * @param sampleId 样品ID
     * @return 传统鉴别记录列表
     */
    List<TraditionalIdentifyRecord> findBySampleId(Long sampleId);

    /**
     * 根据任务ID和鉴别类型查询传统鉴别记录
     * 
     * @param taskId       任务ID
     * @param identifyType 鉴别类型（性状鉴别、显微鉴别、理化鉴别等）
     * @return 传统鉴别记录
     */
    Optional<TraditionalIdentifyRecord> findByTaskIdAndIdentifyType(Long taskId, String identifyType);

    /**
     * 根据鉴别结论查询传统鉴别记录
     * 
     * @param conclusion 鉴别结论
     * @return 传统鉴别记录列表
     */
    List<TraditionalIdentifyRecord> findByConclusion(String conclusion);

    /**
     * 根据鉴别人员查询传统鉴别记录
     * 
     * @param identifier 鉴别人员
     * @return 传统鉴别记录列表
     */
    List<TraditionalIdentifyRecord> findByIdentifier(String identifier);

    /**
     * 根据创建日期范围查询传统鉴别记录
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 传统鉴别记录列表
     */
    List<TraditionalIdentifyRecord> findByCreateDateBetween(Long startDate, Long endDate);

    /**
     * 删除指定任务ID的传统鉴别记录
     * 
     * @param taskId 任务ID
     * @return 删除的记录数量
     */
    long deleteByTaskId(Long taskId);

    /**
     * 删除指定样品ID的传统鉴别记录
     * 
     * @param sampleId 样品ID
     * @return 删除的记录数量
     */
    long deleteBySampleId(Long sampleId);

    /**
     * 统计不同鉴别类型的记录数量
     * 
     * @return 统计结果列表，每个元素是[鉴别类型, 数量]的数组
     */
    <T> List<T> countByIdentifyType(Class<T> type);

    /**
     * 根据关键词模糊查询传统鉴别记录
     * 匹配鉴别描述、鉴别结论或备注中的关键词
     * 
     * @param keyword 关键词
     * @return 传统鉴别记录列表
     */
    List<TraditionalIdentifyRecord> findByIdentifyDescriptionContainingOrConclusionContainingOrRemarkContaining(
            String keyword, String keyword1, String keyword2);
}