package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.PostTag;
import com.aiinpocket.webredgood.entity.PostTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    List<PostTag> findByPostIdIn(Collection<Long> postIds);
    // 用集合查多筆

    @Query(""" 
        SELECT pt.postId from PostTag pt WHERE pt.postId in :postId AND pt.tagId = :tagId
    """)
    List<Long> findPostsIdByPostIdInAndTagId(
            @Param("postId") Collection<Long> postId,
            @Param("tagId") Long tagId
    );
}
