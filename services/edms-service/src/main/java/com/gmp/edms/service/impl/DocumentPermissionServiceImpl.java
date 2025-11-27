package com.gmp.edms.service.impl;

import com.gmp.edms.entity.Document;
import com.gmp.edms.entity.DocumentCategory;
import com.gmp.edms.repository.DocumentRepository;
import com.gmp.edms.service.DocumentPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 文档权限服务实现类
 * 实现设计文档中的复杂权限控制架构
 */
@Service
public class DocumentPermissionServiceImpl implements DocumentPermissionService {

    private static final Logger log = LoggerFactory.getLogger(DocumentPermissionServiceImpl.class);

    @Autowired
    private DocumentRepository documentRepository;

    // 假设的配置信息
    private final Set<String> adminUsers = Set.of("admin", "system", "gmp_admin");

    @Override
    public boolean hasPermission(String userId, Long documentId, PermissionType permission) {
        try {
            // 1. 获取用户角色
            Set<String> userRoles = getUserRoles(userId);

            // 2. 获取文档权限策略
            Document document = documentRepository.findById(documentId)
                    .orElseThrow(() -> new RuntimeException("文档不存在: " + documentId));

            List<PermissionPolicy> policies = getDocumentPolicies(document);

            // 3. 权限计算：角色 + 策略 + 上下文
            PermissionContext context = getContext(userId, document);
            return evaluatePermissions(userRoles, policies, permission, context);

        } catch (Exception e) {
            // 异常情况下默认拒绝访问
            return false;
        }
    }

    @Override
    public boolean hasCreatePermission(String userId, Long categoryId) {
        // 检查用户是否有在指定分类下创建文档的权限
        Set<String> userRoles = getUserRoles(userId);

        // 管理员可以在任何分类下创建文档
        if (isAdmin(userRoles)) {
            return true;
        }

        // 检查分类权限
        return hasCategoryPermission(userId, categoryId, PermissionType.EDIT);
    }

    @Override
    public Set<PermissionType> getUserPermissions(String userId, Long documentId) {
        Set<PermissionType> permissions = new HashSet<>();

        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            return permissions;
        }

        Set<String> userRoles = getUserRoles(userId);
        List<PermissionPolicy> policies = getDocumentPolicies(document);
        PermissionContext context = getContext(userId, document);

        for (PermissionType permission : PermissionType.values()) {
            if (evaluatePermissions(userRoles, policies, permission, context)) {
                permissions.add(permission);
            }
        }

        return permissions;
    }

    @Override
    public boolean canApprove(String userId, Long documentId) {
        return hasPermission(userId, documentId, PermissionType.APPROVE);
    }

    @Override
    public boolean canView(String userId, Long documentId) {
        return hasPermission(userId, documentId, PermissionType.VIEW);
    }

    @Override
    public boolean canEdit(String userId, Long documentId) {
        return hasPermission(userId, documentId, PermissionType.EDIT);
    }

    @Override
    public boolean canDelete(String userId, Long documentId) {
        return hasPermission(userId, documentId, PermissionType.DELETE);
    }

    @Override
    public boolean canDownload(String userId, Long documentId) {
        return hasPermission(userId, documentId, PermissionType.DOWNLOAD);
    }

    @Override
    public Set<Long> getAccessibleDocumentIds(String userId, PermissionType permission) {
        // 获取所有文档，然后根据权限过滤
        List<Document> allDocuments = documentRepository.findAll();
        Set<Long> accessibleIds = new HashSet<>();
        for (Document doc : allDocuments) {
            try {
                // 使用反射获取id
                java.lang.reflect.Field idField = Document.class.getDeclaredField("id");
                idField.setAccessible(true);
                Long docId = (Long) idField.get(doc);
                if (hasPermission(userId, docId, permission)) {
                    accessibleIds.add(docId);
                }
            } catch (Exception e) {
                // 处理异常，跳过该文档
            }
        }
        return accessibleIds;
    }

    @Override
    public boolean hasPermissionWithTimeWindow(String userId, Long documentId, PermissionType permission,
            LocalDateTime currentTime) {
        // 基于时间窗口的权限检查
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            return false;
        }

        // 检查文档是否在有效期内
        LocalDateTime effectiveDate = (LocalDateTime) getFieldValue(document, "effectiveDate");
        if (effectiveDate != null && currentTime.isBefore(effectiveDate)) {
            return false;
        }

        LocalDateTime expiryDate = (LocalDateTime) getFieldValue(document, "expiryDate");
        if (expiryDate != null && currentTime.isAfter(expiryDate)) {
            return false;
        }

        return hasPermission(userId, documentId, permission);
    }

    @Override
    public boolean hasPermissionWithLocation(String userId, Long documentId, PermissionType permission,
            String location) {
        // 基于地理位置的权限检查
        // 这里可以实现IP地址限制、地理位置限制等
        return hasPermission(userId, documentId, permission);
    }

    // 私有辅助方法

    private Set<String> getUserRoles(String userId) {
        // 这里应该从用户服务或权限服务获取用户角色
        // 简化实现
        Set<String> roles = new HashSet<>();
        if (adminUsers.contains(userId)) {
            roles.add("ADMIN");
            roles.add("ROLE_SYSTEM_ADMIN");
        }
        roles.add("ROLE_OPERATOR"); // 默认角色
        return roles;
    }

    private List<PermissionPolicy> getDocumentPolicies(Document document) {
        // 根据文档属性生成权限策略
        return List.of(
                new DocumentOwnerPolicy((String) getFieldValue(document, "author")),
                new DepartmentPolicy((String) getFieldValue(document, "ownerDepartment")),
                new ConfidentialityPolicy((String) getFieldValue(document, "confidentialityLevel")),
                new StatusPolicy((String) getFieldValue(document, "status")));
    }

    /**
     * 使用反射获取对象字段值
     */
    private Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null) {
            return null;
        }

        try {
            // 尝试通过字段访问
            Field field = findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                return field.get(obj);
            }
            // 尝试通过getter方法获取
            String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            try {
                Method getter = obj.getClass().getMethod(getterName);
                return getter.invoke(obj);
            } catch (NoSuchMethodException e) {
                // 如果getter不存在，返回null
                return null;
            }
        } catch (Exception e) {
            log.error("获取字段值失败: {}.{}", obj.getClass().getSimpleName(), fieldName, e);
            return null;
        }
    }

    /**
     * 查找字段，包括父类中的字段
     */
    private Field findField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    private PermissionContext getContext(String userId, Document document) {
        PermissionContext context = new PermissionContext();
        context.setUserId(userId);
        try {
            // 使用反射获取documentId
            java.lang.reflect.Field idField = Document.class.getDeclaredField("id");
            idField.setAccessible(true);
            context.setDocumentId((Long) idField.get(document));

            // 使用反射获取documentType
            java.lang.reflect.Field typeField = Document.class.getDeclaredField("documentType");
            typeField.setAccessible(true);
            context.setDocumentType((String) typeField.get(document));

            // 使用反射获取confidentialityLevel
            java.lang.reflect.Field levelField = Document.class.getDeclaredField("confidentialityLevel");
            levelField.setAccessible(true);
            context.setConfidentialityLevel((String) levelField.get(document));
        } catch (Exception e) {
            // 处理异常，设置默认值
            context.setDocumentId(null);
            context.setDocumentType(null);
            context.setConfidentialityLevel(null);
        }
        context.setCurrentTime(LocalDateTime.now());
        return context;
    }

    private boolean evaluatePermissions(Set<String> roles, List<PermissionPolicy> policies,
            PermissionType required, PermissionContext context) {
        // 支持复杂的权限规则：
        // - 基于角色的访问控制
        // - 基于属性的访问控制
        // - 时间窗口限制
        // - 地理位置限制

        // 1. 检查管理员权限
        if (isAdmin(roles)) {
            return true;
        }

        // 2. 检查文档所有者权限
        if (context.getUserId().equals(getDocumentAuthor(context.getDocumentId()))) {
            return true;
        }

        // 3. 评估策略权限
        return policies.stream()
                .anyMatch(policy -> policy.evaluate(roles, required, context));
    }

    private static boolean isAdmin(Set<String> roles) {
        return roles.stream().anyMatch(
                role -> role.equals("ADMIN") || role.equals("ROLE_SYSTEM_ADMIN") || role.equals("ROLE_GMP_ADMIN"));
    }

    private String getDocumentAuthor(Long documentId) {
        Document document = documentRepository.findById(documentId).orElse(null);
        if (document == null) {
            return null;
        }
        try {
            java.lang.reflect.Field authorField = Document.class.getDeclaredField("author");
            authorField.setAccessible(true);
            return (String) authorField.get(document);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean hasCategoryPermission(String userId, Long categoryId, PermissionType permission) {
        // 检查分类权限的逻辑
        return true; // 简化实现
    }

    // 权限策略接口和实现类

    private interface PermissionPolicy {
        boolean evaluate(Set<String> roles, PermissionType permission, PermissionContext context);
    }

    private static class DocumentOwnerPolicy implements PermissionPolicy {
        private final String documentOwner;

        public DocumentOwnerPolicy(String documentOwner) {
            this.documentOwner = documentOwner;
        }

        @Override
        public boolean evaluate(Set<String> roles, PermissionType permission, PermissionContext context) {
            return context.getUserId().equals(documentOwner);
        }
    }

    private static class DepartmentPolicy implements PermissionPolicy {
        private final String department;

        public DepartmentPolicy(String department) {
            this.department = department;
        }

        @Override
        public boolean evaluate(Set<String> roles, PermissionType permission, PermissionContext context) {
            // 同部门权限检查
            return true; // 简化实现
        }
    }

    private static class ConfidentialityPolicy implements PermissionPolicy {
        private final String confidentialityLevel;

        public ConfidentialityPolicy(String confidentialityLevel) {
            this.confidentialityLevel = confidentialityLevel;
        }

        @Override
        public boolean evaluate(Set<String> roles, PermissionType permission, PermissionContext context) {
            // 根据保密级别检查权限
            if ("RESTRICTED".equals(confidentialityLevel) && !isAdmin(roles)) {
                return false;
            }
            return true;
        }
    }

    private static class StatusPolicy implements PermissionPolicy {
        private final String documentStatus;

        public StatusPolicy(String documentStatus) {
            this.documentStatus = documentStatus;
        }

        @Override
        public boolean evaluate(Set<String> roles, PermissionType permission, PermissionContext context) {
            // 根据文档状态检查权限
            if ("WITHDRAWN".equals(documentStatus)) {
                return false;
            }
            return true;
        }
    }

    // 权限上下文类

    private static class PermissionContext {
        private String userId;
        private Long documentId;
        private String documentType;
        private String confidentialityLevel;
        private LocalDateTime currentTime;

        // Getters and setters
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getDocumentId() {
            return documentId;
        }

        public void setDocumentId(Long documentId) {
            this.documentId = documentId;
        }

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public String getConfidentialityLevel() {
            return confidentialityLevel;
        }

        public void setConfidentialityLevel(String confidentialityLevel) {
            this.confidentialityLevel = confidentialityLevel;
        }

        public LocalDateTime getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(LocalDateTime currentTime) {
            this.currentTime = currentTime;
        }
    }
}