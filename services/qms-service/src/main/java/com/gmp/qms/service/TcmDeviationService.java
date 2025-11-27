package com.gmp.qms.service;

import com.gmp.qms.dto.TcmDeviationDTO;
import com.gmp.qms.entity.TcmDeviation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 中药特色偏差管理服务接口
 *
 * @author GMP系统开发团队
 */
public interface TcmDeviationService {

    /**
     * 创建新的中药特色偏差记录
     *
     * @param tcmDeviationDTO 中药特色偏差信息DTO
     * @return 创建的中药特色偏差实体
     */
    TcmDeviation createTcmDeviation(TcmDeviationDTO tcmDeviationDTO);

    /**
     * 更新中药特色偏差记录
     *
     * @param id 中药特色偏差ID
     * @param tcmDeviationDTO 更新的中药特色偏差信息
     * @return 更新后的中药特色偏差实体
     */
    TcmDeviation updateTcmDeviation(Long id, TcmDeviationDTO tcmDeviationDTO);

    /**
     * 根据ID查询中药特色偏差
     *
     * @param id 中药特色偏差ID
     * @return 中药特色偏差实体
     */
    TcmDeviation getTcmDeviationById(Long id);

    /**
     * 根据编号查询中药特色偏差
     *
     * @param deviationCode 中药特色偏差编号
     * @return 中药特色偏差实体
     */
    TcmDeviation getTcmDeviationByCode(String deviationCode);

    /**
     * 分页查询中药特色偏差列表
     *
     * @param pageable 分页参数
     * @return 中药特色偏差分页列表
     */
    Page<TcmDeviation> getTcmDeviations(Pageable pageable);

    /**
     * 删除中药特色偏差记录
     *
     * @param id 中药特色偏差ID
     */
    void deleteTcmDeviation(Long id);
}