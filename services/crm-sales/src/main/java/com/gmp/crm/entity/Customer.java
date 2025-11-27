package com.gmp.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户实体类
 * 
 * @author TRAE AI
 */
@Data
@Entity
@Table(name = "crm_customers")
public class Customer {

    /**
     * 客户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户名称
     */
    @Column(nullable = false, length = 255)
    private String name;

    /**
     * 客户代码
     */
    @Column(unique = true, length = 50)
    private String code;

    /**
     * 联系人
     */
    @Column(length = 100)
    private String contactPerson;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 电子邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 地址
     */
    @Column(length = 500)
    private String address;

    /**
     * 客户状态：活跃、停用
     */
    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * 更新时间
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * 关联的销售订单
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<SalesOrder> salesOrders;

}