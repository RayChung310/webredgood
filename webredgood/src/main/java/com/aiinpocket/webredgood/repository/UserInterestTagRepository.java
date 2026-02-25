package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.UserInterestTag;
import com.aiinpocket.webredgood.entity.UserInterestTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserInterestTagRepository extends JpaRepository<UserInterestTag, UserInterestTagId> {
    List<UserInterestTag> findByUserId(Long userId);
    Optional<UserInterestTag> findByUserIdAndTagId(Long userId, Long tagId);
}
