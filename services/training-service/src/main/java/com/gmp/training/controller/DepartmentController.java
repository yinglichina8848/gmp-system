package com.gmp.training.controller;

import com.gmp.training.entity.Department;
import com.gmp.training.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 部门控制器，提供部门管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController extends BaseController<Department, Long> {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 根据部门编码查找部门
     *
     * @param code 部门编码
     * @return 部门信息
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Department> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(departmentService.findByCode(code));
    }

    /**
     * 根据部门名称查找部门
     *
     * @param name 部门名称
     * @return 部门信息
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Department> findByName(@PathVariable String name) {
        return ResponseEntity.ok(departmentService.findByName(name));
    }

    /**
     * 检查部门编码是否存在
     *
     * @param code 部门编码
     * @return 是否存在
     */
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(departmentService.existsByCode(code));
    }

    /**
     * 检查部门名称是否存在
     *
     * @param name 部门名称
     * @return 是否存在
     */
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        return ResponseEntity.ok(departmentService.existsByName(name));
    }

    /**
     * 查找顶级部门
     *
     * @return 顶级部门列表
     */
    @GetMapping("/root")
    public ResponseEntity<List<Department>> findRootDepartments() {
        return ResponseEntity.ok(departmentService.findRootDepartments());
    }

    /**
     * 查找指定部门的所有子部门
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<Department>> findChildrenByParentId(@PathVariable Long parentId) {
        return ResponseEntity.ok(departmentService.findChildrenByParentId(parentId));
    }

    /**
     * 查找指定部门的所有父级部门
     *
     * @param id 部门ID
     * @return 父级部门列表
     */
    @GetMapping("/{id}/ancestors")
    public ResponseEntity<List<Department>> findAncestorsById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findAncestorsById(id));
    }

    /**
     * 查找GMP相关部门
     *
     * @param gmpRelated 是否与GMP相关
     * @param pageable 分页参数
     * @return 分页部门列表
     */
    @GetMapping("/gmp")
    public ResponseEntity<Page<Department>> findByGmpRelated(@RequestParam Boolean gmpRelated, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(departmentService.findByGmpRelated(gmpRelated, pageable));
    }

    /**
     * 查找活跃的部门
     *
     * @param pageable 分页参数
     * @return 分页部门列表
     */
    @GetMapping("/active")
    public ResponseEntity<Page<Department>> findActiveDepartments(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(departmentService.findActiveDepartments(pageable));
    }

    /**
     * 更新部门状态
     *
     * @param id 部门ID
     * @param active 是否激活
     * @return 更新后的部门
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Department> updateStatus(@PathVariable Long id, @RequestParam Boolean active) {
        return ResponseEntity.ok(departmentService.updateStatus(id, active));
    }

    /**
     * 更新部门父级
     *
     * @param id 部门ID
     * @param parentId 父部门ID
     * @return 更新后的部门
     */
    @PutMapping("/{id}/parent")
    public ResponseEntity<Department> updateParent(@PathVariable Long id, @RequestParam Long parentId) {
        return ResponseEntity.ok(departmentService.updateParent(id, parentId));
    }

    /**
     * 批量禁用部门
     *
     * @param ids 部门ID列表
     * @return 影响的行数
     */
    @PutMapping("/batch/disable")
    public ResponseEntity<Integer> batchDisable(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(departmentService.batchDisable(ids));
    }

    /**
     * 批量启用部门
     *
     * @param ids 部门ID列表
     * @return 影响的行数
     */
    @PutMapping("/batch/enable")
    public ResponseEntity<Integer> batchEnable(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(departmentService.batchEnable(ids));
    }

    /**
     * 查找部门及其所有子部门
     *
     * @param id 部门ID
     * @return 部门及其子部门列表
     */
    @GetMapping("/{id}/with-children")
    public ResponseEntity<List<Department>> findDepartmentWithChildren(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.findDepartmentWithChildren(id));
    }

    /**
     * 搜索部门
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 分页部门列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Department>> search(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(departmentService.search(keyword, pageable));
    }
}