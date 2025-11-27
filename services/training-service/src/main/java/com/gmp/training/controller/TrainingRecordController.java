package com.gmp.training.controller;

import com.gmp.training.entity.TrainingRecord;
import com.gmp.training.service.TrainingRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 培训记录控制器，提供培训记录管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/training-records")
public class TrainingRecordController extends BaseController<TrainingRecord, Long> {

    @Autowired
    private TrainingRecordService trainingRecordService;

    /**
     * 根据用户ID查找培训记录
     *
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TrainingRecord>> findByUserId(
            @PathVariable Long userId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingRecordService.findByUserId(userId, pageable));
    }

    /**
     * 根据培训场次查找培训记录
     *
     * @param sessionId 培训场次ID
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Page<TrainingRecord>> findBySessionId(
            @PathVariable Long sessionId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingRecordService.findBySessionId(sessionId, pageable));
    }

    /**
     * 根据培训课程查找培训记录
     *
     * @param courseId 培训课程ID
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<TrainingRecord>> findByCourseId(
            @PathVariable Long courseId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingRecordService.findByCourseId(courseId, pageable));
    }

    /**
     * 根据培训记录状态查找
     *
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TrainingRecord>> findByStatus(
            @PathVariable TrainingRecord.Status status, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingRecordService.findByStatus(status, pageable));
    }

    /**
     * 根据部门ID查找培训记录
     *
     * @param departmentId 部门ID
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<TrainingRecord>> findByDepartmentId(
            @PathVariable Long departmentId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingRecordService.findByDepartmentId(departmentId, pageable));
    }

    /**
     * 根据岗位ID查找培训记录
     *
     * @param positionId 岗位ID
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    @GetMapping("/position/{positionId}")
    public ResponseEntity<Page<TrainingRecord>> findByPositionId(
            @PathVariable Long positionId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingRecordService.findByPositionId(positionId, pageable));
    }

    /**
     * 更新培训记录状态
     *
     * @param id 记录ID
     * @param status 状态
     * @return 更新后的培训记录
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TrainingRecord> updateStatus(@PathVariable Long id, @RequestParam TrainingRecord.Status status) {
        return ResponseEntity.ok(trainingRecordService.updateStatus(id, status));
    }

    /**
     * 批量更新培训记录状态
     *
     * @param ids 记录ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Integer> batchUpdateStatus(@RequestBody List<Long> ids, @RequestParam TrainingRecord.Status status) {
        return ResponseEntity.ok(trainingRecordService.batchUpdateStatus(ids, status));
    }

    /**
     * 搜索培训记录
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 分页培训记录列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TrainingRecord>> search(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingRecordService.search(keyword, pageable));
    }

    /**
     * 获取用户的培训完成率
     *
     * @param userId 用户ID
     * @return 完成率（百分比）
     */
    @GetMapping("/user/{userId}/completion-rate")
    public ResponseEntity<Double> getUserCompletionRate(@PathVariable Long userId) {
        return ResponseEntity.ok(trainingRecordService.getUserCompletionRate(userId));
    }

    /**
     * 获取课程的培训完成率
     *
     * @param courseId 课程ID
     * @return 完成率（百分比）
     */
    @GetMapping("/course/{courseId}/completion-rate")
    public ResponseEntity<Double> getCourseCompletionRate(@PathVariable Long courseId) {
        return ResponseEntity.ok(trainingRecordService.getCourseCompletionRate(courseId));
    }
}