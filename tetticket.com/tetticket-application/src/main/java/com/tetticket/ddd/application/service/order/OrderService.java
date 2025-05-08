package com.tetticket.ddd.application.service.order;

import com.tetticket.ddd.application.model.OrderDTO;
import com.tetticket.ddd.application.model.OrderEvent;
import com.tetticket.ddd.domain.model.enums.Status;

import java.util.concurrent.CompletableFuture;

/**
 * Service interface for managing orders
 */
public interface OrderService {
    /**
     * Creates a new order asynchronously for the given user
     * @param userId The ID of the user creating the order
     * @return A CompletableFuture containing the created OrderDTO
     * @throws IllegalStateException if the cart is empty
     */
    CompletableFuture<OrderDTO> createOrder(Long userId);

    /**
     * Updates the status of an existing order
     * @param orderId The ID of the order to update
     * @param status The new status to set
     */
    void updateOrderStatus(Long orderId, Status status);

    /**
     * Processes the order event received from Kafka
     * @param event The order event to process
     */
    void processOrder(OrderEvent event);

    /**
     * Processes payment for the order
     * @param event The order event for payment
     */
    void processPayment(OrderEvent event);

    /**
     * Finalizes the order after processing
     * @param event The order event to finalize
     */
    void finalizeOrder(OrderEvent event);
}