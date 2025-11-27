package com.gmp.hr.service.impl;

import com.gmp.hr.dto.QualificationTypeDTO;
import com.gmp.hr.entity.QualificationType;
import com.gmp.hr.exception.BusinessException;
import com.gmp.hr.repository.QualificationTypeRepository;
import com.gmp.hr.repository.QualificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 资质类型服务实现类单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public class QualificationTypeServiceImplTest {

    @Mock
    private QualificationTypeRepository qualificationTypeRepository;
    
    @Mock
    private QualificationRepository qualificationRepository;
    
    @InjectMocks
    private QualificationTypeServiceImpl qualificationTypeService;
    
    private QualificationType qualificationType;
    private QualificationTypeDTO qualificationTypeDTO;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        qualificationType = new QualificationType();
        qualificationType.setId(1L);
        qualificationType.setCode("CERT001");
        qualificationType.setName("计算机等级证书");
        qualificationType.setDescription("计算机相关技能认证");
        qualificationType.setNeedUpdate(true);
        qualificationType.setUpdateCycle(365); // 365天更新一次
        
        qualificationTypeDTO = new QualificationTypeDTO();
        qualificationTypeDTO.setId(1L);
        qualificationTypeDTO.setCode("CERT001");
        qualificationTypeDTO.setName("计算机等级证书");
        qualificationTypeDTO.setDescription("计算机相关技能认证");
        qualificationTypeDTO.setNeedUpdate(true);
        qualificationTypeDTO.setUpdateCycle(365);
    }
    
    @Test
    void testCreateQualificationType_Success() {
        // 准备
        when(qualificationTypeRepository.existsByCode("CERT001")).thenReturn(false);
        when(qualificationTypeRepository.save(any(QualificationType.class))).thenReturn(qualificationType);
        
        // 执行
        QualificationTypeDTO result = qualificationTypeService.createQualificationType(qualificationTypeDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("CERT001", result.getCode());
        assertEquals("计算机等级证书", result.getName());
        verify(qualificationTypeRepository, times(1)).save(any(QualificationType.class));
    }
    
    @Test
    void testCreateQualificationType_CodeExists() {
        // 准备
        when(qualificationTypeRepository.existsByCode("CERT001")).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> qualificationTypeService.createQualificationType(qualificationTypeDTO));
        assertEquals("资质类型代码 CERT001 已存在", exception.getMessage());
        verify(qualificationTypeRepository, never()).save(any(QualificationType.class));
    }
    
    @Test
    void testGetQualificationTypeById_Success() {
        // 准备
        when(qualificationTypeRepository.findById(1L)).thenReturn(Optional.of(qualificationType));
        when(qualificationRepository.countByTypeId(1L)).thenReturn(10L);
        
        // 执行
        QualificationTypeDTO result = qualificationTypeService.getQualificationTypeById(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CERT001", result.getCode());
        assertEquals(10L, result.getQualificationCount());
    }
    
    @Test
    void testGetQualificationTypeById_NotFound() {
        // 准备
        when(qualificationTypeRepository.findById(1L)).thenReturn(Optional.empty());
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> qualificationTypeService.getQualificationTypeById(1L));
        assertEquals("资质类型不存在，ID: 1", exception.getMessage());
    }
    
    @Test
    void testGetQualificationTypeByCode_Success() {
        // 准备
        when(qualificationTypeRepository.findByCode("CERT001")).thenReturn(Optional.of(qualificationType));
        when(qualificationRepository.countByTypeId(1L)).thenReturn(10L);
        
        // 执行
        QualificationTypeDTO result = qualificationTypeService.getQualificationTypeByCode("CERT001");
        
        // 验证
        assertNotNull(result);
        assertEquals("CERT001", result.getCode());
    }
    
    @Test
    void testGetQualificationTypeByName_Success() {
        // 准备
        when(qualificationTypeRepository.findByName("计算机等级证书")).thenReturn(Optional.of(qualificationType));
        when(qualificationRepository.countByTypeId(1L)).thenReturn(10L);
        
        // 执行
        QualificationTypeDTO result = qualificationTypeService.getQualificationTypeByName("计算机等级证书");
        
        // 验证
        assertNotNull(result);
        assertEquals("计算机等级证书", result.getName());
    }
    
    @Test
    void testUpdateQualificationType_Success() {
        // 准备
        when(qualificationTypeRepository.findById(1L)).thenReturn(Optional.of(qualificationType));
        when(qualificationTypeRepository.existsByCodeAndIdNot("CERT001", 1L)).thenReturn(false);
        when(qualificationTypeRepository.save(any(QualificationType.class))).thenReturn(qualificationType);
        
        // 修改DTO
        qualificationTypeDTO.setName("计算机高级证书");
        
        // 执行
        QualificationTypeDTO result = qualificationTypeService.updateQualificationType(1L, qualificationTypeDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("计算机高级证书", result.getName());
        verify(qualificationTypeRepository, times(1)).save(any(QualificationType.class));
    }
    
    @Test
    void testDeleteQualificationType_Success() {
        // 准备
        when(qualificationTypeRepository.findById(1L)).thenReturn(Optional.of(qualificationType));
        when(qualificationRepository.existsByTypeId(1L)).thenReturn(false);
        doNothing().when(qualificationTypeRepository).deleteById(1L);
        
        // 执行
        qualificationTypeService.deleteQualificationType(1L);
        
        // 验证
        verify(qualificationTypeRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testDeleteQualificationType_HasQualifications() {
        // 准备
        when(qualificationTypeRepository.findById(1L)).thenReturn(Optional.of(qualificationType));
        when(qualificationRepository.existsByTypeId(1L)).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> qualificationTypeService.deleteQualificationType(1L));
        assertEquals("资质类型下存在资质记录，无法删除", exception.getMessage());
        verify(qualificationTypeRepository, never()).deleteById(1L);
    }
    
    @Test
    void testGetAllQualificationTypes_Success() {
        // 准备
        List<QualificationType> qualificationTypes = Collections.singletonList(qualificationType);
        when(qualificationTypeRepository.findAll()).thenReturn(qualificationTypes);
        when(qualificationRepository.countByTypeId(1L)).thenReturn(10L);
        
        // 执行
        List<QualificationTypeDTO> result = qualificationTypeService.getAllQualificationTypes();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CERT001", result.get(0).getCode());
    }
    
    @Test
    void testGetQualificationTypesByNeedUpdate_Success() {
        // 准备
        List<QualificationType> qualificationTypes = Collections.singletonList(qualificationType);
        when(qualificationTypeRepository.findByNeedUpdate(true)).thenReturn(qualificationTypes);
        when(qualificationRepository.countByTypeId(1L)).thenReturn(10L);
        
        // 执行
        List<QualificationTypeDTO> result = qualificationTypeService.getQualificationTypesByNeedUpdate(true);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isNeedUpdate());
    }
}