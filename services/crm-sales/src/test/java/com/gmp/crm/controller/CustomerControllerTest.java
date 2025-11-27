package com.gmp.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmp.crm.dto.CustomerDTO;
import com.gmp.crm.dto.CustomerRequestDTO;
import com.gmp.crm.dto.PageRequestDTO;
import com.gmp.crm.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

/**
 * 客户控制器单元测试
 * 
 * @author TRAE AI
 */
@WebMvcTest(controllers = CustomerController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDTO customerDTO;
    private CustomerRequestDTO customerRequestDTO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("测试客户");
        customerDTO.setCode("C001");
        customerDTO.setContactPerson("张三");
        customerDTO.setPhone("13800138000");
        customerDTO.setEmail("test@example.com");
        customerDTO.setAddress("北京市朝阳区");
        customerDTO.setStatus("ACTIVE");
        customerDTO.setCreatedAt(LocalDateTime.now());
        customerDTO.setUpdatedAt(LocalDateTime.now());

        customerRequestDTO = new CustomerRequestDTO();
        customerRequestDTO.setName("测试客户");
        customerRequestDTO.setCode("C001");
        customerRequestDTO.setContactPerson("张三");
        customerRequestDTO.setPhone("13800138000");
        customerRequestDTO.setEmail("test@example.com");
        customerRequestDTO.setAddress("北京市朝阳区");
    }

    @Test
    void createCustomer_Success() throws Exception {
        // 配置mock行为
        when(customerService.createCustomer(any(CustomerRequestDTO.class))).thenReturn(customerDTO);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("测试客户"));
    }

    @Test
    void createCustomer_InvalidRequest() throws Exception {
        // 准备无效的请求数据
        customerRequestDTO.setName(null); // 名称为空，应该触发验证错误

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateCustomer_Success() throws Exception {
        // 配置mock行为
        when(customerService.updateCustomer(anyLong(), any(CustomerRequestDTO.class))).thenReturn(customerDTO);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.put("/api/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
    }

    @Test
    void getCustomer_Success() throws Exception {
        // 配置mock行为
        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customerDTO));

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("测试客户"));
    }

    @Test
    void getCustomer_NotFound() throws Exception {
        // 配置mock行为
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("客户不存在"));
    }

    @Test
    void deleteCustomer_Success() throws Exception {
        // 配置mock行为
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/customers/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true));
    }

    @Test
    void getCustomers_Pagination() throws Exception {
        // 准备分页数据
        List<CustomerDTO> customerList = Arrays.asList(customerDTO);
        Page<CustomerDTO> customerPage = new PageImpl<>(customerList);

        // 配置mock行为
        when(customerService.getCustomers(any(PageRequestDTO.class))).thenReturn(customerPage);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.total").value(1));
    }

    @Test
    void searchCustomers_Success() throws Exception {
        // 准备搜索结果
        List<CustomerDTO> customerList = Arrays.asList(customerDTO);

        // 配置mock行为
        when(customerService.searchCustomersByName(anyString())).thenReturn(customerList);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/search")
                .param("name", "测试"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("测试客户"));
    }

    @Test
    void checkCustomerCode_Exists() throws Exception {
        // 配置mock行为
        when(customerService.existsByCode(anyString(), isNull())).thenReturn(true);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/check-code")
                .param("code", "C001"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(true));
    }

    @Test
    void checkCustomerCode_NotExists() throws Exception {
        // 配置mock行为
        when(customerService.existsByCode(anyString(), isNull())).thenReturn(false);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/api/customers/check-code")
                .param("code", "C999"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(false));
    }

}