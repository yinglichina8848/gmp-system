package com.gmp.lims.entity.mongo;

import com.gmp.lims.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AuthenticityAssessment实体类单元测试
 */
class AuthenticityAssessmentTest {

    private AuthenticityAssessment assessment;

    @BeforeEach
    void setUp() {
        // 创建评估对象
        assessment = new AuthenticityAssessment();
        assessment.setId("AUA20230001");
        assessment.setTaskId(1L);
        assessment.setSampleId(1L);
        assessment.setMedicineName("人参药材");
        assessment.setProductionArea("吉林长白山");
        assessment.setComprehensiveScore(95.5);
        assessment.setAssessmentLevel("优");
        assessment.setAssessmentConclusion("符合道地药材标准，质量优良");
        assessment.setAssessmentDate(LocalDateTime.now());
        assessment.setCreatedTime(LocalDateTime.now());
        assessment.setUpdatedTime(LocalDateTime.now());

        // 创建评估指标列表
        List<AuthenticityAssessment.AssessmentIndicator> indicators = new ArrayList<>();

        // 添加第一个评估指标
        AuthenticityAssessment.AssessmentIndicator indicator1 = new AuthenticityAssessment.AssessmentIndicator();
        indicator1.setIndicatorName("产地环境");
        indicator1.setWeight(20.0);
        indicator1.setActualScore(19.5);
        indicator1.setFullScore(20.0);
        indicator1.setResultDescription("产地环境优越，符合人参生长条件");
        indicators.add(indicator1);

        // 添加第二个评估指标
        AuthenticityAssessment.AssessmentIndicator indicator2 = new AuthenticityAssessment.AssessmentIndicator();
        indicator2.setIndicatorName("外观特征");
        indicator2.setWeight(15.0);
        indicator2.setActualScore(14.8);
        indicator2.setFullScore(15.0);
        indicator2.setResultDescription("外观特征典型，符合人参正品特征");
        indicators.add(indicator2);

        // 添加第三个评估指标
        AuthenticityAssessment.AssessmentIndicator indicator3 = new AuthenticityAssessment.AssessmentIndicator();
        indicator3.setIndicatorName("显微特征");
        indicator3.setWeight(20.0);
        indicator3.setActualScore(19.2);
        indicator3.setFullScore(20.0);
        indicator3.setResultDescription("显微特征典型，符合人参正品标准");
        indicators.add(indicator3);

        // 添加第四个评估指标
        AuthenticityAssessment.AssessmentIndicator indicator4 = new AuthenticityAssessment.AssessmentIndicator();
        indicator4.setIndicatorName("理化特性");
        indicator4.setWeight(25.0);
        indicator4.setActualScore(24.5);
        indicator4.setFullScore(25.0);
        indicator4.setResultDescription("人参皂苷含量高，符合标准");
        indicators.add(indicator4);

        // 添加第五个评估指标
        AuthenticityAssessment.AssessmentIndicator indicator5 = new AuthenticityAssessment.AssessmentIndicator();
        indicator5.setIndicatorName("安全检测");
        indicator5.setWeight(20.0);
        indicator5.setActualScore(18.5);
        indicator5.setFullScore(20.0);
        indicator5.setResultDescription("农残、重金属含量符合标准");
        indicators.add(indicator5);

        assessment.setAssessmentIndicators(indicators);
    }

    @Test
    void testAssessmentCreation() {
        // 验证对象创建成功
        assertNotNull(assessment);
        assertEquals("AUA20230001", assessment.getId());
        assertNotNull(assessment.getTaskId());
        assertNotNull(assessment.getSampleId());
        assertNotNull(assessment.getMedicineName());
    }

    @Test
    void testBasicProperties() {
        // 测试基本属性设置和获取
        assertEquals("吉林长白山", assessment.getProductionArea());
        assertNotNull(assessment.getComprehensiveScore());
        assertNotNull(assessment.getAssessmentLevel());
        assertEquals("符合道地药材标准，质量优良", assessment.getAssessmentConclusion());
    }

    @Test
    void testAssessmentIndicators() {
        // 测试评估指标列表
        assertNotNull(assessment.getAssessmentIndicators());
        assertTrue(assessment.getAssessmentIndicators().size() > 0);

        // 验证第一个评估指标
        AuthenticityAssessment.AssessmentIndicator indicator1 = assessment.getAssessmentIndicators().get(0);
        assertNotNull(indicator1.getIndicatorName());
        assertNotNull(indicator1.getWeight());
        assertNotNull(indicator1.getActualScore());
        assertNotNull(indicator1.getResultDescription());
    }

    @Test
    void testDateTimeProperties() {
        // 测试日期时间属性
        assertNotNull(assessment.getAssessmentDate());
        assertNotNull(assessment.getCreatedTime());
        assertNotNull(assessment.getUpdatedTime());
    }

    @Test
    void testSetters() {
        // 测试setter方法
        assessment.setId("AUA20230002");
        assessment.setTaskId(2L);
        assessment.setSampleId(2L);
        assessment.setProductionArea("辽宁桓仁");
        assessment.setComprehensiveScore(92.0);
        assessment.setAssessmentLevel("良");
        assessment.setAssessmentConclusion("质量良好，基本符合道地药材标准");

        // 验证设置后的属性值
        assertEquals("AUA20230002", assessment.getId());
        assertEquals(Long.valueOf(2L), assessment.getTaskId());
        assertEquals(Long.valueOf(2L), assessment.getSampleId());
        assertEquals("辽宁桓仁", assessment.getProductionArea());
        assertEquals(92.0, assessment.getComprehensiveScore());
        assertEquals("良", assessment.getAssessmentLevel());
        assertEquals("质量良好，基本符合道地药材标准", assessment.getAssessmentConclusion());
    }

    @Test
    void testAssessmentIndicatorCreation() {
        // 测试评估指标创建
        AuthenticityAssessment.AssessmentIndicator newIndicator = new AuthenticityAssessment.AssessmentIndicator();
        newIndicator.setIndicatorName("新增指标");
        newIndicator.setWeight(10.0);
        newIndicator.setActualScore(9.5);
        newIndicator.setFullScore(10.0);
        newIndicator.setResultDescription("新增评估指标描述");

        assertEquals("新增指标", newIndicator.getIndicatorName());
        assertEquals(10.0, newIndicator.getWeight());
        assertEquals(9.5, newIndicator.getActualScore());
        assertEquals("新增评估指标描述", newIndicator.getResultDescription());
    }

    @Test
    void testAddAssessmentIndicator() {
        // 测试添加评估指标
        List<AuthenticityAssessment.AssessmentIndicator> indicators = assessment.getAssessmentIndicators();
        int originalSize = indicators.size();

        AuthenticityAssessment.AssessmentIndicator newIndicator = new AuthenticityAssessment.AssessmentIndicator();
        newIndicator.setIndicatorName("加工工艺");
        newIndicator.setWeight(15.0);
        newIndicator.setActualScore(14.2);
        newIndicator.setFullScore(15.0);
        newIndicator.setResultDescription("加工工艺规范，符合传统方法");

        indicators.add(newIndicator);
        assessment.setAssessmentIndicators(indicators);

        assertEquals(originalSize + 1, assessment.getAssessmentIndicators().size());
        assertEquals("加工工艺", assessment.getAssessmentIndicators().get(originalSize).getIndicatorName());
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
        assessment.setComprehensiveScore(0.0);
        assertEquals(0.0, assessment.getComprehensiveScore());

        assessment.setComprehensiveScore(100.0);
        assertEquals(100.0, assessment.getComprehensiveScore());

        assessment.setComprehensiveScore(88.8);
        assertEquals(88.8, assessment.getComprehensiveScore());
    }

    @Test
    void testNullValues() {
        // 测试空值设置
        assessment.setAssessmentIndicators(null);
        assessment.setAssessmentConclusion(null);

        assertNull(assessment.getAssessmentIndicators());
        assertNull(assessment.getAssessmentConclusion());
    }

    // 移除气候和土壤条件测试，因为AuthenticityAssessment类中不存在这些属性
    
    // @Test
    // void testClimateConditions() {
    //     // 测试气候条件
    //     assertEquals("温带大陆性气候，年均温5-10℃，年降水量400-800mm", assessment.getClimateConditions());
    //     assessment.setClimateConditions("温带季风气候，年均温8-12℃，年降水量600-900mm");
    //     assertEquals("温带季风气候，年均温8-12℃，年降水量600-900mm", assessment.getClimateConditions());
    // }
    //
    // @Test
    // void testSoilConditions() {
    //     // 测试土壤条件
    //     assertEquals("腐殖质丰富的森林棕壤，pH值5.5-6.5", assessment.getSoilConditions());
    //     assessment.setSoilConditions("肥沃的砂壤土，pH值6.0-7.0");
    //     assertEquals("肥沃的砂壤土，pH值6.0-7.0", assessment.getSoilConditions());
    // }

    @Test
    void testAssessmentGradeVariations() {
        // 测试不同等级
        assessment.setAssessmentLevel("优");
        assertEquals("优", assessment.getAssessmentLevel());

        assessment.setAssessmentLevel("良");
        assertEquals("良", assessment.getAssessmentLevel());

        assessment.setAssessmentLevel("合格");
        assertEquals("合格", assessment.getAssessmentLevel());

        assessment.setAssessmentLevel("不合格");
        assertEquals("不合格", assessment.getAssessmentLevel());
    }
}
