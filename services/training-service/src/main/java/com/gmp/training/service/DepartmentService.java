package com.gmp.training.service;

import com.gmp.training.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 部门服务接口
 * 
 * @author GMP系统开发团队
 */
public interface DepartmentService {

    /**
     * 保存部门
     * 
     * @param department 部门实体
     * @return 保存后的部门实体
     */
    Department save(Department department);

    /**
     * 根据ID删除部门
     * 
     * @param id 部门ID
     */
    void deleteById(Long id);

    /**
     * 根据ID查询部门
     * 
     * @param id 部门ID
     * @return 部门实体
     */
    Optional<Department> findById(Long id);

    /**
     * 查询所有部门
     * 
     * @return 部门列表
     */
    List<Department> findAll();

    /**
     * 分页查询部门
     * 
     * @param pageable 分页参数
     * @return 部门分页结果
     */
    Page<Department> findAll(Pageable pageable);

    /**
     * 根据名称模糊查询部门
     * 
     * @param name 部门名称
     * @return 部门列表
     */
    List<Department> findByNameContaining(String name);

    /**
     * 查询所有启用的部门
     * 
     * @return 启用的部门列表
     */
    List<Department> findActiveDepartments();

    /**
     * 查询所有启用的部门（分页）
     * 
     * @param pageable 分页参数
     * @return 启用的部门分页结果
     */
    Page<Department> findActiveDepartments(Pageable pageable);

    /**
     * 根据上级部门ID查询子部门
     * 
     * @param parentId 上级部门ID
     * @return 子部门列表
     */
    List<Department> findChildrenByParentId(Long parentId);

    /**
     * 根据部门ID查询所有祖先部门
     * 
     * @param id 部门ID
     * @return 祖先部门列表
     */
    List<Department> findAncestorsById(Long id);

    /**
     * 根据部门ID查询所有后代部门
     * 
     * @param id 部门ID
     * @return 后代部门列表
     */
    List<Department> findDescendantsById(Long id);

    /**
     * 查询根部门（无上级部门的部门）
     * 
     * @return 根部门列表
     */
    List<Department> findRootDepartments();

    /**
     * 根据GMP关联状态查询部门
     * 
     * @param gmpRelated 是否GMP关联
     * @return 部门列表
     */
    List<Department> findByGmpRelated(boolean gmpRelated);

    /**
     * 根据GMP关联状态查询部门（分页）
     * 
     * @param gmpRelated 是否GMP关联
     * @param pageable   分页参数
     * @return 部门分页结果
     */
    Page<Department> findByGmpRelated(boolean gmpRelated, Pageable pageable);

    /**
     * 检查部门名称是否已存在
     * 
     * @param name      部门名称
     * @param excludeId 排除的部门ID（用于更新操作）
     * @return 是否已存在
     */
    boolean existsByName(String name, Long excludeId);

    /**
     * 检查部门名称是否已存在（新增操作）
     * 
     * @param name 部门名称
     * @return 是否已存在
     */
    boolean existsByName(String name);

    /**
     * 检查部门编码是否已存在
     * 
     * @param code      部门编码
     * @param excludeId 排除的部门ID（用于更新操作）
     * @return 是否已存在
     */
    boolean existsByCode(String code, Long excludeId);

    /**
     * 检查部门编码是否已存在（新增操作）
     * 
     * @param code 部门编码
     * @return 是否已存在
     */
    boolean existsByCode(String code);

    /**
     * 检查部门编码是否已存在（新增操作）
     * 
     * @param code 部门编码
     * @return 是否已存在
     */
    boolean existsByCode(String code, Long excludeId);

    /**
     * 更新部门状态
     * 
     * @param id     部门ID
     * @param status 部门状态
     */
    void updateStatus(Long id, Department.Status status);

    /**
     * 根据部门负责人查询部门
     * 
     * @param managerId 部门负责人ID
     * @return 部门列表
     */
    List<Department> findByManagerId(Long managerId);

    /**
     * 根据部门编码查询部门
     * 
     * @param code 部门编码
     * @return 部门实体
     */
    Optional<Department> findByCode(String code);
    
    /**
     * 更新部门状态
     * 
     * @param id 部门ID
     * @param active 是否激活
     * @return 更新后的部门
     */
    Department updateStatus(Long id, Boolean active);
    
    /**
     * 更新部门父级
     * 
     * @param id 部门ID
     * @param parentId 父部门ID
     * @return 更新后的部门
     */
    Department updateParent(Long id, Long parentId);
    
    /**
     * 批量禁用部门
     * 
     * @param ids 部门ID列表
     * @return 影响的行数
     */
    Integer batchDisable(List<Long> ids);
}