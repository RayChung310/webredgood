package com.aiinpocket.webredgood.repository;

import com.aiinpocket.webredgood.entity.DimUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<DimUser, Long> {
}
