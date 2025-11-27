package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StockInOrder实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class StockInOrderTest {

    private StockInOrder stockInOrder;

    @BeforeEach
    void setUp() {
        stockInOrder = new StockInOrder();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        stockInOrder.setId(id);
        assertEquals(id, stockInOrder.getId());
    }

    @Test
    void testSetAndGetOrderNo() {
        String orderNo = "IN202401010001";
        stockInOrder.setOrderNo(orderNo);
        assertEquals(orderNo, stockInOrder.getOrderNo());
    }

    @Test
    void testSetAndGetOrderType() {
        String orderType = "采购入库";
        stockInOrder.setOrderType(orderType);
        assertEquals(orderType, stockInOrder.getOrderType());
    }

    @Test
    void testSetAndGetWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("一号仓库");
        stockInOrder.setWarehouse(warehouse);
        assertEquals(warehouse, stockInOrder.getWarehouse());
        assertEquals(1L, stockInOrder.getWarehouse().getId());
    }

    @Test
    void testSetAndGetSupplier() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setName("测试供应商");
        stockInOrder.setSupplier(supplier);
        assertEquals(supplier, stockInOrder.getSupplier());
        assertEquals(1L, stockInOrder.getSupplier().getId());
    }

    @Test
    void testSetAndGetOrderDate() {
        LocalDateTime orderDate = LocalDateTime.now();
        stockInOrder.setOrderDate(orderDate);
        assertEquals(orderDate, stockInOrder.getOrderDate());
    }

    @Test
    void testSetAndGetReceiveDate() {
        LocalDateTime receiveDate = LocalDateTime.now().plusDays(1);
        stockInOrder.setReceiveDate(receiveDate);
        assertEquals(receiveDate, stockInOrder.getReceiveDate());
    }

    @Test
    void testSetAndGetExpectedDate() {
        LocalDateTime expectedDate = LocalDateTime.now().plusDays(2);
        stockInOrder.setExpectedDate(expectedDate);
        assertEquals(expectedDate, stockInOrder.getExpectedDate());
    }

    @Test
    void testSetAndGetTotalQuantity() {
        BigDecimal totalQuantity = new BigDecimal(1000);
        stockInOrder.setTotalQuantity(totalQuantity);
        assertEquals(totalQuantity, stockInOrder.getTotalQuantity());
    }

    @Test
    void testSetAndGetTotalAmount() {
        BigDecimal totalAmount = new BigDecimal(10500);
        stockInOrder.setTotalAmount(totalAmount);
        assertEquals(totalAmount, stockInOrder.getTotalAmount());
    }

    @Test
    void testSetAndGetRelatedOrderNo() {
        String relatedOrderNo = "PO202401010001";
        stockInOrder.setRelatedOrderNo(relatedOrderNo);
        assertEquals(relatedOrderNo, stockInOrder.getRelatedOrderNo());
    }

    @Test
    void testSetAndGetOperator() {
        String operator = "张三";
        stockInOrder.setOperator(operator);
        assertEquals(operator, stockInOrder.getOperator());
    }

    @Test
    void testSetAndGetCheckStatus() {
        String checkStatus = "已审核";
        stockInOrder.setCheckStatus(checkStatus);
        assertEquals(checkStatus, stockInOrder.getCheckStatus());
    }

    @Test
    void testSetAndGetStatus() {
        Integer status = 1;
        stockInOrder.setStatus(status);
        assertEquals(status, stockInOrder.getStatus());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        stockInOrder.setRemark(remark);
        assertEquals(remark, stockInOrder.getRemark());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        stockInOrder.setCreatedAt(createdAt);
        assertEquals(createdAt, stockInOrder.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        stockInOrder.setCreatedBy(createdBy);
        assertEquals(createdBy, stockInOrder.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        stockInOrder.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, stockInOrder.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        stockInOrder.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, stockInOrder.getUpdatedBy());
    }

    @Test
    void testSetAndGetItems() {
        List<StockInOrderItem> items = new ArrayList<>();
        StockInOrderItem item1 = new StockInOrderItem();
        item1.setId(1L);
        StockInOrderItem item2 = new StockInOrderItem();
        item2.setId(2L);
        items.add(item1);
        items.add(item2);
        
        stockInOrder.setItems(items);
        assertEquals(items, stockInOrder.getItems());
        assertEquals(2, stockInOrder.getItems().size());
        assertEquals(1L, stockInOrder.getItems().get(0).getId());
    }

    @Test
    void testEqualsAndHashCode() {
        StockInOrder order1 = new StockInOrder();
        order1.setId(1L);
        order1.setOrderNo("IN202401010001");

        StockInOrder order2 = new StockInOrder();
        order2.setId(1L);
        order2.setOrderNo("IN202401010001");

        StockInOrder order3 = new StockInOrder();
        order3.setId(2L);
        order3.setOrderNo("IN202401010002");

        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

}