package com.gmp.auth.init;

import com.gmp.auth.config.AuthSystemProperties;
import com.gmp.auth.entity.Permission;
import com.gmp.auth.entity.Role;
import com.gmp.auth.entity.RolePermission;
import com.gmp.auth.repository.PermissionRepository;
import com.gmp.auth.repository.RolePermissionRepository;
import com.gmp.auth.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

/**
 * RolePermissionInitializer测试类
 * 测试角色权限初始化器的核心功能
 */
class RolePermissionInitializerTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private AuthSystemProperties authSystemProperties;

    @InjectMocks
    private RolePermissionInitializer rolePermissionInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitialize() {
        // 准备测试数据
        when(roleRepository.count()).thenReturn(0L);

        Role role = new Role();
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Permission permission = new Permission();
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        RolePermission rolePermission = new RolePermission();
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(rolePermission);

        // 执行测试
        rolePermissionInitializer.initialize();

        // 验证结果
        verify(roleRepository).count();
        verify(roleRepository, atLeastOnce()).save(any(Role.class));
        verify(permissionRepository, atLeastOnce()).save(any(Permission.class));
        verify(rolePermissionRepository, atLeastOnce()).save(any(RolePermission.class));
    }

    @Test
    void testInitializeWithExistingData() {
        // 准备测试数据 - 已有角色数据
        when(roleRepository.count()).thenReturn(5L);

        // 执行测试
        rolePermissionInitializer.initialize();

        // 验证结果 - 应该删除现有数据并重新初始化
        verify(roleRepository).count();
        verify(rolePermissionRepository).deleteAll();
        verify(permissionRepository).deleteAll();
        verify(roleRepository).deleteAll();
        verify(roleRepository, atLeastOnce()).save(any(Role.class));
    }

    @Test
    void testInitializeWithExistingDataAndReinitialize() {
        // 准备测试数据 - 已有角色数据
        when(roleRepository.count()).thenReturn(5L);

        Role role = new Role();
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Permission permission = new Permission();
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        RolePermission rolePermission = new RolePermission();
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(rolePermission);

        // 执行测试
        rolePermissionInitializer.initialize();

        // 验证结果 - 应该删除现有数据并重新初始化
        verify(roleRepository).count();
        verify(rolePermissionRepository).deleteAll();
        verify(permissionRepository).deleteAll();
        verify(roleRepository).deleteAll();
        verify(roleRepository, atLeastOnce()).save(any(Role.class));
    }
}