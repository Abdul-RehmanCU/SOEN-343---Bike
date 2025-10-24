package com.qwikride.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TripEndedEvent extends BaseDomainEvent {
    private UUID bikeId;
    private Long userId;
    private Long returnStationId;
    private double durationMinutes;
    private double distanceKm;
    private double cost;

    public TripEndedEvent(UUID bikeId, Long userId, Long returnStationId, double durationMinutes, double distanceKm, double cost) {
        this.bikeId = bikeId;
        this.userId = userId;
        this.returnStationId = returnStationId;
        this.durationMinutes = durationMinutes;
        this.distanceKm = distanceKm;
        this.cost = cost;
    }
}
