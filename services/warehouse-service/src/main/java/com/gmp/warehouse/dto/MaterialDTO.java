package com.gmp.warehouse.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物料DTO类
 * <p>
 * 用于物料信息的数据传输对象。
 * </p>
 *
 * @author GMP系统开发团队
 * @version 1.0.0
 * @since 2024-01-01
 */
@Data
public class MaterialDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 物料ID
     */
    private Long id;

    /**
     * 物料编码
     */
    private String code;

    /**
     * 物料名称
     */
    private String name;

    /**
     * 物料描述
     */
    private String description;

    /**
     * 物料分类ID
     */
    private Long categoryId;

    /**
     * 物料分类名称
     */
    private String categoryName;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 安全库存
     */
    private BigDecimal safetyStock;

    /**
     * 预警库存
     */
    private BigDecimal warningStock;

    /**
     * 批次管理标志
     */
    private Boolean batchManaged;

    /**
     * 有效期管理标志
     */
    private Boolean expiryManaged;

    /**
     * 存储条件
     */
    private String storageCondition;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：0-停用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 更新人
     */
    private String updatedBy;

}