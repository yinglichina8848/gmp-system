package com.gmp.lims.entity;

import com.gmp.lims.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample实体类单元测试
 */
class SampleTest {

    private Sample sample;

    @BeforeEach
    void setUp() {
        // 使用测试工具类创建测试对象
        sample = TestUtils.createSample();
    }

    @Test
    void testSampleCreation() {
        // 验证对象创建成功
        assertNotNull(sample);
        assertEquals(1L, sample.getId());
        assertEquals("S20230001", sample.getSampleCode());
        assertEquals("人参样品1", sample.getSampleName());
        assertEquals("中药材", sample.getSampleType());
    }

    @Test
    void testBasicProperties() {
        // 测试基本属性设置和获取
        assertEquals("B20230001", sample.getBatchNumber());
        assertEquals(500.0, sample.getQuantity());
        assertEquals("g", sample.getUnit());
        assertEquals("供应商A", sample.getSampleSource());
        assertEquals("冷藏库A区", sample.getStorageLocation());
        assertEquals("已接收", sample.getStatus());
        assertEquals("道地药材，需要特殊保存", sample.getRemarks());
    }

    @Test
    void testDateProperties() {
        // 测试日期属性
        assertNotNull(sample.getReceiptDate());
        assertNotNull(sample.getProductionDate());
        assertNotNull(sample.getExpirationDate());
        assertNotNull(sample.getCreatedTime());
        assertNotNull(sample.getUpdatedTime());

        // 验证日期的先后顺序
        assertTrue(sample.getProductionDate().isBefore(sample.getReceiptDate()));
        assertTrue(sample.getReceiptDate().isBefore(sample.getExpirationDate()));
    }

    @Test
    void testChineseMedicineProperties() {
        // 测试中药特有属性
        assertEquals("人参", sample.getChineseMedicineName());
        assertEquals("吉林长白山", sample.getAuthenticProductionArea());
        assertEquals("秋季", sample.getHarvestSeason());
        assertEquals("根及根茎", sample.getMedicinalPart());
    }

    @Test
    void testSetters() {
        // 测试setter方法
        sample.setId(2L);
        sample.setSampleCode("S20230002");
        sample.setSampleName("更新的样品名称");
        sample.setSampleType("饮片");
        sample.setQuantity(1000.0);
        sample.setUnit("kg");
        sample.setStatus("检测中");
        sample.setChineseMedicineName("更新的药材名称");
        sample.setAuthenticProductionArea("更新的产区");

        // 验证设置后的属性值
        assertEquals(2L, sample.getId());
        assertEquals("S20230002", sample.getSampleCode());
        assertEquals("更新的样品名称", sample.getSampleName());
        assertEquals("饮片", sample.getSampleType());
        assertEquals(1000.0, sample.getQuantity());
        assertEquals("kg", sample.getUnit());
        assertEquals("检测中", sample.getStatus());
        assertEquals("更新的药材名称", sample.getChineseMedicineName());
        assertEquals("更新的产区", sample.getAuthenticProductionArea());
    }

    @Test
    void testEqualsAndHashCode() {
        // 测试equals和hashCode方法
        Sample sameSample = TestUtils.createSample(); // 创建相同ID的样品
        Sample differentSample = TestUtils.createSample();
        differentSample.setId(2L); // 设置不同的ID

        // 相同ID的对象应该相等
        assertEquals(sample, sameSample);
        assertEquals(sample.hashCode(), sameSample.hashCode());

        // 不同ID的对象不应该相等
        assertNotEquals(sample, differentSample);
        assertNotEquals(sample.hashCode(), differentSample.hashCode());

        // 与null比较
        assertNotEquals(sample, null);

        // 与不同类型对象比较
        assertNotEquals(sample, "not a sample");
    }

    @Test
    void testToString() {
        // 测试toString方法不应为null
        String toStringResult = sample.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("Sample"));
        assertTrue(toStringResult.contains("S20230001"));
    }

    @Test
    void testUpdateTime() {
        // 测试更新时间
        LocalDateTime oldUpdatedTime = sample.getUpdatedTime();
        sample.setUpdatedTime(LocalDateTime.now());
        assertNotEquals(oldUpdatedTime, sample.getUpdatedTime());
    }

    @Test
    void testNullValues() {
        // 测试空值设置
        sample.setRemarks(null);
        sample.setHarvestSeason(null);
        sample.setMedicinalPart(null);

        assertNull(sample.getRemarks());
        assertNull(sample.getHarvestSeason());
        assertNull(sample.getMedicinalPart());
    }

    @Test
    void testBatchNumber() {
        // 测试批次号属性
        sample.setBatchNumber("NEW-BATCH-001");
        assertEquals("NEW-BATCH-001", sample.getBatchNumber());
    }

    @Test
    void testSampleSource() {
        // 测试样品来源属性
        sample.setSampleSource("新供应商B");
        assertEquals("新供应商B", sample.getSampleSource());
    }
}
