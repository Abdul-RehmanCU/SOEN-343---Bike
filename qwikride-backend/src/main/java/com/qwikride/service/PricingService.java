package com.qwikride.service;

import com.qwikride.event.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PricingService implements EventSubscriber {

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
