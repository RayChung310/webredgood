package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.FactUserLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FactUserLikeRepository extends JpaRepository<FactUserLike, Long> {
    // 找出所有按過該網紅貼文的 user_id
    List<FactUserLike> findByPost_IdIn(Collection<Long> postIds);

    List<FactUserLike> findByUserIdAndPost_IdIn(Long userId, Collection<Long> postIds);
    // 重複按讚
    Optional<FactUserLike> findFirstByUser_IdAndPost_Id(Long userId, Long postId);

    @EntityGraph(attributePaths = {"user"})
    List<FactUserLike> findByTag_Id(Long tagId);

}
