package com.tetticket.ddd.domain.repository;

import com.tetticket.ddd.domain.model.entity.TicketDetail;

import java.util.List;
import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);

    /**
     * Find all ticket details
     * @return List of all ticket details
     */
    List<TicketDetail> findAll();
}
