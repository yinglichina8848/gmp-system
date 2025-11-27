package com.gmp.training.repository;

import com.gmp.training.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查找用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据员工编号查找用户
     * 
     * @param employeeNumber 员工编号
     * @return 用户信息
     */
    Optional<User> findByEmployeeNumber(String employeeNumber);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查员工编号是否存在
     * 
     * @param employeeNumber 员工编号
     * @return 是否存在
     */
    boolean existsByEmployeeNumber(String employeeNumber);

    /**
     * 查找部门中的用户
     * 
     * @param departmentId 部门ID
     * @return 用户列表
     */
    java.util.List<User> findByDepartmentId(Long departmentId);

    /**
     * 查找GMP相关人员
     * 
     * @param gmpPersonnel 是否为GMP相关人员
     * @return 用户列表
     */
    java.util.List<User> findByGmpPersonnel(boolean gmpPersonnel);
}
