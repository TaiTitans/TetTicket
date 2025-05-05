package com.tetticket.ddd.controller.http;

import com.tetticket.ddd.application.model.cart.CartDTO;
import com.tetticket.ddd.application.model.cart.CartItemDTO;
import com.tetticket.ddd.application.service.cart.CartService;
import com.tetticket.ddd.controller.model.enums.ResultCode;
import com.tetticket.ddd.controller.model.vo.ResultMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cart")
@Slf4j
@Tag(name = "Cart", description = "Cart management APIs")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "Get user's cart", description = "Retrieves the current user's Ticket cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<ResultMessage<CartDTO>> getCart(@RequestAttribute Long userId) {
        try {
            CartDTO cart = cartService.getCart(userId);
            return ResponseEntity.ok(
                    new ResultMessage<CartDTO>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult(cart)
            );
        } catch (Exception e) {
            log.error("Error getting cart for user {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ResultMessage<CartDTO>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code()));
        }
    }

    @Operation(summary = "Add item to cart", description = "Adds a new item to the Ticket cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add")
    public ResponseEntity<ResultMessage<CartDTO>> addToCart(
            @RequestAttribute Long userId,
            @Valid @RequestBody CartItemDTO item) {
        try {
            CartDTO updatedCart = cartService.addToCart(userId, item);
            return ResponseEntity.ok(
                    new ResultMessage<CartDTO>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult(updatedCart)
            );
        } catch (Exception e) {
            log.error("Error adding item to cart for user {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ResultMessage<CartDTO>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code()));
        }
    }

    @Operation(summary = "Update cart item", description = "Updates quantity of an item in the cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public ResponseEntity<ResultMessage<CartDTO>> updateCart(
            @RequestAttribute Long userId,
            @Valid @RequestBody CartItemDTO item) {
        try {
            CartDTO updatedCart = cartService.updateCart(userId, item);
            return ResponseEntity.ok(
                    new ResultMessage<CartDTO>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult(updatedCart)
            );
        } catch (Exception e) {
            log.error("Error updating cart for user {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ResultMessage<CartDTO>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code()));
        }
    }

    @Operation(summary = "Remove item from cart", description = "Removes an item from the Ticket cart")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item removed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{ticketId}")
    public ResponseEntity<ResultMessage<CartDTO>> removeFromCart(
            @RequestAttribute Long userId,
            @PathVariable Long ticketId) {
        try {
            CartDTO updatedCart = cartService.removeFromCart(userId, ticketId);
            return ResponseEntity.ok(
                    new ResultMessage<CartDTO>()
                            .setSuccess(true)
                            .setMessage(ResultCode.SUCCESS.message())
                            .setCode(ResultCode.SUCCESS.code())
                            .setResult(updatedCart)
            );
        } catch (Exception e) {
            log.error("Error removing item from cart for user {}: {}", userId, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ResultMessage<CartDTO>()
                            .setSuccess(false)
                            .setMessage(ResultCode.ERROR.message())
                            .setCode(ResultCode.ERROR.code()));
        }
    }
}
