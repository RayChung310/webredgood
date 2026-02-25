package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.FactUserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FactUserLikeRepository extends JpaRepository<FactUserLike, Long> {
    // 找出所有按過該網紅貼文的 user_id
    List<FactUserLike> findByPostIdIn(Collection<Long> postIds);
    List<FactUserLike> findByUserIdAndPostId(Long userId, Long postId);
    // 重複按讚
    Optional<FactUserLike> findFirstByUserIdAndPostId(Long userId, Long postId);

    List<FactUserLike> findByTagId(Long tagId);
}
