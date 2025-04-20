package com.tetticket.ddd.controller.model;

import lombok.Data;

@Data
public class ResultForm<T> {
    private String code;
    private String message;
    private T data;
}
