package com.tetticket.ddd.application.service.event.impl;

import com.tetticket.ddd.application.service.event.EventAppService;
import com.tetticket.ddd.domain.service.HiDomainService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class EventAppServiceImpl implements EventAppService {
    @Resource
    private HiDomainService hiDomainService;
    @Override
    public String sayHi(String who) {

        return hiDomainService.sayHi(who);
    }
}
