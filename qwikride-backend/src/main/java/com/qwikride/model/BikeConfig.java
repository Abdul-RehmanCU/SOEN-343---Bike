package com.qwikride.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BikeConfig {
    @NotNull(message = "Bike type is required")
    private BikeType type;

    @NotNull(message = "Station ID is required")
    private Long stationId;
}
