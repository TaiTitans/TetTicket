package com.tetticket.ddd.infrastructure.repository;

import com.tetticket.ddd.domain.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_id(Long id);
    // Custom query methods can be defined here if needed
}
