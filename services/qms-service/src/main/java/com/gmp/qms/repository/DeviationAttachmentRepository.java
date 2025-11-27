package com.gmp.qms.repository;

import com.gmp.qms.entity.DeviationAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 偏差附件仓库接口
 * 
 * @author GMP系统开发团队
 */
@Repository
public interface DeviationAttachmentRepository extends JpaRepository<DeviationAttachment, Long> {
    
    /**
     * 根据偏差ID查找所有附件
     * 
     * @param deviationId 偏差ID
     * @return 附件列表
     */
    List<DeviationAttachment> findByDeviationId(Long deviationId);
    
    /**
     * 删除指定偏差的所有附件
     * 
     * @param deviationId 偏差ID
     */
    void deleteByDeviationId(Long deviationId);
}