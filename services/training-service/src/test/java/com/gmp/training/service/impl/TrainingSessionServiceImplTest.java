package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingSession;
import com.gmp.training.repository.TrainingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 培训场次服务实现的单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TrainingSessionServiceImplTest {

    @Mock
    private TrainingSessionRepository trainingSessionRepository;

    @InjectMocks
    private TrainingSessionServiceImpl trainingSessionService;

    private TrainingSession trainingSession;

    @BeforeEach
    void setUp() {
        trainingSession = new TrainingSession();
        trainingSession.setId(1L);
        trainingSession.setCourseId(1L);
        trainingSession.setTrainerId(1L);
        trainingSession.setSessionDate(LocalDate.now());
        trainingSession.setStartTime(LocalTime.of(9, 0));
        trainingSession.setEndTime(LocalTime.of(12, 0));
        trainingSession.setTrainingMethod(TrainingSession.TrainingMethod.FACE_TO_FACE);
        trainingSession.setLocation("培训室A");
        trainingSession.setMaxParticipants(30);
        trainingSession.setStatus(TrainingSession.Status.PLANNED);
        trainingSession.setDescription("GMP质量管理规范培训第一场");
        trainingSession.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testSave() {
        when(trainingSessionRepository.save(any(TrainingSession.class))).thenReturn(trainingSession);

        TrainingSession savedSession = trainingSessionService.save(trainingSession);

        assertNotNull(savedSession);
        assertEquals("培训室A", savedSession.getLocation());
        assertEquals(TrainingSession.Status.PLANNED, savedSession.getStatus());
        verify(trainingSessionRepository, times(1)).save(trainingSession);
    }

    @Test
    void testFindById() {
        when(trainingSessionRepository.findById(1L)).thenReturn(Optional.of(trainingSession));

        TrainingSession foundSession = trainingSessionService.findById(1L);

        assertNotNull(foundSession);
        assertEquals(1L, foundSession.getId());
        verify(trainingSessionRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession, new TrainingSession());
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findAll(pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDelete() {
        doNothing().when(trainingSessionRepository).deleteById(1L);

        trainingSessionService.delete(1L);

        verify(trainingSessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByCourseId() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByCourseId(1L, pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findByCourseId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findByCourseId(1L, pageable);
    }

    @Test
    void testFindByTrainerId() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByTrainerId(1L, pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findByTrainerId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findByTrainerId(1L, pageable);
    }

    @Test
    void testFindByStatus() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByStatus(TrainingSession.Status.PLANNED, pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findByStatus(TrainingSession.Status.PLANNED, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findByStatus(TrainingSession.Status.PLANNED, pageable);
    }

    @Test
    void testFindByTrainingMethod() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByTrainingMethod(TrainingSession.TrainingMethod.FACE_TO_FACE, pageable))
                .thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findByTrainingMethod(
                TrainingSession.TrainingMethod.FACE_TO_FACE, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findByTrainingMethod(TrainingSession.TrainingMethod.FACE_TO_FACE, pageable);
    }

    @Test
    void testFindByLocation() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByLocationContainingIgnoreCase("培训室", pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findByLocation("培训室", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findByLocationContainingIgnoreCase("培训室", pageable);
    }

    @Test
    void testFindByDateRange() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        when(trainingSessionRepository.findBySessionDateBetween(start, end, pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findByDateRange(start, end, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findBySessionDateBetween(start, end, pageable);
    }

    @Test
    void testFindFutureSessions() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        LocalDate today = LocalDate.now();
        when(trainingSessionRepository.findBySessionDateGreaterThanEqual(today, pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findFutureSessions(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findBySessionDateGreaterThanEqual(today, pageable);
    }

    @Test
    void testFindCompletedSessions() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByStatus(TrainingSession.Status.COMPLETED, pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findCompletedSessions(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findByStatus(TrainingSession.Status.COMPLETED, pageable);
    }

    @Test
    void testFindOngoingSessions() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByStatus(TrainingSession.Status.IN_PROGRESS, pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.findOngoingSessions(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingSessionRepository, times(1)).findByStatus(TrainingSession.Status.IN_PROGRESS, pageable);
    }

    @Test
    void testUpdateStatus() {
        when(trainingSessionRepository.findById(1L)).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRepository.save(any(TrainingSession.class))).thenReturn(trainingSession);

        TrainingSession updatedSession = trainingSessionService.updateStatus(1L, TrainingSession.Status.IN_PROGRESS);

        assertNotNull(updatedSession);
        assertEquals(TrainingSession.Status.IN_PROGRESS, updatedSession.getStatus());
        verify(trainingSessionRepository, times(1)).findById(1L);
        verify(trainingSessionRepository, times(1)).save(trainingSession);
    }

    @Test
    void testBatchUpdateStatus() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(trainingSessionRepository.updateStatusInBatch(ids, TrainingSession.Status.COMPLETED)).thenReturn(2);

        int result = trainingSessionService.batchUpdateStatus(ids, TrainingSession.Status.COMPLETED);

        assertEquals(2, result);
        verify(trainingSessionRepository, times(1)).updateStatusInBatch(ids, TrainingSession.Status.COMPLETED);
    }

    @Test
    void testGetActualParticipantsCount() {
        when(trainingSessionRepository.countRecordsBySessionIdAndStatus(1L, "ATTENDED")).thenReturn(25L);

        long count = trainingSessionService.getActualParticipantsCount(1L);

        assertEquals(25, count);
        verify(trainingSessionRepository, times(1)).countRecordsBySessionIdAndStatus(1L, "ATTENDED");
    }

    @Test
    void testGetAttendanceRate() {
        when(trainingSessionRepository.findById(1L)).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRepository.countRecordsBySessionId(1L)).thenReturn(28L);
        when(trainingSessionRepository.countRecordsBySessionIdAndStatus(1L, "ATTENDED")).thenReturn(25L);

        double rate = trainingSessionService.getAttendanceRate(1L);

        assertEquals(89.29, rate, 0.01); // 25/28 ≈ 89.29%
        verify(trainingSessionRepository, times(1)).findById(1L);
        verify(trainingSessionRepository, times(1)).countRecordsBySessionId(1L);
        verify(trainingSessionRepository, times(1)).countRecordsBySessionIdAndStatus(1L, "ATTENDED");
    }

    @Test
    void testSearch() {
        List<TrainingSession> sessions = Arrays.asList(trainingSession);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingSession> sessionPage = new PageImpl<>(sessions, pageable, sessions.size());

        when(trainingSessionRepository.findByLocationContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "培训室", "GMP", pageable)).thenReturn(sessionPage);

        Page<TrainingSession> result = trainingSessionService.search("培训室 GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}