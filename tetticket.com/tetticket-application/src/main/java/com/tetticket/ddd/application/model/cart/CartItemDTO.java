package com.tetticket.ddd.application.model.cart;

import lombok.Data;
import java.util.Date;

@Data
public class CartItemDTO {
    private Long ticketId;
    private String name;
    private String description;
    private Integer quantity;
    private Long priceOriginal;
    private Long priceFlash;
    private Date saleEndTime;
    private Long subTotal;
    private Long activityId;
    // True if item is still valid (in stock and sale period)
    private boolean isValid;
}