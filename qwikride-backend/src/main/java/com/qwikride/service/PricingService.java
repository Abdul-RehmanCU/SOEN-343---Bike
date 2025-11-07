package com.qwikride.service;

import com.qwikride.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            System.out.println("Pricing Service: Trip ended for bike " + tripEndedEvent.getBikeId() +
                               ", user " + tripEndedEvent.getUserId() +
                               ", cost: $" + tripEndedEvent.getCost());
            // Here you would implement actual pricing logic, update user balance, etc.
        } else if (event instanceof BikeReservedEvent) {
            BikeReservedEvent bikeReservedEvent = (BikeReservedEvent) event;
            System.out.println("Pricing Service: Bike " + bikeReservedEvent.getBikeId() + " reserved by user " + bikeReservedEvent.getUserId());
        }
    }
}
