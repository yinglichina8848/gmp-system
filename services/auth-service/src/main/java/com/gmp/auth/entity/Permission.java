package com.gmp.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * GMP系统权限实体
 *
 * @author GMP系统开发团队
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sys_permissions")
@EntityListeners(AuditingEntityListener.class)
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "权限代码不能为空")
    @Size(min = 5, max = 100, message = "权限代码长度必须在5-100字符之间")
    @Pattern(regexp = "^PERMISSION_[A-Z_]+$", message = "权限代码必须以PERMISSION_开头并使用大写字母和下划线")
    @Column(name = "permission_code", unique = true, nullable = false)
    private String permissionCode;

    @NotBlank(message = "权限名称不能为空")
    @Size(max = 200, message = "权限名称长度不能超过200字符")
    @Column(name = "permission_name", nullable = false)
    private String permissionName;

    @NotBlank(message = "资源类型不能为空")
    @Pattern(regexp = "^(MENU|API|BUTTON|DATA)$", message = "资源类型必须是MENU、API、BUTTON或DATA")
    @Column(name = "resource_type", nullable = false)
    private String resourceType;

    @Size(max = 200, message = "资源URL长度不能超过200字符")
    @Column(name = "resource_url")
    private String resourceUrl;

    @Pattern(regexp = "^(GET|POST|PUT|DELETE|ALL)$", message = "HTTP方法必须是GET、POST、PUT、DELETE或ALL")
    @Column(name = "http_method")
    private String httpMethod = "ALL";

    @Size(max = 500, message = "权限描述长度不能超过500字符")
    private String description;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // 审计字段
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Version
    @Column(nullable = false)
    private Integer version = 1;

    /**
     * 检查权限是否有效
     */
    @Transient
    public boolean isValid() {
        return this.isActive && this.permissionCode != null && !this.permissionCode.trim().isEmpty();
    }

    /**
     * 检查是否匹配给定的请求
     */
    @Transient
    public boolean matches(String url, String method) {
        if (!this.isActive || this.resourceUrl == null) {
            return false;
        }

        // URL匹配 (简单的路径前缀匹配)
        boolean urlMatch = url != null && url.startsWith(this.resourceUrl.replace("/**", ""));

        // HTTP方法匹配
        boolean methodMatch = "ALL".equals(this.httpMethod) ||
                (method != null && this.httpMethod.equals(method));

        return urlMatch && methodMatch;
    }
}
