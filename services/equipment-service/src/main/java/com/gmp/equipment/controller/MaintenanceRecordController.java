package com.gmp.equipment.controller;

import com.gmp.equipment.dto.MaintenanceRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.MaintenanceRecordService;
import com.gmp.equipment.util.ResponseResult;
import com.gmp.equipment.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 设备维护记录控制器
 * 提供维护记录相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/maintenance-records")
@Api(tags = "设备维护记录管理")
public class MaintenanceRecordController {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceRecordController.class);

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    @ApiOperation("根据ID获取维护记录")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<MaintenanceRecordDTO>> getById(@PathVariable Long id) {
        logger.info("根据ID获取维护记录: id = {}", id);
        try {
            MaintenanceRecordDTO maintenanceRecordDTO = maintenanceRecordService.getById(id);
            return ResponseUtil.success(maintenanceRecordDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取维护记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据维护编号获取维护记录")
    @GetMapping("/maintenance-number/{maintenanceNumber}")
    public ResponseEntity<ResponseResult<MaintenanceRecordDTO>> getByMaintenanceNumber(@PathVariable String maintenanceNumber) {
        logger.info("根据维护编号获取维护记录: maintenanceNumber = {}", maintenanceNumber);
        try {
            MaintenanceRecordDTO maintenanceRecordDTO = maintenanceRecordService.getByMaintenanceNumber(maintenanceNumber);
            return ResponseUtil.success(maintenanceRecordDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取维护记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("分页查询维护记录")
    @GetMapping("/page")
    public ResponseEntity<ResponseResult<PageResultDTO<MaintenanceRecordDTO>>> listPage(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        logger.info("分页查询维护记录: equipmentId={}, pageNum={}, pageSize={}", equipmentId, pageNum, pageSize);
        try {
            PageResultDTO<MaintenanceRecordDTO> pageResult = maintenanceRecordService.listPage(equipmentId, pageNum, pageSize);
            return ResponseUtil.success(pageResult);
        } catch (Exception e) {
            logger.error("分页查询维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据设备ID获取维护记录列表")
    @GetMapping("/by-equipment/{equipmentId}")
    public ResponseEntity<ResponseResult<List<MaintenanceRecordDTO>>> listByEquipmentId(@PathVariable Long equipmentId) {
        logger.info("根据设备ID获取维护记录列表: equipmentId = {}", equipmentId);
        try {
            List<MaintenanceRecordDTO> maintenanceRecordList = maintenanceRecordService.listByEquipmentId(equipmentId);
            return ResponseUtil.success(maintenanceRecordList);
        } catch (Exception e) {
            logger.error("获取维护记录列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据维护计划ID获取维护记录列表")
    @GetMapping("/by-plan/{maintenancePlanId}")
    public ResponseEntity<ResponseResult<List<MaintenanceRecordDTO>>> listByMaintenancePlanId(@PathVariable Long maintenancePlanId) {
        logger.info("根据维护计划ID获取维护记录列表: maintenancePlanId = {}", maintenancePlanId);
        try {
            List<MaintenanceRecordDTO> maintenanceRecordList = maintenanceRecordService.listByMaintenancePlanId(maintenancePlanId);
            return ResponseUtil.success(maintenanceRecordList);
        } catch (Exception e) {
            logger.error("获取维护记录列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据日期范围获取维护记录列表")
    @GetMapping("/by-date-range")
    public ResponseEntity<ResponseResult<List<MaintenanceRecordDTO>>> listByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) throws ParseException {
        logger.info("根据日期范围获取维护记录列表: startDate={}, endDate={}", startDate, endDate);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            List<MaintenanceRecordDTO> maintenanceRecordList = maintenanceRecordService.listByDateRange(start, end);
            return ResponseUtil.success(maintenanceRecordList);
        } catch (ParseException e) {
            logger.error("日期格式解析错误: {}", e.getMessage());
            return ResponseUtil.error(400, "日期格式错误，请使用yyyy-MM-dd格式");
        } catch (Exception e) {
            logger.error("获取维护记录列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取设备最新维护记录")
    @GetMapping("/latest/{equipmentId}")
    public ResponseEntity<ResponseResult<MaintenanceRecordDTO>> getLatestByEquipmentId(@PathVariable Long equipmentId) {
        logger.info("获取设备最新维护记录: equipmentId = {}", equipmentId);
        try {
            MaintenanceRecordDTO maintenanceRecordDTO = maintenanceRecordService.getLatestByEquipmentId(equipmentId);
            return ResponseUtil.success(maintenanceRecordDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取最新维护记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取最新维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("检查维护编号是否已存在")
    @GetMapping("/check-maintenance-number")
    public ResponseEntity<ResponseResult<Boolean>> checkMaintenanceNumber(@RequestParam String maintenanceNumber, @RequestParam(required = false) Long id) {
        logger.info("检查维护编号是否已存在: maintenanceNumber = {}, id = {}", maintenanceNumber, id);
        try {
            boolean exists = maintenanceRecordService.existsByMaintenanceNumber(maintenanceNumber, id);
            return ResponseUtil.success(exists);
        } catch (Exception e) {
            logger.error("检查维护编号异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("创建维护记录")
    @PostMapping
    public ResponseEntity<ResponseResult<MaintenanceRecordDTO>> create(@RequestBody MaintenanceRecordDTO maintenanceRecordDTO) {
        logger.info("创建维护记录: maintenanceRecordDTO = {}", maintenanceRecordDTO);
        try {
            MaintenanceRecordDTO createdMaintenanceRecord = maintenanceRecordService.create(maintenanceRecordDTO);
            return ResponseUtil.success("维护记录创建成功", createdMaintenanceRecord);
        } catch (EquipmentServiceException e) {
            logger.error("创建维护记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("创建维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("更新维护记录")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseResult<MaintenanceRecordDTO>> update(@PathVariable Long id, @RequestBody MaintenanceRecordDTO maintenanceRecordDTO) {
        logger.info("更新维护记录: id = {}, maintenanceRecordDTO = {}", id, maintenanceRecordDTO);
        try {
            MaintenanceRecordDTO updatedMaintenanceRecord = maintenanceRecordService.update(id, maintenanceRecordDTO);
            return ResponseUtil.success("维护记录更新成功", updatedMaintenanceRecord);
        } catch (EquipmentServiceException e) {
            logger.error("更新维护记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("删除维护记录")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResult<Boolean>> delete(@PathVariable Long id) {
        logger.info("删除维护记录: id = {}", id);
        try {
            maintenanceRecordService.delete(id);
            return ResponseUtil.success("维护记录删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("删除维护记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("删除维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("批量删除维护记录")
    @DeleteMapping("/batch")
    public ResponseEntity<ResponseResult<Boolean>> deleteBatch(@RequestBody List<Long> ids) {
        logger.info("批量删除维护记录: ids = {}", ids);
        try {
            maintenanceRecordService.deleteBatch(ids);
            return ResponseUtil.success("批量删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("批量删除维护记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("批量删除维护记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}