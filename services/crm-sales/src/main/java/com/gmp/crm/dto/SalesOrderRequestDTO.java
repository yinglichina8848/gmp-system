package com.gmp.crm.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

/**
 * 销售订单创建请求DTO
 * 
 * @author TRAE AI
 */
@Data
public class SalesOrderRequestDTO {

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remarks;

    /**
     * 订单明细列表
     */
    @NotEmpty(message = "订单明细不能为空")
    @Size(min = 1, message = "至少需要一个订单明细")
    private List<OrderItemRequestDTO> items;

    /**
     * 订单明细请求DTO
     */
    @Data
    public static class OrderItemRequestDTO {
        
        /**
         * 产品编号
         */
        @NotBlank(message = "产品编号不能为空")
        @Size(max = 50, message = "产品编号长度不能超过50个字符")
        private String productCode;
        
        /**
         * 产品名称
         */
        @NotBlank(message = "产品名称不能为空")
        @Size(max = 255, message = "产品名称长度不能超过255个字符")
        private String productName;
        
        /**
         * 单价
         */
        @NotNull(message = "单价不能为空")
        @Positive(message = "单价必须大于0")
        private String unitPrice;
        
        /**
         * 数量
         */
        @NotNull(message = "数量不能为空")
        @Positive(message = "数量必须大于0")
        private Integer quantity;
        
        /**
         * 备注
         */
        @Size(max = 200, message = "备注长度不能超过200个字符")
        private String remarks;
    }

}