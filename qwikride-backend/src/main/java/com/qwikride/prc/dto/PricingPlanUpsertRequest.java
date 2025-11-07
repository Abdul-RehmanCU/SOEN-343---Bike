package com.qwikride.prc.dto;

import com.qwikride.prc.domain.MembershipStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PricingPlanUpsertRequest(String planName,
        BigDecimal baseFee,
        BigDecimal perMinuteRate,
        BigDecimal ebikeSurcharge,
        MembershipStatus membershipTier,
        String cityId,
        LocalDateTime effectiveFrom,
        LocalDateTime effectiveTo,
        String description,
        boolean publish) {
}
