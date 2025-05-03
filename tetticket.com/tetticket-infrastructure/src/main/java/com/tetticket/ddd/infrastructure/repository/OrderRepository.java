package com.tetticket.ddd.infrastructure.repository;

import com.tetticket.ddd.domain.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
