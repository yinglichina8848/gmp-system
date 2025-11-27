package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Supplier实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class SupplierTest {

    private Supplier supplier;

    @BeforeEach
    void setUp() {
        supplier = new Supplier();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        supplier.setId(id);
        assertEquals(id, supplier.getId());
    }

    @Test
    void testSetAndGetCode() {
        String code = "SUP001";
        supplier.setCode(code);
        assertEquals(code, supplier.getCode());
    }

    @Test
    void testSetAndGetName() {
        String name = "测试供应商";
        supplier.setName(name);
        assertEquals(name, supplier.getName());
    }

    @Test
    void testSetAndGetContactPerson() {
        String contactPerson = "张三";
        supplier.setContactPerson(contactPerson);
        assertEquals(contactPerson, supplier.getContactPerson());
    }

    @Test
    void testSetAndGetContactPhone() {
        String contactPhone = "13800138000";
        supplier.setContactPhone(contactPhone);
        assertEquals(contactPhone, supplier.getContactPhone());
    }

    @Test
    void testSetAndGetEmail() {
        String email = "supplier@test.com";
        supplier.setEmail(email);
        assertEquals(email, supplier.getEmail());
    }

    @Test
    void testSetAndGetAddress() {
        String address = "北京市海淀区中关村大街1号";
        supplier.setAddress(address);
        assertEquals(address, supplier.getAddress());
    }

    @Test
    void testSetAndGetBankName() {
        String bankName = "中国银行";
        supplier.setBankName(bankName);
        assertEquals(bankName, supplier.getBankName());
    }

    @Test
    void testSetAndGetBankAccount() {
        String bankAccount = "6222021234567890123";
        supplier.setBankAccount(bankAccount);
        assertEquals(bankAccount, supplier.getBankAccount());
    }

    @Test
    void testSetAndGetTaxId() {
        String taxId = "91110000710938134F";
        supplier.setTaxId(taxId);
        assertEquals(taxId, supplier.getTaxId());
    }

    @Test
    void testSetAndGetSupplierType() {
        String supplierType = "普通供应商";
        supplier.setSupplierType(supplierType);
        assertEquals(supplierType, supplier.getSupplierType());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        supplier.setRemark(remark);
        assertEquals(remark, supplier.getRemark());
    }

    @Test
    void testSetAndGetStatus() {
        Integer status = 1;
        supplier.setStatus(status);
        assertEquals(status, supplier.getStatus());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        supplier.setCreatedAt(createdAt);
        assertEquals(createdAt, supplier.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        supplier.setCreatedBy(createdBy);
        assertEquals(createdBy, supplier.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        supplier.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, supplier.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        supplier.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, supplier.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        Supplier supplier1 = new Supplier();
        supplier1.setId(1L);
        supplier1.setCode("SUP001");

        Supplier supplier2 = new Supplier();
        supplier2.setId(1L);
        supplier2.setCode("SUP001");

        Supplier supplier3 = new Supplier();
        supplier3.setId(2L);
        supplier3.setCode("SUP002");

        assertEquals(supplier1, supplier2);
        assertNotEquals(supplier1, supplier3);
        assertEquals(supplier1.hashCode(), supplier2.hashCode());
    }

}