package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.DimTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<DimTag, Long> {
}
