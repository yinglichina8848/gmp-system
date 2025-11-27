package com.gmp.qms.client;

import com.gmp.qms.dto.NotificationDTO;
import com.gmp.qms.dto.TrainingNeedDTO;
import com.gmp.qms.dto.TrainingRecordDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 培训管理系统Feign客户端，定义与培训管理系统的通信接口
 * 
 * @author GMP系统开发团队
 */
@FeignClient(name = "training-service", url = "${feign.client.training.url}", fallback = TrainingClientFallback.class)
public interface TrainingClient {
    
    /**
     * 获取员工的培训记录
     * 
     * @param employeeId 员工ID
     * @return 培训记录列表
     */
    @GetMapping("/api/employees/{employeeId}/training-records")
    List<TrainingRecordDTO> getEmployeeTrainingRecords(@PathVariable("employeeId") String employeeId);
    
    /**
     * 创建培训需求
     * 
     * @param trainingNeedDTO 培训需求信息
     * @return 创建的培训需求
     */
    @PostMapping("/api/training-needs")
    TrainingNeedDTO createTrainingNeed(@RequestBody TrainingNeedDTO trainingNeedDTO);
    
    /**
     * 检查员工是否完成特定培训
     * 
     * @param employeeId 员工ID
     * @param trainingCode 培训代码
     * @return 是否已完成培训
     */
    @GetMapping("/api/employees/{employeeId}/training-completed/{trainingCode}")
    boolean isTrainingCompleted(@PathVariable("employeeId") String employeeId, 
                               @PathVariable("trainingCode") String trainingCode);
    
    /**
     * 发送通知到培训管理系统
     * 
     * @param notificationDTO 通知信息
     */
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationDTO notificationDTO);
    
    /**
     * 获取培训课程详情
     * 
     * @param trainingCode 培训代码
     * @return 培训记录
     */
    @GetMapping("/api/trainings/{trainingCode}")
    TrainingRecordDTO getTrainingDetails(@PathVariable("trainingCode") String trainingCode);
    
    /**
     * 获取即将到期的培训列表
     * 
     * @param days 天数阈值
     * @return 即将到期的培训记录列表
     */
    @GetMapping("/api/trainings/expiring-soon")
    List<TrainingRecordDTO> getExpiringTrainings(@RequestParam("days") int days);
}
