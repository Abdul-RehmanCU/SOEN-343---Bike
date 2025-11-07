package com.qwikride.service;
import lombok.extern.slf4j.Slf4j;
import com.qwikride.event.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DashboardNotifier implements EventSubscriber {
    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof BikeReservedEvent) {
            BikeReservedEvent bikeReservedEvent = (BikeReservedEvent) event;
            log.info("Dashboard notification - Bike {} reserved by user {} at station {}", 
                    bikeReservedEvent.getBikeId(), bikeReservedEvent.getUserId(), bikeReservedEvent.getStationId());
            // Push notification to dashboard for bike reservation
        } else if (event instanceof TripStartedEvent) {
            TripStartedEvent tripStartedEvent = (TripStartedEvent) event;
            log.info("Dashboard notification - Trip started for bike {}, user {}", 
                    tripStartedEvent.getBikeId(), tripStartedEvent.getUserId());
            // Push notification to dashboard for trip start
        } else if (event instanceof TripEndedEvent) {
            TripEndedEvent tripEndedEvent = (TripEndedEvent) event;
            log.info("Dashboard notification - Trip ended for bike {}, user {}, cost: ${}", 
                    tripEndedEvent.getBikeId(), tripEndedEvent.getUserId(), tripEndedEvent.getCost());
            // Push notification to dashboard for trip end
        } else if (event instanceof BikeMovedEvent) {
            BikeMovedEvent bikeMovedEvent = (BikeMovedEvent) event;
            log.info("Dashboard notification - Bike {} moved to station {}", 
                    bikeMovedEvent.getBikeId(), bikeMovedEvent.getNewStationId());
            // Push notification to dashboard for bike movement
        } else if (event instanceof ReservationExpiredEvent) {
            ReservationExpiredEvent reservationExpiredEvent = (ReservationExpiredEvent) event;
            log.info("Dashboard notification - Reservation expired for bike {}, user {}", 
                    reservationExpiredEvent.getBikeId(), reservationExpiredEvent.getUserId());
        }
    }
}
