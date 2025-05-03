package com.tetticket.ddd.infrastructure.repository;

import com.tetticket.ddd.domain.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom query methods can be defined here if needed
}
