package com.qwikride.event;

import com.qwikride.model.BikeStation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Event published when a bike station changes status.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StationStatusChangedEvent extends BaseDomainEvent {
    private Long stationId;
    private BikeStation.StationStatus oldStatus;
    private BikeStation.StationStatus newStatus;

    public StationStatusChangedEvent(Long stationId, BikeStation.StationStatus oldStatus, BikeStation.StationStatus newStatus) {
        this.stationId = stationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}