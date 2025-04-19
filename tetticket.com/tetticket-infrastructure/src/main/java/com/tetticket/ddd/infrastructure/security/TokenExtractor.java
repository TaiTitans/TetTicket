package com.tetticket.ddd.infrastructure.security;

public class TokenExtractor {
    public static String extractToken(String authorizationHeader){
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            return null;
        }
        return authorizationHeader.substring(7);
    }
}
