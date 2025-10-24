package com.qwikride.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BikeMoveRequestDTO {
    @NotNull(message = "Bike ID is required")
    private UUID bikeId;

    @NotNull(message = "New Station ID is required")
    private Long newStationId;

    @NotNull(message = "Operator ID is required")
    private Long operatorId;
}
