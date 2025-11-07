package com.qwikride.prc.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TripSummaryResponse(
        UUID bikeId,
        Long riderId,
        Long startStationId,
        Long endStationId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        long durationMinutes,
        double distanceKm,
        BigDecimal total,
        List<ChargeLineResponse> charges,
        String planName) {
}
