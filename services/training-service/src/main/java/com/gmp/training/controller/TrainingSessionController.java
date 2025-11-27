package com.gmp.training.controller;

import com.gmp.training.entity.TrainingSession;
import com.gmp.training.service.TrainingSessionService;
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
 * 培训场次控制器，提供培训场次管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/training-sessions")
public class TrainingSessionController extends BaseController<TrainingSession, Long> {

    @Autowired
    private TrainingSessionService trainingSessionService;

    /**
     * 根据培训课程查找培训场次
     *
     * @param courseId 培训课程ID
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<TrainingSession>> findByCourseId(
            @PathVariable Long courseId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findByCourseId(courseId, pageable));
    }

    /**
     * 根据讲师ID查找培训场次
     *
     * @param lecturerId 讲师ID
     * @param pageable   分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<Page<TrainingSession>> findByLecturerId(
            @PathVariable Long lecturerId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findByLecturerId(lecturerId, pageable));
    }

    /**
     * 根据培训场次状态查找
     *
     * @param status   状态
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TrainingSession>> findByStatus(
            @PathVariable TrainingSession.Status status,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findByStatus(status, pageable));
    }

    /**
     * 根据培训方式查找培训场次
     *
     * @param method   培训方式
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/method/{method}")
    public ResponseEntity<Page<TrainingSession>> findByMethod(
            @PathVariable TrainingSession.Method method,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findByMethod(method, pageable));
    }

    /**
     * 根据培训场地查找培训场次
     *
     * @param venue    培训场地
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/venue/{venue}")
    public ResponseEntity<Page<TrainingSession>> findByVenue(
            @PathVariable String venue,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findByVenue(venue, pageable));
    }

    /**
     * 根据日期范围查找培训场次
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageable  分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/date-range")
    public ResponseEntity<Page<TrainingSession>> findBySessionDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findBySessionDateBetween(startDate, endDate, pageable));
    }

    /**
     * 查找未来的培训场次
     *
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/future")
    public ResponseEntity<Page<TrainingSession>> findFutureSessions(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findFutureSessions(pageable));
    }

    /**
     * 查找已完成的培训场次
     *
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/completed")
    public ResponseEntity<Page<TrainingSession>> findCompletedSessions(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findCompletedSessions(pageable));
    }

    /**
     * 查找进行中的培训场次
     *
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/ongoing")
    public ResponseEntity<Page<TrainingSession>> findOngoingSessions(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.findOngoingSessions(pageable));
    }

    /**
     * 更新培训场次状态
     *
     * @param id     场次ID
     * @param status 状态
     * @return 更新后的培训场次
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TrainingSession> updateStatus(@PathVariable Long id,
            @RequestParam TrainingSession.Status status) {
        return ResponseEntity.ok(trainingSessionService.updateStatus(id, status));
    }

    /**
     * 批量更新培训场次状态
     *
     * @param ids    场次ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Integer> batchUpdateStatus(@RequestBody List<Long> ids,
            @RequestParam TrainingSession.Status status) {
        return ResponseEntity.ok(trainingSessionService.batchUpdateStatus(ids, status));
    }

    /**
     * 搜索培训场次
     *
     * @param keyword  关键词
     * @param pageable 分页参数
     * @return 分页培训场次列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TrainingSession>> search(@RequestParam String keyword,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingSessionService.search(keyword, pageable));
    }

    /**
     * 获取培训场次的实际参训人数
     *
     * @param id 场次ID
     * @return 实际参训人数
     */
    @GetMapping("/{id}/actual-attendance")
    public ResponseEntity<Integer> getActualAttendance(@PathVariable Long id) {
        return ResponseEntity.ok(trainingSessionService.getActualAttendance(id));
    }

    /**
     * 获取培训场次的签到率
     *
     * @param id 场次ID
     * @return 签到率（百分比）
     */
    @GetMapping("/{id}/attendance-rate")
    public ResponseEntity<Double> getAttendanceRate(@PathVariable Long id) {
        return ResponseEntity.ok(trainingSessionService.getAttendanceRate(id));
    }
}