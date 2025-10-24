package com.qwikride.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.UUID;

@Data
public class BikeReturnRequestDTO {
    @NotNull(message = "Bike ID is required")
    private UUID bikeId;

    @NotNull(message = "Return Station ID is required")
    private Long returnStationId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @Min(value = 0, message = "Duration must be non-negative")
    private double durationMinutes;

    @Min(value = 0, message = "Distance must be non-negative")
    private double distanceKm;
}
