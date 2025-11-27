package com.gmp.hr.repository;

import com.gmp.hr.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 职位数据访问接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Repository
public interface PositionRepository extends JpaRepository<Position, Long>, JpaSpecificationExecutor<Position> {
    
    /**
     * 根据职位代码查找职位
     * 
     * @param positionCode 职位代码
     * @return 职位信息
     */
    Optional<Position> findByPositionCodeAndDeletedFalse(String positionCode);
    
    /**
     * 根据部门ID查找职位列表
     * 
     * @param departmentId 部门ID
     * @return 职位列表
     */
    List<Position> findByDepartmentIdAndDeletedFalse(Long departmentId);
    
    /**
     * 根据职位等级查找职位列表
     * 
     * @param level 职位等级
     * @return 职位列表
     */
    List<Position> findByLevelAndDeletedFalse(String level);
    
    /**
     * 根据职位状态查找职位列表
     * 
     * @param active 是否激活
     * @return 职位列表
     */
    List<Position> findByActiveAndDeletedFalse(Boolean active);
    
    /**
     * 软删除职位
     * 
     * @param id 职位ID
     * @return 更新记录数
     */
    @Modifying
    @Query("UPDATE Position p SET p.deleted = true WHERE p.id = :id")
    int softDeleteById(Long id);
}