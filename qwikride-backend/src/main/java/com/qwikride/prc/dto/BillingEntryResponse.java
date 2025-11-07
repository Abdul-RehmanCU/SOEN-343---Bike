package com.qwikride.prc.dto;

import com.qwikride.prc.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BillingEntryResponse(
        Long ledgerEntryId,
        UUID planVersionId,
        String planName,
        UUID bikeId,
        Long startStationId,
        Long endStationId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        long durationMinutes,
        double distanceKm,
        BigDecimal total,
        PaymentStatus paymentStatus,
        List<ChargeLineResponse> charges,
        String summary) {
}
