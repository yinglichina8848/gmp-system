package com.gmp.crm.repository;

import com.gmp.crm.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 客户数据访问接口
 * 
 * @author TRAE AI
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /**
     * 根据客户代码查找客户
     * 
     * @param code 客户代码
     * @return 客户信息
     */
    Optional<Customer> findByCode(String code);

    /**
     * 检查客户代码是否存在
     * 
     * @param code 客户代码
     * @param id 排除的客户ID
     * @return 是否存在
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * 根据名称关键字搜索客户
     * 
     * @param name 名称关键字
     * @return 客户列表
     */
    List<Customer> findByNameContaining(String name);

    /**
     * 分页查找有效客户
     * 
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页客户列表
     */
    Page<Customer> findByStatus(String status, Pageable pageable);

    /**
     * 统计客户数量
     * 
     * @param status 状态
     * @return 客户数量
     */
    long countByStatus(String status);

    /**
     * 查询最近创建的客户
     * 
     * @param limit 数量限制
     * @return 客户列表
     */
    @Query("SELECT c FROM Customer c ORDER BY c.createdAt DESC")
    List<Customer> findLatestCustomers(Pageable limit);

}