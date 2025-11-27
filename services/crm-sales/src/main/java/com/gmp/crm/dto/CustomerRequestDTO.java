package com.gmp.crm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 客户创建/更新请求DTO
 * 
 * @author TRAE AI
 */
@Data
public class CustomerRequestDTO {

    /**
     * 客户名称
     */
    @NotBlank(message = "客户名称不能为空")
    @Size(max = 255, message = "客户名称长度不能超过255个字符")
    private String name;

    /**
     * 客户代码
     */
    @Size(max = 50, message = "客户代码长度不能超过50个字符")
    private String code;

    /**
     * 联系人
     */
    @Size(max = 100, message = "联系人长度不能超过100个字符")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Size(max = 20, message = "联系电话长度不能超过20个字符")
    private String phone;

    /**
     * 电子邮箱
     */
    @Size(max = 100, message = "电子邮箱长度不能超过100个字符")
    private String email;

    /**
     * 地址
     */
    @Size(max = 500, message = "地址长度不能超过500个字符")
    private String address;

}