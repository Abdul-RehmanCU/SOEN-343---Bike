package com.qwikride.prc.payment;

import java.time.LocalDateTime;

public record PaymentResponse(boolean success,
        String transactionId,
        LocalDateTime processedAt,
        String failureReason) {
}
