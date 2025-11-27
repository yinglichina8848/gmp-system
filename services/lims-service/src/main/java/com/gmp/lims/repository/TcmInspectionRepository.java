package com.gmp.lims.repository;

import com.gmp.lims.entity.TcmInspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 中药特色检验仓库接口
 */
@Repository
public interface TcmInspectionRepository extends JpaRepository<TcmInspection, Long>, JpaSpecificationExecutor<TcmInspection> {

    /**
     * 根据检验编号查找中药特色检验记录
     *
     * @param inspectionCode 检验编号
     * @return 中药特色检验记录Optional对象
     */
    Optional<TcmInspection> findByInspectionCode(String inspectionCode);
}