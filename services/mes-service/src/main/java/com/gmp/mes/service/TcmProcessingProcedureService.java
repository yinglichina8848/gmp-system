package com.gmp.mes.service;

import com.gmp.mes.dto.TcmProcessingProcedureDTO;
import com.gmp.mes.entity.TcmProcessingProcedure;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 中药炮制工艺服务接口
 *
 * @author GMP系统开发团队
 */
public interface TcmProcessingProcedureService {

    /**
     * 创建新的中药炮制工艺记录
     *
     * @param tcmProcessingProcedureDTO 中药炮制工艺信息DTO
     * @return 创建的中药炮制工艺实体
     */
    TcmProcessingProcedure createTcmProcessingProcedure(TcmProcessingProcedureDTO tcmProcessingProcedureDTO);

    /**
     * 更新中药炮制工艺记录
     *
     * @param id 中药炮制工艺ID
     * @param tcmProcessingProcedureDTO 更新的中药炮制工艺信息
     * @return 更新后的中药炮制工艺实体
     */
    TcmProcessingProcedure updateTcmProcessingProcedure(Long id, TcmProcessingProcedureDTO tcmProcessingProcedureDTO);

    /**
     * 根据ID查询中药炮制工艺
     *
     * @param id 中药炮制工艺ID
     * @return 中药炮制工艺实体
     */
    TcmProcessingProcedure getTcmProcessingProcedureById(Long id);

    /**
     * 根据编号查询中药炮制工艺
     *
     * @param procedureNumber 工艺编号
     * @return 中药炮制工艺实体
     */
    TcmProcessingProcedure getTcmProcessingProcedureByNumber(String procedureNumber);

    /**
     * 分页查询中药炮制工艺列表
     *
     * @param pageable 分页参数
     * @return 中药炮制工艺分页列表
     */
    Page<TcmProcessingProcedure> getTcmProcessingProcedures(Pageable pageable);

    /**
     * 删除中药炮制工艺记录
     *
     * @param id 中药炮制工艺ID
     */
    void deleteTcmProcessingProcedure(Long id);
}