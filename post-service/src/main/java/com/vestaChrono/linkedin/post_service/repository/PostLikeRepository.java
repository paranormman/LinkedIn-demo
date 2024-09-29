package com.vestaChrono.linkedin.post_service.repository;

import com.vestaChrono.linkedin.post_service.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long PostId);

    @Transactional
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
