package com.tetticket.ddd.application.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private Long orderId;
    private String paymentMethod;
    private String paymentStatus;
    private int transactionId;
    private LocalDateTime paidAt;

}
