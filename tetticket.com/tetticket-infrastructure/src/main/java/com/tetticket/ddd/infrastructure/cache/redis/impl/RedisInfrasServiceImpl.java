package com.tetticket.ddd.infrastructure.cache.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetticket.ddd.infrastructure.cache.redis.RedisInfrasService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisInfrasServiceImpl implements RedisInfrasService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setString(String key, String value) {
        if(StringUtils.hasLength(key)){
            //null or ''
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        //        Object result = redisTemplate.opsForValue().get(key);
//        if (result == null) {
//            return null;
//        }
//        return String.valueOf(result);
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
//        log.info("Set redis::1, {}", key);
        if(!StringUtils.hasLength(key)){
            //null or ''
            //            log.info("Set redis::null, {}", StringUtils.hasLength(key));
            return;
        }
        try{
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            log.error("setObject error:{}", e.getMessage());
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
     Object result = redisTemplate.opsForValue().get(key);
     log.info("get Cache:;{}", result);
     if(result == null){
         return null;
     }
     //If result as LinkedHashMap
     if(result instanceof Map){
         try{
             ObjectMapper objectMapper = new ObjectMapper();
             return objectMapper.convertValue(result, targetClass);
         }catch (IllegalArgumentException e){
             log.error("Error converting LinkedHashMap to object: {}", e.getMessage());
             return null;
         }
     }
     // If result as String
        if(result instanceof String){
            try{
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue((String) result, targetClass);
            }catch (JsonProcessingException e){
                log.error("Error deserializing JSON to object: {}", e.getMessage());
                return null;
            }
        }
        return null;
    }

    @Override
    public void setObjectWithTTL(String key, Object value, long timeout, TimeUnit unit) {
        if(!StringUtils.hasLength(key)){
            return;
        }
        try{
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }catch(Exception e){
            log.error("setObjectWithTTL error: {}", e.getMessage());
        }
    }
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
