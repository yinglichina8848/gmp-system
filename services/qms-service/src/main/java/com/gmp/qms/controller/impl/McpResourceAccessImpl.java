package com.gmp.qms.controller.impl;

import com.gmp.qms.controller.McpController;
import com.gmp.qms.repository.DeviationRepository;
import com.gmp.qms.repository.CapaRepository;
import com.gmp.qms.repository.ChangeControlRepository;
import com.gmp.qms.entity.Deviation;
import com.gmp.qms.entity.Capa;
import com.gmp.qms.entity.ChangeControl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * MCP资源访问实现类，负责处理AI模型对QMS系统结构化数据的访问请求
 * 
 * @author GMP系统开发团队
 */
@Component
@RequiredArgsConstructor
public class McpResourceAccessImpl implements McpController.McpResourceAccess {
    
    private final DeviationRepository deviationRepository;
    private final CapaRepository capaRepository;
    private final ChangeControlRepository changeControlRepository;
    
    @Override
    public Object accessResource(String resourceName, String operation, Map<String, Object> parameters) throws Exception {
        // 根据资源名称和操作类型路由到相应的处理方法
        switch (resourceName) {
            // 偏差资源
            case "deviation":
                return handleDeviationResource(operation, parameters);
                
            // CAPA资源
            case "capa":
                return handleCapaResource(operation, parameters);
                
            // 变更控制资源
            case "changeControl":
                return handleChangeControlResource(operation, parameters);
                
            // 其他资源...
            default:
                throw new IllegalArgumentException("Unknown resource: " + resourceName);
        }
    }
    
    /**
     * 处理偏差资源的访问请求
     */
    private Object handleDeviationResource(String operation, Map<String, Object> parameters) {
        switch (operation) {
            case "get":
                return getDeviationById(parameters);
            case "list":
                return listDeviations(parameters);
            case "statistics":
                return getDeviationStatistics(parameters);
            default:
                throw new IllegalArgumentException("Unsupported operation for deviation resource: " + operation);
        }
    }
    
    /**
     * 处理CAPA资源的访问请求
     */
    private Object handleCapaResource(String operation, Map<String, Object> parameters) {
        switch (operation) {
            case "get":
                return getCapaById(parameters);
            case "list":
                return listCapas(parameters);
            case "statistics":
                return getCapaStatistics(parameters);
            default:
                throw new IllegalArgumentException("Unsupported operation for capa resource: " + operation);
        }
    }
    
    /**
     * 处理变更控制资源的访问请求
     */
    private Object handleChangeControlResource(String operation, Map<String, Object> parameters) {
        switch (operation) {
            case "get":
                return getChangeControlById(parameters);
            case "list":
                return listChangeControls(parameters);
            case "statistics":
                return getChangeControlStatistics(parameters);
            default:
                throw new IllegalArgumentException("Unsupported operation for changeControl resource: " + operation);
        }
    }
    
    // ========== 偏差资源访问方法 ==========
    
    /**
     * 根据ID获取偏差记录
     */
    private Deviation getDeviationById(Map<String, Object> parameters) {
        Long id = Long.valueOf(parameters.get("id").toString());
        Optional<Deviation> deviationOpt = deviationRepository.findById(id);
        return deviationOpt.orElse(null);
    }
    
    /**
     * 获取偏差记录列表
     */
    private List<Deviation> listDeviations(Map<String, Object> parameters) {
        String status = (String) parameters.get("status");
        String severity = (String) parameters.get("severity");
        String department = (String) parameters.get("department");
        
        if (status != null) {
            return deviationRepository.findByStatus(Deviation.DeviationStatus.valueOf(status));
        } else if (severity != null) {
            return deviationRepository.findBySeverity(severity);
        } else if (department != null) {
            return deviationRepository.findByDepartment(department);
        } else {
            // 分页获取，默认最多获取100条
            int limit = (int) parameters.getOrDefault("limit", 100);
            return deviationRepository.findTopByOrderByCreatedAtDesc(limit);
        }
    }
    
    /**
     * 获取偏差统计信息
     */
    private Map<String, Long> getDeviationStatistics(Map<String, Object> parameters) {
        // 实现统计功能，如按状态、严重性分组统计
        // 可以根据需要添加时间范围等过滤条件
        return deviationRepository.countByStatus();
    }
    
    // ========== CAPA资源访问方法 ==========
    
    /**
     * 根据ID获取CAPA记录
     */
    private Capa getCapaById(Map<String, Object> parameters) {
        Long id = Long.valueOf(parameters.get("id").toString());
        Optional<Capa> capaOpt = capaRepository.findById(id);
        return capaOpt.orElse(null);
    }
    
    /**
     * 获取CAPA记录列表
     */
    private List<Capa> listCapas(Map<String, Object> parameters) {
        String status = (String) parameters.get("status");
        String priority = (String) parameters.get("priority");
        
        if (status != null) {
            return capaRepository.findByStatus(status);
        } else if (priority != null) {
            return capaRepository.findByPriority(priority);
        } else {
            // 分页获取，默认最多获取100条
            int limit = (int) parameters.getOrDefault("limit", 100);
            return capaRepository.findTopByOrderByCreatedAtDesc(limit);
        }
    }
    
    /**
     * 获取CAPA统计信息
     */
    private Map<String, Long> getCapaStatistics(Map<String, Object> parameters) {
        // 实现统计功能，如按状态、优先级分组统计
        return capaRepository.countByStatus();
    }
    
    // ========== 变更控制资源访问方法 ==========
    
    /**
     * 根据ID获取变更控制记录
     */
    private ChangeControl getChangeControlById(Map<String, Object> parameters) {
        Long id = Long.valueOf(parameters.get("id").toString());
        Optional<ChangeControl> changeControlOpt = changeControlRepository.findById(id);
        return changeControlOpt.orElse(null);
    }
    
    /**
     * 获取变更控制记录列表
     */
    private List<ChangeControl> listChangeControls(Map<String, Object> parameters) {
        String status = (String) parameters.get("status");
        String type = (String) parameters.get("type");
        
        if (status != null) {
            return changeControlRepository.findByStatus(status);
        } else if (type != null) {
            return changeControlRepository.findByType(type);
        } else {
            // 分页获取，默认最多获取100条
            int limit = (int) parameters.getOrDefault("limit", 100);
            return changeControlRepository.findTopByOrderByCreatedAtDesc(limit);
        }
    }
    
    /**
     * 获取变更控制统计信息
     */
    private Map<String, Long> getChangeControlStatistics(Map<String, Object> parameters) {
        // 实现统计功能，如按状态、类型分组统计
        return changeControlRepository.countByStatus();
    }
}
