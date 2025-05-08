# Order Creation API Flow

## Overview
This API handles the creation and processing of orders using asynchronous processing with Kafka messaging. The flow includes order creation, payment processing, and order finalization.

## Flow Steps

### 1. Order Creation (`POST /api/v1/orders`)
- Validates user's cart is not empty
- Creates initial order with PENDING status
- Saves order to a database
- Sends order event to Kafka `order-events` topic
- Returns order details immediately with 202 Accepted status

### 2. Order Processing
- Receives event from `order-events` topic
- Creates order items from cart items
- Saves order items to database
- Updates order status to PENDING
- Sends event to `payment-events` topic

### 3. Payment Processing
- Receives event from `payment-events` topic
- Processes payment
- On SUCCESS:
    - Creates payment record with SUCCESS status
    - Updates order status to PROCESSING
    - Sends event to `order-processing` topic
- On CANCEL:
    - Creates payment record with CANCEL status
    - Updates order status to CANCEL
    - Keeps cart items intact
    - Logs payment failure

### 4. Order Finalization
- Receives event from `order-processing` topic
- Updates order status to SUCCESS
- Clears user's cart
- Completes the order process

## Status Flow

## Error Handling
- Payment failures result in CANCEL status
- Failed payments are logged
- Cart items are preserved for retry
- Each step has error logging
- Transactions are managed at each step

## Technical Details
- Async processing using CompletableFuture
- Kafka for event messaging
- Each processing step simulates a 2-second delay
- Transactional operations
- Event-driven architecture