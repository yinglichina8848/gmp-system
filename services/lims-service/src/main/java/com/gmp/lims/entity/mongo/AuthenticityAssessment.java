package com.gmp.lims.entity.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 道地性评估实体类
 * 存储中药道地性评估相关数据，使用MongoDB存储
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "authenticity_assessment")
public class AuthenticityAssessment {

    /**
     * 评估ID
     */
    @Id
    private String id;

    /**
     * 样品ID
     */
    @Field("sample_id")
    private Long sampleId;

    /**
     * 样品编号
     */
    @Field("sample_code")
    private String sampleCode;

    /**
     * 任务ID
     */
    @Field("task_id")
    private Long taskId;

    /**
     * 任务编号
     */
    @Field("task_code")
    private String taskCode;

    /**
     * 药材名称
     */
    @Field("medicine_name")
    private String medicineName;

    /**
     * 产地信息
     */
    @Field("production_area")
    private String productionArea;

    /**
     * 道地产区标准
     */
    @Field("authentic_area_standard")
    private String authenticAreaStandard;

    /**
     * 评估指标列表
     */
    @Field("assessment_indicators")
    private List<AssessmentIndicator> assessmentIndicators;

    /**
     * 综合评分
     */
    @Field("comprehensive_score")
    private Double comprehensiveScore;

    /**
     * 评估等级
     */
    @Field("assessment_level")
    private String assessmentLevel;

    /**
     * 评估结论
     */
    @Field("assessment_conclusion")
    private String assessmentConclusion;

    /**
     * 特殊成分分析
     */
    @Field("special_component_analysis")
    private Map<String, Object> specialComponentAnalysis;

    /**
     * 评估人员
     */
    @Field("assessment_person")
    private String assessmentPerson;

    /**
     * 评估日期
     */
    @Field("assessment_date")
    private LocalDateTime assessmentDate;

    /**
     * 审核人员
     */
    @Field("review_person")
    private String reviewPerson;

    /**
     * 审核日期
     */
    @Field("review_date")
    private LocalDateTime reviewDate;

    /**
     * 审核状态
     */
    @Field("review_status")
    private String reviewStatus;

    /**
     * 备注
     */
    @Field("remark")
    private String remark;

    /**
     * 创建时间
     */
    @Field("created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Field("updated_time")
    private LocalDateTime updatedTime;

    /**
     * 评估指标内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssessmentIndicator {
        /**
         * 指标名称
         */
        private String indicatorName;
        /**
         * 指标权重
         */
        private Double weight;
        /**
         * 实际得分
         */
        private Double actualScore;
        /**
         * 满分值
         */
        private Double fullScore;
        /**
         * 评估方法
         */
        private String assessmentMethod;
        /**
         * 评估结果描述
         */
        private String resultDescription;
    }
}