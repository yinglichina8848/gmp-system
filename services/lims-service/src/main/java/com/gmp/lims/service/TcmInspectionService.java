package com.gmp.lims.service;

import com.gmp.lims.dto.TcmInspectionDTO;
import com.gmp.lims.entity.TcmInspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 中药特色检验服务接口
 */
public interface TcmInspectionService {

    /**
     * 创建新的中药特色检验记录
     *
     * @param tcmInspectionDTO 中药特色检验信息DTO
     * @return 创建的中药特色检验实体
     */
    TcmInspection createTcmInspection(TcmInspectionDTO tcmInspectionDTO);

    /**
     * 更新中药特色检验记录
     *
     * @param id 中药特色检验ID
     * @param tcmInspectionDTO 更新的中药特色检验信息
     * @return 更新后的中药特色检验实体
     */
    TcmInspection updateTcmInspection(Long id, TcmInspectionDTO tcmInspectionDTO);

    /**
     * 根据ID查询中药特色检验记录
     *
     * @param id 中药特色检验ID
     * @return 中药特色检验实体
     */
    TcmInspection getTcmInspectionById(Long id);

    /**
     * 根据检验编号查询中药特色检验记录
     *
     * @param inspectionCode 检验编号
     * @return 中药特色检验实体
     */
    TcmInspection getTcmInspectionByCode(String inspectionCode);

    /**
     * 分页查询中药特色检验记录列表
     *
     * @param pageable 分页参数
     * @return 中药特色检验记录分页列表
     */
    Page<TcmInspection> getTcmInspections(Pageable pageable);

    /**
     * 删除中药特色检验记录
     *
     * @param id 中药特色检验ID
     */
    void deleteTcmInspection(Long id);
}