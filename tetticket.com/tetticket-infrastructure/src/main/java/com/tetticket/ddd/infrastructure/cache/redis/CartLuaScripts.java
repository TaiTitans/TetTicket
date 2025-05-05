package com.tetticket.ddd.infrastructure.cache.redis;

@Component
public class CartLuaScripts {
    public static final String ADD_TO_CART_SCRIPT =
            "local cart = redis.call('hget', KEYS[1], ARGV[1]) " +
                    "local cartData = cjson.decode(cart or '{}') " +
                    "if not cartData.items then cartData.items = {} end " +
                    "local found = false " +
                    "for i, item in ipairs(cartData.items) do " +
                    "    if item.ticketId == ARGV[2] then " +
                    "        item.quantity = item.quantity + ARGV[3] " +
                    "        item.subTotal = (item.priceFlash > 0 and item.priceFlash or item.priceOriginal) * item.quantity " +
                    "        found = true " +
                    "        break " +
                    "    end " +
                    "end " +
                    "if not found then " +
                    "    local newItem = cjson.decode(ARGV[4]) " +
                    "    table.insert(cartData.items, newItem) " +
                    "end " +
                    "local total = 0 " +
                    "for _, item in ipairs(cartData.items) do " +
                    "    total = total + item.subTotal " +
                    "end " +
                    "cartData.totalAmount = total " +
                    "redis.call('hset', KEYS[1], ARGV[1], cjson.encode(cartData)) " +
                    "redis.call('expire', KEYS[1], ARGV[5]) " +
                    "return cjson.encode(cartData)";

    public static final String REMOVE_FROM_CART_SCRIPT =
            "local cart = redis.call('hget', KEYS[1], ARGV[1]) " +
                    "local cartData = cjson.decode(cart or '{}') " +
                    "if not cartData.items then return cart end " +
                    "local newItems = {} " +
                    "for i, item in ipairs(cartData.items) do " +
                    "    if item.ticketId ~= ARGV[2] then " +
                    "        table.insert(newItems, item) " +
                    "    end " +
                    "end " +
                    "cartData.items = newItems " +
                    "local total = 0 " +
                    "for _, item in ipairs(cartData.items) do " +
                    "    total = total + item.subTotal " +
                    "end " +
                    "cartData.totalAmount = total " +
                    "redis.call('hset', KEYS[1], ARGV[1], cjson.encode(cartData)) " +
                    "return cjson.encode(cartData)";


    public static final String UPDATE_CART_SCRIPT =
            "local cart = redis.call('hget', KEYS[1], ARGV[1]) " +
                    "local cartData = cjson.decode(cart or '{}') " +
                    "if not cartData.items then return cart end " +
                    "local updateItem = cjson.decode(ARGV[2]) " +
                    "local found = false " +
                    "for i, item in ipairs(cartData.items) do " +
                    "    if item.ticketId == ARGV[3] then " +
                    "        item.quantity = updateItem.quantity " +
                    "        item.subTotal = (item.priceFlash > 0 and item.priceFlash or item.priceOriginal) * item.quantity " +
                    "        found = true " +
                    "        break " +
                    "    end " +
                    "end " +
                    "if found then " +
                    "    local total = 0 " +
                    "    for _, item in ipairs(cartData.items) do " +
                    "        total = total + item.subTotal " +
                    "    end " +
                    "    cartData.totalAmount = total " +
                    "    redis.call('hset', KEYS[1], ARGV[1], cjson.encode(cartData)) " +
                    "    redis.call('expire', KEYS[1], ARGV[4]) " +
                    "end " +
                    "return cjson.encode(cartData)";
}