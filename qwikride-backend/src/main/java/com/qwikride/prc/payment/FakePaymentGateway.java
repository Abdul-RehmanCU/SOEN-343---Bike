package com.qwikride.prc.payment;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class FakePaymentGateway implements PaymentGateway {
    @Override
    public PaymentResponse charge(PaymentRequest request) {
        if (request.amount() == null || request.amount().signum() <= 0) {
            return new PaymentResponse(false, null, LocalDateTime.now(), "INVALID_AMOUNT");
        }
        return new PaymentResponse(true,
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                null);
    }
}
