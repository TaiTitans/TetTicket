package com.tetticket.ddd.application.service.cart;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetticket.ddd.application.model.cart.CartDTO;
import com.tetticket.ddd.application.model.cart.CartItemDTO;
import com.tetticket.ddd.infrastructure.cache.redis.CartLuaScripts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
@Slf4j
public class CartService {
    private final RedissonClient redissonClient;
    private static final String CART_KEY_PREFIX = "cart:";
    private static final int CART_EXPIRE_DAYS = 7;
    private final ObjectMapper objectMapper;

    public CartDTO getCart(Long userId){
        RMap<String, String> cart = redissonClient.getMap(CART_KEY_PREFIX + userId);
        String cartJson = cart.get(String.valueOf(userId));

        if(cartJson == null){
            CartDTO cartDTO = new CartDTO();
            cartDTO.setUserId(userId);
            cartDTO.setItems(new ArrayList<>());
            cartDTO.setTotalAmount(0L);
            return cartDTO;
        }

        try{
            return objectMapper.readValue(cartJson, CartDTO.class);
        }catch (JsonProcessingException e){
            log.error("Error parsing cart JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to parse cart data", e);
        }
    }

    public CartDTO addToCart(Long userId, CartItemDTO itemDTO){
        try{
            RScript script = redissonClient.getScript();
            String cartKey = CART_KEY_PREFIX + userId;
            String userIdStr = String.valueOf(userId);
            String tickerIdStr = String.valueOf(itemDTO.getTicketId());
            String quantityStr = String.valueOf(itemDTO.getQuantity());
            String itemJson = objectMapper.writeValueAsString(itemDTO);
            String expireSeconds = String.valueOf(TimeUnit.DAYS.toSeconds(CART_EXPIRE_DAYS));

            String result = script.eval(RScript.Mode.READ_WRITE,
                    CartLuaScripts.ADD_TO_CART_SCRIPT,
                    RScript.ReturnType.VALUE,
                    Collections.singletonList(cartKey),
                    userIdStr, tickerIdStr, quantityStr, itemJson, expireSeconds
                    );
            return objectMapper.readValue(result, CartDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing cart JSON: {}", e.getMessage());
            throw new RuntimeException("Failed to parse cart data", e);
        }
    }

    public CartDTO removeFromCart(Long userId, Long ticketId) {
        try {
            RScript script = redissonClient.getScript();
            String cartKey = CART_KEY_PREFIX + userId;
            String userIdStr = String.valueOf(userId);
            String ticketIdStr = String.valueOf(ticketId);

            String result = script.eval(RScript.Mode.READ_WRITE,
                    CartLuaScripts.REMOVE_FROM_CART_SCRIPT,
                    RScript.ReturnType.VALUE,
                    Collections.singletonList(cartKey),
                    userIdStr, ticketIdStr);

            return objectMapper.readValue(result, CartDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error processing cart data", e);
            throw new RuntimeException("Error removing item from cart");
        }
    }


    public CartDTO updateCart(Long userId, CartItemDTO itemDTO) {
        try {
            RScript script = redissonClient.getScript();
            String cartKey = CART_KEY_PREFIX + userId;
            String userIdStr = String.valueOf(userId);
            String itemJson = objectMapper.writeValueAsString(itemDTO);
            String ticketIdStr = String.valueOf(itemDTO.getTicketId());
            String expireSeconds = String.valueOf(TimeUnit.DAYS.toSeconds(CART_EXPIRE_DAYS));

            // Validate quantity
            if (itemDTO.getQuantity() <= 0) {
                return removeFromCart(userId, itemDTO.getTicketId());
            }

            String result = script.eval(RScript.Mode.READ_WRITE,
                    CartLuaScripts.UPDATE_CART_SCRIPT,
                    RScript.ReturnType.VALUE,
                    Collections.singletonList(cartKey),
                    userIdStr, itemJson, ticketIdStr, expireSeconds);

            return objectMapper.readValue(result, CartDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error updating cart: {}", e.getMessage());
            throw new RuntimeException("Failed to update cart", e);
        }
    }

    public void clearCart(Long userId) {
        RMap<String, String> cart = redissonClient.getMap(CART_KEY_PREFIX + userId);
        cart.delete();
    }


}
