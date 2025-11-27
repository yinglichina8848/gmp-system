package com.gmp.training.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * 基础服务接口，定义所有服务的通用方法
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @author GMP系统开发团队
 */
public interface BaseService<T, ID> {

    /**
     * 保存实体
     *
     * @param entity 实体对象
     * @return 保存后的实体
     */
    T save(T entity);

    /**
     * 批量保存实体
     *
     * @param entities 实体对象列表
     * @return 保存后的实体列表
     */
    List<T> saveAll(List<T> entities);

    /**
     * 根据ID查找实体
     *
     * @param id 实体ID
     * @return 实体对象，如果不存在则返回空
     */
    Optional<T> findById(ID id);

    /**
     * 根据ID查找实体，如果不存在则抛出异常
     *
     * @param id 实体ID
     * @return 实体对象
     * @throws com.gmp.training.exception.EntityNotFoundException 如果实体不存在
     */
    T getById(ID id);

    /**
     * 查询所有实体
     *
     * @return 实体列表
     */
    List<T> findAll();

    /**
     * 根据ID列表查询实体
     *
     * @param ids ID列表
     * @return 实体列表
     */
    List<T> findAllById(List<ID> ids);

    /**
     * 分页查询
     *
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<T> findAll(Pageable pageable);

    /**
     * 排序查询
     *
     * @param sort 排序参数
     * @return 排序后的实体列表
     */
    List<T> findAll(Sort sort);

    /**
     * 判断实体是否存在
     *
     * @param id 实体ID
     * @return 是否存在
     */
    boolean existsById(ID id);

    /**
     * 统计实体数量
     *
     * @return 实体数量
     */
    long count();

    /**
     * 根据ID删除实体
     *
     * @param id 实体ID
     */
    void deleteById(ID id);

    /**
     * 删除实体
     *
     * @param entity 实体对象
     */
    void delete(T entity);

    /**
     * 批量删除实体
     *
     * @param entities 实体列表
     */
    void deleteAll(List<T> entities);

    /**
     * 删除所有实体
     */
    void deleteAll();
}
