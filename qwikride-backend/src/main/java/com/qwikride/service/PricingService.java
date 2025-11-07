package com.qwikride.service;

import com.qwikride.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PricingService implements EventSubscriber {
    // Pricing constants
    private static final double RATE_PER_MINUTE = 0.1;
    private static final double RATE_PER_KM = 0.5;

    /**
     * Calculates the cost of a bike ride.
     * 
     * @param durationMinutes Duration of the ride in minutes
     * @param distanceKm Distance traveled in kilometers
     * @return Total cost of the ride
     */
    public double calculateCost(double durationMinutes, double distanceKm) {
        return (durationMinutes * RATE_PER_MINUTE) + (distanceKm * RATE_PER_KM);
    }

    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof TripEndedEvent) {
            TripEndedEvent tripEndedEvent = (TripEndedEvent) event;
            log.info("Trip cost calculated - bike: {}, user: {}, cost: ${}", 
                    tripEndedEvent.getBikeId(), tripEndedEvent.getUserId(), tripEndedEvent.getCost());
        } else if (event instanceof BikeReservedEvent) {
            BikeReservedEvent bikeReservedEvent = (BikeReservedEvent) event;
            log.debug("Bike reserved - bike: {}, user: {}", 
                    bikeReservedEvent.getBikeId(), bikeReservedEvent.getUserId());
        }
    }
}
