package com.gmp.training.controller;

import com.gmp.training.entity.Position;
import com.gmp.training.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 岗位控制器，提供岗位管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/positions")
public class PositionController extends BaseController<Position, Long> {

    @Autowired
    private PositionService positionService;

    /**
     * 根据岗位编码查找岗位
     *
     * @param code 岗位编码
     * @return 岗位信息
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<Position> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(positionService.findByCode(code));
    }

    /**
     * 根据岗位名称查找岗位
     *
     * @param name 岗位名称
     * @return 岗位信息
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Position> findByName(@PathVariable String name) {
        return ResponseEntity.ok(positionService.findByName(name));
    }

    /**
     * 检查岗位编码是否存在
     *
     * @param code 岗位编码
     * @return 是否存在
     */
    @GetMapping("/exists/code/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        return ResponseEntity.ok(positionService.existsByCode(code));
    }

    /**
     * 检查岗位名称是否存在
     *
     * @param name 岗位名称
     * @return 是否存在
     */
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> existsByName(@PathVariable String name) {
        return ResponseEntity.ok(positionService.existsByName(name));
    }

    /**
     * 根据部门ID查找岗位
     *
     * @param departmentId 部门ID
     * @return 岗位列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Position>> findByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(positionService.findByDepartmentId(departmentId));
    }

    /**
     * 根据部门ID分页查找岗位
     *
     * @param departmentId 部门ID
     * @param pageable 分页参数
     * @return 分页岗位列表
     */
    @GetMapping("/department/{departmentId}/page")
    public ResponseEntity<Page<Position>> findByDepartmentIdWithPage(@PathVariable Long departmentId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(positionService.findByDepartmentId(departmentId, pageable));
    }

    /**
     * 查找GMP相关岗位
     *
     * @param gmpRequired 是否需要GMP资质
     * @param pageable 分页参数
     * @return 分页岗位列表
     */
    @GetMapping("/gmp")
    public ResponseEntity<Page<Position>> findByGmpRequired(@RequestParam Boolean gmpRequired, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(positionService.findByGmpRequired(gmpRequired, pageable));
    }

    /**
     * 更新岗位状态
     *
     * @param id 岗位ID
     * @param active 是否激活
     * @return 更新后的岗位
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Position> updateStatus(@PathVariable Long id, @RequestParam Boolean active) {
        return ResponseEntity.ok(positionService.updateStatus(id, active));
    }

    /**
     * 更新岗位部门
     *
     * @param id 岗位ID
     * @param departmentId 部门ID
     * @return 更新后的岗位
     */
    @PutMapping("/{id}/department")
    public ResponseEntity<Position> updateDepartment(@PathVariable Long id, @RequestParam Long departmentId) {
        return ResponseEntity.ok(positionService.updateDepartment(id, departmentId));
    }

    /**
     * 批量禁用岗位
     *
     * @param ids 岗位ID列表
     * @return 影响的行数
     */
    @PutMapping("/batch/disable")
    public ResponseEntity<Integer> batchDisable(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(positionService.batchDisable(ids));
    }

    /**
     * 批量启用岗位
     *
     * @param ids 岗位ID列表
     * @return 影响的行数
     */
    @PutMapping("/batch/enable")
    public ResponseEntity<Integer> batchEnable(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(positionService.batchEnable(ids));
    }

    /**
     * 根据培训类型查找相关岗位
     *
     * @param trainingTypeId 培训类型ID
     * @return 岗位列表
     */
    @GetMapping("/training-type/{trainingTypeId}")
    public ResponseEntity<List<Position>> findByTrainingTypeId(@PathVariable Long trainingTypeId) {
        return ResponseEntity.ok(positionService.findByTrainingTypeId(trainingTypeId));
    }

    /**
     * 搜索岗位
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 分页岗位列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Position>> search(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(positionService.search(keyword, pageable));
    }
}