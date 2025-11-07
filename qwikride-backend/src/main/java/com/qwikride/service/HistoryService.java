package com.qwikride.service;

import com.qwikride.event.*;
import com.qwikride.model.Bike;
import com.qwikride.model.RideHistory;
import com.qwikride.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service that listens to domain events and persists ride history.
 * Implements Observer Pattern via EventSubscriber interface.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService implements EventSubscriber {
    private final RideHistoryService rideHistoryService;
    private final BikeRepository bikeRepository;

    @Override
    @Transactional
    public void onEvent(DomainEvent event) {
        if (event instanceof TripStartedEvent) {
            handleTripStarted((TripStartedEvent) event);
        } else if (event instanceof TripEndedEvent) {
            handleTripEnded((TripEndedEvent) event);
        } else if (event instanceof BikeMovedEvent) {
            handleBikeMoved((BikeMovedEvent) event);
        }
    }

    private void handleTripStarted(TripStartedEvent event) {
        Optional<Bike> bikeOpt = bikeRepository.findById(event.getBikeId());
        String bikeType = bikeOpt.map(bike -> bike.getType() != null ? bike.getType().name() : "UNKNOWN")
                                  .orElse("UNKNOWN");

        rideHistoryService.createRideHistory(
            event.getBikeId(),
            event.getUserId(),
            event.getStationId(),
            bikeType
        );

        log.info("Trip started - bike: {}, user: {}, station: {}", 
                event.getBikeId(), event.getUserId(), event.getStationId());
    }

    private void handleTripEnded(TripEndedEvent event) {
        Optional<RideHistory> inProgressRide = rideHistoryService.findInProgressRide(event.getUserId());
        
        if (inProgressRide.isPresent()) {
            rideHistoryService.updateRideHistory(
                inProgressRide.get().getId(),
                event.getReturnStationId(),
                event.getDurationMinutes(),
                event.getDistanceKm(),
                event.getCost()
            );
        }

        log.info("Trip ended - bike: {}, user: {}, duration: {}min, distance: {}km, cost: ${}", 
                event.getBikeId(), event.getUserId(), event.getDurationMinutes(), 
                event.getDistanceKm(), event.getCost());
    }

    private void handleBikeMoved(BikeMovedEvent event) {
        log.info("Bike moved - bike: {}, from station: {}, to station: {}, operator: {}", 
                event.getBikeId(), event.getOldStationId(), 
                event.getNewStationId(), event.getOperatorId());
    }
}