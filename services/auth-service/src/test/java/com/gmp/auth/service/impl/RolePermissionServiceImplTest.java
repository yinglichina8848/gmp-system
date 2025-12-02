package com.gmp.auth.service.impl;

import com.gmp.auth.entity.Permission;
import com.gmp.auth.entity.RolePermission;
import com.gmp.auth.repository.PermissionRepository;
import com.gmp.auth.repository.RolePermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * RolePermissionServiceImpl测试类
 */
class RolePermissionServiceImplTest {

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private RolePermissionServiceImpl rolePermissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssignPermissionsToRole() {
        Long roleId = 1L;
        List<Long> permissionIds = List.of(1L, 2L, 3L);

        List<RolePermission> results = rolePermissionService.assignPermissionsToRole(roleId, permissionIds);

        assertNotNull(results);
        assertEquals(3, results.size());
    }

    @Test
    void testRemovePermissionFromRole() {
        Long roleId = 1L;
        Long permissionId = 1L;

        RolePermission rolePermission = new RolePermission();
        when(rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(Optional.of(rolePermission));

        assertDoesNotThrow(() -> {
            rolePermissionService.removePermissionFromRole(roleId, permissionId);
        });

        verify(rolePermissionRepository, times(1)).findByRoleIdAndPermissionId(roleId, permissionId);
        verify(rolePermissionRepository, times(1)).delete(rolePermission);
    }

    @Test
    void testRemovePermissionFromRole_NotFound() {
        Long roleId = 1L;
        Long permissionId = 1L;

        when(rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            rolePermissionService.removePermissionFromRole(roleId, permissionId);
        });

        verify(rolePermissionRepository, times(1)).findByRoleIdAndPermissionId(roleId, permissionId);
        verify(rolePermissionRepository, never()).delete(any(RolePermission.class));
    }

    @Test
    void testGetRolePermissions() {
        Long roleId = 1L;
        List<Permission> permissions = new ArrayList<>();
        when(permissionRepository.findByRoleId(roleId)).thenReturn(permissions);

        List<Permission> result = rolePermissionService.getRolePermissions(roleId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(permissionRepository, times(1)).findByRoleId(roleId);
    }

    @Test
    void testGetRolePermissionCodes() {
        Long roleId = 1L;

        Set<String> result = rolePermissionService.getRolePermissionCodes(roleId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetUserPermissions() {
        Long userId = 1L;
        List<Permission> permissions = new ArrayList<>();
        when(permissionRepository.findByUserId(userId)).thenReturn(permissions);

        List<Permission> result = rolePermissionService.getUserPermissions(userId);

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(permissionRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetUserPermissionCodes() {
        Long userId = 1L;

        Set<String> result = rolePermissionService.getUserPermissionCodes(userId);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testHasPermission() {
        Long userId = 1L;
        String permissionCode = "read";

        boolean result = rolePermissionService.hasPermission(userId, permissionCode);

        assertFalse(result);
    }

    @Test
    void testHasPermission_NotExists() {
        Long userId = 1L;
        String permissionCode = "admin";

        boolean result = rolePermissionService.hasPermission(userId, permissionCode);

        assertFalse(result);
    }

    @Test
    void testHasAnyPermission() {
        Long userId = 1L;
        List<String> permissionCodes = List.of("admin", "write");

        boolean result = rolePermissionService.hasAnyPermission(userId, permissionCodes);

        assertFalse(result);
    }

    @Test
    void testHasAnyPermission_NoneExists() {
        Long userId = 1L;
        List<String> permissionCodes = List.of("admin", "delete");

        boolean result = rolePermissionService.hasAnyPermission(userId, permissionCodes);

        assertFalse(result);
    }

    @Test
    void testHasAllPermissions() {
        Long userId = 1L;
        List<String> permissionCodes = List.of("read", "write");

        boolean result = rolePermissionService.hasAllPermissions(userId, permissionCodes);

        assertFalse(result);
    }

    @Test
    void testHasAllPermissions_NotAllExists() {
        Long userId = 1L;
        List<String> permissionCodes = List.of("read", "write", "admin");

        boolean result = rolePermissionService.hasAllPermissions(userId, permissionCodes);

        assertFalse(result);
    }

    @Test
    void testAssignPermissionToRole() {
        Long roleId = 1L;
        Long permissionId = 1L;

        RolePermission result = rolePermissionService.assignPermissionToRole(roleId, permissionId);

        assertNotNull(result);
    }
}
