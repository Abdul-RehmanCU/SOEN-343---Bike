package com.qwikride.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ride_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "bike_id", nullable = false)
    private UUID bikeId;

    @Column(name = "start_station_id")
    private Long startStationId;

    @Column(name = "end_station_id")
    private Long endStationId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration_minutes")
    private Double durationMinutes;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "cost")
    private Double cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RideStatus status;

    @Column(name = "bike_type")
    private String bikeType;

    public enum RideStatus {
        COMPLETED, IN_PROGRESS, CANCELLED
    }
}

