package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingCourse;
import com.gmp.training.repository.TrainingCourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 培训课程服务实现的单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TrainingCourseServiceImplTest {

    @Mock
    private TrainingCourseRepository trainingCourseRepository;

    @InjectMocks
    private TrainingCourseServiceImpl trainingCourseService;

    private TrainingCourse trainingCourse;

    @BeforeEach
    void setUp() {
        trainingCourse = new TrainingCourse();
        trainingCourse.setId(1L);
        trainingCourse.setCode("GMP-2024-001");
        trainingCourse.setName("GMP质量管理规范培训");
        trainingCourse.setPlanId(1L);
        trainingCourse.setCourseType(TrainingCourse.CourseType.INTERNAL);
        trainingCourse.setDurationHours(4);
        trainingCourse.setRequiredHours(4);
        trainingCourse.setStatus(TrainingCourse.Status.DRAFT);
        trainingCourse.setStartDate(LocalDate.now());
        trainingCourse.setEndDate(LocalDate.now().plusDays(7));
        trainingCourse.setDescription("GMP质量管理规范的基础培训");
        trainingCourse.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testSave() {
        when(trainingCourseRepository.save(any(TrainingCourse.class))).thenReturn(trainingCourse);

        TrainingCourse savedCourse = trainingCourseService.save(trainingCourse);

        assertNotNull(savedCourse);
        assertEquals("GMP质量管理规范培训", savedCourse.getName());
        assertEquals(TrainingCourse.Status.DRAFT, savedCourse.getStatus());
        verify(trainingCourseRepository, times(1)).save(trainingCourse);
    }

    @Test
    void testFindById() {
        when(trainingCourseRepository.findById(1L)).thenReturn(Optional.of(trainingCourse));

        TrainingCourse foundCourse = trainingCourseService.findById(1L);

        assertNotNull(foundCourse);
        assertEquals(1L, foundCourse.getId());
        verify(trainingCourseRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse, new TrainingCourse());
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(trainingCourseRepository.findAll(pageable)).thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(trainingCourseRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDelete() {
        doNothing().when(trainingCourseRepository).deleteById(1L);

        trainingCourseService.delete(1L);

        verify(trainingCourseRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByCode() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(trainingCourseRepository.findByCodeContainingIgnoreCase("GMP", pageable)).thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.findByCode("GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingCourseRepository, times(1)).findByCodeContainingIgnoreCase("GMP", pageable);
    }

    @Test
    void testFindByName() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(trainingCourseRepository.findByNameContainingIgnoreCase("GMP", pageable)).thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.findByName("GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingCourseRepository, times(1)).findByNameContainingIgnoreCase("GMP", pageable);
    }

    @Test
    void testFindByPlanId() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(trainingCourseRepository.findByPlanId(1L, pageable)).thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.findByPlanId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingCourseRepository, times(1)).findByPlanId(1L, pageable);
    }

    @Test
    void testUpdateStatus() {
        when(trainingCourseRepository.findById(1L)).thenReturn(Optional.of(trainingCourse));
        when(trainingCourseRepository.save(any(TrainingCourse.class))).thenReturn(trainingCourse);

        TrainingCourse updatedCourse = trainingCourseService.updateStatus(1L, TrainingCourse.Status.APPROVED);

        assertNotNull(updatedCourse);
        assertEquals(TrainingCourse.Status.APPROVED, updatedCourse.getStatus());
        verify(trainingCourseRepository, times(1)).findById(1L);
        verify(trainingCourseRepository, times(1)).save(trainingCourse);
    }

    @Test
    void testBatchUpdateStatus() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(trainingCourseRepository.updateStatusInBatch(ids, TrainingCourse.Status.APPROVED)).thenReturn(2);

        int result = trainingCourseService.batchUpdateStatus(ids, TrainingCourse.Status.APPROVED);

        assertEquals(2, result);
        verify(trainingCourseRepository, times(1)).updateStatusInBatch(ids, TrainingCourse.Status.APPROVED);
    }

    @Test
    void testFindByCourseType() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(trainingCourseRepository.findByCourseType(TrainingCourse.CourseType.INTERNAL, pageable))
                .thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.findByCourseType(
                TrainingCourse.CourseType.INTERNAL, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingCourseRepository, times(1)).findByCourseType(TrainingCourse.CourseType.INTERNAL, pageable);
    }

    @Test
    void testFindByStatus() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(trainingCourseRepository.findByStatus(TrainingCourse.Status.DRAFT, pageable)).thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.findByStatus(TrainingCourse.Status.DRAFT, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingCourseRepository, times(1)).findByStatus(TrainingCourse.Status.DRAFT, pageable);
    }

    @Test
    void testFindByDateRange() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(8);
        when(trainingCourseRepository.findByStartDateBetween(start, end, pageable)).thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.findByDateRange(start, end, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingCourseRepository, times(1)).findByStartDateBetween(start, end, pageable);
    }

    @Test
    void testSearch() {
        List<TrainingCourse> courses = Arrays.asList(trainingCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingCourse> coursePage = new PageImpl<>(courses, pageable, courses.size());

        when(trainingCourseRepository.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "GMP", "质量管理", "培训", pageable)).thenReturn(coursePage);

        Page<TrainingCourse> result = trainingCourseService.search("GMP 质量管理 培训", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testValidateUniqueCode() {
        when(trainingCourseRepository.existsByCodeAndIdNot("GMP-2024-001", null)).thenReturn(false);
        when(trainingCourseRepository.existsByCodeAndIdNot("GMP-2024-001", 2L)).thenReturn(false);

        assertTrue(trainingCourseService.validateUniqueCode("GMP-2024-001", null));
        assertTrue(trainingCourseService.validateUniqueCode("GMP-2024-001", 2L));

        when(trainingCourseRepository.existsByCodeAndIdNot("GMP-2024-001", null)).thenReturn(true);
        assertFalse(trainingCourseService.validateUniqueCode("GMP-2024-001", null));
    }

    @Test
    void testCountByPlanId() {
        when(trainingCourseRepository.countByPlanId(1L)).thenReturn(5L);

        long count = trainingCourseService.countByPlanId(1L);

        assertEquals(5, count);
        verify(trainingCourseRepository, times(1)).countByPlanId(1L);
    }
}