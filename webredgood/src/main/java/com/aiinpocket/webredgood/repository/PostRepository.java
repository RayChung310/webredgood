package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.DimPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<DimPost, Long> {

    List<DimPost> findByInfluencerId(Long influencerId);
    // 取得該網紅所有post_id
}
