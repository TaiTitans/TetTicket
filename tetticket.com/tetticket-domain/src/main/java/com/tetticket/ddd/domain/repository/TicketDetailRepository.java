package com.tetticket.ddd.domain.repository;

import com.tetticket.ddd.domain.model.entity.TicketDetail;

import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);
}
