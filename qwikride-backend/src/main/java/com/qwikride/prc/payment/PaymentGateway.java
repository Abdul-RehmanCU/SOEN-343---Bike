package com.qwikride.prc.payment;

public interface PaymentGateway {
    PaymentResponse charge(PaymentRequest request);
}
