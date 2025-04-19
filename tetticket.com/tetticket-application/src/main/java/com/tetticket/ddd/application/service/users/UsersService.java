package com.tetticket.ddd.application.service.users;

import com.tetticket.ddd.application.mapper.UsersMapper;
import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.domain.model.entity.Users;
import com.tetticket.ddd.infrastructure.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    public UsersService(UsersRepository usersRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.usersMapper = usersMapper;
    }

    public UsersDTO getUserById(Long userID) {
        Optional<Users> usersOptional = usersRepository.findById(userID);
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            return usersMapper.mapperToUsersDTO(users);
        } else {
            return null;
        }
    }

    public void saveUser(UsersDTO usersDTO) {
        Users users = usersMapper.mapperToUsers(usersDTO);
        usersRepository.save(users);
    }
}