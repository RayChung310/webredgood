package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.FactUserLike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aiinpocket.webredgood.dto.CitySummary;
import com.aiinpocket.webredgood.dto.TagSummary;
import org.springframework.data.jpa.repository.Query;

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

    @Query("""
       SELECT new com.aiinpocket.webredgood.dto.CitySummary(l.cityName, COUNT(f))
       FROM FactUserLike f
       JOIN f.location l
       WHERE l.cityName IS NOT NULL
       GROUP BY l.id, l.cityName
       ORDER BY COUNT(f) DESC
       """)
    List<CitySummary> getCitySummary();

    @Query("""
       SELECT new com.aiinpocket.webredgood.dto.TagSummary(t.tagName, COUNT(f))
       FROM FactUserLike f
       JOIN f.tag t
       WHERE t.tagName IS NOT NULL
       GROUP BY t.id, t.tagName
       ORDER BY COUNT(f) DESC
       """)
    List<TagSummary> getTagSummary();

}
