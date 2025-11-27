package com.gmp.training.service.impl;

import com.gmp.training.entity.TrainingNeed;
import com.gmp.training.repository.TrainingNeedRepository;
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
 * 培训需求服务实现的单元测试
 */
@ExtendWith(MockitoExtension.class)
public class TrainingNeedServiceImplTest {

    @Mock
    private TrainingNeedRepository trainingNeedRepository;

    @InjectMocks
    private TrainingNeedServiceImpl trainingNeedService;

    private TrainingNeed trainingNeed;

    @BeforeEach
    void setUp() {
        trainingNeed = new TrainingNeed();
        trainingNeed.setId(1L);
        trainingNeed.setName("GMP基础知识培训");
        trainingNeed.setDescription("针对新员工的GMP基础知识培训");
        trainingNeed.setDepartmentId(1L);
        trainingNeed.setPositionId(1L);
        trainingNeed.setStatus(TrainingNeed.Status.PENDING);
        trainingNeed.setPriority(TrainingNeed.Priority.HIGH);
        trainingNeed.setEstimatedHours(8);
        trainingNeed.setCreatorId(1L);
        trainingNeed.setCreatedDate(LocalDateTime.now());
    }

    @Test
    void testSave() {
        when(trainingNeedRepository.save(any(TrainingNeed.class))).thenReturn(trainingNeed);

        TrainingNeed savedNeed = trainingNeedService.save(trainingNeed);

        assertNotNull(savedNeed);
        assertEquals("GMP基础知识培训", savedNeed.getName());
        assertEquals(TrainingNeed.Status.PENDING, savedNeed.getStatus());
        verify(trainingNeedRepository, times(1)).save(trainingNeed);
    }

    @Test
    void testFindById() {
        when(trainingNeedRepository.findById(1L)).thenReturn(Optional.of(trainingNeed));

        TrainingNeed foundNeed = trainingNeedService.findById(1L);

        assertNotNull(foundNeed);
        assertEquals(1L, foundNeed.getId());
        verify(trainingNeedRepository, times(1)).findById(1L);
    }

    @Test
    void testFindAll() {
        List<TrainingNeed> needs = Arrays.asList(trainingNeed, new TrainingNeed());
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingNeed> needPage = new PageImpl<>(needs, pageable, needs.size());

        when(trainingNeedRepository.findAll(pageable)).thenReturn(needPage);

        Page<TrainingNeed> result = trainingNeedService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(trainingNeedRepository, times(1)).findAll(pageable);
    }

    @Test
    void testDelete() {
        doNothing().when(trainingNeedRepository).deleteById(1L);

        trainingNeedService.delete(1L);

        verify(trainingNeedRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByName() {
        List<TrainingNeed> needs = Arrays.asList(trainingNeed);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingNeed> needPage = new PageImpl<>(needs, pageable, needs.size());

        when(trainingNeedRepository.findByNameContainingIgnoreCase("GMP", pageable)).thenReturn(needPage);

        Page<TrainingNeed> result = trainingNeedService.findByName("GMP", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingNeedRepository, times(1)).findByNameContainingIgnoreCase("GMP", pageable);
    }

    @Test
    void testFindByDepartmentId() {
        List<TrainingNeed> needs = Arrays.asList(trainingNeed);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingNeed> needPage = new PageImpl<>(needs, pageable, needs.size());

        when(trainingNeedRepository.findByDepartmentId(1L, pageable)).thenReturn(needPage);

        Page<TrainingNeed> result = trainingNeedService.findByDepartmentId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingNeedRepository, times(1)).findByDepartmentId(1L, pageable);
    }

    @Test
    void testFindByStatus() {
        List<TrainingNeed> needs = Arrays.asList(trainingNeed);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingNeed> needPage = new PageImpl<>(needs, pageable, needs.size());

        when(trainingNeedRepository.findByStatus(TrainingNeed.Status.PENDING, pageable)).thenReturn(needPage);

        Page<TrainingNeed> result = trainingNeedService.findByStatus(TrainingNeed.Status.PENDING, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingNeedRepository, times(1)).findByStatus(TrainingNeed.Status.PENDING, pageable);
    }

    @Test
    void testUpdateStatus() {
        when(trainingNeedRepository.findById(1L)).thenReturn(Optional.of(trainingNeed));
        when(trainingNeedRepository.save(any(TrainingNeed.class))).thenReturn(trainingNeed);

        TrainingNeed updatedNeed = trainingNeedService.updateStatus(1L, TrainingNeed.Status.APPROVED);

        assertNotNull(updatedNeed);
        assertEquals(TrainingNeed.Status.APPROVED, updatedNeed.getStatus());
        verify(trainingNeedRepository, times(1)).findById(1L);
        verify(trainingNeedRepository, times(1)).save(trainingNeed);
    }

    @Test
    void testBatchUpdateStatus() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(trainingNeedRepository.updateStatusInBatch(ids, TrainingNeed.Status.APPROVED)).thenReturn(2);

        int result = trainingNeedService.batchUpdateStatus(ids, TrainingNeed.Status.APPROVED);

        assertEquals(2, result);
        verify(trainingNeedRepository, times(1)).updateStatusInBatch(ids, TrainingNeed.Status.APPROVED);
    }

    @Test
    void testFindApprovedNeeds() {
        List<TrainingNeed> needs = Arrays.asList(trainingNeed);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingNeed> needPage = new PageImpl<>(needs, pageable, needs.size());

        when(trainingNeedRepository.findByStatus(TrainingNeed.Status.APPROVED, pageable)).thenReturn(needPage);

        Page<TrainingNeed> result = trainingNeedService.findApprovedNeeds(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(trainingNeedRepository, times(1)).findByStatus(TrainingNeed.Status.APPROVED, pageable);
    }

    @Test
    void testSearch() {
        List<TrainingNeed> needs = Arrays.asList(trainingNeed);
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingNeed> needPage = new PageImpl<>(needs, pageable, needs.size());

        when(trainingNeedRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "GMP", "基础知识", pageable)).thenReturn(needPage);

        Page<TrainingNeed> result = trainingNeedService.search("GMP 基础知识", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}