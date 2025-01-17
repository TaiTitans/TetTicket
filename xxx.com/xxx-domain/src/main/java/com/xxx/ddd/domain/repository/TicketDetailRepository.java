package com.xxx.ddd.domain.repository;

import com.xxx.ddd.domain.model.entity.TicketDetail;

import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);
}
