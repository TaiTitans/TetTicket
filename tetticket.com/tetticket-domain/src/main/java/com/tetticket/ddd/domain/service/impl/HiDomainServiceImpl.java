package com.tetticket.ddd.domain.service.impl;

import com.tetticket.ddd.domain.repository.HiDomainRepository;
import com.tetticket.ddd.domain.service.HiDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class HiDomainServiceImpl implements HiDomainService {
    @Resource
    private HiDomainRepository hiDomainRepository;

    @Override
    public String sayHi(String who) {
        return hiDomainRepository.sayHi(who);
    }
}
