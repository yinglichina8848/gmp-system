package com.gmp.lims.entity.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 传统鉴别记录实体类
 * 存储中药的传统鉴别相关数据，使用MongoDB存储
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "traditional_identify_records")
public class TraditionalIdentifyRecord {

    /**
     * 记录ID
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
     * 性状鉴别结果
     */
    @Field("character_identify")
    private String characterIdentify;

    /**
     * 显微鉴别结果
     */
    @Field("microscopic_identify")
    private String microscopicIdentify;

    /**
     * 理化鉴别结果
     */
    @Field("physical_chemical_identify")
    private String physicalChemicalIdentify;

    /**
     * 鉴别图片列表
     */
    @Field("identify_images")
    private List<String> identifyImages;

    /**
     * 鉴别结论
     */
    @Field("identify_conclusion")
    private String identifyConclusion;

    /**
     * 鉴别人员
     */
    @Field("identify_person")
    private String identifyPerson;

    /**
     * 鉴别日期
     */
    @Field("identify_date")
    private LocalDateTime identifyDate;

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
     * 鉴别特征描述
     */
    @Field("feature_description")
    private String featureDescription;

    /**
     * 经验鉴别要点
     */
    @Field("experience_points")
    private String experiencePoints;

    /**
     * 参考标准
     */
    @Field("reference_standard")
    private String referenceStandard;
}