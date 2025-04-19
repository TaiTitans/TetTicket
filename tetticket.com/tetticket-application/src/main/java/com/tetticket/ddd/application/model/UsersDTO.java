package com.tetticket.ddd.application.model;

import lombok.Data;

@Data
public class UsersDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
}
