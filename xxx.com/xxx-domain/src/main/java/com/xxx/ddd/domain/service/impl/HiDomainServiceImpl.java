package com.xxx.ddd.domain.service.impl;

import com.xxx.ddd.domain.repository.HiDomainRepository;
import com.xxx.ddd.domain.service.HiDomainService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
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
