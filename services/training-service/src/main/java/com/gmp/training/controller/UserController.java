package com.gmp.training.controller;

import com.gmp.training.entity.User;
import com.gmp.training.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 用户控制器，提供用户管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<User, Long> {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    /**
     * 根据员工编号查找用户
     *
     * @param employeeId 员工编号
     * @return 用户信息
     */
    @GetMapping("/employeeId/{employeeId}")
    public ResponseEntity<User> findByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(userService.findByEmployeeId(employeeId));
    }

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.existsByUsername(username));
    }

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    /**
     * 检查员工编号是否存在
     *
     * @param employeeId 员工编号
     * @return 是否存在
     */
    @GetMapping("/exists/employeeId/{employeeId}")
    public ResponseEntity<Boolean> existsByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(userService.existsByEmployeeId(employeeId));
    }

    /**
     * 根据部门ID查找用户
     *
     * @param departmentId 部门ID
     * @param pageable 分页参数
     * @return 分页用户列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<User>> findByDepartmentId(@PathVariable Long departmentId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.findByDepartmentId(departmentId, pageable));
    }

    /**
     * 根据岗位ID查找用户
     *
     * @param positionId 岗位ID
     * @param pageable 分页参数
     * @return 分页用户列表
     */
    @GetMapping("/position/{positionId}")
    public ResponseEntity<Page<User>> findByPositionId(@PathVariable Long positionId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.findByPositionId(positionId, pageable));
    }

    /**
     * 查找GMP相关人员
     *
     * @param gmpResponsible GMP责任人标识
     * @param pageable 分页参数
     * @return 分页用户列表
     */
    @GetMapping("/gmp")
    public ResponseEntity<Page<User>> findByGmpResponsible(@RequestParam Boolean gmpResponsible, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.findByGmpResponsible(gmpResponsible, pageable));
    }

    /**
     * 更新用户状态
     *
     * @param id 用户ID
     * @param enabled 是否启用
     * @return 更新后的用户
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<User> updateStatus(@PathVariable Long id, @RequestParam Boolean enabled) {
        return ResponseEntity.ok(userService.updateStatus(id, enabled));
    }

    /**
     * 重置用户密码
     *
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 更新后的用户
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<User> resetPassword(@PathVariable Long id, @RequestParam String newPassword) {
        return ResponseEntity.ok(userService.updatePassword(id, newPassword));
    }

    /**
     * 更新用户部门
     *
     * @param id 用户ID
     * @param departmentId 部门ID
     * @return 更新后的用户
     */
    @PutMapping("/{id}/department")
    public ResponseEntity<User> updateDepartment(@PathVariable Long id, @RequestParam Long departmentId) {
        return ResponseEntity.ok(userService.updateDepartment(id, departmentId));
    }

    /**
     * 更新用户岗位
     *
     * @param id 用户ID
     * @param positionId 岗位ID
     * @return 更新后的用户
     */
    @PutMapping("/{id}/position")
    public ResponseEntity<User> updatePosition(@PathVariable Long id, @RequestParam Long positionId) {
        return ResponseEntity.ok(userService.updatePosition(id, positionId));
    }

    /**
     * 批量禁用用户
     *
     * @param ids 用户ID列表
     * @return 影响的行数
     */
    @PutMapping("/batch/disable")
    public ResponseEntity<Integer> batchDisable(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(userService.batchDisable(ids));
    }

    /**
     * 批量启用用户
     *
     * @param ids 用户ID列表
     * @return 影响的行数
     */
    @PutMapping("/batch/enable")
    public ResponseEntity<Integer> batchEnable(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(userService.batchEnable(ids));
    }

    /**
     * 搜索用户
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 分页用户列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<User>> search(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.search(keyword, pageable));
    }
}