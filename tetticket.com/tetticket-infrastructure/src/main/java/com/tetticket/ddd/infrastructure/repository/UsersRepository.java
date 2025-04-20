package com.tetticket.ddd.infrastructure.repository;

import com.tetticket.ddd.domain.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
