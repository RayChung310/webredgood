package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.DimDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DateRepository extends JpaRepository<DimDate, Long> {
}
