package com.tetticket.ddd.domain.service;

import com.tetticket.ddd.domain.model.entity.TicketDetail;

public interface TicketDetailDomainService {
    TicketDetail getTicketDetailById(Long ticketId);
}
