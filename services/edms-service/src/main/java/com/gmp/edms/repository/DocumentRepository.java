package com.gmp.edms.repository;

import com.gmp.edms.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 文档Repository接口
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

        /**
         * 根据文档编号查询文档
         */
        Optional<Document> findByDocCode(String docCode);

        /**
         * 根据分类ID查询文档列表
         */
        List<Document> findByCategoryId(Long categoryId);

        /**
         * 根据创建者查询文档列表
         */
        List<Document> findByCreatedBy(String createdBy);

        /**
         * 根据标题模糊查询文档
         */
        List<Document> findByTitleContaining(String keyword);
        
        /**
         * 按关键词搜索文档，支持分页
         */
        List<Document> searchByKeyword(String keyword, int offset, int limit);

        /**
         * 分页查询文档
         */
        Page<Document> findByStatus(String status, Pageable pageable);

        /**
         * 统计分类下文档数量
         */
        long countByCategoryId(Long categoryId);

        /**
         * 根据部门ID查询文档
         */
        List<Document> findByDepartmentId(Long departmentId);

        /**
         * 根据审批状态查询文档
         */
        List<Document> findByApprovalStatus(String approvalStatus);

        /**
         * 高级查询文档
         */
        @Query("SELECT d FROM Document d WHERE " +
                        "(:keyword IS NULL OR d.title LIKE %:keyword% OR d.docCode LIKE %:keyword%) AND " +
                        "(:categoryId IS NULL OR d.categoryId = :categoryId) AND " +
                        "(:status IS NULL OR d.status = :status) AND " +
                        "(:departmentId IS NULL OR d.departmentId = :departmentId) AND " +
                        "(:approvalStatus IS NULL OR d.approvalStatus = :approvalStatus)")
        Page<Document> advancedSearch(
                        @Param("keyword") String keyword,
                        @Param("categoryId") Long categoryId,
                        @Param("status") String status,
                        @Param("departmentId") Long departmentId,
                        @Param("approvalStatus") String approvalStatus,
                        Pageable pageable);

        /**
         * 查询用户权限下的文档
         */
        @Query("SELECT d FROM Document d WHERE " +
                        "d.createdBy = :username OR " +
                        "d.approvalStatus = 'APPROVED'")
        List<Document> findDocumentsByUserPermission(@Param("username") String username);
}
