package com.xxx.ddd.infrastructure.persistence.repository;

import com.xxx.ddd.domain.repository.HiDomainRepository;

public class HiInfrasRepositoryImpl implements HiDomainRepository {
    @Override
    public String sayHi(String name) {
        return "Hi Infrastructure";
    }
}
