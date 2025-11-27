package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingRecord;
import com.gmp.training.repository.TrainingRecordRepository;
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
 * 培训记录服务实现的单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TrainingRecordServiceImplTest {

    @Mock
    private TrainingRecordRepository trainingRecordRepository;

    @InjectMocks
    private TrainingRecordServiceImpl trainingRecordService;

    private TrainingRecord trainingRecord;

    @BeforeEach
    void setUp() {
        trainingRecord = new TrainingRecord();
        trainingRecord.setId(1L);
        trainingRecord.setUserId(1L);
        trainingRecord.setSessionId(1L);
        trainingRecord.setCourseId(1L);
        trainingRecord.setDepartmentId(1L);
        trainingRecord.setPositionId(1L);
        trainingRecord.setStatus(TrainingRecord.Status.PENDING);
        trainingRecord.setRegisterDate(LocalDateTime.now());
        trainingRecord.setCompletionDate(null);
        trainingRecord.setAttendanceTime(null);
        trainingRecord.setScore(null);
        trainingRecord.setNotes("需要参加GMP培训");
        trainingRecord.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testSave() {
        when(trainingRecordRepository.save(any(TrainingRecord.class))).thenReturn(trainingRecord);

        TrainingRecord savedRecord = trainingRecordService.save(trainingRecord);

        assertNotNull(savedRecord);
        assertEquals(TrainingRecord.Status.PENDING, savedRecord.getStatus());
        verify(trainingRecordRepository, times(1)).save(trainingRecord);
    }

    @Test
    void testFindById() {
        when(trainingRecordRepository.findById(1L)).thenReturn(Optional.of(trainingRecord));

        TrainingRecord foundRecord = trainingRecordService.findById(1L);

        assertNotNull(foundRecord);
        assertEquals(1L, foundRecord.getId());
        verify(trainingRecordRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord, new TrainingRecord());
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findAll(pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(trainingRecordRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDelete() {
        doNothing().when(trainingRecordRepository).deleteById(1L);

        trainingRecordService.delete(1L);

        verify(trainingRecordRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByUserId() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findByUserId(1L, pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.findByUserId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingRecordRepository, times(1)).findByUserId(1L, pageable);
    }

    @Test
    void testFindBySessionId() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findBySessionId(1L, pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.findBySessionId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingRecordRepository, times(1)).findBySessionId(1L, pageable);
    }

    @Test
    void testFindByCourseId() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findByCourseId(1L, pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.findByCourseId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingRecordRepository, times(1)).findByCourseId(1L, pageable);
    }

    @Test
    void testFindByStatus() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findByStatus(TrainingRecord.Status.PENDING, pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.findByStatus(TrainingRecord.Status.PENDING, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingRecordRepository, times(1)).findByStatus(TrainingRecord.Status.PENDING, pageable);
    }

    @Test
    void testFindByDepartmentId() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findByDepartmentId(1L, pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.findByDepartmentId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingRecordRepository, times(1)).findByDepartmentId(1L, pageable);
    }

    @Test
    void testFindByPositionId() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findByPositionId(1L, pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.findByPositionId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingRecordRepository, times(1)).findByPositionId(1L, pageable);
    }

    @Test
    void testUpdateStatus() {
        when(trainingRecordRepository.findById(1L)).thenReturn(Optional.of(trainingRecord));
        when(trainingRecordRepository.save(any(TrainingRecord.class))).thenReturn(trainingRecord);

        TrainingRecord updatedRecord = trainingRecordService.updateStatus(1L, TrainingRecord.Status.ATTENDED);

        assertNotNull(updatedRecord);
        assertEquals(TrainingRecord.Status.ATTENDED, updatedRecord.getStatus());
        verify(trainingRecordRepository, times(1)).findById(1L);
        verify(trainingRecordRepository, times(1)).save(trainingRecord);
    }

    @Test
    void testBatchUpdateStatus() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(trainingRecordRepository.updateStatusInBatch(ids, TrainingRecord.Status.COMPLETED)).thenReturn(2);

        int result = trainingRecordService.batchUpdateStatus(ids, TrainingRecord.Status.COMPLETED);

        assertEquals(2, result);
        verify(trainingRecordRepository, times(1)).updateStatusInBatch(ids, TrainingRecord.Status.COMPLETED);
    }

    @Test
    void testFindByUserIdAndCourseId() {
        when(trainingRecordRepository.findByUserIdAndCourseId(1L, 1L))
                .thenReturn(Optional.of(trainingRecord));

        TrainingRecord foundRecord = trainingRecordService.findByUserIdAndCourseId(1L, 1L);

        assertNotNull(foundRecord);
        assertEquals(1L, foundRecord.getUserId());
        assertEquals(1L, foundRecord.getCourseId());
        verify(trainingRecordRepository, times(1)).findByUserIdAndCourseId(1L, 1L);
    }

    @Test
    void testGetUserCompletionRate() {
        when(trainingRecordRepository.countByUserId(1L)).thenReturn(10L);
        when(trainingRecordRepository.countByUserIdAndStatus(1L, TrainingRecord.Status.COMPLETED)).thenReturn(8L);

        double rate = trainingRecordService.getUserCompletionRate(1L);

        assertEquals(80.0, rate);
        verify(trainingRecordRepository, times(1)).countByUserId(1L);
        verify(trainingRecordRepository, times(1)).countByUserIdAndStatus(1L, TrainingRecord.Status.COMPLETED);
    }

    @Test
    void testGetCourseCompletionRate() {
        when(trainingRecordRepository.countByCourseId(1L)).thenReturn(30L);
        when(trainingRecordRepository.countByCourseIdAndStatus(1L, TrainingRecord.Status.COMPLETED)).thenReturn(25L);

        double rate = trainingRecordService.getCourseCompletionRate(1L);

        assertEquals(83.33, rate, 0.01); // 25/30 ≈ 83.33%
        verify(trainingRecordRepository, times(1)).countByCourseId(1L);
        verify(trainingRecordRepository, times(1)).countByCourseIdAndStatus(1L, TrainingRecord.Status.COMPLETED);
    }

    @Test
    void testSearch() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingRecord> recordPage = new PageImpl<>(records, pageable, records.size());

        when(trainingRecordRepository.findByNotesContainingIgnoreCase("GMP", pageable)).thenReturn(recordPage);

        Page<TrainingRecord> result = trainingRecordService.search("GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testBatchCreate() {
        List<TrainingRecord> records = Arrays.asList(trainingRecord, new TrainingRecord());
        when(trainingRecordRepository.saveAll(records)).thenReturn(records);

        List<TrainingRecord> savedRecords = trainingRecordService.batchCreate(records);

        assertNotNull(savedRecords);
        assertEquals(2, savedRecords.size());
        verify(trainingRecordRepository, times(1)).saveAll(records);
    }
}
