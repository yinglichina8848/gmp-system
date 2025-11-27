package com.gmp.equipment.controller;

import com.gmp.equipment.dto.EquipmentTypeDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.EquipmentTypeService;
import com.gmp.equipment.util.ResponseResult;
import com.gmp.equipment.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备类型控制器
 * 提供设备类型相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/equipment-types")
@Api(tags = "设备类型管理")
public class EquipmentTypeController {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentTypeController.class);

    @Autowired
    private EquipmentTypeService equipmentTypeService;

    @ApiOperation("根据ID获取设备类型")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<EquipmentTypeDTO>> getById(@PathVariable Long id) {
        logger.info("根据ID获取设备类型: id = {}", id);
        try {
            EquipmentTypeDTO equipmentTypeDTO = equipmentTypeService.getById(id);
            return ResponseUtil.success(equipmentTypeDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据编码获取设备类型")
    @GetMapping("/code/{code}")
    public ResponseEntity<ResponseResult<EquipmentTypeDTO>> getByCode(@PathVariable String code) {
        logger.info("根据编码获取设备类型: code = {}", code);
        try {
            EquipmentTypeDTO equipmentTypeDTO = equipmentTypeService.getByCode(code);
            return ResponseUtil.success(equipmentTypeDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据名称获取设备类型")
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseResult<EquipmentTypeDTO>> getByName(@PathVariable String name) {
        logger.info("根据名称获取设备类型: name = {}", name);
        try {
            EquipmentTypeDTO equipmentTypeDTO = equipmentTypeService.getByName(name);
            return ResponseUtil.success(equipmentTypeDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("分页查询设备类型")
    @GetMapping("/page")
    public ResponseEntity<ResponseResult<PageResultDTO<EquipmentTypeDTO>>> listPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        logger.info("分页查询设备类型: keyword={}, pageNum={}, pageSize={}", keyword, pageNum, pageSize);
        try {
            PageResultDTO<EquipmentTypeDTO> pageResult = equipmentTypeService.listPage(keyword, pageNum, pageSize);
            return ResponseUtil.success(pageResult);
        } catch (Exception e) {
            logger.error("分页查询设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取所有设备类型")
    @GetMapping
    public ResponseEntity<ResponseResult<List<EquipmentTypeDTO>>> listAll() {
        logger.info("获取所有设备类型");
        try {
            List<EquipmentTypeDTO> equipmentTypeList = equipmentTypeService.listAll();
            return ResponseUtil.success(equipmentTypeList);
        } catch (Exception e) {
            logger.error("获取设备类型列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取启用的设备类型列表")
    @GetMapping("/enabled")
    public ResponseEntity<ResponseResult<List<EquipmentTypeDTO>>> listEnabled() {
        logger.info("获取启用的设备类型列表");
        try {
            List<EquipmentTypeDTO> equipmentTypeList = equipmentTypeService.listEnabled();
            return ResponseUtil.success(equipmentTypeList);
        } catch (Exception e) {
            logger.error("获取设备类型列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("检查编码是否已存在")
    @GetMapping("/check-code")
    public ResponseEntity<ResponseResult<Boolean>> checkCode(@RequestParam String code, @RequestParam(required = false) Long id) {
        logger.info("检查编码是否已存在: code = {}, id = {}", code, id);
        try {
            boolean exists = equipmentTypeService.existsByCode(code, id);
            return ResponseUtil.success(exists);
        } catch (Exception e) {
            logger.error("检查编码异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("检查名称是否已存在")
    @GetMapping("/check-name")
    public ResponseEntity<ResponseResult<Boolean>> checkName(@RequestParam String name, @RequestParam(required = false) Long id) {
        logger.info("检查名称是否已存在: name = {}, id = {}", name, id);
        try {
            boolean exists = equipmentTypeService.existsByName(name, id);
            return ResponseUtil.success(exists);
        } catch (Exception e) {
            logger.error("检查名称异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("创建设备类型")
    @PostMapping
    public ResponseEntity<ResponseResult<EquipmentTypeDTO>> create(@RequestBody EquipmentTypeDTO equipmentTypeDTO) {
        logger.info("创建设备类型: equipmentTypeDTO = {}", equipmentTypeDTO);
        try {
            EquipmentTypeDTO createdEquipmentType = equipmentTypeService.create(equipmentTypeDTO);
            return ResponseUtil.success("设备类型创建成功", createdEquipmentType);
        } catch (EquipmentServiceException e) {
            logger.error("创建设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("创建设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("更新设备类型")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseResult<EquipmentTypeDTO>> update(@PathVariable Long id, @RequestBody EquipmentTypeDTO equipmentTypeDTO) {
        logger.info("更新设备类型: id = {}, equipmentTypeDTO = {}", id, equipmentTypeDTO);
        try {
            EquipmentTypeDTO updatedEquipmentType = equipmentTypeService.update(id, equipmentTypeDTO);
            return ResponseUtil.success("设备类型更新成功", updatedEquipmentType);
        } catch (EquipmentServiceException e) {
            logger.error("更新设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("启用设备类型")
    @PutMapping("/{id}/enable")
    public ResponseEntity<ResponseResult<EquipmentTypeDTO>> enable(@PathVariable Long id) {
        logger.info("启用设备类型: id = {}", id);
        try {
            EquipmentTypeDTO equipmentTypeDTO = equipmentTypeService.enable(id);
            return ResponseUtil.success("设备类型启用成功", equipmentTypeDTO);
        } catch (EquipmentServiceException e) {
            logger.error("启用设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("启用设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("禁用设备类型")
    @PutMapping("/{id}/disable")
    public ResponseEntity<ResponseResult<EquipmentTypeDTO>> disable(@PathVariable Long id) {
        logger.info("禁用设备类型: id = {}", id);
        try {
            EquipmentTypeDTO equipmentTypeDTO = equipmentTypeService.disable(id);
            return ResponseUtil.success("设备类型禁用成功", equipmentTypeDTO);
        } catch (EquipmentServiceException e) {
            logger.error("禁用设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("禁用设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("删除设备类型")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResult<Boolean>> delete(@PathVariable Long id) {
        logger.info("删除设备类型: id = {}", id);
        try {
            equipmentTypeService.delete(id);
            return ResponseUtil.success("设备类型删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("删除设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("删除设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("批量删除设备类型")
    @DeleteMapping("/batch")
    public ResponseEntity<ResponseResult<Boolean>> deleteBatch(@RequestBody List<Long> ids) {
        logger.info("批量删除设备类型: ids = {}", ids);
        try {
            equipmentTypeService.deleteBatch(ids);
            return ResponseUtil.success("批量删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("批量删除设备类型失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("批量删除设备类型异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}