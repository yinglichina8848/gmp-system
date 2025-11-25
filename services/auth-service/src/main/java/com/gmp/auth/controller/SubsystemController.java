package com.gmp.auth.controller;

import com.gmp.auth.dto.ApiResponse;
import com.gmp.auth.entity.Subsystem;
import com.gmp.auth.entity.SubsystemPermission;
import com.gmp.auth.service.AuthService;
import com.gmp.auth.service.SubsystemService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 子系统管理API控制器
 *
 * @author GMP系统开发团队
 */
@RestController
@RequestMapping("/api/subsystems")
@CrossOrigin("*")
public class SubsystemController {
    private static final Logger log = LoggerFactory.getLogger(SubsystemController.class);

    @Autowired
    private SubsystemService subsystemService;
    
    @Autowired
    private AuthService authService;

    /**
     * 获取所有子系统列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Subsystem>>> getAllSubsystems() {
        try {
            List<Subsystem> subsystems = subsystemService.getAllSubsystems();
            return ResponseEntity.ok(ApiResponse.success("获取子系统列表成功", subsystems));
        } catch (Exception e) {
            log.error("获取子系统列表失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_SUBSYSTEMS_FAILED", "获取子系统列表失败"));
        }
    }

    /**
     * 获取启用的子系统列表
     */
    @GetMapping("/enabled")
    public ResponseEntity<ApiResponse<List<Subsystem>>> getEnabledSubsystems() {
        try {
            List<Subsystem> subsystems = subsystemService.getEnabledSubsystems();
            return ResponseEntity.ok(ApiResponse.success("获取启用子系统列表成功", subsystems));
        } catch (Exception e) {
            log.error("获取启用子系统列表失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_ENABLED_SUBSYSTEMS_FAILED", "获取启用子系统列表失败"));
        }
    }

    /**
     * 根据ID获取子系统详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Subsystem>> getSubsystemById(@PathVariable Long id) {
        try {
            Subsystem subsystem = subsystemService.getSubsystemById(id);
            return ResponseEntity.ok(ApiResponse.success("获取子系统详情成功", subsystem));
        } catch (Exception e) {
            log.error("获取子系统详情失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_SUBSYSTEM_FAILED", "获取子系统详情失败"));
        }
    }

    /**
     * 根据代码获取子系统详情
     */
    @GetMapping("/code/{subsystemCode}")
    public ResponseEntity<ApiResponse<Subsystem>> getSubsystemByCode(@PathVariable String subsystemCode) {
        try {
            Subsystem subsystem = subsystemService.getSubsystemByCode(subsystemCode);
            return ResponseEntity.ok(ApiResponse.success("获取子系统详情成功", subsystem));
        } catch (Exception e) {
            log.error("获取子系统详情失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_SUBSYSTEM_BY_CODE_FAILED", "获取子系统详情失败"));
        }
    }

    /**
     * 创建子系统
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Subsystem>> createSubsystem(@RequestBody Subsystem subsystem) {
        try {
            Subsystem createdSubsystem = subsystemService.createSubsystem(subsystem);
            return ResponseEntity.ok(ApiResponse.success("创建子系统成功", createdSubsystem));
        } catch (Exception e) {
            log.error("创建子系统失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("CREATE_SUBSYSTEM_FAILED", "创建子系统失败: " + e.getMessage()));
        }
    }

    /**
     * 更新子系统
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Subsystem>> updateSubsystem(
            @PathVariable Long id, 
            @RequestBody Subsystem subsystem) {
        try {
            // 不再调用setId方法，因为Subsystem类可能没有这个方法
            // 让service层处理id的设置
            Subsystem updatedSubsystem = subsystemService.updateSubsystem(subsystem);
            return ResponseEntity.ok(ApiResponse.success("更新子系统成功", updatedSubsystem));
        } catch (Exception e) {
            log.error("更新子系统失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("UPDATE_SUBSYSTEM_FAILED", "更新子系统失败: " + e.getMessage()));
        }
    }

    /**
     * 删除子系统
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubsystem(@PathVariable Long id) {
        try {
            subsystemService.deleteSubsystem(id);
            return ResponseEntity.ok(ApiResponse.success("删除子系统成功", null));
        } catch (Exception e) {
            log.error("删除子系统失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("DELETE_SUBSYSTEM_FAILED", "删除子系统失败"));
        }
    }

    /**
     * 切换子系统状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Subsystem>> toggleSubsystemStatus(
            @PathVariable Long id, 
            @RequestParam boolean enabled) {
        try {
            Subsystem subsystem = subsystemService.toggleSubsystemStatus(id, enabled);
            return ResponseEntity.ok(ApiResponse.success("切换子系统状态成功", subsystem));
        } catch (Exception e) {
            log.error("切换子系统状态失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("TOGGLE_SUBSYSTEM_STATUS_FAILED", "切换子系统状态失败"));
        }
    }

    /**
     * 获取子系统权限列表
     */
    @GetMapping("/{subsystemId}/permissions")
    public ResponseEntity<ApiResponse<List<SubsystemPermission>>> getSubsystemPermissions(@PathVariable Long subsystemId) {
        try {
            List<SubsystemPermission> permissions = subsystemService.getSubsystemPermissions(subsystemId);
            return ResponseEntity.ok(ApiResponse.success("获取子系统权限列表成功", permissions));
        } catch (Exception e) {
            log.error("获取子系统权限列表失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_SUBSYSTEM_PERMISSIONS_FAILED", "获取子系统权限列表失败"));
        }
    }

    /**
     * 为子系统添加权限
     */
    @PostMapping("/{subsystemId}/permissions")
    public ResponseEntity<ApiResponse<SubsystemPermission>> addPermissionToSubsystem(
            @PathVariable Long subsystemId, 
            @RequestParam Long permissionId, 
            @RequestParam Integer accessLevel) {
        try {
            SubsystemPermission subsystemPermission = subsystemService.addPermissionToSubsystem(subsystemId, permissionId, accessLevel);
            return ResponseEntity.ok(ApiResponse.success("添加子系统权限成功", subsystemPermission));
        } catch (Exception e) {
            log.error("添加子系统权限失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("ADD_SUBSYSTEM_PERMISSION_FAILED", "添加子系统权限失败: " + e.getMessage()));
        }
    }

    /**
     * 从子系统移除权限
     */
    @DeleteMapping("/{subsystemId}/permissions/{permissionId}")
    public ResponseEntity<ApiResponse<Void>> removePermissionFromSubsystem(
            @PathVariable Long subsystemId, 
            @PathVariable Long permissionId) {
        try {
            subsystemService.removePermissionFromSubsystem(subsystemId, permissionId);
            return ResponseEntity.ok(ApiResponse.success("移除子系统权限成功", null));
        } catch (Exception e) {
            log.error("移除子系统权限失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("REMOVE_SUBSYSTEM_PERMISSION_FAILED", "移除子系统权限失败"));
        }
    }

    /**
     * 更新子系统权限访问级别
     */
    @PutMapping("/{subsystemId}/permissions/{permissionId}/access-level")
    public ResponseEntity<ApiResponse<SubsystemPermission>> updateSubsystemPermissionAccessLevel(
            @PathVariable Long subsystemId, 
            @PathVariable Long permissionId, 
            @RequestParam Integer accessLevel) {
        try {
            SubsystemPermission subsystemPermission = subsystemService.updateSubsystemPermissionAccessLevel(subsystemId, permissionId, accessLevel);
            return ResponseEntity.ok(ApiResponse.success("更新子系统权限访问级别成功", subsystemPermission));
        } catch (Exception e) {
            log.error("更新子系统权限访问级别失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("UPDATE_ACCESS_LEVEL_FAILED", "更新子系统权限访问级别失败: " + e.getMessage()));
        }
    }

    /**
     * 检查用户对子系统的访问权限
     */
    @GetMapping("/check/{username}/access")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkSubsystemAccess(
            @PathVariable String username, 
            @RequestParam String subsystemCode) {
        try {
            boolean hasAccess = authService.hasSubsystemAccess(username, subsystemCode);
            Integer accessLevel = authService.getSubsystemAccessLevel(username, subsystemCode);
            
            Map<String, Object> result = Map.of(
                "username", username,
                "subsystemCode", subsystemCode,
                "hasAccess", hasAccess,
                "accessLevel", accessLevel
            );
            
            return ResponseEntity.ok(ApiResponse.success("子系统访问权限检查完成", result));
        } catch (Exception e) {
            log.error("检查子系统访问权限失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("CHECK_SUBSYSTEM_ACCESS_FAILED", "检查子系统访问权限失败"));
        }
    }

    /**
     * 获取用户可访问的子系统列表
     */
    @GetMapping("/accessible/{username}")
    public ResponseEntity<ApiResponse<List<String>>> getUserAccessibleSubsystems(@PathVariable String username) {
        try {
            // getUserAccessibleSubsystems返回的是List<String>，保持返回类型一致
            List<String> subsystemCodes = authService.getUserAccessibleSubsystems(username);
            return ResponseEntity.ok(ApiResponse.success("获取用户可访问子系统列表成功", subsystemCodes));
        } catch (Exception e) {
            log.error("获取用户可访问子系统列表失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_ACCESSIBLE_SUBSYSTEMS_FAILED", "获取用户可访问子系统列表失败"));
        }
    }

    /**
     * 获取用户可访问的子系统代码列表
     */
    @GetMapping("/accessible/{username}/codes")
    public ResponseEntity<ApiResponse<Set<String>>> getUserAccessibleSubsystemCodes(@PathVariable String username) {
        try {
            // 直接使用getUserAccessibleSubsystemCodes方法
            Set<String> subsystemCodes = authService.getUserAccessibleSubsystemCodes(username);
            return ResponseEntity.ok(ApiResponse.success("获取用户可访问子系统代码列表成功", subsystemCodes));
        } catch (Exception e) {
            log.error("获取用户可访问子系统代码列表失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_ACCESSIBLE_SUBSYSTEM_CODES_FAILED", "获取用户可访问子系统代码列表失败"));
        }
    }

    /**
     * 获取用户对子系统的访问级别映射
     */
    @GetMapping("/access-levels/{username}")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getUserSubsystemAccessLevels(@PathVariable String username) {
        try {
            Map<String, Integer> accessLevels = authService.getUserSubsystemAccessLevels(username);
            return ResponseEntity.ok(ApiResponse.success("获取用户子系统访问级别映射成功", accessLevels));
        } catch (Exception e) {
            log.error("获取用户子系统访问级别映射失败: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error("GET_SUBSYSTEM_ACCESS_LEVELS_FAILED", "获取用户子系统访问级别映射失败"));
        }
    }
}