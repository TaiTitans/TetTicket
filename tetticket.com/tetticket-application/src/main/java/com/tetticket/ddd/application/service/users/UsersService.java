package com.tetticket.ddd.application.service.users;

import com.tetticket.ddd.application.mapper.UsersMapper;
import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.domain.model.entity.Users;
import com.tetticket.ddd.infrastructure.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;



    

}