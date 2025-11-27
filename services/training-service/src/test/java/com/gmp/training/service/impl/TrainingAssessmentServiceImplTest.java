package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingAssessment;
import com.gmp.training.repository.TrainingAssessmentRepository;
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
 * 培训考核服务实现的单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TrainingAssessmentServiceImplTest {

    @Mock
    private TrainingAssessmentRepository trainingAssessmentRepository;

    @InjectMocks
    private TrainingAssessmentServiceImpl trainingAssessmentService;

    private TrainingAssessment trainingAssessment;

    @BeforeEach
    void setUp() {
        trainingAssessment = new TrainingAssessment();
        trainingAssessment.setId(1L);
        trainingAssessment.setCourseId(1L);
        trainingAssessment.setUserId(1L);
        trainingAssessment.setAssessmentType(TrainingAssessment.AssessmentType.THEORY);
        trainingAssessment.setStatus(TrainingAssessment.Status.NOT_STARTED);
        trainingAssessment.setScore(null);
        trainingAssessment.setPassScore(60);
        trainingAssessment.setStartTime(null);
        trainingAssessment.setCompletionTime(null);
        trainingAssessment.setAttemptCount(0);
        trainingAssessment.setNotes("GMP理论考核");
        trainingAssessment.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testSave() {
        when(trainingAssessmentRepository.save(any(TrainingAssessment.class))).thenReturn(trainingAssessment);

        TrainingAssessment savedAssessment = trainingAssessmentService.save(trainingAssessment);

        assertNotNull(savedAssessment);
        assertEquals(TrainingAssessment.Status.NOT_STARTED, savedAssessment.getStatus());
        verify(trainingAssessmentRepository, times(1)).save(trainingAssessment);
    }

    @Test
    void testFindById() {
        when(trainingAssessmentRepository.findById(1L)).thenReturn(Optional.of(trainingAssessment));

        TrainingAssessment foundAssessment = trainingAssessmentService.findById(1L);

        assertNotNull(foundAssessment);
        assertEquals(1L, foundAssessment.getId());
        verify(trainingAssessmentRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment, new TrainingAssessment());
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findAll(pageable)).thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDelete() {
        doNothing().when(trainingAssessmentRepository).deleteById(1L);

        trainingAssessmentService.delete(1L);

        verify(trainingAssessmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByCourseId() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByCourseId(1L, pageable)).thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findByCourseId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByCourseId(1L, pageable);
    }

    @Test
    void testFindByUserId() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByUserId(1L, pageable)).thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findByUserId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void testFindByStatus() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByStatus(TrainingAssessment.Status.NOT_STARTED, pageable)).thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findByStatus(TrainingAssessment.Status.NOT_STARTED, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByStatus(TrainingAssessment.Status.NOT_STARTED, pageable);
    }

    @Test
    void testFindByAssessmentType() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByAssessmentType(TrainingAssessment.AssessmentType.THEORY, pageable))
                .thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findByAssessmentType(
                TrainingAssessment.AssessmentType.THEORY, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByAssessmentType(TrainingAssessment.AssessmentType.THEORY, pageable);
    }

    @Test
    void testFindByDateRange() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        when(trainingAssessmentRepository.findByCreatedDateBetween(
                start.atStartOfDay(), end.plusDays(1).atStartOfDay(), pageable)).thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findByDateRange(start, end, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByCreatedDateBetween(
                start.atStartOfDay(), end.plusDays(1).atStartOfDay(), pageable);
    }

    @Test
    void testFindNotStarted() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByStatus(TrainingAssessment.Status.NOT_STARTED, pageable))
                .thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findNotStarted(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByStatus(TrainingAssessment.Status.NOT_STARTED, pageable);
    }

    @Test
    void testFindInProgress() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByStatus(TrainingAssessment.Status.IN_PROGRESS, pageable))
                .thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findInProgress(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByStatus(TrainingAssessment.Status.IN_PROGRESS, pageable);
    }

    @Test
    void testFindCompleted() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByStatus(TrainingAssessment.Status.COMPLETED, pageable))
                .thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findCompleted(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByStatus(TrainingAssessment.Status.COMPLETED, pageable);
    }

    @Test
    void testUpdateStatus() {
        when(trainingAssessmentRepository.findById(1L)).thenReturn(Optional.of(trainingAssessment));
        when(trainingAssessmentRepository.save(any(TrainingAssessment.class))).thenReturn(trainingAssessment);

        TrainingAssessment updatedAssessment = trainingAssessmentService.updateStatus(1L, TrainingAssessment.Status.COMPLETED);

        assertNotNull(updatedAssessment);
        assertEquals(TrainingAssessment.Status.COMPLETED, updatedAssessment.getStatus());
        verify(trainingAssessmentRepository, times(1)).findById(1L);
        verify(trainingAssessmentRepository, times(1)).save(trainingAssessment);
    }

    @Test
    void testBatchUpdateStatus() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(trainingAssessmentRepository.updateStatusInBatch(ids, TrainingAssessment.Status.COMPLETED)).thenReturn(2);

        int result = trainingAssessmentService.batchUpdateStatus(ids, TrainingAssessment.Status.COMPLETED);

        assertEquals(2, result);
        verify(trainingAssessmentRepository, times(1)).updateStatusInBatch(ids, TrainingAssessment.Status.COMPLETED);
    }

    @Test
    void testGetCourseAverageScore() {
        when(trainingAssessmentRepository.findAverageScoreByCourseId(1L)).thenReturn(85.5);

        double avgScore = trainingAssessmentService.getCourseAverageScore(1L);

        assertEquals(85.5, avgScore);
        verify(trainingAssessmentRepository, times(1)).findAverageScoreByCourseId(1L);
    }

    @Test
    void testFindPassedAssessments() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByCourseIdAndScoreGreaterThanEqual(1L, 60, pageable))
                .thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.findPassedAssessments(1L, 60, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingAssessmentRepository, times(1)).findByCourseIdAndScoreGreaterThanEqual(1L, 60, pageable);
    }

    @Test
    void testSearch() {
        List<TrainingAssessment> assessments = Arrays.asList(trainingAssessment);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingAssessment> assessmentPage = new PageImpl<>(assessments, pageable, assessments.size());

        when(trainingAssessmentRepository.findByNotesContainingIgnoreCase("GMP", pageable)).thenReturn(assessmentPage);

        Page<TrainingAssessment> result = trainingAssessmentService.search("GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testStartAssessment() {
        when(trainingAssessmentRepository.findById(1L)).thenReturn(Optional.of(trainingAssessment));
        when(trainingAssessmentRepository.save(any(TrainingAssessment.class))).thenReturn(trainingAssessment);

        TrainingAssessment startedAssessment = trainingAssessmentService.startAssessment(1L);

        assertNotNull(startedAssessment);
        assertEquals(TrainingAssessment.Status.IN_PROGRESS, startedAssessment.getStatus());
        verify(trainingAssessmentRepository, times(1)).findById(1L);
        verify(trainingAssessmentRepository, times(1)).save(trainingAssessment);
    }

    @Test
    void testSubmitAssessment() {
        when(trainingAssessmentRepository.findById(1L)).thenReturn(Optional.of(trainingAssessment));
        when(trainingAssessmentRepository.save(any(TrainingAssessment.class))).thenReturn(trainingAssessment);

        TrainingAssessment submittedAssessment = trainingAssessmentService.submitAssessment(1L, 85);

        assertNotNull(submittedAssessment);
        assertEquals(TrainingAssessment.Status.COMPLETED, submittedAssessment.getStatus());
        assertEquals(85, submittedAssessment.getScore());
        verify(trainingAssessmentRepository, times(1)).findById(1L);
        verify(trainingAssessmentRepository, times(1)).save(trainingAssessment);
    }
}
