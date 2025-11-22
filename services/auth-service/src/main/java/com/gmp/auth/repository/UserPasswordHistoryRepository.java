package com.gmp.auth.repository;

import com.gmp.auth.model.UserPasswordHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户密码历史记录仓库
 */
@Repository
public interface UserPasswordHistoryRepository extends JpaRepository<UserPasswordHistory, Long> {

    /**
     * 按用户ID查找密码历史记录，按创建时间降序排序
     * @param userId 用户ID
     * @param pageable 分页参数（限制返回数量）
     * @return 密码历史记录列表
     */
    List<UserPasswordHistory> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 查找需要删除的旧密码历史记录ID
     * @param userId 用户ID
     * @param keepCount 保留的记录数
     * @return 需要删除的记录ID列表
     */
    @Query("SELECT h.id FROM UserPasswordHistory h WHERE h.userId = :userId AND h.id NOT IN " +
           "(SELECT h2.id FROM UserPasswordHistory h2 WHERE h2.userId = :userId ORDER BY h2.createdAt DESC LIMIT :keepCount)")
    List<Long> findIdsToDelete(Long userId, int keepCount);
}