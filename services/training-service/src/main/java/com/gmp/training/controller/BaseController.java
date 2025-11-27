package com.gmp.training.controller;

import com.gmp.training.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.Serializable;
import java.util.List;

/**
 * 基础控制器类，提供通用的CRUD操作接口
 *
 * @param <T> 实体类型
 * @param <ID> 主键类型
 * @author GMP系统开发团队
 */
public abstract class BaseController<T, ID extends Serializable> {

    @Autowired
    protected BaseService<T, ID> service;

    /**
     * 获取所有实体列表
     *
     * @return 实体列表
     */
    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * 分页获取实体列表
     *
     * @param pageable 分页参数
     * @return 分页实体列表
     */
    @GetMapping("/page")
    public ResponseEntity<Page<T>> getPage(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    /**
     * 根据ID获取实体
     *
     * @param id 实体ID
     * @return 实体
     */
    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    /**
     * 保存新实体
     *
     * @param entity 实体对象
     * @return 保存后的实体
     */
    @PostMapping
    public ResponseEntity<T> create(@Valid @RequestBody T entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    /**
     * 更新实体
     *
     * @param id 实体ID
     * @param entity 更新后的实体对象
     * @return 更新后的实体
     */
    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @Valid @RequestBody T entity) {
        // 确保更新的是指定ID的实体
        // 这里需要根据具体实体类型实现ID设置逻辑
        return ResponseEntity.ok(service.save(entity));
    }

    /**
     * 删除实体
     *
     * @param id 实体ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 批量删除实体
     *
     * @param ids 实体ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Void> deleteBatch(@RequestBody List<ID> ids) {
        service.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * 根据ID列表获取实体列表
     *
     * @param ids 实体ID列表
     * @return 实体列表
     */
    @GetMapping("/batch")
    public ResponseEntity<List<T>> getBatch(@RequestParam List<ID> ids) {
        return ResponseEntity.ok(service.findAllById(ids));
    }
}