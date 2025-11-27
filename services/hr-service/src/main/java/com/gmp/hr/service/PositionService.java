package com.gmp.hr.service;

import com.gmp.hr.dto.PositionDTO;

import java.util.List;

/**
 * 职位服务接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
public interface PositionService {
    
    /**
     * 创建职位
     * 
     * @param positionDTO 职位DTO
     * @return 创建的职位DTO
     */
    PositionDTO createPosition(PositionDTO positionDTO);
    
    /**
     * 根据ID获取职位
     * 
     * @param id 职位ID
     * @return 职位DTO
     */
    PositionDTO getPositionById(Long id);
    
    /**
     * 根据职位代码获取职位
     * 
     * @param positionCode 职位代码
     * @return 职位DTO
     */
    PositionDTO getPositionByCode(String positionCode);
    
    /**
     * 更新职位
     * 
     * @param id 职位ID
     * @param positionDTO 职位DTO
     * @return 更新后的职位DTO
     */
    PositionDTO updatePosition(Long id, PositionDTO positionDTO);
    
    /**
     * 删除职位
     * 
     * @param id 职位ID
     */
    void deletePosition(Long id);
    
    /**
     * 获取所有职位
     * 
     * @return 职位DTO列表
     */
    List<PositionDTO> getAllPositions();
    
    /**
     * 根据部门ID获取职位
     * 
     * @param departmentId 部门ID
     * @return 职位DTO列表
     */
    List<PositionDTO> getPositionsByDepartment(Long departmentId);
    
    /**
     * 根据职位等级获取职位
     * 
     * @param level 职位等级
     * @return 职位DTO列表
     */
    List<PositionDTO> getPositionsByLevel(Integer level);
    
    /**
     * 根据职位状态获取职位
     * 
     * @param status 职位状态
     * @return 职位DTO列表
     */
    List<PositionDTO> getPositionsByStatus(String status);
}