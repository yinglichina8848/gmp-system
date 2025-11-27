package com.gmp.training.controller;

import com.gmp.training.entity.TrainingNeed;
import com.gmp.training.service.TrainingNeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 培训需求控制器，提供培训需求管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/training-needs")
public class TrainingNeedController extends BaseController<TrainingNeed, Long> {

    @Autowired
    private TrainingNeedService trainingNeedService;

    /**
     * 根据需求名称查找培训需求
     *
     * @param name 需求名称
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Page<TrainingNeed>> findByName(@PathVariable String name, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findByName(name, pageable));
    }

    /**
     * 根据部门ID查找培训需求
     *
     * @param departmentId 部门ID
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<TrainingNeed>> findByDepartmentId(@PathVariable Long departmentId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findByDepartmentId(departmentId, pageable));
    }

    /**
     * 根据岗位ID查找培训需求
     *
     * @param positionId 岗位ID
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/position/{positionId}")
    public ResponseEntity<Page<TrainingNeed>> findByPositionId(@PathVariable Long positionId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findByPositionId(positionId, pageable));
    }

    /**
     * 根据需求状态查找培训需求
     *
     * @param status 需求状态
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TrainingNeed>> findByStatus(@PathVariable TrainingNeed.Status status, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findByStatus(status, pageable));
    }

    /**
     * 根据创建者查找培训需求
     *
     * @param creatorId 创建者ID
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<Page<TrainingNeed>> findByCreatorId(@PathVariable Long creatorId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findByCreatorId(creatorId, pageable));
    }

    /**
     * 根据时间范围查找培训需求
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<TrainingNeed>> findByCreatedDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findByCreatedDateBetween(startDate, endDate, pageable));
    }

    /**
     * 根据优先级查找培训需求
     *
     * @param priority 优先级
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<Page<TrainingNeed>> findByPriority(@PathVariable TrainingNeed.Priority priority, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findByPriority(priority, pageable));
    }

    /**
     * 查找已批准的培训需求
     *
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/approved")
    public ResponseEntity<Page<TrainingNeed>> findApprovedNeeds(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findApprovedNeeds(pageable));
    }

    /**
     * 查找未处理的培训需求
     *
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/pending")
    public ResponseEntity<Page<TrainingNeed>> findPendingNeeds(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.findPendingNeeds(pageable));
    }

    /**
     * 更新培训需求状态
     *
     * @param id 需求ID
     * @param status 状态
     * @return 更新后的培训需求
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TrainingNeed> updateStatus(@PathVariable Long id, @RequestParam TrainingNeed.Status status) {
        return ResponseEntity.ok(trainingNeedService.updateStatus(id, status));
    }

    /**
     * 批量更新培训需求状态
     *
     * @param ids 需求ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Integer> batchUpdateStatus(@RequestBody List<Long> ids, @RequestParam TrainingNeed.Status status) {
        return ResponseEntity.ok(trainingNeedService.batchUpdateStatus(ids, status));
    }

    /**
     * 搜索培训需求
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 分页培训需求列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TrainingNeed>> search(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingNeedService.search(keyword, pageable));
    }
}