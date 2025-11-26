package com.gmp.auth.dto;

import lombok.Data;

/**
 * 分配角色的DTO类
 */
@Data
public class AssignRoleRequest {
    private String username;
    private String organizationCode;
    private String roleCode;
}