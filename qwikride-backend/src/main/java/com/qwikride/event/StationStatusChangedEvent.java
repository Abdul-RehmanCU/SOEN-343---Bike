package com.qwikride.event;

import com.qwikride.model.BikeStation;

/**
 * Event published when a bike station changes status.
 */
public record StationStatusChangedEvent(Long stationId,
        BikeStation.StationStatus oldStatus,
        BikeStation.StationStatus newStatus) {
}
