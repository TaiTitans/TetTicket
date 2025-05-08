package com.tetticket.ddd.application.model;

import com.tetticket.ddd.application.model.cart.CartItemDTO;
import com.tetticket.ddd.domain.model.enums.Status;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class OrderEvent {
    private Long orderId;
    private Long userId;
    private List<CartItemDTO> cartItems;
    private Status status;
}