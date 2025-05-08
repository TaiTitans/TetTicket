package com.tetticket.ddd.infrastructure.persistence.repository;

import com.tetticket.ddd.domain.model.entity.TicketDetail;
import com.tetticket.ddd.domain.repository.TicketDetailRepository;
import com.tetticket.ddd.infrastructure.persistence.mapper.TicketDetailJPAMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TicketDetailInfrasRepositoryImpl implements TicketDetailRepository {
    //Call JPA Mapper

    @Autowired
    private TicketDetailJPAMapper ticketDetailJPAMapper;

    @Override
    public Optional<TicketDetail> findById(Long id){
        log.info("Implement Infrastructure : {}", id);
        return ticketDetailJPAMapper.findById(id);
    }

    @Override
    public List<TicketDetail> findAll() {
        log.info("Fetching all ticket details");
        return ticketDetailJPAMapper.findAll();
    }
}
