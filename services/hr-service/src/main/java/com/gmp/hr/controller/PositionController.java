package com.gmp.hr.controller;

import com.gmp.hr.dto.PositionDTO;
import com.gmp.hr.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 职位管理控制器
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/hr/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    /**
     * 创建职位
     * 
     * @param positionDTO 职位DTO
     * @return 创建的职位DTO
     */
    @PostMapping
    public ResponseEntity<PositionDTO> createPosition(@RequestBody PositionDTO positionDTO) {
        PositionDTO createdPosition = positionService.createPosition(positionDTO);
        return new ResponseEntity<>(createdPosition, HttpStatus.CREATED);
    }

    /**
     * 根据ID获取职位
     * 
     * @param id 职位ID
     * @return 职位DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<PositionDTO> getPositionById(@PathVariable Long id) {
        PositionDTO positionDTO = positionService.getPositionById(id);
        return ResponseEntity.ok(positionDTO);
    }

    /**
     * 根据代码获取职位
     * 
     * @param code 职位代码
     * @return 职位DTO
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<PositionDTO> getPositionByCode(@PathVariable String code) {
        PositionDTO positionDTO = positionService.getPositionByCode(code);
        return ResponseEntity.ok(positionDTO);
    }

    /**
     * 更新职位
     * 
     * @param id          职位ID
     * @param positionDTO 职位DTO
     * @return 更新后的职位DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<PositionDTO> updatePosition(@PathVariable Long id, @RequestBody PositionDTO positionDTO) {
        PositionDTO updatedPosition = positionService.updatePosition(id, positionDTO);
        return ResponseEntity.ok(updatedPosition);
    }

    /**
     * 删除职位
     * 
     * @param id 职位ID
     * @return 响应状态
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable Long id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取所有职位
     * 
     * @return 职位DTO列表
     */
    @GetMapping
    public ResponseEntity<List<PositionDTO>> getAllPositions() {
        List<PositionDTO> positions = positionService.getAllPositions();
        return ResponseEntity.ok(positions);
    }

    /**
     * 根据部门获取职位列表
     * 
     * @param departmentId 部门ID
     * @return 职位DTO列表
     */
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<PositionDTO>> getPositionsByDepartment(@PathVariable Long departmentId) {
        List<PositionDTO> positions = positionService.getPositionsByDepartment(departmentId);
        return ResponseEntity.ok(positions);
    }

    /**
     * 根据等级获取职位列表
     * 
     * @param level 职位等级
     * @return 职位DTO列表
     */
    @GetMapping("/level/{level}")
    public ResponseEntity<List<PositionDTO>> getPositionsByLevel(@PathVariable String level) {
        // 将String类型转换为Integer类型
        Integer levelInt = Integer.parseInt(level);
        List<PositionDTO> positions = positionService.getPositionsByLevel(levelInt);
        return ResponseEntity.ok(positions);
    }

    /**
     * 根据状态获取职位列表
     * 
     * @param status 职位状态
     * @return 职位DTO列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PositionDTO>> getPositionsByStatus(@PathVariable String status) {
        List<PositionDTO> positions = positionService.getPositionsByStatus(status);
        return ResponseEntity.ok(positions);
    }
}