package com.tetticket.ddd.application.service.ticket.impl;

import com.tetticket.ddd.application.mapper.TicketDetailMapper;
import com.tetticket.ddd.application.model.TicketDetailDTO;
import com.tetticket.ddd.application.model.cache.TicketDetailCache;
import com.tetticket.ddd.application.service.ticket.TicketDetailAppService;
import com.tetticket.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.tetticket.ddd.application.service.ticket.cache.TicketDetailCacheServiceRefactor;
import com.tetticket.ddd.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    // CALL Service Domain Module
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    // CALL CACHE
    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Autowired
    private TicketDetailCacheServiceRefactor ticketDetailCacheServiceRefactor;

    @Override
    public TicketDetailDTO getTicketDetailById(Long ticketId, Long version) {
        log.info("Implement Application : {}, {}: ", ticketId, version);
        TicketDetailCache ticketDetailCache = ticketDetailCacheServiceRefactor.getTicketDetail(ticketId, version);
        // mapper to DTO
        TicketDetailDTO ticketDetailDTO = TicketDetailMapper.mapperToTicketDetailDTO(ticketDetailCache.getTicketDetail());
        ticketDetailDTO.setVersion(ticketDetailCache.getVersion());
        return ticketDetailDTO;
    }

    @Override
    public boolean orderTicketByUser(Long ticketId) {
        return ticketDetailCacheServiceRefactor.orderTicketByUser(ticketId);
    }


}
