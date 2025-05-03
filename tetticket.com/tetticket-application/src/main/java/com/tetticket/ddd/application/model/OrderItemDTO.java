package com.tetticket.ddd.application.model;

import lombok.Data;

@Data
public class OrderItemDTO {
    private int id;
    private int orderId;
    private int ticketId;
    private int quantity;
    private double price;
}
