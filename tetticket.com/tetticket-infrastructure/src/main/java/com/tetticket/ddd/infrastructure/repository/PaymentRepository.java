package com.tetticket.ddd.infrastructure.repository;

import com.tetticket.ddd.domain.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Custom query methods can be defined here if needed
}
