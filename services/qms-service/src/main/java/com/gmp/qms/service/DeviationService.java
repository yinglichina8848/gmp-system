package com.gmp.qms.service;

import com.gmp.qms.dto.DeviationDTO;
import com.gmp.qms.dto.DeviationSearchCriteria;
import com.gmp.qms.entity.Deviation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 偏差管理服务接口
 * 
 * @author GMP系统开发团队
 */
public interface DeviationService {

    /**
     * 创建新的偏差记录
     * 
     * @param deviationDTO 偏差信息DTO
     * @return 创建的偏差实体
     */
    Deviation createDeviation(DeviationDTO deviationDTO);

    /**
     * 更新偏差记录
     * 
     * @param id 偏差ID
     * @param deviationDTO 更新的偏差信息
     * @return 更新后的偏差实体
     */
    Deviation updateDeviation(Long id, DeviationDTO deviationDTO);

    /**
     * 根据ID查询偏差
     * 
     * @param id 偏差ID
     * @return 偏差实体
     */
    Deviation getDeviationById(Long id);

    /**
     * 根据编号查询偏差
     * 
     * @param deviationCode 偏差编号
     * @return 偏差实体
     */
    Deviation getDeviationByCode(String deviationCode);

    /**
     * 分页查询偏差列表
     * 
     * @param pageable 分页参数
     * @return 偏差分页列表
     */
    Page<Deviation> findAllDeviations(Pageable pageable);

    /**
     * 根据条件查询偏差列表
     * 
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 偏差分页列表
     */
    Page<Deviation> searchDeviations(DeviationSearchCriteria criteria, Pageable pageable);

    /**
     * 更新偏差状态
     * 
     * @param id 偏差ID
     * @param status 新状态
     * @param comments 状态变更说明
     * @return 更新后的偏差实体
     */
    Deviation updateDeviationStatus(Long id, Deviation.DeviationStatus status, String comments);

    /**
     * 为偏差添加附件
     * 
     * @param deviationId 偏差ID
     * @param file 上传的文件
     * @param description 文件描述
     * @return 更新后的偏差实体
     */
    Deviation addAttachment(Long deviationId, MultipartFile file, String description);

    /**
     * 删除偏差附件
     * 
     * @param deviationId 偏差ID
     * @param attachmentId 附件ID
     * @return 更新后的偏差实体
     */
    Deviation removeAttachment(Long deviationId, Long attachmentId);

    /**
     * 查询逾期未处理的偏差
     * 
     * @return 逾期偏差列表
     */
    List<Deviation> findOverdueDeviations();

    /**
     * 生成偏差编号
     * 
     * @return 新的偏差编号
     */
    String generateDeviationCode();
}
