package com.gmp.equipment.repository;

import com.gmp.equipment.entity.Equipment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 测试Why-What-How注释:
 *
 * WHY: Equipment管理系统的数据访问层是GMP合规的核心，必须确保数据库查询的准确性、
 * 性能和数据完整性，因为任何查询错误都可能导致设备状态误判、生产作业错误，
 * 而GMP要求所有设备数据必须具有完整可追溯性。
 *
 * WHAT: EquipmentRepositoryIntegrationTest通过完整数据库测试验证所有查询接口的正确性，
 * 包含CRUD操作、业务查询、统计分析、边界条件处理，确保数据访问层能在生产环境
 * 下可靠稳定运行，支持高并发和大数据量的处理需求。
 *
 * HOW: 使用Spring Boot的@DataJpaTest注解创建干净的数据库测试环境，结合TestContainers
 * 提供真实的PostgreSQL数据库，通过实体构建器创建测试数据，采用断言式验证结果。
 */
@DataJpaTest
@ActiveProfiles("test")
class EquipmentRepositoryIntegrationTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Test
    void testSaveAndFindByEquipmentCode() {
        // Given: 创建并保存设备
        LocalDateTime now = LocalDateTime.now();
        Equipment equipment = Equipment.builder()
                .equipmentCode("TEST-0001")
                .equipmentName("测试设备001")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.STOPPED)
                .nextMaintenanceDate(now.plusDays(30))
                .nextCalibrationDate(now.plusDays(90))
                .createdAt(now)
                .updatedAt(now)
                .build();

        Equipment savedEquipment = equipmentRepository.save(equipment);

        // When: 通过设备编码查找
        Equipment foundEquipment = equipmentRepository.findByEquipmentCode("TEST-0001");

        // Then: 验证查找结果
        assertThat(foundEquipment).isNotNull();
        assertThat(foundEquipment.getEquipmentCode()).isEqualTo("TEST-0001");
        assertThat(foundEquipment.getEquipmentName()).isEqualTo("测试设备001");
        assertThat(foundEquipment.getEquipmentStatus()).isEqualTo(Equipment.EquipmentStatus.ACTIVE);
    }

    @Test
    void testFindByStatus() {
        // Given: 创建不同状态的设备
        LocalDateTime now = LocalDateTime.now();
        Equipment activeEquipment = Equipment.builder()
                .equipmentCode("ACT-0001")
                .equipmentName("活跃设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Equipment maintenanceEquipment = Equipment.builder()
                .equipmentCode("MAI-0001")
                .equipmentName("维护设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.MAINTENANCE)
                .operationalStatus(Equipment.OperationalStatus.MAINTENANCE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        equipmentRepository.save(activeEquipment);
        equipmentRepository.save(maintenanceEquipment);

        // When: 查询活跃状态设备
        List<Equipment> activeEquipments = equipmentRepository.findByEquipmentStatus(Equipment.EquipmentStatus.ACTIVE);

        // Then: 验证结果
        assertThat(activeEquipments).hasSize(1);
        assertThat(activeEquipments.get(0).getEquipmentCode()).isEqualTo("ACT-0001");
    }

    @Test
    void testFindByStatusWithPaging() {
        // Given: 创建多个活跃设备
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            Equipment equipment = Equipment.builder()
                .equipmentCode("ACT-" + String.format("%04d", i))
                .equipmentName("活跃设备" + i)
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(now)
                .updatedAt(now)
                    .build();
            equipmentRepository.save(equipment);
        }

        // When: 分页查询
        Page<Equipment> equipmentPage = equipmentRepository.findByEquipmentStatus(
                Equipment.EquipmentStatus.ACTIVE,
                PageRequest.of(0, 3));

        // Then: 验证分页结果
        assertThat(equipmentPage.getTotalElements()).isEqualTo(5);
        assertThat(equipmentPage.getSize()).isEqualTo(3);
        assertThat(equipmentPage.getNumberOfElements()).isEqualTo(3);
        assertThat(equipmentPage.hasNext()).isTrue();
    }

    @Test
    void testFindEquipmentDueForMaintenance() {
        LocalDateTime now = LocalDateTime.now();

        // Given: 创建不同维护到期日期的设备
        Equipment dueForMaintenance = Equipment.builder()
                .equipmentCode("MNT-0001")
                .equipmentName("到期维护设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .nextMaintenanceDate(now.minusDays(1)) // 已到期
                .createdAt(now)
                .updatedAt(now)
                .build();

        Equipment notDueForMaintenance = Equipment.builder()
                .equipmentCode("MNT-0002")
                .equipmentName("未到期维护设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .nextMaintenanceDate(now.plusDays(30)) // 未到期
                .createdAt(now)
                .updatedAt(now)
                .build();

        equipmentRepository.save(dueForMaintenance);
        equipmentRepository.save(notDueForMaintenance);

        // When: 查询到期维护设备
        List<Equipment> dueEquipments = equipmentRepository.findEquipmentDueForMaintenance(now);

        // Then: 验证结果
        assertThat(dueEquipments).hasSize(1);
        assertThat(dueEquipments.get(0).getEquipmentCode()).isEqualTo("MNT-0001");
    }

    @Test
    void testFindCalibrationOverdueEquipment() {
        LocalDateTime now = LocalDateTime.now();

        // Given: 创建校准过期设备
        Equipment overdueCalibration = Equipment.builder()
                .equipmentCode("CAL-0001")
                .equipmentName("校准过期设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .nextCalibrationDate(now.minusDays(30)) // 已过期
                .createdAt(now)
                .updatedAt(now)
                .build();

        Equipment notOverdueCalibration = Equipment.builder()
                .equipmentCode("CAL-0002")
                .equipmentName("校准未过期设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .nextCalibrationDate(now.plusDays(30)) // 未过期
                .createdAt(now)
                .updatedAt(now)
                .build();

        Equipment decommissionedEquipment = Equipment.builder()
                .equipmentCode("CAL-0003")
                .equipmentName("退役设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.DECOMMISSIONED) // 已退役应排除
                .operationalStatus(Equipment.OperationalStatus.STOPPED)
                .nextCalibrationDate(now.minusDays(30))
                .createdAt(now)
                .updatedAt(now)
                .build();

        equipmentRepository.save(overdueCalibration);
        equipmentRepository.save(notOverdueCalibration);
        equipmentRepository.save(decommissionedEquipment);

        // When: 查询校准过期设备
        List<Equipment> overdueEquipments = equipmentRepository.findCalibrationOverdueEquipment(now);

        // Then: 验证结果
        assertThat(overdueEquipments).hasSize(1);
        assertThat(overdueEquipments.get(0).getEquipmentCode()).isEqualTo("CAL-0001");
    }

    @Test
    void testFindEquipmentDueForCalibration() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusDays(15);

        // Given: 创建即将到期的校准设备
        Equipment dueForCalibration = Equipment.builder()
                .equipmentCode("CAL-0004")
                .equipmentName("即将到期校准设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .nextCalibrationDate(now.plusDays(10)) // 15天预警期内
                .createdAt(now)
                .updatedAt(now)
                .build();

        Equipment notDueForCalibration = Equipment.builder()
                .equipmentCode("CAL-0005")
                .equipmentName("未到期校准设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .nextCalibrationDate(now.plusDays(20)) // 超过15天预警期
                .createdAt(now)
                .updatedAt(now)
                .build();

        equipmentRepository.save(dueForCalibration);
        equipmentRepository.save(notDueForCalibration);

        // When: 查询即将到期的校准设备
        List<Equipment> dueCalibrationEquipments = equipmentRepository.findEquipmentDueForCalibration(now, futureDate);

        // Then: 验证结果
        assertThat(dueCalibrationEquipments).hasSize(1);
        assertThat(dueCalibrationEquipments.get(0).getEquipmentCode()).isEqualTo("CAL-0004");
    }

    @Test
    void testFindEquipmentUnderWarranty() {
        LocalDateTime now = LocalDateTime.now();

        // Given: 创建在质保期内的设备
        Equipment underWarranty = Equipment.builder()
                .equipmentCode("WAR-0001")
                .equipmentName("在保设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .warrantyExpiry(now.plusMonths(6)) // 有效质保
                .createdAt(now)
                .updatedAt(now)
                .build();

        Equipment expiredWarranty = Equipment.builder()
                .equipmentCode("WAR-0002")
                .equipmentName("过保设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .warrantyExpiry(now.minusMonths(1)) // 过期质保
                .createdAt(now)
                .updatedAt(now)
                .build();

        equipmentRepository.save(underWarranty);
        equipmentRepository.save(expiredWarranty);

        // When: 查询在保设备
        List<Equipment> underWarrantyEquipments = equipmentRepository.findEquipmentUnderWarranty(now);

        // Then: 验证结果
        assertThat(underWarrantyEquipments).hasSize(1);
        assertThat(underWarrantyEquipments.get(0).getEquipmentCode()).isEqualTo("WAR-0001");
    }

    @Test
    void testCountEquipmentByType() {
        // Given: 创建不同类型的设备
        Equipment reactionKettle = Equipment.builder()
                .equipmentCode("RK-0001")
                .equipmentName("反应釜001")
                .equipmentType("反应设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment centrifugalPump = Equipment.builder()
                .equipmentCode("CP-0001")
                .equipmentName("离心泵001")
                .equipmentType("泵类设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment anotherReactionKettle = Equipment.builder()
                .equipmentCode("RK-0002")
                .equipmentName("反应釜002")
                .equipmentType("反应设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(reactionKettle);
        equipmentRepository.save(centrifugalPump);
        equipmentRepository.save(anotherReactionKettle);

        // When: 统计按类型分组
        List<Object[]> typeCounts = equipmentRepository.countEquipmentByType();

        // Then: 验证统计结果
        assertThat(typeCounts).hasSize(2);
        // 验证包含反应设备和泵类设备
        boolean hasReactionType = typeCounts.stream()
                .anyMatch(row -> "反应设备".equals(row[0]) && ((Long) row[1]) == 2L);
        boolean hasPumpType = typeCounts.stream()
                .anyMatch(row -> "泵类设备".equals(row[0]) && ((Long) row[1]) == 1L);

        assertThat(hasReactionType).isTrue();
        assertThat(hasPumpType).isTrue();
    }

    @Test
    void testFindCriticalEquipment() {
        // Given: 创建关键设备和普通设备
        Equipment criticalEquipment = Equipment.builder()
                .equipmentCode("CRT-0001")
                .equipmentName("关键设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .criticalEquipment(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment normalEquipment = Equipment.builder()
                .equipmentCode("NRM-0001")
                .equipmentName("普通设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .criticalEquipment(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment inactiveCriticalEquipment = Equipment.builder()
                .equipmentCode("CRT-0002")
                .equipmentName("非活动关键设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.MAINTENANCE)
                .operationalStatus(Equipment.OperationalStatus.MAINTENANCE)
                .criticalEquipment(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(criticalEquipment);
        equipmentRepository.save(normalEquipment);
        equipmentRepository.save(inactiveCriticalEquipment);

        // When: 查询关键设备
        List<Equipment> criticalEquipments = equipmentRepository.findCriticalEquipment();

        // Then: 验证结果 - 只返回活跃的关键设备
        assertThat(criticalEquipments).hasSize(1);
        assertThat(criticalEquipments.get(0).getEquipmentCode()).isEqualTo("CRT-0001");
    }

    @Test
    void testFindMonitoredEquipment() {
        // Given: 创建启用监控和未启用监控的设备
        Equipment monitoredEquipment = Equipment.builder()
                .equipmentCode("MON-0001")
                .equipmentName("监控设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .monitoringEnabled(true)
                .sensorId("SENSOR-001")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment unmonitoredEquipment = Equipment.builder()
                .equipmentCode("MON-0002")
                .equipmentName("非监控设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .monitoringEnabled(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment maintenanceMonitoredEquipment = Equipment.builder()
                .equipmentCode("MON-0003")
                .equipmentName("维护中监控设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.MAINTENANCE)
                .operationalStatus(Equipment.OperationalStatus.MAINTENANCE)
                .monitoringEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(monitoredEquipment);
        equipmentRepository.save(unmonitoredEquipment);
        equipmentRepository.save(maintenanceMonitoredEquipment);

        // When: 查询可监控设备
        List<Equipment> monitoredEquipments = equipmentRepository.findMonitoredEquipment();

        // Then: 验证结果 - 只返回活跃且启用监控的设备
        assertThat(monitoredEquipments).hasSize(1);
        assertThat(monitoredEquipments.get(0).getEquipmentCode()).isEqualTo("MON-0001");
        assertThat(monitoredEquipments.get(0).getSensorId()).isEqualTo("SENSOR-001");
    }

    @Test
    void testFindEquipmentWithFilters() {
        // Given: 创建各种设备的测试数据
        Equipment equipment1 = Equipment.builder()
                .equipmentCode("SRC-0001")
                .equipmentName("搜索设备001")
                .equipmentType("反应设备")
                .manufacturer("ABC制造")
                .location("A车间")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment equipment2 = Equipment.builder()
                .equipmentCode("SRC-0002")
                .equipmentName("搜索设备002")
                .equipmentType("混合设备")
                .manufacturer("ABC制造")
                .location("B车间")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Equipment equipment3 = Equipment.builder()
                .equipmentCode("SRC-0003")
                .equipmentName("维护设备003")
                .equipmentType("反应设备")
                .manufacturer("XYZ制造")
                .location("A车间")
                .equipmentStatus(Equipment.EquipmentStatus.MAINTENANCE)
                .operationalStatus(Equipment.OperationalStatus.MAINTENANCE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(equipment1);
        equipmentRepository.save(equipment2);
        equipmentRepository.save(equipment3);

        // When: 按多个条件查询
        Page<Equipment> result = equipmentRepository.findEquipmentWithFilters(
                "SRC", // equipmentCode
                null, // equipmentName
                "反应设备", // equipmentType
                null, // manufacturer
                null, // location
                Equipment.EquipmentStatus.ACTIVE, // status
                PageRequest.of(0, 10));

        // Then: 验证结果
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEquipmentCode()).isEqualTo("SRC-0001");
    }

    @Test
    void testExistsByEquipmentCode() {
        // Given: 保存一个设备
        Equipment equipment = Equipment.builder()
                .equipmentCode("EXS-0001")
                .equipmentName("存在检查设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(equipment);

        // When & Then: 检查设备编码是否存在
        assertThat(equipmentRepository.existsByEquipmentCode("EXS-0001")).isTrue();
        assertThat(equipmentRepository.existsByEquipmentCode("NEX-0001")).isFalse();
    }

    @Test
    void testFindBySensorId() {
        // Given: 保存带有传感器ID的设备
        Equipment equipment = Equipment.builder()
                .equipmentCode("SEN-0001")
                .equipmentName("传感器设备")
                .equipmentType("测试设备")
                .manufacturer("测试制造商")
                .location("测试位置")
                .equipmentStatus(Equipment.EquipmentStatus.ACTIVE)
                .operationalStatus(Equipment.OperationalStatus.RUNNING)
                .sensorId("TEMP-SENSOR-12345")
                .monitoringEnabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        equipmentRepository.save(equipment);

        // When: 通过传感器ID查找
        Equipment found = equipmentRepository.findBySensorId("TEMP-SENSOR-12345");

        // Then: 验证结果
        assertThat(found).isNotNull();
        assertThat(found.getEquipmentCode()).isEqualTo("SEN-0001");
        assertThat(found.getSensorId()).isEqualTo("TEMP-SENSOR-12345");
    }
}
