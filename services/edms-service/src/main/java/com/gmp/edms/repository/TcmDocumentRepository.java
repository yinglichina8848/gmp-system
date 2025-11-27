package com.gmp.edms.repository;

import com.gmp.edms.entity.TcmDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 中药特色文档仓库接口
 */
@Repository
public interface TcmDocumentRepository extends JpaRepository<TcmDocument, Long>, JpaSpecificationExecutor<TcmDocument> {

    /**
     * 根据文档编号查找中药特色文档
     *
     * @param documentNumber 文档编号
     * @return 中药特色文档Optional对象
     */
    Optional<TcmDocument> findByDocumentNumber(String documentNumber);

    /**
     * 根据中药材名称查找中药特色文档
     *
     * @param herbName 中药材名称
     * @return 中药特色文档列表
     */
    java.util.List<TcmDocument> findByHerbName(String herbName);
}