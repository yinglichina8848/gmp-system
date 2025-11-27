package com.gmp.training.controller;

import com.gmp.training.entity.TrainingAssessment;
import com.gmp.training.service.TrainingAssessmentService;
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
 * 培训考核控制器，提供培训考核管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/training-assessments")
public class TrainingAssessmentController extends BaseController<TrainingAssessment, Long> {

    @Autowired
    private TrainingAssessmentService trainingAssessmentService;

    /**
     * 根据培训课程查找培训考核
     *
     * @param courseId 培训课程ID
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<TrainingAssessment>> findByCourseId(
            @PathVariable Long courseId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findByCourseId(courseId, pageable));
    }

    /**
     * 根据用户ID查找培训考核
     *
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TrainingAssessment>> findByUserId(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findByUserId(userId, pageable));
    }

    /**
     * 根据考核状态查找培训考核
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TrainingAssessment>> findByStatus(
            @PathVariable TrainingAssessment.Status status,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findByStatus(status, pageable));
    }

    /**
     * 根据考核类型查找培训考核
     *
     * @param type     类型
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<TrainingAssessment>> findByType(
            @PathVariable TrainingAssessment.Type type,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findByType(type, pageable));
    }

    /**
     * 根据日期范围查找培训考核
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageable  分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<TrainingAssessment>> findByAssessmentDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findByAssessmentDateBetween(startDate, endDate, pageable));
    }

    /**
     * 查找未开始的考核
     *
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/not-started")
    public ResponseEntity<Page<TrainingAssessment>> findNotStartedAssessments(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findNotStartedAssessments(pageable));
    }

    /**
     * 查找进行中的考核
     *
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/in-progress")
    public ResponseEntity<Page<TrainingAssessment>> findInProgressAssessments(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findInProgressAssessments(pageable));
    }

    /**
     * 查找已完成的考核
     *
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/completed")
    public ResponseEntity<Page<TrainingAssessment>> findCompletedAssessments(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findCompletedAssessments(pageable));
    }

    /**
     * 更新培训考核状态
     *
     * @param id     考核ID
     * @param status 状态
     * @return 更新后的培训考核
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TrainingAssessment> updateStatus(@PathVariable Long id,
            @RequestParam TrainingAssessment.Status status) {
        return ResponseEntity.ok(trainingAssessmentService.updateStatus(id, status));
    }

    /**
     * 批量更新培训考核状态
     *
     * @param ids    考核ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Integer> batchUpdateStatus(@RequestBody List<Long> ids,
            @RequestParam TrainingAssessment.Status status) {
        return ResponseEntity.ok(trainingAssessmentService.batchUpdateStatus(ids, status));
    }

    /**
     * 搜索培训考核
     *
     * @param keyword  关键词
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TrainingAssessment>> search(@RequestParam String keyword,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.search(keyword, pageable));
    }

    /**
     * 获取课程的平均考核分数
     *
     * @param courseId 课程ID
     * @return 平均分数
     */
    @GetMapping("/course/{courseId}/average-score")
    public ResponseEntity<Double> getCourseAverageScore(@PathVariable Long courseId) {
        return ResponseEntity.ok(trainingAssessmentService.getCourseAverageScore(courseId));
    }

    /**
     * 查找通过考核的记录
     *
     * @param courseId 课程ID
     * @param pageable 分页参数
     * @return 分页培训考核列表
     */
    @GetMapping("/passed/{courseId}")
    public ResponseEntity<Page<TrainingAssessment>> findPassedAssessments(
            @PathVariable Long courseId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingAssessmentService.findPassedAssessments(courseId, pageable));
    }
}