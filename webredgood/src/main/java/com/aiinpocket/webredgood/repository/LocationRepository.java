package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.DimLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<DimLocation, Long> {
}
