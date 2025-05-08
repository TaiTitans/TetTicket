package com.tetticket.ddd.domain.model.entity;

import com.tetticket.ddd.domain.model.enums.PaymentMethod;
import com.tetticket.ddd.domain.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order_id;
    private PaymentMethod payment_method;
    private PaymentStatus payment_status;
    private int transaction_id;
    private LocalDateTime paid_at;
}
