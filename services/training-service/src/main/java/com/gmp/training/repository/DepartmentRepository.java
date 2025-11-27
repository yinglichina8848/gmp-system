package com.gmp.training.repository;

import com.gmp.training.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 部门数据访问接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

        /**
         * 根据部门名称模糊查询
         * 
         * @param departmentName 部门名称
         * @return 部门列表
         */
        List<Department> findByDepartmentNameContaining(String departmentName);

        /**
         * 根据部门状态查询
         * 
         * @param status 部门状态
         * @return 部门列表
         */
        List<Department> findByStatus(Department.Status status);

        /**
         * 根据部门状态分页查询
         * 
         * @param status   部门状态
         * @param pageable 分页参数
         * @return 部门分页结果
         */
        Page<Department> findByStatus(Department.Status status, Pageable pageable);

        /**
         * 根据上级部门ID查询子部门
         * 
         * @param parentId 上级部门ID
         * @return 子部门列表
         */
        List<Department> findByParentId(Long parentId);

        /**
         * 查询根部门（无上级部门）
         * 
         * @return 根部门列表
         */
        List<Department> findByParentIsNull();

        /**
         * 根据部门名称查询
         * 
         * @param departmentName 部门名称
         * @return 部门实体
         */
        Optional<Department> findByDepartmentName(String departmentName);

        /**
         * 根据部门编码查询
         * 
         * @param departmentCode 部门编码
         * @return 部门实体
         */
        Optional<Department> findByDepartmentCode(String departmentCode);

        /**
         * 检查部门名称是否存在
         * 
         * @param departmentName 部门名称
         * @return 是否存在
         */
        boolean existsByDepartmentName(String departmentName);

        /**
         * 检查部门名称是否存在（排除指定ID）
         * 
         * @param departmentName 部门名称
         * @param id             排除的部门ID
         * @return 是否存在
         */
        boolean existsByDepartmentNameAndIdNot(String departmentName, Long id);

        /**
         * 检查部门编码是否存在
         * 
         * @param departmentCode 部门编码
         * @return 是否存在
         */
        boolean existsByDepartmentCode(String departmentCode);

        /**
         * 检查部门编码是否存在（排除指定ID）
         * 
         * @param departmentCode 部门编码
         * @param id             排除的部门ID
         * @return 是否存在
         */
        boolean existsByDepartmentCodeAndIdNot(String departmentCode, Long id);

        /**
         * 根据部门负责人查询部门
         * 
         * @param managerId 部门负责人ID
         * @return 部门列表
         */
        List<Department> findByManagerId(Long managerId);

        /**
         * 根据GMP关联状态查询部门
         * 
         * @param isGmpRelated 是否GMP关联
         * @return 部门列表
         */
        @Query(value = "SELECT d FROM Department d WHERE d.isGmpRelated = :isGmpRelated")
        List<Department> findByIsGmpRelated(boolean isGmpRelated);

        /**
         * 根据GMP关联状态分页查询部门
         * 
         * @param isGmpRelated 是否GMP关联
         * @param pageable     分页参数
         * @return 部门分页结果
         */
        @Query(value = "SELECT d FROM Department d WHERE d.isGmpRelated = :isGmpRelated")
        Page<Department> findByIsGmpRelated(boolean isGmpRelated, Pageable pageable);

        /**
         * 分页查询所有部门（按排序号和创建时间排序）
         * 
         * @param pageable 分页参数
         * @return 部门分页结果
         */
        @Query(value = "SELECT d FROM Department d ORDER BY d.sortOrder ASC, d.createdTime DESC")
        Page<Department> findAllOrderBySortOrderAndCreatedTime(Pageable pageable);

        /**
         * 查询指定部门的所有祖先部门（通过递归查询）
         * 
         * @param id 部门ID
         * @return 祖先部门列表
         */
        @Query(value = "WITH RECURSIVE cte AS (" +
                        " SELECT * FROM t_department WHERE department_id = :id " +
                        " UNION ALL " +
                        " SELECT d.* FROM t_department d " +
                        " INNER JOIN cte ON d.department_id = cte.parent_id " +
                        ") " +
                        " SELECT * FROM cte WHERE department_id != :id ORDER BY department_id", nativeQuery = true)
        List<Department> findAncestorsById(Long id);

        /**
         * 查询指定部门的所有后代部门（通过递归查询）
         * 
         * @param id 部门ID
         * @return 后代部门列表
         */
        @Query(value = "WITH RECURSIVE cte AS (" +
                        " SELECT * FROM t_department WHERE parent_id = :id " +
                        " UNION ALL " +
                        " SELECT d.* FROM t_department d " +
                        " INNER JOIN cte ON d.parent_id = cte.department_id " +
                        ") " +
                        " SELECT * FROM cte ORDER BY department_id", nativeQuery = true)
        List<Department> findDescendantsById(Long id);
}