package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.DimDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DateRepository extends JpaRepository<DimDate, Long> {
    Optional<DimDate> findByFullDate(LocalDate fullDate);
}
