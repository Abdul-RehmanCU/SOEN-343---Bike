package com.qwikride.dto;

import com.qwikride.model.RideHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideHistoryResponseDTO {
    private Long id;
    private Long userId;
    private UUID bikeId;
    private Long startStationId;
    private Long endStationId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double durationMinutes;
    private Double distanceKm;
    private Double cost;
    private RideHistory.RideStatus status;
    private String bikeType;

    public static RideHistoryResponseDTO fromEntity(RideHistory rideHistory) {
        return RideHistoryResponseDTO.builder()
                .id(rideHistory.getId())
                .userId(rideHistory.getUserId())
                .bikeId(rideHistory.getBikeId())
                .startStationId(rideHistory.getStartStationId())
                .endStationId(rideHistory.getEndStationId())
                .startTime(rideHistory.getStartTime())
                .endTime(rideHistory.getEndTime())
                .durationMinutes(rideHistory.getDurationMinutes())
                .distanceKm(rideHistory.getDistanceKm())
                .cost(rideHistory.getCost())
                .status(rideHistory.getStatus())
                .bikeType(rideHistory.getBikeType())
                .build();
    }
}

