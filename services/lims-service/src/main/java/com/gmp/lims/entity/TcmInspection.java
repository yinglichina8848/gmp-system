package com.gmp.lims.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 中药特色检验实体类
 * 用于记录中药材特有的检验项目和结果，如性状鉴别、显微鉴别、薄层色谱等
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "lims_tcm_inspection", indexes = {
        @Index(name = "idx_tcm_inspection_code", columnList = "inspection_code", unique = true),
        @Index(name = "idx_tcm_inspection_task_id", columnList = "task_id"),
        @Index(name = "idx_tcm_inspection_herb_name", columnList = "herb_name"),
        @Index(name = "idx_tcm_inspection_create_date", columnList = "create_date")
})
public class TcmInspection {

    /**
     * 检验ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 检验编号
     */
    @NotBlank(message = "检验编号不能为空")
    @Size(max = 50, message = "检验编号长度不能超过50个字符")
    @Column(name = "inspection_code", nullable = false, unique = true, length = 50)
    private String inspectionCode;

    /**
     * 关联的检验任务ID
     */
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    /**
     * 中药材名称
     */
    @NotBlank(message = "中药材名称不能为空")
    @Size(max = 100, message = "中药材名称长度不能超过100个字符")
    @Column(name = "herb_name", nullable = false, length = 100)
    private String herbName;

    /**
     * 检验方法类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_method", nullable = false, length = 50)
    private InspectionMethod inspectionMethod;

    /**
     * 性状鉴别结果
     */
    @Column(name = "appearance_result", columnDefinition = "TEXT")
    private String appearanceResult;

    /**
     * 显微鉴别结果
     */
    @Column(name = "microscopic_result", columnDefinition = "TEXT")
    private String microscopicResult;

    /**
     * 薄层色谱结果
     */
    @Column(name = "tlc_result", columnDefinition = "TEXT")
    private String tlcResult;

    /**
     * 高效液相色谱结果
     */
    @Column(name = "hplc_result", columnDefinition = "TEXT")
    private String hplcResult;

    /**
     * 检验结果判定
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "result_judgment", nullable = false, length = 20)
    private ResultJudgment resultJudgment;

    /**
     * 检验员ID
     */
    @Column(name = "inspector_id", nullable = false)
    private Long inspectorId;

    /**
     * 审核员ID
     */
    @Column(name = "reviewer_id")
    private Long reviewerId;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 创建日期
     */
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    /**
     * 更新日期
     */
    @Column(name = "update_date", nullable = false)
    private LocalDateTime updateDate;

    /**
     * 检验方法枚举
     */
    public enum InspectionMethod {
        /**
         * 性状鉴别
         */
        APPEARANCE,

        /**
         * 显微鉴别
         */
        MICROSCOPIC,

        /**
         * 薄层色谱法
         */
        TLC,

        /**
         * 高效液相色谱法
         */
        HPLC,

        /**
         * 气相色谱法
         */
        GC,

        /**
         * 紫外-可见分光光度法
         */
        UV_VIS,

        /**
         * 原子吸收光谱法
         */
        AAS,

        /**
         * 电感耦合等离子体质谱法
         */
        ICP_MS,

        /**
         * DNA条形码鉴定
         */
        DNA_BARCODING,

        /**
         * 其他方法
         */
        OTHER
    }

    /**
     * 检验结果判定枚举
     */
    public enum ResultJudgment {
        /**
         * 符合规定
         */
        COMPLIES,

        /**
         * 不符合规定
         */
        NON_COMPLIANCE,

        /**
         * 待复检
         */
        PENDING_RETEST,

        /**
         * 异常
         */
        ABNORMAL
    }
}