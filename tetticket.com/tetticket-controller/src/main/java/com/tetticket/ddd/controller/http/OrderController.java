package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.model.OrderDTO;
import com.tetticket.ddd.application.service.order.OrderService;
import com.tetticket.ddd.controller.model.enums.ResultCode;
import com.tetticket.ddd.controller.model.vo.ResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create new order", description = "Creates new order from user's cart")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Order creation accepted"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ResultMessage<OrderDTO>> createOrder(@RequestAttribute Long userId) {
        try {
            CompletableFuture<OrderDTO> futureOrder = orderService.createOrder(userId);
            OrderDTO order = futureOrder.get(5, TimeUnit.SECONDS);

            return ResponseEntity.accepted()
                    .body(new ResultMessage<OrderDTO>()
                            .setSuccess(true)
                            .setMessage("Order created successfully")
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult(order));

        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ResultMessage<OrderDTO>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code()));
        }
    }

//    @GetMapping("/{orderId}")
//    public ResponseEntity<ResultMessage<OrderDTO>> getOrder(
//            @RequestAttribute Long userId,
//            @PathVariable Long orderId) {
//        try {
//            OrderDTO order = orderService.getOrder(userId, orderId);
//            return ResponseEntity.ok(
//                    new ResultMessage<OrderDTO>()
//                            .setSuccess(true)
//                            .setMessage(ResultCode.SUCCESS.message())
//                            .setCode(ResultCode.SUCCESS.code())
//                            .setResult(order));
//
//        } catch (Exception e) {
//            log.error("Error getting order: {}", e.getMessage());
//            return ResponseEntity.internalServerError()
//                    .body(new ResultMessage<OrderDTO>()
//                            .setSuccess(false)
//                            .setMessage(ResultCode.ERROR.message())
//                            .setCode(ResultCode.ERROR.code()));
//        }
//    }
}