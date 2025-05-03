package com.tetticket.ddd.application.model.cart;

import lombok.Data;
import java.util.List;

@Data
public class CartDTO {
    private Long userId;
    private List<CartItemDTO> items;
    private Long totalAmount;
}