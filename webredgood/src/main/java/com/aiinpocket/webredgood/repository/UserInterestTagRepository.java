package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.UserInterestTag;
import com.aiinpocket.webredgood.entity.UserInterestTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestTagRepository extends JpaRepository<UserInterestTag, UserInterestTagId> {
}
