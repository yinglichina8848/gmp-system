package com.gmp.lims.entity;

import com.gmp.lims.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * InspectionResult实体类单元测试
 */
class InspectionResultTest {

    private InspectionResult result;

    @BeforeEach
    void setUp() {
        // 使用测试工具类创建测试对象
        result = TestUtils.createInspectionResult();
    }

    @Test
    void testResultCreation() {
        // 验证对象创建成功
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("人参皂苷Rg1含量", result.getItemName());
        assertEquals("HPLC法", result.getTestMethod());
        assertEquals("1.25", result.getResultValue());
        assertEquals("≥0.3", result.getStandardValue());
    }

    @Test
    void testBasicProperties() {
        // 测试基本属性设置和获取
        assertEquals("合格", result.getConclusion());
        assertEquals("含量符合规定", result.getRemarks());
        assertEquals(1L, result.getTaskId());
        assertEquals("IT20230001", result.getTaskCode());
        assertEquals(1L, result.getSampleId());
        assertEquals("S20230001", result.getSampleCode());
    }

    @Test
    void testAuditProperties() {
        // 测试审核相关属性
        assertEquals("待审核", result.getAuditStatus());
        assertNull(result.getAuditor());
        assertNull(result.getAuditDate());
    }

    @Test
    void testChineseMedicineProperties() {
        // 测试中药特有属性
        assertEquals("含量测定符合中国药典标准", result.getResultDescription());
        assertTrue(result.isKeyIndicator());
    }

    @Test
    void testSetters() {
        // 测试setter方法
        result.setId(2L);
        result.setItemName("人参皂苷Rb1含量");
        result.setResultValue("2.35");
        result.setStandardValue("≥0.2");
        result.setConclusion("合格");
        result.setAuditStatus("已审核");
        result.setAuditor("审核员");
        result.setKeyIndicator(false);

        // 验证设置后的属性值
        assertEquals(2L, result.getId());
        assertEquals("人参皂苷Rb1含量", result.getItemName());
        assertEquals("2.35", result.getResultValue());
        assertEquals("≥0.2", result.getStandardValue());
        assertEquals("合格", result.getConclusion());
        assertEquals("已审核", result.getAuditStatus());
        assertEquals("审核员", result.getAuditor());
        assertFalse(result.isKeyIndicator());
    }

    @Test
    void testEqualsAndHashCode() {
        // 测试equals和hashCode方法
        InspectionResult sameResult = TestUtils.createInspectionResult(); // 创建相同ID的结果
        InspectionResult differentResult = TestUtils.createInspectionResult();
        differentResult.setId(2L); // 设置不同的ID

        // 相同ID的对象应该相等
        assertEquals(result, sameResult);
        assertEquals(result.hashCode(), sameResult.hashCode());

        // 不同ID的对象不应该相等
        assertNotEquals(result, differentResult);
        assertNotEquals(result.hashCode(), differentResult.hashCode());

        // 与null比较
        assertNotEquals(result, null);

        // 与不同类型对象比较
        assertNotEquals(result, "not a result");
    }

    @Test
    void testToString() {
        // 测试toString方法不应为null
        String toStringResult = result.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("InspectionResult"));
        assertTrue(toStringResult.contains("人参皂苷Rg1含量"));
    }

    @Test
    void testTaskRelationship() {
        // 测试与检测任务的关联
        InspectionTask task = TestUtils.createInspectionTask();
        result.setInspectionTask(task);
        assertNotNull(result.getInspectionTask());
        assertEquals("IT20230001", result.getInspectionTask().getTaskCode());
    }

    @Test
    void testAuditStatusChanges() {
        // 测试审核状态的变化
        assertEquals("待审核", result.getAuditStatus());

        result.setAuditStatus("审核中");
        assertEquals("审核中", result.getAuditStatus());

        result.setAuditStatus("已审核");
        assertEquals("已审核", result.getAuditStatus());

        result.setAuditStatus("审核驳回");
        assertEquals("审核驳回", result.getAuditStatus());
    }

    @Test
    void testConclusionVariations() {
        // 测试不同结论值
        result.setConclusion("合格");
        assertEquals("合格", result.getConclusion());

        result.setConclusion("不合格");
        assertEquals("不合格", result.getConclusion());

        result.setConclusion("需复测");
        assertEquals("需复测", result.getConclusion());
    }

    @Test
    void testNullValues() {
        // 测试空值设置
        result.setRemarks(null);
        result.setAuditor(null);
        result.setResultDescription(null);

        assertNull(result.getRemarks());
        assertNull(result.getAuditor());
        assertNull(result.getResultDescription());
    }

    @Test
    void testKeyIndicatorToggle() {
        // 测试关键指标标志的切换
        assertTrue(result.isKeyIndicator());
        result.setKeyIndicator(false);
        assertFalse(result.isKeyIndicator());
        result.setKeyIndicator(true);
        assertTrue(result.isKeyIndicator());
    }

    @Test
    void testResultDescription() {
        // 测试结果描述更新
        String newDescription = "更新后的详细结果描述，包含更多检测信息";
        result.setResultDescription(newDescription);
        assertEquals(newDescription, result.getResultDescription());
    }
}
