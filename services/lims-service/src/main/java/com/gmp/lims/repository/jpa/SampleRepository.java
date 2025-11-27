package com.gmp.lims.repository.jpa;

import com.gmp.lims.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 样品Repository接口
 * 提供样品数据的CRUD操作和查询功能
 */
@Repository
public interface SampleRepository extends JpaRepository<Sample, Long>, JpaSpecificationExecutor<Sample> {

    /**
     * 根据样品编号查询样品
     * 
     * @param sampleCode 样品编号
     * @return 样品对象
     */
    Optional<Sample> findBySampleCode(String sampleCode);

    /**
     * 根据样品状态查询样品列表
     * 
     * @param status 样品状态
     * @return 样品列表
     */
    List<Sample> findByStatus(String status);

    /**
     * 根据样品类型查询样品列表
     * 
     * @param sampleType 样品类型
     * @return 样品列表
     */
    List<Sample> findBySampleType(String sampleType);

    /**
     * 根据样品来源查询样品列表
     * 
     * @param sampleSource 样品来源
     * @return 样品列表
     */
    List<Sample> findBySampleSource(String sampleSource);

    /**
     * 根据创建时间范围查询样品列表
     * 
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 样品列表
     */
    List<Sample> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据接收日期范围查询样品列表
     * 
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 样品列表
     */
    List<Sample> findByReceiptDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 根据药材名称查询样品列表（中药特色查询）
     * 
     * @param chineseMedicineName 药材名称
     * @return 样品列表
     */
    List<Sample> findByChineseMedicineName(String chineseMedicineName);

    /**
     * 根据道地产区查询样品列表（中药特色查询）
     * 
     * @param authenticProductionArea 道地产区
     * @return 样品列表
     */
    List<Sample> findByAuthenticProductionArea(String authenticProductionArea);

    /**
     * 查询样品统计信息
     * 
     * @return 统计结果列表
     */
    @Query("SELECT s.sampleType, COUNT(s.id) FROM Sample s GROUP BY s.sampleType")
    List<Object[]> countBySampleType();

    /**
     * 根据状态统计样品数量
     * 
     * @param status 样品状态
     * @return 样品数量
     */
    long countByStatus(String status);

    /**
     * 根据关键词模糊查询样品
     * 
     * @param keyword 关键词
     * @return 样品列表
     */
    @Query("SELECT s FROM Sample s WHERE s.sampleCode LIKE %:keyword% OR s.sampleName LIKE %:keyword% OR s.batchNumber LIKE %:keyword%")
    List<Sample> findByKeyword(@Param("keyword") String keyword);
}