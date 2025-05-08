package com.tetticket.ddd.domain.service;

import com.tetticket.ddd.domain.model.entity.TicketDetail;

import java.util.List;

public interface TicketDetailDomainService {
    TicketDetail getTicketDetailById(Long ticketId);

    /**
     * Get all ticket details
     * @return List of all ticket details
     */
    List<TicketDetail> getAllTicketDetails();
}
