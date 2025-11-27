package com.gmp.training.repository;

import com.gmp.training.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 证书数据访问层接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {

    /**
     * 根据证书编号查找证书
     * 
     * @param certificateNumber 证书编号
     * @return 证书信息
     */
    Optional<Certificate> findByCertificateNumber(String certificateNumber);

    /**
     * 根据培训记录ID查找证书
     * 
     * @param trainingRecordId 培训记录ID
     * @return 证书信息
     */
    Optional<Certificate> findByTrainingRecordId(Long trainingRecordId);

    /**
     * 根据证书状态查找证书
     * 
     * @param status 证书状态
     * @return 证书列表
     */
    List<Certificate> findByStatus(Certificate.CertificateStatus status);

    /**
     * 查找员工的所有证书
     * 
     * @param participantId 员工ID
     * @return 证书列表
     */
    List<Certificate> findByTrainingRecordParticipantId(Long participantId);

    /**
     * 查找即将过期的证书
     * 
     * @param now 当前时间
     * @param futureTime 未来时间
     * @param status 证书状态
     * @return 证书列表
     */
    List<Certificate> findByExpiryDateBetweenAndStatus(LocalDateTime now, LocalDateTime futureTime, Certificate.CertificateStatus status);

    /**
     * 查找已过期的证书
     * 
     * @param now 当前时间
     * @param status 证书状态
     * @return 证书列表
     */
    List<Certificate> findByExpiryDateBeforeAndStatus(LocalDateTime now, Certificate.CertificateStatus status);

    /**
     * 查找需要复训且即将过期的证书
     * 
     * @param now 当前时间
     * @param futureTime 未来时间
     * @param requiresRefresher 是否需要复训
     * @param status 证书状态
     * @return 证书列表
     */
    List<Certificate> findByExpiryDateBetweenAndRequiresRefresherAndStatus(LocalDateTime now, LocalDateTime futureTime, boolean requiresRefresher, Certificate.CertificateStatus status);

    /**
     * 检查证书编号是否存在
     * 
     * @param certificateNumber 证书编号
     * @return 是否存在
     */
    boolean existsByCertificateNumber(String certificateNumber);

    /**
     * 查找特定培训课程的证书
     * 
     * @param courseId 课程ID
     * @return 证书列表
     */
    List<Certificate> findByTrainingRecordSessionCourseId(Long courseId);

    /**
     * 查找特定时间范围内颁发的证书
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 证书列表
     */
    List<Certificate> findByIssueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 查找没有过期日期的永久有效证书
     * 
     * @param status 证书状态
     * @return 证书列表
     */
    List<Certificate> findByExpiryDateIsNullAndStatus(Certificate.CertificateStatus status);
}
