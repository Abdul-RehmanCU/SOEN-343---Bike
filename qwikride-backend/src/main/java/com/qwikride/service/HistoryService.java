package com.qwikride.service;

import com.qwikride.event.*;
import com.qwikride.model.Bike;
import com.qwikride.model.RideHistory;
import com.qwikride.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service that listens to domain events and persists ride history.
 * Implements Observer Pattern via EventSubscriber interface.
 */
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

    /**
     * Handle trip started event - create a new ride history entry.
     */
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

        System.out.println("History Service: Trip started for bike " + event.getBikeId() + 
                         ", user " + event.getUserId() + 
                         ", station " + event.getStationId());
    }

    /**
     * Handle trip ended event - update ride history entry.
     */
    private void handleTripEnded(TripEndedEvent event) {
        // Find the in-progress ride for this user
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

        System.out.println("History Service: Trip ended for bike " + event.getBikeId() + 
                         ", user " + event.getUserId() + 
                         ", duration: " + event.getDurationMinutes() + " min, " +
                         "distance: " + event.getDistanceKm() + " km, " +
                         "cost: $" + event.getCost());
    }

    /**
     * Handle bike moved event - log for audit purposes.
     */
    private void handleBikeMoved(BikeMovedEvent event) {
        System.out.println("History Service: Bike " + event.getBikeId() + 
                         " moved from station " + event.getOldStationId() + 
                         " to station " + event.getNewStationId() + 
                         " by operator " + event.getOperatorId());
    }
}
