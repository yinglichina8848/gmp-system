package com.gmp.mes.repository;

import com.gmp.mes.entity.TcmProcessingProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 中药炮制工艺仓库接口
 *
 * @author GMP系统开发团队
 */
@Repository
public interface TcmProcessingProcedureRepository extends JpaRepository<TcmProcessingProcedure, Long>, JpaSpecificationExecutor<TcmProcessingProcedure> {

    /**
     * 根据工艺编号查找中药炮制工艺
     *
     * @param procedureNumber 工艺编号
     * @return 中药炮制工艺Optional对象
     */
    Optional<TcmProcessingProcedure> findByProcedureNumber(String procedureNumber);
}