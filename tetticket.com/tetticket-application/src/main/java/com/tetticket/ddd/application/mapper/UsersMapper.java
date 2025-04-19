package com.tetticket.ddd.application.mapper;

import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.domain.model.entity.Users;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {
    public static UsersDTO mapperToUsersDTO(Users users) {
        if(users == null) return null;

        UsersDTO usersDTO = new UsersDTO();
        BeanUtils.copyProperties(users, usersDTO);

        return usersDTO;
    }
    public static Users mapperToUsers(UsersDTO usersDTO) {
        if(usersDTO == null) return null;

        Users users = new Users();
        BeanUtils.copyProperties(usersDTO, users);

        return users;
    }
}
