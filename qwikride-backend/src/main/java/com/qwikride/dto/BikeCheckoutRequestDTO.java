package com.qwikride.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BikeCheckoutRequestDTO {
    @NotNull(message = "Bike ID is required")
    private UUID bikeId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
