package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingPlan;
import com.gmp.training.repository.TrainingPlanRepository;
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
 * 培训计划服务实现的单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TrainingPlanServiceImplTest {

    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @InjectMocks
    private TrainingPlanServiceImpl trainingPlanService;

    private TrainingPlan trainingPlan;

    @BeforeEach
    void setUp() {
        trainingPlan = new TrainingPlan();
        trainingPlan.setId(1L);
        trainingPlan.setName("2024年第一季度GMP培训计划");
        trainingPlan.setYear(2024);
        trainingPlan.setQuarter(1);
        trainingPlan.setResponsibleUserId(1L);
        trainingPlan.setStatus(TrainingPlan.Status.DRAFT);
        trainingPlan.setPlanDate(LocalDate.now());
        trainingPlan.setEstimatedBudget(10000.0);
        trainingPlan.setDescription("2024年第一季度的GMP培训计划");
        trainingPlan.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testSave() {
        when(trainingPlanRepository.save(any(TrainingPlan.class))).thenReturn(trainingPlan);

        TrainingPlan savedPlan = trainingPlanService.save(trainingPlan);

        assertNotNull(savedPlan);
        assertEquals("2024年第一季度GMP培训计划", savedPlan.getName());
        assertEquals(TrainingPlan.Status.DRAFT, savedPlan.getStatus());
        verify(trainingPlanRepository, times(1)).save(trainingPlan);
    }

    @Test
    void testFindById() {
        when(trainingPlanRepository.findById(1L)).thenReturn(Optional.of(trainingPlan));

        TrainingPlan foundPlan = trainingPlanService.findById(1L);

        assertNotNull(foundPlan);
        assertEquals(1L, foundPlan.getId());
        verify(trainingPlanRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<TrainingPlan> plans = Arrays.asList(trainingPlan, new TrainingPlan());
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingPlan> planPage = new PageImpl<>(plans, pageable, plans.size());

        when(trainingPlanRepository.findAll(pageable)).thenReturn(planPage);

        Page<TrainingPlan> result = trainingPlanService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(trainingPlanRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDelete() {
        doNothing().when(trainingPlanRepository).deleteById(1L);

        trainingPlanService.delete(1L);

        verify(trainingPlanRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByName() {
        List<TrainingPlan> plans = Arrays.asList(trainingPlan);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingPlan> planPage = new PageImpl<>(plans, pageable, plans.size());

        when(trainingPlanRepository.findByNameContainingIgnoreCase("GMP", pageable)).thenReturn(planPage);

        Page<TrainingPlan> result = trainingPlanService.findByName("GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingPlanRepository, times(1)).findByNameContainingIgnoreCase("GMP", pageable);
    }

    @Test
    void testFindByYear() {
        List<TrainingPlan> plans = Arrays.asList(trainingPlan);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingPlan> planPage = new PageImpl<>(plans, pageable, plans.size());

        when(trainingPlanRepository.findByYear(2024, pageable)).thenReturn(planPage);

        Page<TrainingPlan> result = trainingPlanService.findByYear(2024, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingPlanRepository, times(1)).findByYear(2024, pageable);
    }

    @Test
    void testFindByYearAndQuarter() {
        List<TrainingPlan> plans = Arrays.asList(trainingPlan);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingPlan> planPage = new PageImpl<>(plans, pageable, plans.size());

        when(trainingPlanRepository.findByYearAndQuarter(2024, 1, pageable)).thenReturn(planPage);

        Page<TrainingPlan> result = trainingPlanService.findByYearAndQuarter(2024, 1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingPlanRepository, times(1)).findByYearAndQuarter(2024, 1, pageable);
    }

    @Test
    void testUpdateStatus() {
        when(trainingPlanRepository.findById(1L)).thenReturn(Optional.of(trainingPlan));
        when(trainingPlanRepository.save(any(TrainingPlan.class))).thenReturn(trainingPlan);

        TrainingPlan updatedPlan = trainingPlanService.updateStatus(1L, TrainingPlan.Status.APPROVED);

        assertNotNull(updatedPlan);
        assertEquals(TrainingPlan.Status.APPROVED, updatedPlan.getStatus());
        verify(trainingPlanRepository, times(1)).findById(1L);
        verify(trainingPlanRepository, times(1)).save(trainingPlan);
    }

    @Test
    void testBatchUpdateStatus() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(trainingPlanRepository.updateStatusInBatch(ids, TrainingPlan.Status.APPROVED)).thenReturn(2);

        int result = trainingPlanService.batchUpdateStatus(ids, TrainingPlan.Status.APPROVED);

        assertEquals(2, result);
        verify(trainingPlanRepository, times(1)).updateStatusInBatch(ids, TrainingPlan.Status.APPROVED);
    }

    @Test
    void testCalculateCompletionRate() {
        // 模拟计算完成率的情况
        when(trainingPlanRepository.findById(1L)).thenReturn(Optional.of(trainingPlan));
        // 假设该计划有5个课程，其中3个已完成
        when(trainingPlanRepository.countCoursesByPlanId(1L)).thenReturn(5L);
        when(trainingPlanRepository.countCompletedCoursesByPlanId(1L)).thenReturn(3L);

        double completionRate = trainingPlanService.calculateCompletionRate(1L);

        assertEquals(60.0, completionRate);
        verify(trainingPlanRepository, times(1)).findById(1L);
        verify(trainingPlanRepository, times(1)).countCoursesByPlanId(1L);
        verify(trainingPlanRepository, times(1)).countCompletedCoursesByPlanId(1L);
    }

    @Test
    void testGetActualCost() {
        when(trainingPlanRepository.findById(1L)).thenReturn(Optional.of(trainingPlan));
        when(trainingPlanRepository.sumActualCostByPlanId(1L)).thenReturn(8500.0);

        double actualCost = trainingPlanService.getActualCost(1L);

        assertEquals(8500.0, actualCost);
        verify(trainingPlanRepository, times(1)).findById(1L);
        verify(trainingPlanRepository, times(1)).sumActualCostByPlanId(1L);
    }

    @Test
    void testSearch() {
        List<TrainingPlan> plans = Arrays.asList(trainingPlan);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingPlan> planPage = new PageImpl<>(plans, pageable, plans.size());

        when(trainingPlanRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "2024", "GMP", pageable)).thenReturn(planPage);

        Page<TrainingPlan> result = trainingPlanService.search("2024 GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}