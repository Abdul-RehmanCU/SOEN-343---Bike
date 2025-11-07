package com.qwikride.prc.dto;

import java.math.BigDecimal;

public record ExampleCostResponse(
        int durationMinutes,
        boolean ebike,
        BigDecimal estimatedTotal) {
}
