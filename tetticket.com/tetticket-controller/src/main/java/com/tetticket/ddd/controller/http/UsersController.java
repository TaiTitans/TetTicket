package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.model.UsersDTO;
import com.tetticket.ddd.application.service.users.UsersService;
import com.tetticket.ddd.domain.model.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
public class UsersController {
    @Autowired
    private UsersService usersAppService;

}
