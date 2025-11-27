package com.gmp.qms.service;

import com.gmp.qms.dto.CapaDTO;
import com.gmp.qms.dto.CapaSearchCriteria;
import com.gmp.qms.entity.Capa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * CAPA管理服务接口
 * 
 * @author GMP系统开发团队
 */
public interface CapaService {

    /**
     * 创建新的CAPA记录
     * 
     * @param capaDTO CAPA信息DTO
     * @return 创建的CAPA实体
     */
    Capa createCapa(CapaDTO capaDTO);

    /**
     * 更新CAPA记录
     * 
     * @param id CAPA ID
     * @param capaDTO 更新的CAPA信息
     * @return 更新后的CAPA实体
     */
    Capa updateCapa(Long id, CapaDTO capaDTO);

    /**
     * 根据ID查询CAPA
     * 
     * @param id CAPA ID
     * @return CAPA实体
     */
    Capa getCapaById(Long id);

    /**
     * 根据编号查询CAPA
     * 
     * @param capaCode CAPA编号
     * @return CAPA实体
     */
    Capa getCapaByCode(String capaCode);

    /**
     * 分页查询CAPA列表
     * 
     * @param pageable 分页参数
     * @return CAPA分页列表
     */
    Page<Capa> findAllCapas(Pageable pageable);

    /**
     * 根据条件查询CAPA列表
     * 
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return CAPA分页列表
     */
    Page<Capa> searchCapas(CapaSearchCriteria criteria, Pageable pageable);

    /**
     * 更新CAPA状态
     * 
     * @param id CAPA ID
     * @param status 新状态
     * @param comments 状态变更说明
     * @return 更新后的CAPA实体
     */
    Capa updateCapaStatus(Long id, Capa.CapaStatus status, String comments);

    /**
     * 为CAPA添加附件
     * 
     * @param capaId CAPA ID
     * @param file 上传的文件
     * @param description 文件描述
     * @return 更新后的CAPA实体
     */
    Capa addAttachment(Long capaId, MultipartFile file, String description);

    /**
     * 删除CAPA附件
     * 
     * @param capaId CAPA ID
     * @param attachmentId 附件ID
     * @return 更新后的CAPA实体
     */
    Capa removeAttachment(Long capaId, Long attachmentId);

    /**
     * 查询逾期未处理的CAPA
     * 
     * @return 逾期CAPA列表
     */
    List<Capa> findOverdueCapas();

    /**
     * 生成CAPA编号
     * 
     * @return 新的CAPA编号
     */
    String generateCapaCode();
}
