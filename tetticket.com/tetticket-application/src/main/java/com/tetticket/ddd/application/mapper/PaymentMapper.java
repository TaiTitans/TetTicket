package com.tetticket.ddd.application.mapper;

import com.tetticket.ddd.application.model.PaymentDTO;
import com.tetticket.ddd.domain.model.entity.Payment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public static PaymentDTO mapperToPaymentDTO(Payment payment) {
        if (payment == null) return null;
        PaymentDTO paymentDTO = new PaymentDTO();
        BeanUtils.copyProperties(payment, paymentDTO);
        return paymentDTO;
    }
    public static Payment mapperToPayment(PaymentDTO paymentDTO) {
        if (paymentDTO == null) return null;
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentDTO, payment);
        return payment;
    }
}
