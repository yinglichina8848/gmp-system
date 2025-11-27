package com.gmp.equipment.controller;

import com.gmp.equipment.dto.EquipmentDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.EquipmentService;
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
 * 设备控制器
 * 提供设备相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/equipment")
@Api(tags = "设备管理")
public class EquipmentController {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentController.class);

    @Autowired
    private EquipmentService equipmentService;

    @ApiOperation("根据ID获取设备")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<EquipmentDTO>> getById(@PathVariable Long id) {
        logger.info("根据ID获取设备: id = {}", id);
        try {
            EquipmentDTO equipmentDTO = equipmentService.getById(id);
            return ResponseUtil.success(equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据编码获取设备")
    @GetMapping("/code/{code}")
    public ResponseEntity<ResponseResult<EquipmentDTO>> getByCode(@PathVariable String code) {
        logger.info("根据编码获取设备: code = {}", code);
        try {
            EquipmentDTO equipmentDTO = equipmentService.getByCode(code);
            return ResponseUtil.success(equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据序列号获取设备")
    @GetMapping("/serial-number/{serialNumber}")
    public ResponseEntity<ResponseResult<EquipmentDTO>> getBySerialNumber(@PathVariable String serialNumber) {
        logger.info("根据序列号获取设备: serialNumber = {}", serialNumber);
        try {
            EquipmentDTO equipmentDTO = equipmentService.getBySerialNumber(serialNumber);
            return ResponseUtil.success(equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("分页查询设备")
    @GetMapping("/page")
    public ResponseEntity<ResponseResult<PageResultDTO<EquipmentDTO>>> listPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long equipmentTypeId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        logger.info("分页查询设备: keyword={}, equipmentTypeId={}, status={}, pageNum={}, pageSize={}",
                keyword, equipmentTypeId, status, pageNum, pageSize);
        try {
            PageResultDTO<EquipmentDTO> pageResult = equipmentService.listPage(keyword, equipmentTypeId, status, pageNum, pageSize);
            return ResponseUtil.success(pageResult);
        } catch (Exception e) {
            logger.error("分页查询设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取所有设备")
    @GetMapping
    public ResponseEntity<ResponseResult<List<EquipmentDTO>>> listAll() {
        logger.info("获取所有设备");
        try {
            List<EquipmentDTO> equipmentList = equipmentService.listAll();
            return ResponseUtil.success(equipmentList);
        } catch (Exception e) {
            logger.error("获取设备列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据设备类型获取设备列表")
    @GetMapping("/by-type/{equipmentTypeId}")
    public ResponseEntity<ResponseResult<List<EquipmentDTO>>> listByEquipmentTypeId(@PathVariable Long equipmentTypeId) {
        logger.info("根据设备类型获取设备列表: equipmentTypeId = {}", equipmentTypeId);
        try {
            List<EquipmentDTO> equipmentList = equipmentService.listByEquipmentTypeId(equipmentTypeId);
            return ResponseUtil.success(equipmentList);
        } catch (Exception e) {
            logger.error("获取设备列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取可用设备列表")
    @GetMapping("/available")
    public ResponseEntity<ResponseResult<List<EquipmentDTO>>> listAvailable() {
        logger.info("获取可用设备列表");
        try {
            List<EquipmentDTO> equipmentList = equipmentService.listAvailable();
            return ResponseUtil.success(equipmentList);
        } catch (Exception e) {
            logger.error("获取设备列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取需要校准的设备列表")
    @GetMapping("/need-calibration")
    public ResponseEntity<ResponseResult<List<EquipmentDTO>>> listNeedCalibration() {
        logger.info("获取需要校准的设备列表");
        try {
            List<EquipmentDTO> equipmentList = equipmentService.listNeedCalibration();
            return ResponseUtil.success(equipmentList);
        } catch (Exception e) {
            logger.error("获取设备列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取需要维护的设备列表")
    @GetMapping("/need-maintenance")
    public ResponseEntity<ResponseResult<List<EquipmentDTO>>> listNeedMaintenance() {
        logger.info("获取需要维护的设备列表");
        try {
            List<EquipmentDTO> equipmentList = equipmentService.listNeedMaintenance();
            return ResponseUtil.success(equipmentList);
        } catch (Exception e) {
            logger.error("获取设备列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("检查编码是否已存在")
    @GetMapping("/check-code")
    public ResponseEntity<ResponseResult<Boolean>> checkCode(@RequestParam String code, @RequestParam(required = false) Long id) {
        logger.info("检查编码是否已存在: code = {}, id = {}", code, id);
        try {
            boolean exists = equipmentService.existsByCode(code, id);
            return ResponseUtil.success(exists);
        } catch (Exception e) {
            logger.error("检查编码异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("检查序列号是否已存在")
    @GetMapping("/check-serial-number")
    public ResponseEntity<ResponseResult<Boolean>> checkSerialNumber(@RequestParam String serialNumber, @RequestParam(required = false) Long id) {
        logger.info("检查序列号是否已存在: serialNumber = {}, id = {}", serialNumber, id);
        try {
            boolean exists = equipmentService.existsBySerialNumber(serialNumber, id);
            return ResponseUtil.success(exists);
        } catch (Exception e) {
            logger.error("检查序列号异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("创建设备")
    @PostMapping
    public ResponseEntity<ResponseResult<EquipmentDTO>> create(@RequestBody EquipmentDTO equipmentDTO) {
        logger.info("创建设备: equipmentDTO = {}", equipmentDTO);
        try {
            EquipmentDTO createdEquipment = equipmentService.create(equipmentDTO);
            return ResponseUtil.success("设备创建成功", createdEquipment);
        } catch (EquipmentServiceException e) {
            logger.error("创建设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("创建设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("更新设备")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseResult<EquipmentDTO>> update(@PathVariable Long id, @RequestBody EquipmentDTO equipmentDTO) {
        logger.info("更新设备: id = {}, equipmentDTO = {}", id, equipmentDTO);
        try {
            EquipmentDTO updatedEquipment = equipmentService.update(id, equipmentDTO);
            return ResponseUtil.success("设备更新成功", updatedEquipment);
        } catch (EquipmentServiceException e) {
            logger.error("更新设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("启用设备")
    @PutMapping("/{id}/enable")
    public ResponseEntity<ResponseResult<EquipmentDTO>> enable(@PathVariable Long id) {
        logger.info("启用设备: id = {}", id);
        try {
            EquipmentDTO equipmentDTO = equipmentService.enable(id);
            return ResponseUtil.success("设备启用成功", equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("启用设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("启用设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("禁用设备")
    @PutMapping("/{id}/disable")
    public ResponseEntity<ResponseResult<EquipmentDTO>> disable(@PathVariable Long id) {
        logger.info("禁用设备: id = {}", id);
        try {
            EquipmentDTO equipmentDTO = equipmentService.disable(id);
            return ResponseUtil.success("设备禁用成功", equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("禁用设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("禁用设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("更新设备状态")
    @PutMapping("/{id}/status")
    public ResponseEntity<ResponseResult<EquipmentDTO>> updateStatus(@PathVariable Long id, @RequestParam String status) {
        logger.info("更新设备状态: id = {}, status = {}", id, status);
        try {
            EquipmentDTO equipmentDTO = equipmentService.updateStatus(id, status);
            return ResponseUtil.success("设备状态更新成功", equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("更新设备状态失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新设备状态异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("更新校准日期")
    @PutMapping("/{id}/calibration-date")
    public ResponseEntity<ResponseResult<EquipmentDTO>> updateCalibrationDate(@PathVariable Long id, @RequestParam String nextCalibrationDate) {
        logger.info("更新校准日期: id = {}, nextCalibrationDate = {}", id, nextCalibrationDate);
        try {
            EquipmentDTO equipmentDTO = equipmentService.updateNextCalibrationDate(id, nextCalibrationDate);
            return ResponseUtil.success("校准日期更新成功", equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("更新校准日期失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新校准日期异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("更新维护日期")
    @PutMapping("/{id}/maintenance-date")
    public ResponseEntity<ResponseResult<EquipmentDTO>> updateMaintenanceDate(@PathVariable Long id, @RequestParam String nextMaintenanceDate) {
        logger.info("更新维护日期: id = {}, nextMaintenanceDate = {}", id, nextMaintenanceDate);
        try {
            EquipmentDTO equipmentDTO = equipmentService.updateNextMaintenanceDate(id, nextMaintenanceDate);
            return ResponseUtil.success("维护日期更新成功", equipmentDTO);
        } catch (EquipmentServiceException e) {
            logger.error("更新维护日期失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新维护日期异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("删除设备")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResult<Boolean>> delete(@PathVariable Long id) {
        logger.info("删除设备: id = {}", id);
        try {
            equipmentService.delete(id);
            return ResponseUtil.success("设备删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("删除设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("删除设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("批量删除设备")
    @DeleteMapping("/batch")
    public ResponseEntity<ResponseResult<Boolean>> deleteBatch(@RequestBody List<Long> ids) {
        logger.info("批量删除设备: ids = {}", ids);
        try {
            equipmentService.deleteBatch(ids);
            return ResponseUtil.success("批量删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("批量删除设备失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("批量删除设备异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}