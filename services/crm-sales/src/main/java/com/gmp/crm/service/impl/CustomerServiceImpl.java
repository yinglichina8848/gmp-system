package com.gmp.crm.service.impl;

import com.gmp.crm.dto.CustomerDTO;
import com.gmp.crm.dto.CustomerRequestDTO;
import com.gmp.crm.dto.PageRequestDTO;
import com.gmp.crm.entity.Customer;
import com.gmp.crm.repository.CustomerRepository;
import com.gmp.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客户服务实现类
 * 
 * @author TRAE AI
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerRequestDTO request) {
        // 检查客户代码是否已存在
        if (customerRepository.findByCode(request.getCode()).isPresent()) {
            throw new IllegalArgumentException("客户代码已存在: " + request.getCode());
        }

        // 转换请求DTO为实体
        Customer customer = modelMapper.map(request, Customer.class);
        customer.setStatus("ACTIVE"); // 默认激活状态

        // 保存客户
        Customer savedCustomer = customerRepository.save(customer);

        // 转换为响应DTO
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerRequestDTO request) {
        // 查找客户
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("客户不存在: " + id));

        // 检查客户代码是否已被其他客户使用
        if (request.getCode() != null && !request.getCode().equals(customer.getCode()) &&
                customerRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new IllegalArgumentException("客户代码已存在: " + request.getCode());
        }

        // 更新客户信息
        modelMapper.map(request, customer);

        // 保存更新
        Customer updatedCustomer = customerRepository.save(customer);

        // 转换为响应DTO
        return modelMapper.map(updatedCustomer, CustomerDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    @Override
    @Transactional
    public boolean deleteCustomer(Long id) {
        // 查找客户
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("客户不存在: " + id));

        // 软删除：将状态设置为INACTIVE
        customer.setStatus("INACTIVE");
        customerRepository.save(customer);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> getCustomers(PageRequestDTO pageRequest) {
        // 构建排序
        Sort.Direction direction = "desc".equalsIgnoreCase(pageRequest.getSortOrder()) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        String sortField = pageRequest.getSortField() != null ? pageRequest.getSortField() : "createdAt";
        Sort sort = Sort.by(direction, sortField);

        // 构建分页请求
        PageRequest pageable = PageRequest.of(pageRequest.getPage() - 1, pageRequest.getSize(), sort);

        // 查询有效客户
        Page<Customer> customerPage = customerRepository.findByStatus("ACTIVE", pageable);

        // 转换为DTO
        return customerPage.map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> searchCustomers(String name) {
        List<Customer> customers = customerRepository.findByNameContaining(name);
        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CustomerDTO> searchCustomersByName(String name) {
        return searchCustomers(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code, Long excludeId) {
        if (excludeId != null) {
            return customerRepository.existsByCodeAndIdNot(code, excludeId);
        }
        return customerRepository.findByCode(code).isPresent();
    }

}