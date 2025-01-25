package com.xxx.ddd.application.service.ticket.impl;

import com.xxx.ddd.application.service.ticket.TicketDetailAppService;
import com.xxx.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.xxx.ddd.domain.model.entity.TicketDetail;
import com.xxx.ddd.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {
    //Call Service Domain Module
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    //Call Cache
    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        log.info("Implement Application : {}", ticketId);

        return ticketDetailCacheService.getTicketDefaultCacheLocal(ticketId, System.currentTimeMillis());
    }
}
