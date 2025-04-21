package com.tetticket.ddd.infrastructure.repository;

import com.tetticket.ddd.domain.model.entity.Roles;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RolesRepository extends JpaRepository<Roles, Long> {
    @Query("SELECT r FROM Roles r WHERE r.role_name = :roleName")
    Roles findByRole_name(@Param("roleName") String role_name);
}
