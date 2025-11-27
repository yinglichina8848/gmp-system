package com.gmp.hr.repository;

import com.gmp.hr.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 部门数据访问接口
 * 
 * @author GMP Team
 * @version 1.0.0
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    
    /**
     * 根据部门代码查找部门
     * 
     * @param departmentCode 部门代码
     * @return 部门信息
     */
    Optional<Department> findByDepartmentCodeAndDeletedFalse(String departmentCode);
    
    /**
     * 根据部门名称查找部门
     * 
     * @param departmentName 部门名称
     * @return 部门信息
     */
    Optional<Department> findByDepartmentNameAndDeletedFalse(String departmentName);
    
    /**
     * 查找所有顶级部门（无父部门）
     * 
     * @return 顶级部门列表
     */
    List<Department> findByParentIsNullAndDeletedFalse();
    
    /**
     * 根据父部门ID查找子部门
     * 
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<Department> findByParentIdAndDeletedFalse(Long parentId);
    
    /**
     * 根据部门经理ID查找部门
     * 
     * @param managerId 部门经理ID
     * @return 部门信息
     */
    Optional<Department> findByManagerIdAndDeletedFalse(Long managerId);
    
    /**
     * 软删除部门
     * 
     * @param id 部门ID
     * @return 更新记录数
     */
    @Modifying
    @Query("UPDATE Department d SET d.deleted = true WHERE d.id = :id")
    int softDeleteById(Long id);
    
    /**
     * 查找部门树结构中的所有部门
     * 
     * @param rootId 根部门ID
     * @return 部门列表
     */
    @Query(value = "WITH RECURSIVE dept_tree AS (" +
                   "    SELECT * FROM hr_department WHERE id = :rootId AND deleted = false " +
                   "    UNION ALL " +
                   "    SELECT d.* FROM hr_department d " +
                   "    JOIN dept_tree dt ON d.parent_id = dt.id " +
                   "    WHERE d.deleted = false " +
                   ") SELECT * FROM dept_tree", nativeQuery = true)
    List<Department> findDepartmentTree(Long rootId);
}