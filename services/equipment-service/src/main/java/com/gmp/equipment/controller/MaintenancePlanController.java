package com.gmp.equipment.controller;

import com.gmp.equipment.dto.MaintenancePlanDTO;
import com.gmp.equipment.dto.PageResultDTO;
import com.gmp.equipment.service.MaintenancePlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 设备维护计划控制器
 * 提供维护计划相关的RESTful API接口
 */
@RestController
@RequestMapping("/api/maintenance-plans")
@Api(tags = "设备维护计划管理")
public class MaintenancePlanController {

    @Autowired
    private MaintenancePlanService maintenancePlanService;

    @ApiOperation("根据ID获取维护计划")
    @GetMapping("/{id}")
    public MaintenancePlanDTO getById(@PathVariable Long id) {
        return maintenancePlanService.getById(id);
    }

    @ApiOperation("根据计划编号获取维护计划")
    @GetMapping("/plan-number/{planNumber}")
    public MaintenancePlanDTO getByPlanNumber(@PathVariable String planNumber) {
        return maintenancePlanService.getByPlanNumber(planNumber);
    }

    @ApiOperation("分页查询维护计划")
    @GetMapping("/page")
    public PageResultDTO<MaintenancePlanDTO> listPage(
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return maintenancePlanService.listPage(equipmentId, status, pageNum, pageSize);
    }

    @ApiOperation("根据设备ID获取维护计划列表")
    @GetMapping("/by-equipment/{equipmentId}")
    public List<MaintenancePlanDTO> listByEquipmentId(@PathVariable Long equipmentId) {
        return maintenancePlanService.listByEquipmentId(equipmentId);
    }

    @ApiOperation("根据日期范围获取维护计划列表")
    @GetMapping("/by-date-range")
    public List<MaintenancePlanDTO> listByDateRange(
            @RequestParam String startDate, 
            @RequestParam String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);
        return maintenancePlanService.listByDateRange(start, end);
    }

    @ApiOperation("获取设备最新维护计划")
    @GetMapping("/latest/{equipmentId}")
    public MaintenancePlanDTO getLatestByEquipmentId(@PathVariable Long equipmentId) {
        return maintenancePlanService.getLatestByEquipmentId(equipmentId);
    }

    @ApiOperation("检查计划编号是否已存在")
    @GetMapping("/check-plan-number")
    public boolean checkPlanNumber(@RequestParam String planNumber, @RequestParam(required = false) Long id) {
        return maintenancePlanService.existsByPlanNumber(planNumber, id);
    }

    @ApiOperation("创建维护计划")
    @PostMapping
    public MaintenancePlanDTO create(@RequestBody MaintenancePlanDTO maintenancePlanDTO) {
        return maintenancePlanService.create(maintenancePlanDTO);
    }

    @ApiOperation("更新维护计划")
    @PutMapping("/{id}")
    public MaintenancePlanDTO update(@PathVariable Long id, @RequestBody MaintenancePlanDTO maintenancePlanDTO) {
        return maintenancePlanService.update(id, maintenancePlanDTO);
    }

    @ApiOperation("启动维护计划")
    @PutMapping("/{id}/start")
    public MaintenancePlanDTO start(@PathVariable Long id) {
        return maintenancePlanService.start(id);
    }

    @ApiOperation("暂停维护计划")
    @PutMapping("/{id}/pause")
    public MaintenancePlanDTO pause(@PathVariable Long id) {
        return maintenancePlanService.pause(id);
    }

    @ApiOperation("完成维护计划")
    @PutMapping("/{id}/complete")
    public MaintenancePlanDTO complete(@PathVariable Long id) {
        return maintenancePlanService.complete(id);
    }

    @ApiOperation("取消维护计划")
    @PutMapping("/{id}/cancel")
    public MaintenancePlanDTO cancel(@PathVariable Long id) {
        return maintenancePlanService.cancel(id);
    }

    @ApiOperation("删除维护计划")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        maintenancePlanService.delete(id);
    }

    @ApiOperation("批量删除维护计划")
    @DeleteMapping("/batch")
    public void deleteBatch(@RequestBody List<Long> ids) {
        maintenancePlanService.deleteBatch(ids);
    }
}