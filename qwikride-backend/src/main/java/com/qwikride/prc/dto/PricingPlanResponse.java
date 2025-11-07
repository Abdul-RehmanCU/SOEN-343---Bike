package com.qwikride.prc.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PricingPlanResponse(
        UUID planVersionId,
        String planName,
        BigDecimal baseFee,
        BigDecimal perMinuteRate,
        BigDecimal ebikeSurcharge,
        LocalDateTime effectiveFrom,
        LocalDateTime effectiveTo,
        String description,
        List<ExampleCostResponse> exampleCosts) {
}
