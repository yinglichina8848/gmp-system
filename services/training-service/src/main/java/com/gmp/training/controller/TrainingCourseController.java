package com.gmp.training.controller;

import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.service.TrainingCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 培训课程控制器，提供培训课程管理相关的API接口
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/training-courses")
public class TrainingCourseController extends BaseController<TrainingCourse, Long> {

    @Autowired
    private TrainingCourseService trainingCourseService;

    /**
     * 根据课程编码查找培训课程
     *
     * @param code 课程编码
     * @return 培训课程
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<TrainingCourse> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(trainingCourseService.findByCode(code));
    }

    /**
     * 根据课程名称查找培训课程
     *
     * @param name 课程名称
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Page<TrainingCourse>> findByName(@PathVariable String name, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.findByName(name, pageable));
    }

    /**
     * 根据培训计划查找培训课程
     *
     * @param planId 培训计划ID
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/plan/{planId}")
    public ResponseEntity<Page<TrainingCourse>> findByPlanId(@PathVariable Long planId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.findByPlanId(planId, pageable));
    }

    /**
     * 根据培训课程状态查找
     *
     * @param status 状态
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TrainingCourse>> findByStatus(
            @PathVariable TrainingCourse.Status status, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.findByStatus(status, pageable));
    }

    /**
     * 根据课程类型查找培训课程
     *
     * @param type 课程类型
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<TrainingCourse>> findByType(
            @PathVariable TrainingCourse.Type type, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.findByType(type, pageable));
    }

    /**
     * 根据讲师ID查找培训课程
     *
     * @param lecturerId 讲师ID
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/lecturer/{lecturerId}")
    public ResponseEntity<Page<TrainingCourse>> findByLecturerId(
            @PathVariable Long lecturerId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.findByLecturerId(lecturerId, pageable));
    }

    /**
     * 查找必修课程
     *
     * @param required 是否必修
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/required/{required}")
    public ResponseEntity<Page<TrainingCourse>> findByRequired(
            @PathVariable Boolean required, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.findByRequired(required, pageable));
    }

    /**
     * 根据部门ID查找相关培训课程
     *
     * @param departmentId 部门ID
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<TrainingCourse>> findByDepartmentId(
            @PathVariable Long departmentId, 
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.findByDepartmentId(departmentId, pageable));
    }

    /**
     * 更新培训课程状态
     *
     * @param id 课程ID
     * @param status 状态
     * @return 更新后的培训课程
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TrainingCourse> updateStatus(@PathVariable Long id, @RequestParam TrainingCourse.Status status) {
        return ResponseEntity.ok(trainingCourseService.updateStatus(id, status));
    }

    /**
     * 批量更新培训课程状态
     *
     * @param ids 课程ID列表
     * @param status 状态
     * @return 影响的行数
     */
    @PutMapping("/batch/status")
    public ResponseEntity<Integer> batchUpdateStatus(@RequestBody List<Long> ids, @RequestParam TrainingCourse.Status status) {
        return ResponseEntity.ok(trainingCourseService.batchUpdateStatus(ids, status));
    }

    /**
     * 搜索培训课程
     *
     * @param keyword 关键词
     * @param pageable 分页参数
     * @return 分页培训课程列表
     */
    @GetMapping("/search")
    public ResponseEntity<Page<TrainingCourse>> search(@RequestParam String keyword, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(trainingCourseService.search(keyword, pageable));
    }
}