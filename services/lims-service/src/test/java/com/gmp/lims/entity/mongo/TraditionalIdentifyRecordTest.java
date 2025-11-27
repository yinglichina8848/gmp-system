package com.gmp.lims.entity.mongo;

import com.gmp.lims.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TraditionalIdentifyRecord实体类单元测试
 */
class TraditionalIdentifyRecordTest {

    private TraditionalIdentifyRecord record;

    @BeforeEach
    void setUp() {
        // 使用测试工具类创建测试对象
        record = TestUtils.createTraditionalIdentifyRecord();
    }

    @Test
    void testRecordCreation() {
        // 验证对象创建成功
        assertNotNull(record);
        assertEquals("TIR20230001", record.getId());
        assertEquals("IT20230001", record.getTaskId());
        assertEquals("S20230001", record.getSampleId());
        assertEquals("人参", record.getSampleName());
    }

    @Test
    void testIdentificationProperties() {
        // 测试鉴别相关属性
        assertEquals("人参药材具有芦头、芦碗，表面灰黄色，有纵皱纹", record.getMorphologyDescription());
        assertEquals("可见草酸钙簇晶、树脂道、木栓细胞等特征结构", record.getMicroscopicDescription());
        assertEquals("三氯甲烷-甲醇提取液呈紫红色", record.getChemicalDescription());
        assertEquals("符合中国药典规定的人参性状特征", record.getConclusion());
    }

    @Test
    void testImageProperties() {
        // 测试图片列表
        assertNotNull(record.getImageUrls());
        assertEquals(3, record.getImageUrls().size());
        assertTrue(record.getImageUrls().contains("images/morphology/20230001-1.jpg"));
        assertTrue(record.getImageUrls().contains("images/microscopic/20230001-1.jpg"));
        assertTrue(record.getImageUrls().contains("images/chemical/20230001-1.jpg"));
    }

    @Test
    void testDateTimeProperties() {
        // 测试日期时间属性
        assertNotNull(record.getIdentifyDate());
        assertNotNull(record.getCreatedTime());
        assertNotNull(record.getUpdatedTime());
    }

    @Test
    void testSetters() {
        // 测试setter方法
        record.setId("TIR20230002");
        record.setTaskId("IT20230002");
        record.setSampleId("S20230002");
        record.setMorphologyDescription("更新的性状描述");
        record.setMicroscopicDescription("更新的显微描述");
        record.setChemicalDescription("更新的理化描述");
        record.setConclusion("更新的鉴别结论");

        // 验证设置后的属性值
        assertEquals("TIR20230002", record.getId());
        assertEquals("IT20230002", record.getTaskId());
        assertEquals("S20230002", record.getSampleId());
        assertEquals("更新的性状描述", record.getMorphologyDescription());
        assertEquals("更新的显微描述", record.getMicroscopicDescription());
        assertEquals("更新的理化描述", record.getChemicalDescription());
        assertEquals("更新的鉴别结论", record.getConclusion());
    }

    @Test
    void testImageUrlListModification() {
        // 测试图片URL列表修改
        List<String> newImages = List.of(
                "images/new/morphology-1.jpg",
                "images/new/morphology-2.jpg");
        record.setImageUrls(newImages);

        assertNotNull(record.getImageUrls());
        assertEquals(2, record.getImageUrls().size());
        assertTrue(record.getImageUrls().contains("images/new/morphology-1.jpg"));
        assertTrue(record.getImageUrls().contains("images/new/morphology-2.jpg"));
    }

    @Test
    void testToString() {
        // 测试toString方法不应为null
        String toStringResult = record.toString();
        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("TraditionalIdentifyRecord"));
        assertTrue(toStringResult.contains("TIR20230001"));
    }

    @Test
    void testIdentifyMethod() {
        // 测试鉴别方法属性
        assertEquals("传统鉴别法", record.getIdentifyMethod());
        record.setIdentifyMethod("显微鉴别法");
        assertEquals("显微鉴别法", record.getIdentifyMethod());
    }

    @Test
    void testIdentifier() {
        // 测试鉴别人员属性
        assertEquals("王药师", record.getIdentifier());
        record.setIdentifier("张药师");
        assertEquals("张药师", record.getIdentifier());
    }

    @Test
    void testNullValues() {
        // 测试空值设置
        record.setMorphologyDescription(null);
        record.setMicroscopicDescription(null);
        record.setChemicalDescription(null);
        record.setImageUrls(null);

        assertNull(record.getMorphologyDescription());
        assertNull(record.getMicroscopicDescription());
        assertNull(record.getChemicalDescription());
        assertNull(record.getImageUrls());
    }

    @Test
    void testIdentifyType() {
        // 测试鉴别类型属性
        assertEquals("全项鉴别", record.getIdentifyType());
        record.setIdentifyType("部分鉴别");
        assertEquals("部分鉴别", record.getIdentifyType());
    }

    @Test
    void testDescriptionLength() {
        // 测试描述长度
        String longDescription = "A".repeat(1000); // 创建一个长描述
        record.setConclusion(longDescription);
        assertEquals(longDescription, record.getConclusion());
        assertEquals(1000, record.getConclusion().length());
    }

    @Test
    void testSampleName() {
        // 测试样品名称更新
        record.setSampleName("更新的样品名称");
        assertEquals("更新的样品名称", record.getSampleName());
    }
}
