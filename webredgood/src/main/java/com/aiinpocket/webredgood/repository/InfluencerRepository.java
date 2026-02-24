package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.DimInfluencer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfluencerRepository extends JpaRepository<DimInfluencer, Long> {
}
