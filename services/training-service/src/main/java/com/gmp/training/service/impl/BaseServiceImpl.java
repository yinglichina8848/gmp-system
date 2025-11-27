package com.gmp.training.service.impl;

import com.gmp.training.exception.EntityNotFoundException;
import com.gmp.training.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 基础服务实现类，提供通用的CRUD操作
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @param <R> Repository接口类型
 * @author GMP系统开发团队
 */
@Transactional(readOnly = true)
public abstract class BaseServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements BaseService<T, ID> {

    /**
     * 数据访问层接口
     */
    protected final R repository;

    /**
     * 构造函数
     *
     * @param repository 数据访问层接口
     */
    protected BaseServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    @Transactional
    public List<T> saveAll(List<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public T getById(ID id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(getEntityName() + " not found with id: " + id));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public List<T> findAllById(List<ID> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<T> findAll(Sort sort) {
        return repository.findAll(sort);
    }

    @Override
    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException(getEntityName() + " not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteAll(List<T> entities) {
        repository.deleteAll(entities);
    }

    @Override
    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    /**
     * 获取实体类名称，用于异常信息
     *
     * @return 实体类名称
     */
    protected abstract String getEntityName();
}
