package com.gmp.hr.controller;

import com.gmp.hr.dto.QualificationTypeDTO;
import com.gmp.hr.service.QualificationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资质类型管理控制器
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/hr/qualification-types")
public class QualificationTypeController {

    @Autowired
    private QualificationTypeService qualificationTypeService;
    
    /**
     * 创建资质类型
     * 
     * @param qualificationTypeDTO 资质类型DTO
     * @return 创建的资质类型DTO
     */
    @PostMapping
    public ResponseEntity<QualificationTypeDTO> createQualificationType(@RequestBody QualificationTypeDTO qualificationTypeDTO) {
        QualificationTypeDTO createdType = qualificationTypeService.createQualificationType(qualificationTypeDTO);
        return new ResponseEntity<>(createdType, HttpStatus.CREATED);
    }
    
    /**
     * 根据ID获取资质类型
     * 
     * @param id 资质类型ID
     * @return 资质类型DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<QualificationTypeDTO> getQualificationTypeById(@PathVariable Long id) {
        QualificationTypeDTO typeDTO = qualificationTypeService.getQualificationTypeById(id);
        return ResponseEntity.ok(typeDTO);
    }
    
    /**
     * 根据代码获取资质类型
     * 
     * @param code 资质类型代码
     * @return 资质类型DTO
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<QualificationTypeDTO> getQualificationTypeByCode(@PathVariable String code) {
        QualificationTypeDTO typeDTO = qualificationTypeService.getQualificationTypeByCode(code);
        return ResponseEntity.ok(typeDTO);
    }
    
    /**
     * 更新资质类型
     * 
     * @param id 资质类型ID
     * @param qualificationTypeDTO 资质类型DTO
     * @return 更新后的资质类型DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<QualificationTypeDTO> updateQualificationType(@PathVariable Long id, @RequestBody QualificationTypeDTO qualificationTypeDTO) {
        QualificationTypeDTO updatedType = qualificationTypeService.updateQualificationType(id, qualificationTypeDTO);
        return ResponseEntity.ok(updatedType);
    }
    
    /**
     * 删除资质类型
     * 
     * @param id 资质类型ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQualificationType(@PathVariable Long id) {
        qualificationTypeService.deleteQualificationType(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 获取所有资质类型
     * 
     * @return 资质类型DTO列表
     */
    @GetMapping
    public ResponseEntity<List<QualificationTypeDTO>> getAllQualificationTypes() {
        List<QualificationTypeDTO> types = qualificationTypeService.getAllQualificationTypes();
        return ResponseEntity.ok(types);
    }
    
    /**
     * 根据名称模糊查询资质类型
     * 
     * @param name 资质类型名称
     * @return 资质类型DTO列表
     */
    @GetMapping("/search/name")
    public ResponseEntity<List<QualificationTypeDTO>> getQualificationTypesByName(@RequestParam String name) {
        List<QualificationTypeDTO> types = qualificationTypeService.getQualificationTypesByName(name);
        return ResponseEntity.ok(types);
    }
    
    /**
     * 获取需更新的资质类型
     * 
     * @return 资质类型DTO列表
     */
    @GetMapping("/need-update")
    public ResponseEntity<List<QualificationTypeDTO>> getQualificationTypesNeedingUpdate() {
        List<QualificationTypeDTO> types = qualificationTypeService.getQualificationTypesNeedingUpdate();
        return ResponseEntity.ok(types);
    }
}