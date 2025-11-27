package com.gmp.warehouse.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StockOutOrder实体类的单元测试
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
class StockOutOrderTest {

    private StockOutOrder stockOutOrder;

    @BeforeEach
    void setUp() {
        stockOutOrder = new StockOutOrder();
    }

    @Test
    void testSetAndGetId() {
        Long id = 1L;
        stockOutOrder.setId(id);
        assertEquals(id, stockOutOrder.getId());
    }

    @Test
    void testSetAndGetOrderNo() {
        String orderNo = "OUT202401010001";
        stockOutOrder.setOrderNo(orderNo);
        assertEquals(orderNo, stockOutOrder.getOrderNo());
    }

    @Test
    void testSetAndGetOrderType() {
        String orderType = "生产出库";
        stockOutOrder.setOrderType(orderType);
        assertEquals(orderType, stockOutOrder.getOrderType());
    }

    @Test
    void testSetAndGetWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("一号仓库");
        stockOutOrder.setWarehouse(warehouse);
        assertEquals(warehouse, stockOutOrder.getWarehouse());
        assertEquals(1L, stockOutOrder.getWarehouse().getId());
    }

    @Test
    void testSetAndGetOrderDate() {
        LocalDateTime orderDate = LocalDateTime.now();
        stockOutOrder.setOrderDate(orderDate);
        assertEquals(orderDate, stockOutOrder.getOrderDate());
    }

    @Test
    void testSetAndGetDeliveryDate() {
        LocalDateTime deliveryDate = LocalDateTime.now().plusDays(1);
        stockOutOrder.setDeliveryDate(deliveryDate);
        assertEquals(deliveryDate, stockOutOrder.getDeliveryDate());
    }

    @Test
    void testSetAndGetExpectedDate() {
        LocalDateTime expectedDate = LocalDateTime.now().plusDays(2);
        stockOutOrder.setExpectedDate(expectedDate);
        assertEquals(expectedDate, stockOutOrder.getExpectedDate());
    }

    @Test
    void testSetAndGetTotalQuantity() {
        BigDecimal totalQuantity = new BigDecimal(1000);
        stockOutOrder.setTotalQuantity(totalQuantity);
        assertEquals(totalQuantity, stockOutOrder.getTotalQuantity());
    }

    @Test
    void testSetAndGetTotalAmount() {
        BigDecimal totalAmount = new BigDecimal(10500);
        stockOutOrder.setTotalAmount(totalAmount);
        assertEquals(totalAmount, stockOutOrder.getTotalAmount());
    }

    @Test
    void testSetAndGetRelatedOrderNo() {
        String relatedOrderNo = "SO202401010001";
        stockOutOrder.setRelatedOrderNo(relatedOrderNo);
        assertEquals(relatedOrderNo, stockOutOrder.getRelatedOrderNo());
    }

    @Test
    void testSetAndGetOperator() {
        String operator = "张三";
        stockOutOrder.setOperator(operator);
        assertEquals(operator, stockOutOrder.getOperator());
    }

    @Test
    void testSetAndGetCheckStatus() {
        String checkStatus = "已审核";
        stockOutOrder.setCheckStatus(checkStatus);
        assertEquals(checkStatus, stockOutOrder.getCheckStatus());
    }

    @Test
    void testSetAndGetStatus() {
        Integer status = 1;
        stockOutOrder.setStatus(status);
        assertEquals(status, stockOutOrder.getStatus());
    }

    @Test
    void testSetAndGetRemark() {
        String remark = "备注信息";
        stockOutOrder.setRemark(remark);
        assertEquals(remark, stockOutOrder.getRemark());
    }

    @Test
    void testSetAndGetCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        stockOutOrder.setCreatedAt(createdAt);
        assertEquals(createdAt, stockOutOrder.getCreatedAt());
    }

    @Test
    void testSetAndGetCreatedBy() {
        String createdBy = "admin";
        stockOutOrder.setCreatedBy(createdBy);
        assertEquals(createdBy, stockOutOrder.getCreatedBy());
    }

    @Test
    void testSetAndGetUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        stockOutOrder.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, stockOutOrder.getUpdatedAt());
    }

    @Test
    void testSetAndGetUpdatedBy() {
        String updatedBy = "admin";
        stockOutOrder.setUpdatedBy(updatedBy);
        assertEquals(updatedBy, stockOutOrder.getUpdatedBy());
    }

    @Test
    void testSetAndGetItems() {
        List<StockOutOrderItem> items = new ArrayList<>();
        StockOutOrderItem item1 = new StockOutOrderItem();
        item1.setId(1L);
        StockOutOrderItem item2 = new StockOutOrderItem();
        item2.setId(2L);
        items.add(item1);
        items.add(item2);
        
        stockOutOrder.setItems(items);
        assertEquals(items, stockOutOrder.getItems());
        assertEquals(2, stockOutOrder.getItems().size());
        assertEquals(1L, stockOutOrder.getItems().get(0).getId());
    }

    @Test
    void testEqualsAndHashCode() {
        StockOutOrder order1 = new StockOutOrder();
        order1.setId(1L);
        order1.setOrderNo("OUT202401010001");

        StockOutOrder order2 = new StockOutOrder();
        order2.setId(1L);
        order2.setOrderNo("OUT202401010001");

        StockOutOrder order3 = new StockOutOrder();
        order3.setId(2L);
        order3.setOrderNo("OUT202401010002");

        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
        assertEquals(order1.hashCode(), order2.hashCode());
    }

}