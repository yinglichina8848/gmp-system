package com.gmp.equipment.repository;

import com.gmp.equipment.entity.Equipment;
import com.gmp.equipment.entity.EquipmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 设备仓库单元测试
 * 测试设备数据访问层的各种查询方法
 * 
 * @author GMP系统开发团队
 * @version 1.0
 * @since 2023-07-01
 */
@DataJpaTest
class EquipmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentTypeRepository equipmentTypeRepository;

    private Equipment equipment;
    private EquipmentType equipmentType;

    @BeforeEach
    void setUp() {
        // 创建设备类型
        equipmentType = new EquipmentType();
        equipmentType.setCode("ET001");
        equipmentType.setName("生产设备");
        equipmentType.setDescription("用于生产的设备");
        entityManager.persist(equipmentType);
        entityManager.flush();

        // 创建测试设备
        equipment = new Equipment();
        equipment.setEquipmentCode("EQU202307010001");
        equipment.setEquipmentName("测试设备");
        equipment.setModel("Model-A");
        equipment.setManufacturer("测试厂商");
        equipment.setStatus("ACTIVE");
        equipment.setLocation("生产车间A");
        equipment.setResponsiblePerson("张三");
        equipment.setCalibrationDueDate(LocalDate.now().plusDays(30));
        equipment.setMaintenanceDueDate(LocalDate.now().plusDays(15));
        equipment.setEquipmentType(equipmentType);
        equipment.setCreatedBy("admin");
        equipment.setCreatedTime(LocalDateTime.now());
        equipment.setLastModifiedBy("admin");
        equipment.setLastModifiedTime(LocalDateTime.now());
        entityManager.persist(equipment);
        entityManager.flush();

        // 创建更多测试数据
        Equipment equipment2 = new Equipment();
        equipment2.setEquipmentCode("EQU202307010002");
        equipment2.setEquipmentName("测试设备2");
        equipment2.setModel("Model-B");
        equipment2.setManufacturer("测试厂商2");
        equipment2.setStatus("MAINTENANCE");
        equipment2.setLocation("生产车间B");
        equipment2.setResponsiblePerson("李四");
        equipment2.setCalibrationDueDate(LocalDate.now().minusDays(5)); // 已过期
        equipment2.setMaintenanceDueDate(LocalDate.now().plusDays(10));
        equipment2.setEquipmentType(equipmentType);
        equipment2.setCreatedBy("admin");
        equipment2.setCreatedTime(LocalDateTime.now());
        entityManager.persist(equipment2);
        entityManager.flush();

        Equipment equipment3 = new Equipment();
        equipment3.setEquipmentCode("EQU202307010003");
        equipment3.setEquipmentName("测试设备3");
        equipment3.setModel("Model-C");
        equipment3.setManufacturer("测试厂商3");
        equipment3.setStatus("INACTIVE");
        equipment3.setLocation("仓库");
        equipment3.setResponsiblePerson("王五");
        equipment3.setCalibrationDueDate(LocalDate.now().plusDays(5));
        equipment3.setMaintenanceDueDate(LocalDate.now().minusDays(3)); // 已过期
        equipment3.setEquipmentType(equipmentType);
        equipment3.setCreatedBy("admin");
        equipment3.setCreatedTime(LocalDateTime.now());
        entityManager.persist(equipment3);
        entityManager.flush();
    }

    @Test
    void testFindByEquipmentCode() {
        Optional<Equipment> found = equipmentRepository.findByEquipmentCode("EQU202307010001");
        assertThat(found).isPresent();
        assertThat(found.get().getEquipmentName()).isEqualTo("测试设备");
    }

    @Test
    void testFindByEquipmentCodeNotFound() {
        Optional<Equipment> found = equipmentRepository.findByEquipmentCode("NON_EXISTENT_CODE");
        assertThat(found).isNotPresent();
    }

    @Test
    void testExistsByEquipmentCode() {
        boolean exists = equipmentRepository.existsByEquipmentCode("EQU202307010001");
        assertThat(exists).isTrue();

        boolean notExists = equipmentRepository.existsByEquipmentCode("NON_EXISTENT_CODE");
        assertThat(notExists).isFalse();
    }

    @Test
    void testExistsByEquipmentCodeAndIdNot() {
        // 排除当前ID后，该编码不存在重复
        boolean exists = equipmentRepository.existsByEquipmentCodeAndIdNot("EQU202307010001", equipment.getId());
        assertThat(exists).isFalse();

        // 其他设备的编码仍然存在
        boolean existsOther = equipmentRepository.existsByEquipmentCodeAndIdNot("EQU202307010002", equipment.getId());
        assertThat(existsOther).isTrue();
    }

    @Test
    void testFindByStatus() {
        List<Equipment> activeEquipment = equipmentRepository.findByStatus("ACTIVE");
        assertThat(activeEquipment).hasSize(1);
        assertThat(activeEquipment.get(0).getEquipmentCode()).isEqualTo("EQU202307010001");

        List<Equipment> maintenanceEquipment = equipmentRepository.findByStatus("MAINTENANCE");
        assertThat(maintenanceEquipment).hasSize(1);
        assertThat(maintenanceEquipment.get(0).getEquipmentCode()).isEqualTo("EQU202307010002");
    }

    @Test
    void testFindByEquipmentType() {
        List<Equipment> equipmentList = equipmentRepository.findByEquipmentType(equipmentType);
        assertThat(equipmentList).hasSize(3); // 所有设备都属于同一类型
    }

    @Test
    void testFindByLocation() {
        List<Equipment> locationList = equipmentRepository.findByLocation("生产车间A");
        assertThat(locationList).hasSize(1);
        assertThat(locationList.get(0).getEquipmentCode()).isEqualTo("EQU202307010001");
    }

    @Test
    void testFindByResponsiblePerson() {
        List<Equipment> personList = equipmentRepository.findByResponsiblePerson("张三");
        assertThat(personList).hasSize(1);
        assertThat(personList.get(0).getEquipmentCode()).isEqualTo("EQU202307010001");
    }

    @Test
    void testFindByCalibrationDueDateBefore() {
        // 查找校准已过期的设备（当前日期之后5天的设备）
        List<Equipment> dueCalibration = equipmentRepository.findByCalibrationDueDateBefore(LocalDate.now().plusDays(5));
        assertThat(dueCalibration).hasSize(2); // 包含已过期的和5天内到期的
    }

    @Test
    void testFindByMaintenanceDueDateBefore() {
        // 查找维护已过期的设备
        List<Equipment> dueMaintenance = equipmentRepository.findByMaintenanceDueDateBefore(LocalDate.now().plusDays(5));
        assertThat(dueMaintenance).hasSize(2); // 包含已过期的和5天内到期的
    }

    @Test
    void testFindByEquipmentCodeContainingOrEquipmentNameContaining() {
        // 按编码或名称模糊查询
        List<Equipment> searchList = equipmentRepository.findByEquipmentCodeContainingOrEquipmentNameContaining("001", "设备2");
        assertThat(searchList).hasSize(2);
        assertThat(searchList.stream().anyMatch(e -> e.getEquipmentCode().equals("EQU202307010001"))).isTrue();
        assertThat(searchList.stream().anyMatch(e -> e.getEquipmentCode().equals("EQU202307010002"))).isTrue();
    }

    @Test
    void testFindByEquipmentCodeContaining() {
        List<Equipment> codeList = equipmentRepository.findByEquipmentCodeContaining("000");
        assertThat(codeList).hasSize(3); // 所有设备编码都包含000
    }

    @Test
    void testFindByEquipmentNameContaining() {
        List<Equipment> nameList = equipmentRepository.findByEquipmentNameContaining("测试");
        assertThat(nameList).hasSize(3); // 所有设备名称都包含"测试"
    }

    @Test
    void testFindByManufacturer() {
        List<Equipment> manufacturerList = equipmentRepository.findByManufacturer("测试厂商");
        assertThat(manufacturerList).hasSize(1);
        assertThat(manufacturerList.get(0).getEquipmentCode()).isEqualTo("EQU202307010001");
    }

    @Test
    void testFindByModel() {
        List<Equipment> modelList = equipmentRepository.findByModel("Model-A");
        assertThat(modelList).hasSize(1);
        assertThat(modelList.get(0).getEquipmentCode()).isEqualTo("EQU202307010001");
    }

    @Test
    void testFindByCalibrationDueDateBetween() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(20);
        List<Equipment> betweenList = equipmentRepository.findByCalibrationDueDateBetween(startDate, endDate);
        assertThat(betweenList).isNotEmpty();
    }

    @Test
    void testFindByMaintenanceDueDateBetween() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(20);
        List<Equipment> betweenList = equipmentRepository.findByMaintenanceDueDateBetween(startDate, endDate);
        assertThat(betweenList).isNotEmpty();
    }

    @Test
    void testFindByActiveAndCalibrationDueDateBefore() {
        List<Equipment> activeDueCalibration = equipmentRepository.findByStatusAndCalibrationDueDateBefore("ACTIVE", LocalDate.now().plusDays(20));
        assertThat(activeDueCalibration).hasSize(1);
        assertThat(activeDueCalibration.get(0).getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testFindByActiveAndMaintenanceDueDateBefore() {
        List<Equipment> activeDueMaintenance = equipmentRepository.findByStatusAndMaintenanceDueDateBefore("ACTIVE", LocalDate.now().plusDays(20));
        assertThat(activeDueMaintenance).hasSize(1);
        assertThat(activeDueMaintenance.get(0).getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void testFindAllWithPagination() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Equipment> page = equipmentRepository.findAll(pageable);
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
    }
}
