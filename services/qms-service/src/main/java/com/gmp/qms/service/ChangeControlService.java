package com.gmp.qms.service;

import com.gmp.qms.dto.ChangeControlDTO;
import com.gmp.qms.dto.ChangeControlSearchCriteria;
import com.gmp.qms.entity.ChangeControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 变更控制服务接口
 * 
 * @author GMP系统开发团队
 */
public interface ChangeControlService {

    /**
     * 创建新的变更控制记录
     * 
     * @param changeControlDTO 变更控制信息DTO
     * @return 创建的变更控制实体
     */
    ChangeControl createChangeControl(ChangeControlDTO changeControlDTO);

    /**
     * 更新变更控制记录
     * 
     * @param id 变更控制ID
     * @param changeControlDTO 更新的变更控制信息
     * @return 更新后的变更控制实体
     */
    ChangeControl updateChangeControl(Long id, ChangeControlDTO changeControlDTO);

    /**
     * 根据ID查询变更控制
     * 
     * @param id 变更控制ID
     * @return 变更控制实体
     */
    ChangeControl getChangeControlById(Long id);

    /**
     * 根据编号查询变更控制
     * 
     * @param changeCode 变更编号
     * @return 变更控制实体
     */
    ChangeControl getChangeControlByCode(String changeCode);

    /**
     * 分页查询变更控制列表
     * 
     * @param pageable 分页参数
     * @return 变更控制分页列表
     */
    Page<ChangeControl> findAllChangeControls(Pageable pageable);

    /**
     * 根据条件查询变更控制列表
     * 
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 变更控制分页列表
     */
    Page<ChangeControl> searchChangeControls(ChangeControlSearchCriteria criteria, Pageable pageable);

    /**
     * 更新变更控制状态
     * 
     * @param id 变更控制ID
     * @param status 新状态
     * @param comments 状态变更说明
     * @return 更新后的变更控制实体
     */
    ChangeControl updateChangeControlStatus(Long id, ChangeControl.ChangeStatus status, String comments);

    /**
     * 为变更控制添加附件
     * 
     * @param changeControlId 变更控制ID
     * @param file 上传的文件
     * @param description 文件描述
     * @return 更新后的变更控制实体
     */
    ChangeControl addAttachment(Long changeControlId, MultipartFile file, String description);

    /**
     * 删除变更控制附件
     * 
     * @param changeControlId 变更控制ID
     * @param attachmentId 附件ID
     * @return 更新后的变更控制实体
     */
    ChangeControl removeAttachment(Long changeControlId, Long attachmentId);

    /**
     * 查询逾期未实施的变更控制
     * 
     * @return 逾期变更控制列表
     */
    List<ChangeControl> findOverdueChangeControls();

    /**
     * 生成变更控制编号
     * 
     * @return 新的变更控制编号
     */
    String generateChangeCode();
}
