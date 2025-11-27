package com.gmp.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 * 
 * @author GMP系统开发团队
 */
@Data
@Entity
@Table(name = "t_user")
@EntityListeners(AuditingEntityListener.class)
public class User {

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 密码重置标志
     */
    @Column(name = "password_reset", nullable = false)
    private boolean passwordReset = false;

    /**
     * 员工工号
     */
    @Column(name = "employee_number", nullable = false, unique = true, length = 20)
    private String employeeNumber;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;

    /**
     * 邮箱
     */
    @Column(name = "email", unique = true, length = 100)
    private String email;

    /**
     * 手机号码
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * 所属部门ID
     */
    @Column(name = "department_id")
    private Long departmentId;

    /**
     * 所属部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    /**
     * 职位ID
     */
    @Column(name = "position_id")
    private Long positionId;

    /**
     * 职位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    private Position position;

    /**
     * 用户状态（ACTIVE/INACTIVE/DELETED）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    /**
     * 用户角色关联
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    /**
     * 创建者
     */
    @CreatedBy
    @Column(name = "created_by", length = 50)
    private String createdBy;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private LocalDateTime updatedTime;

    /**
     * 更新者
     */
    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    /**
     * 获取职位信息
     * 
     * @return 职位信息
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * 设置用户状态
     * 
     * @param status 用户状态
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * 设置密码
     * 
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 设置密码重置标志
     * 
     * @param passwordReset 密码重置标志
     */
    public void setPasswordReset(boolean passwordReset) {
        this.passwordReset = passwordReset;
    }

    /**
     * 凭证是否过期
     */
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;

    /**
     * 设置凭证是否过期
     * 
     * @param credentialsNonExpired 凭证是否过期
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * 设置部门
     * 
     * @param department 部门对象
     */
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     * 设置职位
     * 
     * @param position 职位对象
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * 获取用户ID
     * 
     * @return 用户ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 获取用户名
     * 
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取邮箱
     * 
     * @return 邮箱
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * 获取员工编号
     * 
     * @return 员工编号
     */
    public String getEmployeeNumber() {
        return employeeNumber;
    }
    
    /**
     * 获取密码
     * 
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 用户状态枚举
     */
    public enum Status {
        ACTIVE, INACTIVE, DELETED
    }
}
