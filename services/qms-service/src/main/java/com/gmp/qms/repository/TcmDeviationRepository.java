package com.gmp.qms.repository;

import com.gmp.qms.entity.TcmDeviation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 中药特色偏差仓库接口
 *
 * @author GMP系统开发团队
 */
@Repository
public interface TcmDeviationRepository extends JpaRepository<TcmDeviation, Long>, JpaSpecificationExecutor<TcmDeviation> {

    /**
     * 根据偏差编号查找中药特色偏差
     *
     * @param deviationCode 偏差编号
     * @return 中药特色偏差Optional对象
     */
    Optional<TcmDeviation> findByDeviationCode(String deviationCode);
}