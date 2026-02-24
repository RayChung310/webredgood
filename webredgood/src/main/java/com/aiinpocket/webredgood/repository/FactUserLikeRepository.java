package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.FactUserLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FactUserLikeRepository extends JpaRepository<FactUserLike, Long> {
    List<FactUserLike> findByPostIdIn(Collection<Long> postIds);
    // 找出所有按過該網紅貼文的 user_id
}
