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
 * 指纹图谱分析实体类
 * 存储中药指纹图谱相关数据，使用MongoDB存储
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "fingerprint_analysis")
public class FingerprintAnalysis {

    /**
     * 分析ID
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
     * 图谱类型
     */
    @Field("fingerprint_type")
    private String fingerprintType;

    /**
     * 分析方法
     */
    @Field("analysis_method")
    private String analysisMethod;

    /**
     * 图谱数据（JSON格式）
     */
    @Field("fingerprint_data")
    private Map<String, Object> fingerprintData;

    /**
     * 特征峰信息
     */
    @Field("characteristic_peaks")
    private List<CharacteristicPeak> characteristicPeaks;

    /**
     * 相似度计算结果
     */
    @Field("similarity_result")
    private Double similarityResult;

    /**
     * 对照图谱ID
     */
    @Field("reference_fingerprint_id")
    private String referenceFingerprintId;

    /**
     * 图谱文件路径
     */
    @Field("fingerprint_file_path")
    private String fingerprintFilePath;

    /**
     * 分析结论
     */
    @Field("analysis_conclusion")
    private String analysisConclusion;

    /**
     * 分析人员
     */
    @Field("analysis_person")
    private String analysisPerson;

    /**
     * 分析日期
     */
    @Field("analysis_date")
    private LocalDateTime analysisDate;

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
     * 特征峰内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CharacteristicPeak {
        /**
         * 峰号
         */
        private Integer peakNumber;
        /**
         * 保留时间
         */
        private Double retentionTime;
        /**
         * 峰面积
         */
        private Double peakArea;
        /**
         * 峰高
         */
        private Double peakHeight;
        /**
         * 相对保留时间
         */
        private Double relativeRetentionTime;
        /**
         * 相对峰面积
         */
        private Double relativePeakArea;
        /**
         * 峰名称（成分）
         */
        private String peakName;
    }
}