package com.gmp.lims.entity.mongo;

import com.gmp.lims.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuthenticityAssessment实体类单元测试
 */
class AuthenticityAssessmentTest {

    private AuthenticityAssessment assessment;

    @BeforeEach
    void setUp() {
        // 使用测试工具类创建测试对象
        assessment = TestUtils.createAuthenticityAssessment();
    }

    @Test
    void testAssessmentCreation() {
        // 验证对象创建成功
        assertNotNull(assessment);
        assertEquals("AUA20230001", assessment.getId());
        assertEquals("IT20230001", assessment.getTaskId());
        assertEquals("S20230001", assessment.getSampleId());
        assertEquals("人参药材", assessment.getSampleName());
    }

    @Test
    void testBasicProperties() {
        // 测试基本属性设置和获取
        assertEquals("吉林长白山", assessment.getProducingArea());
        assertEquals(95.5, assessment.getOverallScore());
        assertEquals("优", assessment.getAssessmentGrade());
        assertEquals("符合道地药材标准，质量优良", assessment.getAssessmentConclusion());
    }

    @Test
    void testAssessmentIndicators() {
        // 测试评估指标列表
        assertNotNull(assessment.getAssessmentIndicators());
        assertEquals(5, assessment.getAssessmentIndicators().size());

        // 验证第一个评估指标
        AuthenticityAssessment.AssessmentIndicator indicator1 = assessment.getAssessmentIndicators().get(0);
        assertEquals("产地环境", indicator1.getIndicatorName());
        assertEquals(20.0, indicator1.getWeight());
        assertEquals(19.5, indicator1.getScore());
        assertEquals("产地环境优越，符合人参生长条件", indicator1.getDescription());
    }

    @Test
    void testDateTimeProperties() {
        // 测试日期时间属性
        assertNotNull(assessment.getAssessmentDate());
        assertNotNull(assessment.getHarvestDate());
        assertNotNull(assessment.getCreatedTime());
        assertNotNull(assessment.getUpdatedTime());
    }

    @Test
    void testSetters() {
        // 测试setter方法
        assessment.setId("AUA20230002");
        assessment.setTaskId("IT20230002");
        assessment.setSampleId("S20230002");
        assessment.setProducingArea("辽宁桓仁");
        assessment.setOverallScore(92.0);
        assessment.setAssessmentGrade("良");
        assessment.setAssessmentConclusion("质量良好，基本符合道地药材标准");

        // 验证设置后的属性值
        assertEquals("AUA20230002", assessment.getId());
        assertEquals("IT20230002", assessment.getTaskId());
        assertEquals("S20230002", assessment.getSampleId());
        assertEquals("辽宁桓仁", assessment.getProducingArea());
        assertEquals(92.0, assessment.getOverallScore());
        assertEquals("良", assessment.getAssessmentGrade());
        assertEquals("质量良好，基本符合道地药材标准", assessment.getAssessmentConclusion());
    }

    @Test
    void testAssessmentIndicatorCreation() {
        // 测试评估指标创建
        AuthenticityAssessment.AssessmentIndicator newIndicator = new AuthenticityAssessment.AssessmentIndicator();
        newIndicator.setIndicatorName("新增指标");
        newIndicator.setWeight(10.0);
        newIndicator.setScore(9.5);
        newIndicator.setDescription("新增评估指标描述");

        assertEquals("新增指标", newIndicator.getIndicatorName());
        assertEquals(10.0, newIndicator.getWeight());
        assertEquals(9.5, newIndicator.getScore());
        assertEquals("新增评估指标描述", newIndicator.getDescription());
    }

    @Test
    void testAddAssessmentIndicator() {
        // 测试添加评估指标
        List<AuthenticityAssessment.AssessmentIndicator> indicators = assessment.getAssessmentIndicators();
        int originalSize = indicators.size();

        AuthenticityAssessment.AssessmentIndicator newIndicator = new AuthenticityAssessment.AssessmentIndicator();
        newIndicator.setIndicatorName("加工工艺");
        newIndicator.setWeight(15.0);
        newIndicator.setScore(14.2);
        newIndicator.setDescription("加工工艺规范，符合传统方法");

        indicators.add(newIndicator);
        assessment.setAssessmentIndicators(indicators);

        assertEquals(originalSize + 1, assessment.getAssessmentIndicators().size());
        assertEquals("加工工艺", assessment.getAssessmentIndicators().get(5).getIndicatorName());
    }

    @Test
    void testToString() {
        // 测试toString方法不应为null
        String toStringResult = assessment.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("AuthenticityAssessment"));
        assertTrue(toStringResult.contains("AUA20230001"));
    }

    @Test
    void testScoreRange() {
        // 测试分数范围
        assessment.setOverallScore(0.0);
        assertEquals(0.0, assessment.getOverallScore());

        assessment.setOverallScore(100.0);
        assertEquals(100.0, assessment.getOverallScore());

        assessment.setOverallScore(88.8);
        assertEquals(88.8, assessment.getOverallScore());
    }

    @Test
    void testNullValues() {
        // 测试空值设置
        assessment.setAssessmentIndicators(null);
        assessment.setAssessmentConclusion(null);
        assessment.setClimateConditions(null);

        assertNull(assessment.getAssessmentIndicators());
        assertNull(assessment.getAssessmentConclusion());
        assertNull(assessment.getClimateConditions());
    }

    @Test
    void testClimateConditions() {
        // 测试气候条件
        assertEquals("温带湿润气候，年均温度2-6℃", assessment.getClimateConditions());
        assessment.setClimateConditions("温带半湿润气候，年均温度5-8℃");
        assertEquals("温带半湿润气候，年均温度5-8℃", assessment.getClimateConditions());
    }

    @Test
    void testSoilConditions() {
        // 测试土壤条件
        assertEquals("腐殖质丰富的森林棕壤，pH值5.5-6.5", assessment.getSoilConditions());
        assessment.setSoilConditions("肥沃的砂壤土，pH值6.0-7.0");
        assertEquals("肥沃的砂壤土，pH值6.0-7.0", assessment.getSoilConditions());
    }

    @Test
    void testAssessmentGradeVariations() {
        // 测试不同等级
        assessment.setAssessmentGrade("优");
        assertEquals("优", assessment.getAssessmentGrade());

        assessment.setAssessmentGrade("良");
        assertEquals("良", assessment.getAssessmentGrade());

        assessment.setAssessmentGrade("合格");
        assertEquals("合格", assessment.getAssessmentGrade());

        assessment.setAssessmentGrade("不合格");
        assertEquals("不合格", assessment.getAssessmentGrade());
    }
}
