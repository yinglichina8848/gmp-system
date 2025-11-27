package com.gmp.mes.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 设备监控实体类的单元测试
 * 
 * @author gmp-system
 */
class EquipmentMonitorTest {

    private EquipmentMonitor equipmentMonitor;

    @BeforeEach
    void setUp() {
        equipmentMonitor = new EquipmentMonitor();
    }

    @Test
    void testIdGetterAndSetter() {
        Long id = 1L;
        equipmentMonitor.setId(id);
        assertEquals(id, equipmentMonitor.getId());
    }

    @Test
    void testEquipmentCodeGetterAndSetter() {
        String equipmentCode = "EQUIP-001";
        equipmentMonitor.setEquipmentCode(equipmentCode);
        assertEquals(equipmentCode, equipmentMonitor.getEquipmentCode());
    }

    @Test
    void testEquipmentNameGetterAndSetter() {
        String equipmentName = "反应釜A";
        equipmentMonitor.setEquipmentName(equipmentName);
        assertEquals(equipmentName, equipmentMonitor.getEquipmentName());
    }

    @Test
    void testEquipmentTypeGetterAndSetter() {
        String equipmentType = "反应设备";
        equipmentMonitor.setEquipmentType(equipmentType);
        assertEquals(equipmentType, equipmentMonitor.getEquipmentType());
    }

    @Test
    void testLocationGetterAndSetter() {
        String location = "生产车间1";
        equipmentMonitor.setLocation(location);
        assertEquals(location, equipmentMonitor.getLocation());
    }

    @Test
    void testStatusGetterAndSetter() {
        EquipmentMonitor.EquipmentStatus status = EquipmentMonitor.EquipmentStatus.ONLINE;
        equipmentMonitor.setStatus(status);
        assertEquals(status, equipmentMonitor.getStatus());
    }

    @Test
    void testTemperatureGetterAndSetter() {
        Double temperature = 65.5;
        equipmentMonitor.setTemperature(temperature);
        assertEquals(temperature, equipmentMonitor.getTemperature());
    }

    @Test
    void testPressureGetterAndSetter() {
        Double pressure = 8.5;
        equipmentMonitor.setPressure(pressure);
        assertEquals(pressure, equipmentMonitor.getPressure());
    }

    @Test
    void testHumidityGetterAndSetter() {
        Double humidity = 45.0;
        equipmentMonitor.setHumidity(humidity);
        assertEquals(humidity, equipmentMonitor.getHumidity());
    }

    @Test
    void testVibrationGetterAndSetter() {
        Double vibration = 2.5;
        equipmentMonitor.setVibration(vibration);
        assertEquals(vibration, equipmentMonitor.getVibration());
    }

    @Test
    void testLastUpdatedTimeGetterAndSetter() {
        LocalDateTime lastUpdatedTime = LocalDateTime.now();
        equipmentMonitor.setLastUpdatedTime(lastUpdatedTime);
        assertEquals(lastUpdatedTime, equipmentMonitor.getLastUpdatedTime());
    }

    @Test
    void testAlertThresholdTemperatureGetterAndSetter() {
        Double alertThresholdTemperature = 80.0;
        equipmentMonitor.setAlertThresholdTemperature(alertThresholdTemperature);
        assertEquals(alertThresholdTemperature, equipmentMonitor.getAlertThresholdTemperature());
    }

    @Test
    void testAlertThresholdPressureGetterAndSetter() {
        Double alertThresholdPressure = 10.0;
        equipmentMonitor.setAlertThresholdPressure(alertThresholdPressure);
        assertEquals(alertThresholdPressure, equipmentMonitor.getAlertThresholdPressure());
    }

    @Test
    void testAlertThresholdHumidityGetterAndSetter() {
        Double alertThresholdHumidity = 70.0;
        equipmentMonitor.setAlertThresholdHumidity(alertThresholdHumidity);
        assertEquals(alertThresholdHumidity, equipmentMonitor.getAlertThresholdHumidity());
    }

    @Test
    void testAlertThresholdVibrationGetterAndSetter() {
        Double alertThresholdVibration = 5.0;
        equipmentMonitor.setAlertThresholdVibration(alertThresholdVibration);
        assertEquals(alertThresholdVibration, equipmentMonitor.getAlertThresholdVibration());
    }

    @Test
    void testMaintenanceDateGetterAndSetter() {
        LocalDateTime maintenanceDate = LocalDateTime.now().plusDays(30);
        equipmentMonitor.setMaintenanceDate(maintenanceDate);
        assertEquals(maintenanceDate, equipmentMonitor.getMaintenanceDate());
    }

    @Test
    void testMaintenancePersonGetterAndSetter() {
        String maintenancePerson = "李四";
        equipmentMonitor.setMaintenancePerson(maintenancePerson);
        assertEquals(maintenancePerson, equipmentMonitor.getMaintenancePerson());
    }

    @Test
    void testRemarkGetterAndSetter() {
        String remark = "正常运行";
        equipmentMonitor.setRemark(remark);
        assertEquals(remark, equipmentMonitor.getRemark());
    }

    @Test
    void testIsTemperatureOutOfRange() {
        // 设置温度阈值为80度
        equipmentMonitor.setAlertThresholdTemperature(80.0);
        
        // 温度在正常范围内
        equipmentMonitor.setTemperature(75.0);
        assertFalse(equipmentMonitor.isTemperatureOutOfRange());
        
        // 温度超出阈值
        equipmentMonitor.setTemperature(85.0);
        assertTrue(equipmentMonitor.isTemperatureOutOfRange());
    }

    @Test
    void testIsPressureOutOfRange() {
        // 设置压力阈值为10.0
        equipmentMonitor.setAlertThresholdPressure(10.0);
        
        // 压力在正常范围内
        equipmentMonitor.setPressure(9.5);
        assertFalse(equipmentMonitor.isPressureOutOfRange());
        
        // 压力超出阈值
        equipmentMonitor.setPressure(10.5);
        assertTrue(equipmentMonitor.isPressureOutOfRange());
    }

    @Test
    void testIsHumidityOutOfRange() {
        // 设置湿度阈值为70.0
        equipmentMonitor.setAlertThresholdHumidity(70.0);
        
        // 湿度在正常范围内
        equipmentMonitor.setHumidity(65.0);
        assertFalse(equipmentMonitor.isHumidityOutOfRange());
        
        // 湿度超出阈值
        equipmentMonitor.setHumidity(75.0);
        assertTrue(equipmentMonitor.isHumidityOutOfRange());
    }

    @Test
    void testIsVibrationOutOfRange() {
        // 设置振动阈值为5.0
        equipmentMonitor.setAlertThresholdVibration(5.0);
        
        // 振动在正常范围内
        equipmentMonitor.setVibration(4.5);
        assertFalse(equipmentMonitor.isVibrationOutOfRange());
        
        // 振动超出阈值
        equipmentMonitor.setVibration(5.5);
        assertTrue(equipmentMonitor.isVibrationOutOfRange());
    }

    @Test
    void testIsAnyParameterOutOfRange() {
        equipmentMonitor.setAlertThresholdTemperature(80.0);
        equipmentMonitor.setAlertThresholdPressure(10.0);
        equipmentMonitor.setAlertThresholdHumidity(70.0);
        equipmentMonitor.setAlertThresholdVibration(5.0);
        
        // 所有参数都在正常范围内
        equipmentMonitor.setTemperature(75.0);
        equipmentMonitor.setPressure(9.5);
        equipmentMonitor.setHumidity(65.0);
        equipmentMonitor.setVibration(4.5);
        assertFalse(equipmentMonitor.isAnyParameterOutOfRange());
        
        // 温度超出范围
        equipmentMonitor.setTemperature(85.0);
        assertTrue(equipmentMonitor.isAnyParameterOutOfRange());
    }

    @Test
    void testEqualsAndHashCode() {
        equipmentMonitor.setId(1L);
        equipmentMonitor.setEquipmentCode("EQUIP-001");
        
        EquipmentMonitor sameIdEquipment = new EquipmentMonitor();
        sameIdEquipment.setId(1L);
        
        EquipmentMonitor differentIdEquipment = new EquipmentMonitor();
        differentIdEquipment.setId(2L);
        
        assertEquals(equipmentMonitor, sameIdEquipment);
        assertEquals(equipmentMonitor.hashCode(), sameIdEquipment.hashCode());
        assertNotEquals(equipmentMonitor, differentIdEquipment);
        assertNotEquals(equipmentMonitor.hashCode(), differentIdEquipment.hashCode());
    }

    @Test
    void testToString() {
        equipmentMonitor.setId(1L);
        equipmentMonitor.setEquipmentCode("EQUIP-001");
        equipmentMonitor.setEquipmentName("反应釜A");
        
        String toString = equipmentMonitor.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("equipmentCode='EQUIP-001'"));
        assertTrue(toString.contains("equipmentName='反应釜A'"));
    }
}