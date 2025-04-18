package com.tetticket.ddd.domain.service.impl;

import com.tetticket.ddd.domain.model.entity.TicketDetail;
import com.tetticket.ddd.domain.repository.TicketDetailRepository;
import com.tetticket.ddd.domain.service.TicketDetailDomainService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {
    @Resource
    private TicketDetailRepository ticketDetailRepository;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
       log.info("Implement Domain : {}", ticketId);
       return ticketDetailRepository.findById(ticketId).orElse(null);
    }
}
