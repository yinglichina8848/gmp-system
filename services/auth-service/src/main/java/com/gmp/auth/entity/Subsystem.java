package com.gmp.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 子系统实体类
 * 
 * @author GMP
 */
@Entity
@Table(name = "sys_subsystems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subsystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    /**
     * 子系统编码
     */
    private String subsystemCode;

    /**
     * 子系统名称
     */
    private String subsystemName;

    /**
     * 子系统描述
     */
    private String description;

    /**
     * 子系统图标路径
     */
    private String iconPath;

    /**
     * 子系统排序号
     */
    private Integer sortOrder;

    /**
     * 子系统状态：true-启用，false-禁用
     */
    private Boolean enabled = Boolean.TRUE;

    // 添加必要的setter和getter方法
    public void setSubsystemCode(String subsystemCode) {
        this.subsystemCode = subsystemCode;
    }
    
    public void setSubsystemName(String subsystemName) {
        this.subsystemName = subsystemName;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public Long getId() {
        return id;
    }
    
    public String getSubsystemCode() {
        return subsystemCode;
    }

    public String getSubsystemName() {
        return subsystemName;
    }

    /**
     * 是否为GMP关键子系统
     */
    private Boolean gmpCritical = Boolean.FALSE;

    // 审计字段
    private String createdBy;
    private LocalDateTime createdTime;
    private String updatedBy;
    private LocalDateTime updatedTime;

    /**
     * 验证子系统是否有效
     * @return 是否有效
     */
    public boolean isValid() {
        return enabled != null && enabled && 
               subsystemCode != null && !subsystemCode.isEmpty() &&
               subsystemName != null && !subsystemName.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subsystem subsystem = (Subsystem) o;
        return Objects.equals(id, subsystem.id) && 
               Objects.equals(subsystemCode, subsystem.subsystemCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subsystemCode);
    }
}