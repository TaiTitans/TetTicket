package com.tetticket.ddd.application.service.order.impl;

import com.tetticket.ddd.application.mapper.OrderMapper;
import com.tetticket.ddd.application.model.OrderDTO;
import com.tetticket.ddd.application.model.OrderEvent;
import com.tetticket.ddd.application.model.cart.CartDTO;
import com.tetticket.ddd.application.model.cart.CartItemDTO;
import com.tetticket.ddd.application.service.cart.CartService;
import com.tetticket.ddd.application.service.order.OrderService;
import com.tetticket.ddd.domain.model.entity.*;
import com.tetticket.ddd.domain.model.enums.PaymentMethod;
import com.tetticket.ddd.domain.model.enums.PaymentStatus;
import com.tetticket.ddd.domain.model.enums.Status;
import com.tetticket.ddd.infrastructure.repository.OrderItemRepository;
import com.tetticket.ddd.infrastructure.repository.OrderRepository;
import com.tetticket.ddd.infrastructure.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String ORDER_TOPIC = "order-events";
    private static final String PAYMENT_TOPIC = "payment-events";
    private static final String ORDER_PROCESSING_TOPIC = "order-processing";

    @Async
    @Transactional
    public CompletableFuture<OrderDTO> createOrder(Long userId) {
        try {
            validateCart(userId);
            Order savedOrder = createAndSaveOrder(userId);
            publishOrderEvent(savedOrder, userId);
            return CompletableFuture.completedFuture(OrderMapper.mapperToOrderDTO(savedOrder));
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            throw new RuntimeException("Failed to create order", e);
        }
    }

    // ===============================

    private void validateCart(Long userId) {
        CartDTO cart = cartService.getCart(userId);
        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
    }

    private Order createAndSaveOrder(Long userId) {
        CartDTO cart = cartService.getCart(userId);
        Order order = new Order();
        Users user = new Users();
        user.setUser_id(userId);
        order.setUser_id(user);
        order.setTotal_amount(cart.getTotalAmount().intValue());
        order.setStatus(Status.PENDING);
        order.setCreated_at(LocalDateTime.now());
        return orderRepository.save(order);
    }

    private void publishOrderEvent(Order order, Long userId) {
        CartDTO cart = cartService.getCart(userId);
        OrderEvent orderEvent = new OrderEvent()
                .setOrderId(order.getId())
                .setUserId(userId)
                .setCartItems(cart.getItems())
                .setStatus(Status.PENDING);
        kafkaTemplate.send(ORDER_TOPIC, orderEvent);
    }

    @KafkaListener(topics = ORDER_TOPIC)
    public void processOrder(OrderEvent event) {
        try {
            Thread.sleep(2000); // Simulate processing time
            List<OrderItem> orderItems = createOrderItems(event);
            orderItemRepository.saveAll(orderItems);
            updateOrderStatus(event.getOrderId(), Status.PENDING);
            kafkaTemplate.send(PAYMENT_TOPIC, event);
        } catch (Exception e) {
            handleProcessingError("Error processing order", e, event.getOrderId());
        }
    }

    private List<OrderItem> createOrderItems(OrderEvent event) {
        return event.getCartItems().stream()
                .map(item -> createOrderItem(item, event.getOrderId()))
                .collect(Collectors.toList());
    }

    private OrderItem createOrderItem(CartItemDTO item, Long orderId) {
        OrderItem orderItem = new OrderItem();
        Order order = new Order();
        order.setId(orderId);
        orderItem.setOrder_id(order);
        orderItem.setTicket_id(new TicketDetail().setId(item.getTicketId()));
        orderItem.setQuantity(item.getQuantity());
        orderItem.setPrice(calculatePrice(item));
        return orderItem;
    }

    private Double calculatePrice(CartItemDTO item) {
        return item.getPriceFlash() != null && item.getPriceFlash() > 0
                ? item.getPriceFlash().doubleValue()
                : item.getPriceOriginal().doubleValue();
    }

    @KafkaListener(topics = PAYMENT_TOPIC)
    public void processPayment(OrderEvent event) {
        try {
            Thread.sleep(2000); // Simulate payment processing
            Payment payment = createPayment(event.getOrderId());

            // Simulate payment result (success/failure)
            boolean isPaymentSuccessful = simulatePaymentProcess();

            if (isPaymentSuccessful) {
                payment.setPayment_status(PaymentStatus.SUCCESS);
                paymentRepository.save(payment);
                updateOrderStatus(event.getOrderId(), Status.PENDING);
                kafkaTemplate.send(ORDER_PROCESSING_TOPIC, event);
            } else {
                payment.setPayment_status(PaymentStatus.CANCEL);
                paymentRepository.save(payment);
                handlePaymentFailure(event.getOrderId(), event.getUserId());
            }
        } catch (Exception e) {
            handleProcessingError("Error processing payment", e, event.getOrderId());
        }
    }

    private boolean simulatePaymentProcess() {
        // Simulate payment processing logic
        // For demo: 80% success rate
        return Math.random() > 0.2;
    }

    private void handlePaymentFailure(Long orderId, Long userId) {
        // Update order status to CANCEL
        updateOrderStatus(orderId, Status.CANCEL);

        // Log payment failure
        log.error("Payment failed for order: {}", orderId);

        // Optionally notify user about payment failure
        // You can add notification service here

        // Keep items in cart for retry
        // No need to clear cart on failure
    }

    private Payment createPayment(Long orderId) {
        Payment payment = new Payment();
        Order order = new Order();
        order.setId(orderId);
        payment.setOrder_id(order);
        payment.setPayment_method(PaymentMethod.BANKING);
        payment.setPayment_status(PaymentStatus.SUCCESS);
        payment.setTransaction_id(generateTransactionId());
        payment.setPaid_at(LocalDateTime.now());
        return payment;
    }

    private int generateTransactionId() {
        return (int) (Math.random() * 1000000);
    }

    @KafkaListener(topics = ORDER_PROCESSING_TOPIC)
    public void finalizeOrder(OrderEvent event) {
        try {
            Thread.sleep(2000); // Simulate processing
            updateOrderStatus(event.getOrderId(), Status.SUCCESS);
            cartService.clearCart(event.getUserId());
        } catch (Exception e) {
            handleProcessingError("Error finalizing order", e, event.getOrderId());
        }
    }

    private void handleProcessingError(String message, Exception e, Long orderId) {
        log.error("{}: {}", message, e.getMessage());
        updateOrderStatus(orderId, Status.CANCEL);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        order.setUpdated_at(LocalDateTime.now());
        orderRepository.save(order);
    }
    // ===============================
}
