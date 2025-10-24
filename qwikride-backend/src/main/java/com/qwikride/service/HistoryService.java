package com.qwikride.service;

import com.qwikride.event.*;
import org.springframework.stereotype.Service;

@Service
public class HistoryService implements EventSubscriber {
    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof TripStartedEvent) {
            TripStartedEvent tripStartedEvent = (TripStartedEvent) event;
            System.out.println("History Service: Trip started for bike " + tripStartedEvent.getBikeId() + 
                             ", user " + tripStartedEvent.getUserId() + 
                             ", station " + tripStartedEvent.getStationId());
            // Here you would log the start of a trip to a ride history database
        } else if (event instanceof TripEndedEvent) {
            TripEndedEvent tripEndedEvent = (TripEndedEvent) event;
            System.out.println("History Service: Trip ended for bike " + tripEndedEvent.getBikeId() + 
                             ", user " + tripEndedEvent.getUserId() + 
                             ", duration: " + tripEndedEvent.getDurationMinutes() + " min, " +
                             "distance: " + tripEndedEvent.getDistanceKm() + " km, " +
                             "cost: $" + tripEndedEvent.getCost());
            // Here you would log the end of a trip, calculate statistics, etc.
        } else if (event instanceof BikeMovedEvent) {
            BikeMovedEvent bikeMovedEvent = (BikeMovedEvent) event;
            System.out.println("History Service: Bike " + bikeMovedEvent.getBikeId() + 
                             " moved from station " + bikeMovedEvent.getOldStationId() + 
                             " to station " + bikeMovedEvent.getNewStationId() + 
                             " by operator " + bikeMovedEvent.getOperatorId());
        }
    }
}
