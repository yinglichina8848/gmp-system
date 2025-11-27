package com.gmp.lims.entity.mongo;

import com.gmp.lims.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FingerprintAnalysis实体类单元测试
 */
class FingerprintAnalysisTest {

    private FingerprintAnalysis analysis;

    @BeforeEach
    void setUp() {
        // 使用测试工具类创建测试对象
        analysis = TestUtils.createFingerprintAnalysis();
    }

    @Test
    void testAnalysisCreation() {
        // 验证对象创建成功
        assertNotNull(analysis);
        assertEquals("FPA20230001", analysis.getId());
        assertEquals("IT20230001", analysis.getTaskId());
        assertEquals("S20230001", analysis.getSampleId());
        assertEquals("人参药材", analysis.getSampleName());
    }

    @Test
    void testBasicProperties() {
        // 测试基本属性设置和获取
        assertEquals("HPLC指纹图谱", analysis.getFingerprintType());
        assertEquals("相似度评价法", analysis.getAnalysisMethod());
        assertEquals(0.985, analysis.getSimilarityScore());
        assertEquals("符合标准指纹图谱", analysis.getAnalysisConclusion());
    }

    @Test
    void testFingerprintData() {
        // 测试图谱数据
        assertNotNull(analysis.getFingerprintData());
        assertTrue(analysis.getFingerprintData()
                .contains("{\"peaks\":[\"p1\",\"p2\",\"p3\"],\"retentionTimes\":[1.2,2.5,3.8]}"));
    }

    @Test
    void testCharacteristicPeaks() {
        // 测试特征峰列表
        assertNotNull(analysis.getCharacteristicPeaks());
        assertEquals(3, analysis.getCharacteristicPeaks().size());

        // 验证第一个特征峰
        FingerprintAnalysis.CharacteristicPeak peak1 = analysis.getCharacteristicPeaks().get(0);
        assertEquals(1, peak1.getPeakNumber());
        assertEquals(1.2, peak1.getRetentionTime());
        assertEquals("人参皂苷Rg1", peak1.getComponentName());
        assertEquals(25.5, peak1.getRelativeContent());
        assertTrue(peak1.isReferencePeak());
    }

    @Test
    void testDateTimeProperties() {
        // 测试日期时间属性
        assertNotNull(analysis.getAnalysisDate());
        assertNotNull(analysis.getCreatedTime());
        assertNotNull(analysis.getUpdatedTime());
    }

    @Test
    void testSetters() {
        // 测试setter方法
        analysis.setId("FPA20230002");
        analysis.setTaskId("IT20230002");
        analysis.setSampleId("S20230002");
        analysis.setFingerprintType("TLC指纹图谱");
        analysis.setSimilarityScore(0.992);
        analysis.setAnalysisConclusion("相似度极高");

        // 验证设置后的属性值
        assertEquals("FPA20230002", analysis.getId());
        assertEquals("IT20230002", analysis.getTaskId());
        assertEquals("S20230002", analysis.getSampleId());
        assertEquals("TLC指纹图谱", analysis.getFingerprintType());
        assertEquals(0.992, analysis.getSimilarityScore());
        assertEquals("相似度极高", analysis.getAnalysisConclusion());
    }

    @Test
    void testCharacteristicPeakCreation() {
        // 测试特征峰创建
        FingerprintAnalysis.CharacteristicPeak newPeak = new FingerprintAnalysis.CharacteristicPeak();
        newPeak.setPeakNumber(4);
        newPeak.setRetentionTime(4.5);
        newPeak.setComponentName("新增成分");
        newPeak.setRelativeContent(15.8);
        newPeak.setReferencePeak(false);

        assertEquals(4, newPeak.getPeakNumber());
        assertEquals(4.5, newPeak.getRetentionTime());
        assertEquals("新增成分", newPeak.getComponentName());
        assertEquals(15.8, newPeak.getRelativeContent());
        assertFalse(newPeak.isReferencePeak());
    }

    @Test
    void testAddCharacteristicPeak() {
        // 测试添加特征峰
        List<FingerprintAnalysis.CharacteristicPeak> peaks = analysis.getCharacteristicPeaks();
        int originalSize = peaks.size();

        FingerprintAnalysis.CharacteristicPeak newPeak = new FingerprintAnalysis.CharacteristicPeak();
        newPeak.setPeakNumber(4);
        newPeak.setRetentionTime(5.2);
        newPeak.setComponentName("人参皂苷Rd");
        newPeak.setRelativeContent(12.3);
        newPeak.setReferencePeak(false);

        peaks.add(newPeak);
        analysis.setCharacteristicPeaks(peaks);

        assertEquals(originalSize + 1, analysis.getCharacteristicPeaks().size());
        assertEquals("人参皂苷Rd", analysis.getCharacteristicPeaks().get(3).getComponentName());
    }

    @Test
    void testToString() {
        // 测试toString方法不应为null
        String toStringResult = analysis.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("FingerprintAnalysis"));
        assertTrue(toStringResult.contains("FPA20230001"));
    }

    @Test
    void testSimilarityScoreRange() {
        // 测试相似度分数范围
        analysis.setSimilarityScore(0.0);
        assertEquals(0.0, analysis.getSimilarityScore());

        analysis.setSimilarityScore(1.0);
        assertEquals(1.0, analysis.getSimilarityScore());

        analysis.setSimilarityScore(0.955);
        assertEquals(0.955, analysis.getSimilarityScore());
    }

    @Test
    void testNullValues() {
        // 测试空值设置
        analysis.setFingerprintData(null);
        analysis.setCharacteristicPeaks(null);
        analysis.setAnalysisConclusion(null);

        assertNull(analysis.getFingerprintData());
        assertNull(analysis.getCharacteristicPeaks());
        assertNull(analysis.getAnalysisConclusion());
    }

    @Test
    void testReferenceStandard() {
        // 测试对照品信息
        assertEquals("人参对照药材", analysis.getReferenceStandard());
        analysis.setReferenceStandard("人参皂苷Rg1对照品");
        assertEquals("人参皂苷Rg1对照品", analysis.getReferenceStandard());
    }

    @Test
    void testAnalysisInstrument() {
        // 测试分析仪器
        assertEquals("高效液相色谱仪", analysis.getAnalysisInstrument());
        analysis.setAnalysisInstrument("超高效液相色谱仪");
        assertEquals("超高效液相色谱仪", analysis.getAnalysisInstrument());
    }
}
