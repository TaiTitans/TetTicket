package com.xxx.ddd.domain.repository.impl;

import com.xxx.ddd.domain.repository.HiDomainRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HiDomainRepositoryImpl implements HiDomainRepository {

    @Override
    public String sayHi(String name) {
        return "Hi Infrastructure";
    }
}
