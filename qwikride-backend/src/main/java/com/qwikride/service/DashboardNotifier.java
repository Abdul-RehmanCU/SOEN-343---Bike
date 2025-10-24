package com.qwikride.service;

import com.qwikride.event.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardNotifier implements EventSubscriber {
    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof BikeReservedEvent) {
            BikeReservedEvent bikeReservedEvent = (BikeReservedEvent) event;
            System.out.println("Dashboard Notifier: Bike " + bikeReservedEvent.getBikeId() + 
                             " reserved by user " + bikeReservedEvent.getUserId() + 
                             " at station " + bikeReservedEvent.getStationId());
            // Push notification to dashboard for bike reservation
        } else if (event instanceof TripStartedEvent) {
            TripStartedEvent tripStartedEvent = (TripStartedEvent) event;
            System.out.println("Dashboard Notifier: Trip started for bike " + tripStartedEvent.getBikeId() + 
                             ", user " + tripStartedEvent.getUserId());
            // Push notification to dashboard for trip start
        } else if (event instanceof TripEndedEvent) {
            TripEndedEvent tripEndedEvent = (TripEndedEvent) event;
            System.out.println("Dashboard Notifier: Trip ended for bike " + tripEndedEvent.getBikeId() + 
                             ", user " + tripEndedEvent.getUserId() + 
                             ", cost: $" + tripEndedEvent.getCost());
            // Push notification to dashboard for trip end
        } else if (event instanceof BikeMovedEvent) {
            BikeMovedEvent bikeMovedEvent = (BikeMovedEvent) event;
            System.out.println("Dashboard Notifier: Bike " + bikeMovedEvent.getBikeId() + 
                             " moved to station " + bikeMovedEvent.getNewStationId());
            // Push notification to dashboard for bike movement
        } else if (event instanceof ReservationExpiredEvent) {
            ReservationExpiredEvent reservationExpiredEvent = (ReservationExpiredEvent) event;
            System.out.println("Dashboard Notifier: Reservation expired for bike " + reservationExpiredEvent.getBikeId() + 
                             ", user " + reservationExpiredEvent.getUserId());
        }
    }
}
