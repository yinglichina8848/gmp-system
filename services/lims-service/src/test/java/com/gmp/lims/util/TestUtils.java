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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        sample.setSampleQuantity(500);
        sample.setUnit("g");
        sample.setSampleSource("供应商A");
        sample.setReceiptDate(LocalDateTime.now().minusDays(10));
        sample.setStatus("已接收");
        sample.setRemark("道地药材，需要特殊保存");
        // 保留基本属性，移除可能不存在的中药特有属性
        sample.setSpecification("一等品");
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
        sampleDTO.setUnit("g");
        sampleDTO.setSampleSource("供应商A");
        sampleDTO.setReceiptDate(LocalDateTime.now().minusDays(10));
        sampleDTO.setStatus("已接收");
        // 保留DTO中的属性设置
        return sampleDTO;
    }

    /**
     * 创建测试用检测任务实体
     */
    public static InspectionTask createInspectionTask() {
        InspectionTask task = new InspectionTask();
        task.setId(1L);
        task.setTaskCode("IT20230001");
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
        taskDTO.setRemark("重点关注道地性和有效成分含量");
        return taskDTO;
    }

    /**
     * 创建测试用检测结果实体
     */
    public static InspectionResult createInspectionResult() {
        InspectionResult result = new InspectionResult();
        result.setId(1L);
        // 移除不存在的方法调用
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
        record.setCharacterIdentify("主根呈纺锤形或圆柱形，表面灰黄色，上部或全体有疏浅断续的粗横纹及明显的纵皱，下部有支根2-3条，并着生多数细长的须根。");
        record.setMicroscopicIdentify("无显微鉴别数据");
        record.setIdentifyImages(Arrays.asList("image1.jpg", "image2.jpg"));
        record.setIdentifyPerson("赵六");
        record.setIdentifyDate(LocalDateTime.now());
        record.setRemark("色泽较好，质地坚实");
        record.setCreatedTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());
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
        // 移除不存在的方法调用
        
        // 修复指纹数据类型
        Map<String, Object> fingerprintData = new HashMap<>();
        fingerprintData.put("data1", 0.1);
        fingerprintData.put("data2", 0.2);
        analysis.setFingerprintData(fingerprintData);

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
        
        // 修复类型兼容性问题
        analysis.setSimilarityResult(0.95);
        
        analysis.setAnalysisPerson("钱七");
        analysis.setAnalysisDate(LocalDateTime.now());
        analysis.setRemark("图谱基线平稳，分离度良好");
        analysis.setCreatedTime(LocalDateTime.now());
        analysis.setUpdatedTime(LocalDateTime.now());
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
        assessment.setRemark("推荐使用");
        assessment.setCreatedTime(LocalDateTime.now());
        assessment.setUpdatedTime(LocalDateTime.now());
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
        task2.setStatus("进行中");
        tasks.add(task2);

        InspectionTask task3 = createInspectionTask();
        task3.setId(3L);
        task3.setTaskCode("IT20230003");
        task3.setStatus("已完成");
        task3.setActualCompleteDate(LocalDateTime.now());
        tasks.add(task3);

        return tasks;
    }
}