package com.tetticket.ddd.infrastructure.repository;

public interface EmailRepository {
    void sendOTPEmail(String email, String otp);
}
