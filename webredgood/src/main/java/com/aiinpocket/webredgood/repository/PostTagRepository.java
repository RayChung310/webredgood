package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.PostTag;
import com.aiinpocket.webredgood.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {
}
