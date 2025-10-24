package com.qwikride.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class BikeReservationRequestDTO {
    @NotNull(message = "Station ID is required")
    private Long stationId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @Min(value = 1, message = "Expires after minutes must be at least 1")
    private int expiresAfterMinutes = 15; // Default 15 minutes
}
