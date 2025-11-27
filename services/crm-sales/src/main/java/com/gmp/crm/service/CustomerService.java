package com.gmp.crm.service;

import com.gmp.crm.dto.CustomerDTO;
import com.gmp.crm.dto.CustomerRequestDTO;
import com.gmp.crm.dto.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * 客户服务接口
 * 
 * @author TRAE AI
 */
public interface CustomerService {

    /**
     * 创建客户
     * 
     * @param request 客户请求DTO
     * @return 创建的客户DTO
     */
    CustomerDTO createCustomer(CustomerRequestDTO request);

    /**
     * 更新客户
     * 
     * @param id      客户ID
     * @param request 客户请求DTO
     * @return 更新后的客户DTO
     */
    CustomerDTO updateCustomer(Long id, CustomerRequestDTO request);

    /**
     * 根据ID获取客户
     * 
     * @param id 客户ID
     * @return 客户DTO
     */
    Optional<CustomerDTO> getCustomerById(Long id);

    /**
     * 根据ID删除客户
     * 
     * @param id 客户ID
     * @return 是否删除成功
     */
    boolean deleteCustomer(Long id);

    /**
     * 分页查询客户列表
     * 
     * @param pageRequest 分页请求
     * @return 客户分页列表
     */
    Page<CustomerDTO> getCustomers(PageRequestDTO pageRequest);

    /**
     * 按名称搜索客户
     * 
     * @param name 客户名称关键字
     * @return 客户列表
     */
    List<CustomerDTO> searchCustomersByName(String name);

    /**
     * 检查客户代码是否存在
     * 
     * @param code 客户代码
     * @param excludeId 排除的客户ID（更新时使用）
     * @return 是否存在
     */
    boolean existsByCode(String code, Long excludeId);

}