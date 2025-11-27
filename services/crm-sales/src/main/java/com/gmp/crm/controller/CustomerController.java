package com.gmp.crm.controller;

import com.gmp.crm.dto.ApiResponse;
import com.gmp.crm.dto.CustomerDTO;
import com.gmp.crm.dto.CustomerRequestDTO;
import com.gmp.crm.dto.PageRequestDTO;
import com.gmp.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 客户控制器
 * 
 * @author TRAE AI
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerController {

    private final CustomerService customerService;

    /**
     * 创建客户
     * 
     * @param request 客户请求DTO
     * @return 创建的客户DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDTO>> createCustomer(@Valid @RequestBody CustomerRequestDTO request) {
        CustomerDTO customerDTO = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(customerDTO));
    }

    /**
     * 更新客户
     * 
     * @param id      客户ID
     * @param request 客户请求DTO
     * @return 更新后的客户DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO request) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success(customerDTO));
    }

    /**
     * 获取客户详情
     * 
     * @param id 客户ID
     * @return 客户DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(customerDTO -> ResponseEntity.ok(ApiResponse.success(customerDTO)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error(404, "客户不存在")));
    }

    /**
     * 删除客户
     * 
     * @param id 客户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(deleted));
    }

    /**
     * 分页查询客户列表
     * 
     * @param pageRequest 分页请求
     * @return 客户分页列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<ApiResponse.PageResponse<List<CustomerDTO>>>> getCustomers(
            PageRequestDTO pageRequest) {
        var customerPage = customerService.getCustomers(pageRequest);
        
        ApiResponse.PageResponse<List<CustomerDTO>> pageResponse = new ApiResponse.PageResponse<>();
        pageResponse.setPage(pageRequest.getPage());
        pageResponse.setSize(pageRequest.getSize());
        pageResponse.setTotal(customerPage.getTotalElements());
        pageResponse.setTotalPages(customerPage.getTotalPages());
        pageResponse.setList(customerPage.getContent());
        
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }

    /**
     * 搜索客户
     * 
     * @param name 客户名称关键字
     * @return 客户列表
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchCustomers(
            @RequestParam String name) {
        List<CustomerDTO> customers = customerService.searchCustomersByName(name);
        return ResponseEntity.ok(ApiResponse.success(customers));
    }

    /**
     * 检查客户代码是否存在
     * 
     * @param code      客户代码
     * @param excludeId 排除的客户ID
     * @return 是否存在
     */
    @GetMapping("/check-code")
    public ResponseEntity<ApiResponse<Boolean>> checkCustomerCode(
            @RequestParam String code,
            @RequestParam(required = false) Long excludeId) {
        boolean exists = customerService.existsByCode(code, excludeId);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

}