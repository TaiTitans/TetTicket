package com.tetticket.ddd.infrastructure.persistence.repository;

import com.tetticket.ddd.domain.repository.HiDomainRepository;

public class HiInfrasRepositoryImpl implements HiDomainRepository {
    @Override
    public String sayHi(String name) {
        return "Hi Infrastructure";
    }
}
