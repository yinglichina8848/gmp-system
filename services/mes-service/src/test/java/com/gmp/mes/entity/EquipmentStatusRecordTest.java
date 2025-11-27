package com.gmp.mes.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 设备状态记录实体类的单元测试
 * 
 * @author gmp-system
 */
class EquipmentStatusRecordTest {

    private EquipmentStatusRecord equipmentStatusRecord;

    @BeforeEach
    void setUp() {
        equipmentStatusRecord = new EquipmentStatusRecord();
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        equipmentStatusRecord.setId(id);
        assertEquals(id, equipmentStatusRecord.getId());
    }

    @Test
    void testEquipmentCodeGetterAndSetter() {
        String equipmentCode = "EQUIP-001";
        equipmentStatusRecord.setEquipmentCode(equipmentCode);
        assertEquals(equipmentCode, equipmentStatusRecord.getEquipmentCode());
    }

    @Test
    void testEquipmentNameGetterAndSetter() {
        String equipmentName = "反应釜A";
        equipmentStatusRecord.setEquipmentName(equipmentName);
        assertEquals(equipmentName, equipmentStatusRecord.getEquipmentName());
    }

    @Test
    void testStatusGetterAndSetter() {
        EquipmentStatusRecord.EquipmentStatus status = EquipmentStatusRecord.EquipmentStatus.ONLINE;
        equipmentStatusRecord.setStatus(status);
        assertEquals(status, equipmentStatusRecord.getStatus());
    }

    @Test
    void testTemperatureGetterAndSetter() {
        Double temperature = 65.5;
        equipmentStatusRecord.setTemperature(temperature);
        assertEquals(temperature, equipmentStatusRecord.getTemperature());
    }

    @Test
    void testPressureGetterAndSetter() {
        Double pressure = 8.5;
        equipmentStatusRecord.setPressure(pressure);
        assertEquals(pressure, equipmentStatusRecord.getPressure());
    }

    @Test
    void testHumidityGetterAndSetter() {
        Double humidity = 45.0;
        equipmentStatusRecord.setHumidity(humidity);
        assertEquals(humidity, equipmentStatusRecord.getHumidity());
    }

    @Test
    void testVibrationGetterAndSetter() {
        Double vibration = 2.5;
        equipmentStatusRecord.setVibration(vibration);
        assertEquals(vibration, equipmentStatusRecord.getVibration());
    }

    @Test
    void testOperatorGetterAndSetter() {
        String operator = "张三";
        equipmentStatusRecord.setOperator(operator);
        assertEquals(operator, equipmentStatusRecord.getOperator());
    }

    @Test
    void testRemarkGetterAndSetter() {
        String remark = "正常停机维护";
        equipmentStatusRecord.setRemark(remark);
        assertEquals(remark, equipmentStatusRecord.getRemark());
    }

    @Test
    void testCreateTimeGetterAndSetter() {
        LocalDateTime createTime = LocalDateTime.now();
        equipmentStatusRecord.setCreateTime(createTime);
        assertEquals(createTime, equipmentStatusRecord.getCreateTime());
    }

    @Test
    void testEqualsAndHashCode() {
        equipmentStatusRecord.setId(1L);
        equipmentStatusRecord.setEquipmentCode("EQUIP-001");
        
        EquipmentStatusRecord sameIdRecord = new EquipmentStatusRecord();
        sameIdRecord.setId(1L);
        
        EquipmentStatusRecord differentIdRecord = new EquipmentStatusRecord();
        differentIdRecord.setId(2L);
        
        assertEquals(equipmentStatusRecord, sameIdRecord);
        assertEquals(equipmentStatusRecord.hashCode(), sameIdRecord.hashCode());
        assertNotEquals(equipmentStatusRecord, differentIdRecord);
        assertNotEquals(equipmentStatusRecord.hashCode(), differentIdRecord.hashCode());
    }

    @Test
    void testToString() {
        equipmentStatusRecord.setId(1L);
        equipmentStatusRecord.setEquipmentCode("EQUIP-001");
        equipmentStatusRecord.setEquipmentName("反应釜A");
        
        String toString = equipmentStatusRecord.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("equipmentCode='EQUIP-001'"));
        assertTrue(toString.contains("equipmentName='反应釜A'"));
    }

    @Test
    void testStatusEnumValues() {
        // 验证枚举值是否正确
        assertEquals("ONLINE", EquipmentStatusRecord.EquipmentStatus.ONLINE.name());
        assertEquals("OFFLINE", EquipmentStatusRecord.EquipmentStatus.OFFLINE.name());
        assertEquals("MAINTENANCE", EquipmentStatusRecord.EquipmentStatus.MAINTENANCE.name());
        assertEquals("ERROR", EquipmentStatusRecord.EquipmentStatus.ERROR.name());
    }

    @Test
    void testCompleteRecordCreation() {
        // 测试完整记录创建
        equipmentStatusRecord.setEquipmentCode("EQUIP-001");
        equipmentStatusRecord.setEquipmentName("反应釜A");
        equipmentStatusRecord.setStatus(EquipmentStatusRecord.EquipmentStatus.ONLINE);
        equipmentStatusRecord.setTemperature(65.5);
        equipmentStatusRecord.setPressure(8.5);
        equipmentStatusRecord.setHumidity(45.0);
        equipmentStatusRecord.setVibration(2.5);
        equipmentStatusRecord.setOperator("张三");
        equipmentStatusRecord.setRemark("正常运行");
        
        assertEquals("EQUIP-001", equipmentStatusRecord.getEquipmentCode());
        assertEquals(EquipmentStatusRecord.EquipmentStatus.ONLINE, equipmentStatusRecord.getStatus());
        assertEquals(65.5, equipmentStatusRecord.getTemperature());
        assertEquals("张三", equipmentStatusRecord.getOperator());
    }
}