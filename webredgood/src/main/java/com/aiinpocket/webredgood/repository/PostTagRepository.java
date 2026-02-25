package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.PostTag;
import com.aiinpocket.webredgood.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    List<PostTag> findByPostIdIn(Collection<Long> postIds);
    // 用集合查多筆
}
