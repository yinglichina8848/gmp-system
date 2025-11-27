package com.gmp.training.controller;

import com.gmp.training.entity.TrainingPlan;
import com.gmp.training.service.TrainingPlanService;
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
 * 培训计划控制器，提供培训计划管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanController extends BaseController<TrainingPlan, Long> {

    @Autowired
    private TrainingPlanService trainingPlanService;

    /**
     * 根据计划名称查找培训计划
     *
     * @param name 计划名称
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Page<TrainingPlan>> findByName(@PathVariable String name, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByName(name, pageable));
    }

    /**
     * 根据年度查找培训计划
     *
     * @param year 年度
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/year/{year}")
    public ResponseEntity<Page<TrainingPlan>> findByYear(@PathVariable Integer year, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByYear(year, pageable));
    }

    /**
     * 根据季度查找培训计划
     *
     * @param year 年度
     * @param quarter 季度
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/quarter")
    public ResponseEntity<Page<TrainingPlan>> findByYearAndQuarter(
            @RequestParam Integer year, 
            @RequestParam Integer quarter, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByYearAndQuarter(year, quarter, pageable));
    }

    /**
     * 根据负责人查找培训计划
     *
     * @param responsibleUserId 负责人ID
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/responsible/{responsibleUserId}")
    public ResponseEntity<Page<TrainingPlan>> findByResponsibleUserId(
            @PathVariable Long responsibleUserId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByResponsibleUserId(responsibleUserId, pageable));
    }

    /**
     * 根据培训计划状态查找
     *
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TrainingPlan>> findByStatus(
            @PathVariable TrainingPlan.Status status, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByStatus(status, pageable));
    }

    /**
     * 根据培训需求查找培训计划
     *
     * @param needId 培训需求ID
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/need/{needId}")
    public ResponseEntity<Page<TrainingPlan>> findByNeedId(@PathVariable Long needId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByNeedId(needId, pageable));
    }

    /**
     * 根据时间范围查找培训计划
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<TrainingPlan>> findByPlanDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByPlanDateBetween(startDate, endDate, pageable));
    }

    /**
     * 根据部门查找培训计划
     *
     * @param departmentId 部门ID
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<TrainingPlan>> findByDepartmentId(
            @PathVariable Long departmentId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.findByDepartmentId(departmentId, pageable));
    }

    /**
     * 更新培训计划状态
     *
     * @param id 计划ID
     * @param status 状态
     * @return 更新后的培训计划
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TrainingPlan> updateStatus(@PathVariable Long id, @RequestParam TrainingPlan.Status status) {
        return ResponseEntity.ok(trainingPlanService.updateStatus(id, status));
    }

    /**
     * 批量更新培训计划状态
     *
     * @param ids 计划ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Integer> batchUpdateStatus(@RequestBody List<Long> ids, @RequestParam TrainingPlan.Status status) {
        return ResponseEntity.ok(trainingPlanService.batchUpdateStatus(ids, status));
    }

    /**
     * 搜索培训计划
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 分页培训计划列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TrainingPlan>> search(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingPlanService.search(keyword, pageable));
    }

    /**
     * 计算培训计划完成率
     *
     * @param id 计划ID
     * @return 完成率（百分比）
     */
    @GetMapping("/{id}/completion-rate")
    public ResponseEntity<Double> calculateCompletionRate(@PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanService.calculateCompletionRate(id));
    }

    /**
     * 获取培训计划实际花费
     *
     * @param id 计划ID
     * @return 实际花费金额
     */
    @GetMapping("/{id}/actual-cost")
    public ResponseEntity<Double> getActualCost(@PathVariable Long id) {
        return ResponseEntity.ok(trainingPlanService.getActualCost(id));
    }
}