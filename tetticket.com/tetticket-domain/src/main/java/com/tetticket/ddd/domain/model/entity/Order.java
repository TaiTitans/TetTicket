package com.tetticket.ddd.domain.model.entity;

import com.tetticket.ddd.domain.model.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "orders")
public class Order {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
@JoinColumn(name = "user_id", nullable = false)
private Users user_id;

@NotNull
@Min(0)
private Integer total_amount;

@NotNull
private Status status;

@NotNull
private LocalDateTime created_at;

private LocalDateTime updated_at;
}
