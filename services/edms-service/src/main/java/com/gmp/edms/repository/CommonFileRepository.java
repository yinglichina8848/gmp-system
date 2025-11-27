package com.gmp.edms.repository;

import com.gmp.edms.entity.CommonFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 通用文件数据访问接口
 */
public interface CommonFileRepository extends JpaRepository<CommonFile, Long> {
    
    /**
     * 根据模块查询文件
     */
    Page<CommonFile> findByModule(String module, Pageable pageable);
    
    /**
     * 根据模块和状态查询文件
     */
    Page<CommonFile> findByModuleAndStatus(String module, String status, Pageable pageable);
    
    /**
     * 根据校验和查找文件
     */
    Optional<CommonFile> findByChecksum(String checksum);
    
    /**
     * 批量查询文件
     */
    List<CommonFile> findByIdIn(List<Long> ids);
    
    /**
     * 根据创建者查询文件
     */
    Page<CommonFile> findByCreatedBy(String createdBy, Pageable pageable);
    
    /**
     * 按模块统计文件数量
     */
    @Query("SELECT f.module, COUNT(f) FROM CommonFile f WHERE f.status = 'ACTIVE' GROUP BY f.module")
    List<Object[]> countByModule();
    
    /**
     * 按模块统计文件总大小
     */
    @Query("SELECT f.module, SUM(f.fileSize) FROM CommonFile f WHERE f.status = 'ACTIVE' GROUP BY f.module")
    List<Object[]> sumFileSizeByModule();
    
    /**
     * 全文搜索文件
     */
    @Query("SELECT f FROM CommonFile f WHERE f.fileName LIKE %:keyword% AND f.status = 'ACTIVE'")
    Page<CommonFile> searchByFileName(@Param("keyword") String keyword, Pageable pageable);
    
    /**
     * 查询模块下的文件数量
     */
    long countByModuleAndStatus(String module, String status);
    
    /**
     * 查询最近上传的文件
     */
    Page<CommonFile> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
}