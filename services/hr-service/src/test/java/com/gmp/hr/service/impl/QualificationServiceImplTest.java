package com.gmp.hr.service.impl;

import com.gmp.hr.dto.QualificationDTO;
import com.gmp.hr.entity.Employee;
import com.gmp.hr.entity.Qualification;
import com.gmp.hr.entity.QualificationType;
import com.gmp.hr.exception.BusinessException;
import com.gmp.hr.repository.EmployeeRepository;
import com.gmp.hr.repository.QualificationRepository;
import com.gmp.hr.repository.QualificationTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 资质服务实现类单元测试
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public class QualificationServiceImplTest {

    @Mock
    private QualificationRepository qualificationRepository;
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @Mock
    private QualificationTypeRepository qualificationTypeRepository;
    
    @InjectMocks
    private QualificationServiceImpl qualificationService;
    
    private Qualification qualification;
    private QualificationDTO qualificationDTO;
    private Employee employee;
    private QualificationType qualificationType;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // 初始化测试数据
        employee = new Employee();
        employee.setId(1L);
        employee.setCode("EMP001");
        employee.setName("张三");
        
        qualificationType = new QualificationType();
        qualificationType.setId(1L);
        qualificationType.setCode("CERT001");
        qualificationType.setName("计算机等级证书");
        qualificationType.setNeedUpdate(true);
        qualificationType.setUpdateCycle(365);
        
        qualification = new Qualification();
        qualification.setId(1L);
        qualification.setCertificateNumber("CERT-2023-0001");
        qualification.setIssueDate(LocalDate.of(2023, 1, 1));
        qualification.setExpiryDate(LocalDate.of(2026, 1, 1));
        qualification.setIssueAuthority("国家信息产业部");
        qualification.setStatus("有效");
        qualification.setEmployeeId(1L);
        qualification.setTypeId(1L);
        qualification.setEmployee(employee);
        qualification.setType(qualificationType);
        
        qualificationDTO = new QualificationDTO();
        qualificationDTO.setId(1L);
        qualificationDTO.setCertificateNumber("CERT-2023-0001");
        qualificationDTO.setIssueDate(LocalDate.of(2023, 1, 1));
        qualificationDTO.setExpiryDate(LocalDate.of(2026, 1, 1));
        qualificationDTO.setIssueAuthority("国家信息产业部");
        qualificationDTO.setStatus("有效");
        qualificationDTO.setEmployeeId(1L);
        qualificationDTO.setTypeId(1L);
        qualificationDTO.setEmployeeName("张三");
        qualificationDTO.setTypeName("计算机等级证书");
    }
    
    @Test
    void testCreateQualification_Success() {
        // 准备
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(qualificationTypeRepository.existsById(1L)).thenReturn(true);
        when(qualificationRepository.existsByCertificateNumber("CERT-2023-0001")).thenReturn(false);
        when(qualificationRepository.existsByEmployeeIdAndTypeId(1L, 1L)).thenReturn(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(qualificationTypeRepository.findById(1L)).thenReturn(Optional.of(qualificationType));
        when(qualificationRepository.save(any(Qualification.class))).thenReturn(qualification);
        
        // 执行
        QualificationDTO result = qualificationService.createQualification(qualificationDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("CERT-2023-0001", result.getCertificateNumber());
        assertEquals("张三", result.getEmployeeName());
        verify(qualificationRepository, times(1)).save(any(Qualification.class));
    }
    
    @Test
    void testCreateQualification_EmployeeNotExists() {
        // 准备
        when(employeeRepository.existsById(1L)).thenReturn(false);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> qualificationService.createQualification(qualificationDTO));
        assertEquals("关联的员工不存在，ID: 1", exception.getMessage());
        verify(qualificationRepository, never()).save(any(Qualification.class));
    }
    
    @Test
    void testCreateQualification_CertificateNumberExists() {
        // 准备
        when(employeeRepository.existsById(1L)).thenReturn(true);
        when(qualificationTypeRepository.existsById(1L)).thenReturn(true);
        when(qualificationRepository.existsByCertificateNumber("CERT-2023-0001")).thenReturn(true);
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> qualificationService.createQualification(qualificationDTO));
        assertEquals("证书编号 CERT-2023-0001 已存在", exception.getMessage());
        verify(qualificationRepository, never()).save(any(Qualification.class));
    }
    
    @Test
    void testGetQualificationById_Success() {
        // 准备
        when(qualificationRepository.findById(1L)).thenReturn(Optional.of(qualification));
        
        // 执行
        QualificationDTO result = qualificationService.getQualificationById(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("CERT-2023-0001", result.getCertificateNumber());
    }
    
    @Test
    void testGetQualificationById_NotFound() {
        // 准备
        when(qualificationRepository.findById(1L)).thenReturn(Optional.empty());
        
        // 执行和验证
        BusinessException exception = assertThrows(BusinessException.class, 
                () -> qualificationService.getQualificationById(1L));
        assertEquals("资质不存在，ID: 1", exception.getMessage());
    }
    
    @Test
    void testGetQualificationByCertificateNumber_Success() {
        // 准备
        when(qualificationRepository.findByCertificateNumber("CERT-2023-0001")).thenReturn(Optional.of(qualification));
        
        // 执行
        QualificationDTO result = qualificationService.getQualificationByCertificateNumber("CERT-2023-0001");
        
        // 验证
        assertNotNull(result);
        assertEquals("CERT-2023-0001", result.getCertificateNumber());
    }
    
    @Test
    void testUpdateQualification_Success() {
        // 准备
        when(qualificationRepository.findById(1L)).thenReturn(Optional.of(qualification));
        when(qualificationRepository.existsByCertificateNumberAndIdNot("CERT-2023-0001", 1L)).thenReturn(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(qualificationTypeRepository.findById(1L)).thenReturn(Optional.of(qualificationType));
        when(qualificationRepository.save(any(Qualification.class))).thenReturn(qualification);
        
        // 修改DTO
        qualificationDTO.setIssueAuthority("国家信息技术部");
        
        // 执行
        QualificationDTO result = qualificationService.updateQualification(1L, qualificationDTO);
        
        // 验证
        assertNotNull(result);
        assertEquals("国家信息技术部", result.getIssueAuthority());
        verify(qualificationRepository, times(1)).save(any(Qualification.class));
    }
    
    @Test
    void testDeleteQualification_Success() {
        // 准备
        when(qualificationRepository.findById(1L)).thenReturn(Optional.of(qualification));
        doNothing().when(qualificationRepository).deleteById(1L);
        
        // 执行
        qualificationService.deleteQualification(1L);
        
        // 验证
        verify(qualificationRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void testGetQualificationsByEmployeeId_Success() {
        // 准备
        List<Qualification> qualifications = Collections.singletonList(qualification);
        when(qualificationRepository.findByEmployeeId(1L)).thenReturn(qualifications);
        
        // 执行
        List<QualificationDTO> result = qualificationService.getQualificationsByEmployeeId(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getEmployeeId());
    }
    
    @Test
    void testGetQualificationsByTypeId_Success() {
        // 准备
        List<Qualification> qualifications = Collections.singletonList(qualification);
        when(qualificationRepository.findByTypeId(1L)).thenReturn(qualifications);
        
        // 执行
        List<QualificationDTO> result = qualificationService.getQualificationsByTypeId(1L);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getTypeId());
    }
    
    @Test
    void testGetExpiringQualifications_Success() {
        // 准备
        LocalDate futureDate = LocalDate.now().plusDays(15);
        List<Qualification> qualifications = Collections.singletonList(qualification);
        when(qualificationRepository.findByExpiryDateBetween(any(LocalDate.class), eq(futureDate))).thenReturn(qualifications);
        
        // 执行
        List<QualificationDTO> result = qualificationService.getExpiringQualifications(30);
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetExpiredQualifications_Success() {
        // 准备
        List<Qualification> qualifications = Collections.singletonList(qualification);
        when(qualificationRepository.findByExpiryDateBefore(any(LocalDate.class))).thenReturn(qualifications);
        
        // 执行
        List<QualificationDTO> result = qualificationService.getExpiredQualifications();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    void testGetAllQualifications_Success() {
        // 准备
        List<Qualification> qualifications = Collections.singletonList(qualification);
        when(qualificationRepository.findAll()).thenReturn(qualifications);
        
        // 执行
        List<QualificationDTO> result = qualificationService.getAllQualifications();
        
        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}