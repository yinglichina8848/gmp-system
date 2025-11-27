package com.gmp.equipment.controller;

import com.gmp.equipment.dto.CalibrationRecordDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.exception.EquipmentServiceException;
import com.gmp.equipment.service.CalibrationRecordService;
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
 * 设备校准记录控制器
 * 提供校准记录相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/calibration-records")
@Api(tags = "设备校准记录管理")
public class CalibrationRecordController {

    private static final Logger logger = LoggerFactory.getLogger(CalibrationRecordController.class);

    @Autowired
    private CalibrationRecordService calibrationRecordService;

    @ApiOperation("根据ID获取校准记录")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseResult<CalibrationRecordDTO>> getById(@PathVariable Long id) {
        logger.info("根据ID获取校准记录: id = {}", id);
        try {
            CalibrationRecordDTO calibrationRecordDTO = calibrationRecordService.getById(id);
            return ResponseUtil.success(calibrationRecordDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取校准记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据校准编号获取校准记录")
    @GetMapping("/calibration-number/{calibrationNumber}")
    public ResponseEntity<ResponseResult<CalibrationRecordDTO>> getByCalibrationNumber(@PathVariable String calibrationNumber) {
        logger.info("根据校准编号获取校准记录: calibrationNumber = {}", calibrationNumber);
        try {
            CalibrationRecordDTO calibrationRecordDTO = calibrationRecordService.getByCalibrationNumber(calibrationNumber);
            return ResponseUtil.success(calibrationRecordDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取校准记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("分页查询校准记录")
    @GetMapping("/page")
    public ResponseEntity<ResponseResult<PageResultDTO<CalibrationRecordDTO>>> listPage(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        logger.info("分页查询校准记录: equipmentId={}, pageNum={}, pageSize={}", equipmentId, pageNum, pageSize);
        try {
            PageResultDTO<CalibrationRecordDTO> pageResult = calibrationRecordService.listPage(equipmentId, pageNum, pageSize);
            return ResponseUtil.success(pageResult);
        } catch (Exception e) {
            logger.error("分页查询校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据设备ID获取校准记录列表")
    @GetMapping("/by-equipment/{equipmentId}")
    public ResponseEntity<ResponseResult<List<CalibrationRecordDTO>>> listByEquipmentId(@PathVariable Long equipmentId) {
        logger.info("根据设备ID获取校准记录列表: equipmentId = {}", equipmentId);
        try {
            List<CalibrationRecordDTO> calibrationRecordList = calibrationRecordService.listByEquipmentId(equipmentId);
            return ResponseUtil.success(calibrationRecordList);
        } catch (Exception e) {
            logger.error("获取校准记录列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("根据日期范围获取校准记录列表")
    @GetMapping("/by-date-range")
    public ResponseEntity<ResponseResult<List<CalibrationRecordDTO>>> listByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) throws ParseException {
        logger.info("根据日期范围获取校准记录列表: startDate={}, endDate={}", startDate, endDate);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            List<CalibrationRecordDTO> calibrationRecordList = calibrationRecordService.listByDateRange(start, end);
            return ResponseUtil.success(calibrationRecordList);
        } catch (ParseException e) {
            logger.error("日期格式解析错误: {}", e.getMessage());
            return ResponseUtil.error(400, "日期格式错误，请使用yyyy-MM-dd格式");
        } catch (Exception e) {
            logger.error("获取校准记录列表异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("获取设备最新校准记录")
    @GetMapping("/latest/{equipmentId}")
    public ResponseEntity<ResponseResult<CalibrationRecordDTO>> getLatestByEquipmentId(@PathVariable Long equipmentId) {
        logger.info("获取设备最新校准记录: equipmentId = {}", equipmentId);
        try {
            CalibrationRecordDTO calibrationRecordDTO = calibrationRecordService.getLatestByEquipmentId(equipmentId);
            return ResponseUtil.success(calibrationRecordDTO);
        } catch (EquipmentServiceException e) {
            logger.error("获取最新校准记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 404, e.getMessage());
        } catch (Exception e) {
            logger.error("获取最新校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("检查校准编号是否已存在")
    @GetMapping("/check-calibration-number")
    public ResponseEntity<ResponseResult<Boolean>> checkCalibrationNumber(@RequestParam String calibrationNumber, @RequestParam(required = false) Long id) {
        logger.info("检查校准编号是否已存在: calibrationNumber = {}, id = {}", calibrationNumber, id);
        try {
            boolean exists = calibrationRecordService.existsByCalibrationNumber(calibrationNumber, id);
            return ResponseUtil.success(exists);
        } catch (Exception e) {
            logger.error("检查校准编号异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("创建校准记录")
    @PostMapping
    public ResponseEntity<ResponseResult<CalibrationRecordDTO>> create(@RequestBody CalibrationRecordDTO calibrationRecordDTO) {
        logger.info("创建校准记录: calibrationRecordDTO = {}", calibrationRecordDTO);
        try {
            CalibrationRecordDTO createdCalibrationRecord = calibrationRecordService.create(calibrationRecordDTO);
            return ResponseUtil.success("校准记录创建成功", createdCalibrationRecord);
        } catch (EquipmentServiceException e) {
            logger.error("创建校准记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("创建校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("更新校准记录")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseResult<CalibrationRecordDTO>> update(@PathVariable Long id, @RequestBody CalibrationRecordDTO calibrationRecordDTO) {
        logger.info("更新校准记录: id = {}, calibrationRecordDTO = {}", id, calibrationRecordDTO);
        try {
            CalibrationRecordDTO updatedCalibrationRecord = calibrationRecordService.update(id, calibrationRecordDTO);
            return ResponseUtil.success("校准记录更新成功", updatedCalibrationRecord);
        } catch (EquipmentServiceException e) {
            logger.error("更新校准记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("更新校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("删除校准记录")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseResult<Boolean>> delete(@PathVariable Long id) {
        logger.info("删除校准记录: id = {}", id);
        try {
            calibrationRecordService.delete(id);
            return ResponseUtil.success("校准记录删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("删除校准记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("删除校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }

    @ApiOperation("批量删除校准记录")
    @DeleteMapping("/batch")
    public ResponseEntity<ResponseResult<Boolean>> deleteBatch(@RequestBody List<Long> ids) {
        logger.info("批量删除校准记录: ids = {}", ids);
        try {
            calibrationRecordService.deleteBatch(ids);
            return ResponseUtil.success("批量删除成功", true);
        } catch (EquipmentServiceException e) {
            logger.error("批量删除校准记录失败: {}", e.getMessage());
            return ResponseUtil.error(e.getErrorCode() != null ? Integer.parseInt(e.getErrorCode()) : 400, e.getMessage());
        } catch (Exception e) {
            logger.error("批量删除校准记录异常: {}", e.getMessage(), e);
            throw e;
        }
    }
}