package com.qwikride.prc.payment;

import java.math.BigDecimal;

public record PaymentRequest(Long riderId,
        BigDecimal amount,
        String paymentMethodToken,
        String referenceId) {
}
