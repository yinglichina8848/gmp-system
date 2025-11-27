package com.gmp.training.service;

import com.gmp.training.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 * 
 * @author GMP系统开发团队
 */
public interface UserService extends BaseService<User, Long> {

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
    List<User> findByDepartmentId(Long departmentId);

    /**
     * 查找GMP相关人员
     * 
     * @param gmpPersonnel 是否为GMP相关人员
     * @return 用户列表
     */
    List<User> findByGmpPersonnel(boolean gmpPersonnel);

    /**
     * 根据岗位查找用户
     * 
     * @param positionId 岗位ID
     * @return 用户列表
     */
    List<User> findByPositionId(Long positionId);

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 更新后的用户
     */
    User updateStatus(Long id, User.Status status);

    /**
     * 重置用户密码
     * 
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 更新后的用户
     */
    User resetPassword(Long id, String newPassword);

    /**
     * 更新用户部门
     * 
     * @param id 用户ID
     * @param departmentId 部门ID
     * @return 更新后的用户
     */
    User updateDepartment(Long id, Long departmentId);

    /**
     * 更新用户岗位
     * 
     * @param id 用户ID
     * @param positionId 岗位ID
     * @return 更新后的用户
     */
    User updatePosition(Long id, Long positionId);

    /**
     * 批量禁用用户
     * 
     * @param ids 用户ID列表
     * @return 影响的行数
     */
    int batchDisable(List<Long> ids);

    /**
     * 批量启用用户
     * 
     * @param ids 用户ID列表
     * @return 影响的行数
     */
    int batchEnable(List<Long> ids);
}
