package com.gmp.lims.util;

import com.gmp.lims.dto.InspectionResultDTO;
import com.gmp.lims.dto.InspectionTaskDTO;
import com.gmp.lims.dto.SampleDTO;
import com.gmp.lims.entity.InspectionResult;
import com.gmp.lims.entity.InspectionTask;
import com.gmp.lims.entity.Sample;
import com.gmp.lims.entity.mongo.AuthenticityAssessment;
import com.gmp.lims.entity.mongo.FingerprintAnalysis;
import com.gmp.lims.entity.mongo.TraditionalIdentifyRecord;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 测试工具类
 * 提供测试数据和辅助方法
 */
public class TestUtils {

    /**
     * 创建测试用样品实体
     */
    public static Sample createSample() {
        Sample sample = new Sample();
        sample.setId(1L);
        sample.setSampleCode("S20230001");
        sample.setSampleName("人参样品1");
        sample.setSampleType("中药材");
        sample.setBatchNumber("B20230001");
        sample.setQuantity(500.0);
        sample.setUnit("g");
        sample.setSampleSource("供应商A");
        sample.setReceiptDate(LocalDateTime.now().minusDays(10));
        sample.setProductionDate(LocalDateTime.now().minusMonths(2));
        sample.setExpirationDate(LocalDateTime.now().plusYears(2));
        sample.setStorageLocation("冷藏库A区");
        sample.setStatus("已接收");
        sample.setRemarks("道地药材，需要特殊保存");
        sample.setChineseMedicineName("人参");
        sample.setAuthenticProductionArea("吉林长白山");
        sample.setHarvestSeason("秋季");
        sample.setMedicinalPart("根及根茎");
        sample.setCreatedTime(LocalDateTime.now().minusDays(10));
        sample.setUpdatedTime(LocalDateTime.now().minusDays(10));
        return sample;
    }

    /**
     * 创建测试用样品DTO
     */
    public static SampleDTO createSampleDTO() {
        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.setId(1L);
        sampleDTO.setSampleCode("S20230001");
        sampleDTO.setSampleName("人参样品1");
        sampleDTO.setSampleType("中药材");
        sampleDTO.setBatchNumber("B20230001");
        sampleDTO.setQuantity(500.0);
        sampleDTO.setUnit("g");
        sampleDTO.setSampleSource("供应商A");
        sampleDTO.setReceiptDate(LocalDateTime.now().minusDays(10));
        sampleDTO.setProductionDate(LocalDateTime.now().minusMonths(2));
        sampleDTO.setExpirationDate(LocalDateTime.now().plusYears(2));
        sampleDTO.setStorageLocation("冷藏库A区");
        sampleDTO.setStatus("已接收");
        sampleDTO.setRemarks("道地药材，需要特殊保存");
        sampleDTO.setChineseMedicineName("人参");
        sampleDTO.setAuthenticProductionArea("吉林长白山");
        sampleDTO.setHarvestSeason("秋季");
        sampleDTO.setMedicinalPart("根及根茎");
        return sampleDTO;
    }

    /**
     * 创建测试用检测任务实体
     */
    public static InspectionTask createInspectionTask() {
        InspectionTask task = new InspectionTask();
        task.setId(1L);
        task.setTaskCode("IT20230001");
        task.setSampleId(1L);
        task.setSampleCode("S20230001");
        task.setTaskName("人参药材质量检测");
        task.setInspectionType("全面检测");
        task.setInspectionPurpose("入库检验");
        task.setStatus("进行中");
        task.setResponsiblePerson("张三");
        task.setCreateDate(LocalDateTime.now().minusDays(5));
        task.setPlanStartDate(LocalDateTime.now().minusDays(3));
        task.setPlanCompleteDate(LocalDateTime.now().plusDays(2));
        task.setActualStartDate(LocalDateTime.now().minusDays(3));
        task.setIncludeFingerprint(true);
        task.setIncludeTraditionalIdentify(true);
        task.setNeedAuthenticityAssessment(true);
        task.setRemarks("重点关注道地性和有效成分含量");
        return task;
    }

    /**
     * 创建测试用检测任务DTO
     */
    public static InspectionTaskDTO createInspectionTaskDTO() {
        InspectionTaskDTO taskDTO = new InspectionTaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTaskCode("IT20230001");
        taskDTO.setSampleId(1L);
        taskDTO.setSampleCode("S20230001");
        taskDTO.setSampleName("人参样品1");
        taskDTO.setTaskName("人参药材质量检测");
        taskDTO.setInspectionType("全面检测");
        taskDTO.setInspectionPurpose("入库检验");
        taskDTO.setStatus("进行中");
        taskDTO.setResponsiblePerson("张三");
        taskDTO.setCreateDate(LocalDateTime.now().minusDays(5));
        taskDTO.setPlanStartDate(LocalDateTime.now().minusDays(3));
        taskDTO.setPlanCompleteDate(LocalDateTime.now().plusDays(2));
        taskDTO.setActualStartDate(LocalDateTime.now().minusDays(3));
        taskDTO.setIncludeFingerprint(true);
        taskDTO.setIncludeTraditionalIdentify(true);
        taskDTO.setNeedAuthenticityAssessment(true);
        taskDTO.setRemarks("重点关注道地性和有效成分含量");
        return taskDTO;
    }

    /**
     * 创建测试用检测结果实体
     */
    public static InspectionResult createInspectionResult() {
        InspectionResult result = new InspectionResult();
        result.setId(1L);
        result.setTaskId(1L);
        result.setTaskCode("IT20230001");
        result.setItemName("人参皂苷Rg1含量");
        result.setTestMethod("高效液相色谱法");
        result.setTestValue("0.35");
        result.setMeasurementUnit("%");
        result.setStandardValue("≥0.30");
        result.setStandardRange("0.30-0.50");
        result.setJudgmentResult("合格");
        result.setTestDate(LocalDateTime.now().minusDays(1));
        result.setTestPerson("李四");
        result.setReviewPerson("王五");
        result.setReviewDate(LocalDateTime.now());
        result.setReviewStatus("已审核");
        result.setRemark("含量符合《中国药典》标准");
        result.setResultDescription("色谱峰分离度良好，含量测定准确");
        result.setIsKeyIndicator(true);
        result.setCreatedTime(LocalDateTime.now().minusDays(1));
        result.setUpdatedTime(LocalDateTime.now());
        return result;
    }

    /**
     * 创建测试用检测结果DTO
     */
    public static InspectionResultDTO createInspectionResultDTO() {
        InspectionResultDTO resultDTO = new InspectionResultDTO();
        resultDTO.setId(1L);
        resultDTO.setTaskId(1L);
        resultDTO.setTaskCode("IT20230001");
        resultDTO.setItemName("人参皂苷Rg1含量");
        resultDTO.setTestMethod("高效液相色谱法");
        resultDTO.setTestValue("0.35");
        resultDTO.setMeasurementUnit("%");
        resultDTO.setStandardValue("≥0.30");
        resultDTO.setStandardRange("0.30-0.50");
        resultDTO.setJudgmentResult("合格");
        resultDTO.setTestDate(LocalDateTime.now().minusDays(1));
        resultDTO.setTestPerson("李四");
        resultDTO.setReviewPerson("王五");
        resultDTO.setReviewDate(LocalDateTime.now());
        resultDTO.setReviewStatus("已审核");
        resultDTO.setRemark("含量符合《中国药典》标准");
        resultDTO.setResultDescription("色谱峰分离度良好，含量测定准确");
        resultDTO.setIsKeyIndicator(true);
        resultDTO.setCreatedTime(LocalDateTime.now().minusDays(1));
        resultDTO.setUpdatedTime(LocalDateTime.now());
        return resultDTO;
    }

    /**
     * 创建测试用传统鉴别记录
     */
    public static TraditionalIdentifyRecord createTraditionalIdentifyRecord() {
        TraditionalIdentifyRecord record = new TraditionalIdentifyRecord();
        record.setId("TIR001");
        record.setTaskId(1L);
        record.setSampleId(1L);
        record.setIdentifyType("性状鉴别");
        record.setIdentifyDescription("主根呈纺锤形或圆柱形，表面灰黄色，上部或全体有疏浅断续的粗横纹及明显的纵皱，下部有支根2-3条，并着生多数细长的须根。");
        record.setMorphologicalIdentifyResult("符合人参性状特征");
        record.setMicroscopicIdentifyResult("无显微鉴别数据");
        record.setPhysicalChemicalIdentifyResult("无理化鉴别数据");
        record.setIdentifyImages(Arrays.asList("image1.jpg", "image2.jpg"));
        record.setConclusion("通过性状鉴别，符合正品特征");
        record.setIdentifier("赵六");
        record.setIdentifyDate(new Date());
        record.setRemark("色泽较好，质地坚实");
        record.setCreateDate(System.currentTimeMillis());
        record.setUpdateDate(System.currentTimeMillis());
        return record;
    }

    /**
     * 创建测试用指纹图谱分析记录
     */
    public static FingerprintAnalysis createFingerprintAnalysis() {
        FingerprintAnalysis analysis = new FingerprintAnalysis();
        analysis.setId("FPA001");
        analysis.setTaskId(1L);
        analysis.setSampleId(1L);
        analysis.setFingerprintType("HPLC指纹图谱");
        analysis.setAnalysisMethod("《中国药典》人参项下方法");
        analysis.setInstrumentInfo("Agilent 1260高效液相色谱仪");
        analysis.setChromatographicConditions("色谱柱：C18柱，流动相：乙腈-水梯度洗脱");
        analysis.setFingerprintData(Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0));

        List<FingerprintAnalysis.CharacteristicPeak> peaks = new ArrayList<>();
        FingerprintAnalysis.CharacteristicPeak peak1 = new FingerprintAnalysis.CharacteristicPeak();
        peak1.setPeakNumber(1);
        peak1.setRetentionTime(2.5);
        peak1.setPeakArea(1000.0);
        peak1.setPeakHeight(500.0);
        peak1.setPeakName("峰1");
        peaks.add(peak1);

        FingerprintAnalysis.CharacteristicPeak peak2 = new FingerprintAnalysis.CharacteristicPeak();
        peak2.setPeakNumber(2);
        peak2.setRetentionTime(5.2);
        peak2.setPeakArea(2000.0);
        peak2.setPeakHeight(1000.0);
        peak2.setPeakName("峰2（人参皂苷Rg1）");
        peaks.add(peak2);

        analysis.setCharacteristicPeaks(peaks);
        analysis.setReferenceFingerprintId("REF001");
        analysis.setSimilarityScore(0.95);
        analysis.setSimilarityMethod("夹角余弦法");
        analysis.setConclusion("指纹图谱相似度符合要求");
        analysis.setAnalyst("钱七");
        analysis.setAnalyzeDate(new Date());
        analysis.setRemark("图谱基线平稳，分离度良好");
        analysis.setCreateDate(System.currentTimeMillis());
        analysis.setUpdateDate(System.currentTimeMillis());
        return analysis;
    }

    /**
     * 创建测试用道地性评估记录
     */
    public static AuthenticityAssessment createAuthenticityAssessment() {
        AuthenticityAssessment assessment = new AuthenticityAssessment();
        assessment.setId("AA001");
        assessment.setTaskId(1L);
        assessment.setSampleId(1L);
        assessment.setOriginPlace("吉林长白山");
        assessment.setPlantingEnvironment("温带大陆性气候，海拔500-1000米山地");
        assessment.setHarvestTime("秋季");
        assessment.setProcessingMethod("晒干");

        List<AuthenticityAssessment.AssessmentIndicator> indicators = new ArrayList<>();
        AuthenticityAssessment.AssessmentIndicator indicator1 = new AuthenticityAssessment.AssessmentIndicator();
        indicator1.setIndicatorName("外观特征");
        indicator1.setIndicatorValue(95.0);
        indicator1.setIndicatorWeight(0.2);
        indicator1.setDescription("符合道地药材外观特征");
        indicators.add(indicator1);

        AuthenticityAssessment.AssessmentIndicator indicator2 = new AuthenticityAssessment.AssessmentIndicator();
        indicator2.setIndicatorName("有效成分含量");
        indicator2.setIndicatorValue(90.0);
        indicator2.setIndicatorWeight(0.4);
        indicator2.setDescription("人参皂苷含量符合道地标准");
        indicators.add(indicator2);

        AuthenticityAssessment.AssessmentIndicator indicator3 = new AuthenticityAssessment.AssessmentIndicator();
        indicator3.setIndicatorName("产地环境");
        indicator3.setIndicatorValue(100.0);
        indicator3.setIndicatorWeight(0.2);
        indicator3.setDescription("来自道地产区吉林长白山");
        indicators.add(indicator3);

        AuthenticityAssessment.AssessmentIndicator indicator4 = new AuthenticityAssessment.AssessmentIndicator();
        indicator4.setIndicatorName("加工方法");
        indicator4.setIndicatorValue(90.0);
        indicator4.setIndicatorWeight(0.2);
        indicator4.setDescription("传统加工方法");
        indicators.add(indicator4);

        assessment.setAssessmentIndicators(indicators);
        assessment.setOverallScore(93.0);
        assessment.setAssessmentLevel("优");
        assessment.setConclusion("符合道地药材标准，品质优良");
        assessment.setAssessor("孙八");
        assessment.setAssessDate(new Date());
        assessment.setRemark("推荐使用");
        assessment.setCreateDate(System.currentTimeMillis());
        assessment.setUpdateDate(System.currentTimeMillis());
        return assessment;
    }

    /**
     * 获取不同状态的样品列表
     */
    public static List<Sample> getSamplesWithDifferentStatus() {
        List<Sample> samples = new ArrayList<>();

        Sample sample1 = createSample();
        sample1.setId(1L);
        sample1.setSampleCode("S20230001");
        sample1.setStatus("已接收");
        samples.add(sample1);

        Sample sample2 = createSample();
        sample2.setId(2L);
        sample2.setSampleCode("S20230002");
        sample2.setSampleName("当归样品1");
        sample2.setChineseMedicineName("当归");
        sample2.setAuthenticProductionArea("甘肃岷县");
        sample2.setStatus("检测中");
        samples.add(sample2);

        Sample sample3 = createSample();
        sample3.setId(3L);
        sample3.setSampleCode("S20230003");
        sample3.setSampleName("黄芪样品1");
        sample3.setChineseMedicineName("黄芪");
        sample3.setAuthenticProductionArea("内蒙古");
        sample3.setStatus("已完成");
        samples.add(sample3);

        return samples;
    }

    /**
     * 获取不同状态的检测任务列表
     */
    public static List<InspectionTask> getTasksWithDifferentStatus() {
        List<InspectionTask> tasks = new ArrayList<>();

        InspectionTask task1 = createInspectionTask();
        task1.setId(1L);
        task1.setTaskCode("IT20230001");
        task1.setStatus("待分配");
        tasks.add(task1);

        InspectionTask task2 = createInspectionTask();
        task2.setId(2L);
        task2.setTaskCode("IT20230002");
        task2.setTaskName("当归药材质量检测");
        task2.setSampleId(2L);
        task2.setSampleCode("S20230002");
        task2.setStatus("进行中");
        tasks.add(task2);

        InspectionTask task3 = createInspectionTask();
        task3.setId(3L);
        task3.setTaskCode("IT20230003");
        task3.setTaskName("黄芪药材质量检测");
        task3.setSampleId(3L);
        task3.setSampleCode("S20230003");
        task3.setStatus("已完成");
        task3.setActualCompleteDate(LocalDateTime.now());
        tasks.add(task3);

        return tasks;
    }
}