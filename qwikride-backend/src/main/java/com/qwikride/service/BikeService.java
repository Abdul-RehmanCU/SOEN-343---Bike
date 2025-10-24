package com.qwikride.service;

import com.qwikride.adapter.BikeLocationPort;
import com.qwikride.event.*;
import com.qwikride.factory.BikeFactory;
import com.qwikride.factory.BikeFactoryRegistry;
import com.qwikride.model.*;
import com.qwikride.repository.BikeRepository;
import com.qwikride.repository.BikeStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BikeService {
    private final BikeRepository bikeRepository;
    private final BikeStationRepository bikeStationRepository;
    private final EventBus eventBus;
    private final BikeLocationPort bikeLocationPort;
    private final BikeFactoryRegistry bikeFactoryRegistry;

    @Transactional
    public Bike createBike(BikeConfig config) {
        BikeFactory factory = bikeFactoryRegistry.getFactory(config.getType());
        Bike bike = factory.createBike(config);
        Bike savedBike = bikeRepository.save(bike);
        
        // Update station count
        BikeStation station = bikeStationRepository.findById(config.getStationId()).orElse(null);
        if (station != null) {
            station.setCurrentBikeCount(station.getCurrentBikeCount() + 1);
            bikeStationRepository.save(station);
        }
        
        return savedBike;
    }

    @Transactional
    public Bike reserveBike(Long stationId, Long userId, int expiresAfterMinutes) {
        // Check if station is active and has available bikes
        BikeStation station = bikeStationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));
        
        if (station.getStatus() != BikeStation.StationStatus.ACTIVE) {
            throw new IllegalStateException("Station is out of service");
        }

        if (station.getCurrentBikeCount() <= 0) {
            throw new IllegalStateException("No bikes available at this station");
        }

        // Check if user already has a reservation
        Optional<Bike> existingReservation = bikeRepository.findByReservedByUserId(userId);
        if (existingReservation.isPresent()) {
            throw new IllegalStateException("User already has an active reservation");
        }

        // Find an available bike at the station
        List<Bike> availableBikes = bikeRepository.findByStationIdAndStatus(stationId, BikeStatus.AVAILABLE);
        if (availableBikes.isEmpty()) {
            throw new IllegalStateException("No available bikes at this station");
        }

        Bike bike = availableBikes.get(0);
        bike.reserve(userId, expiresAfterMinutes);
        bikeRepository.save(bike);

        // Update station count
        station.setCurrentBikeCount(station.getCurrentBikeCount() - 1);
        bikeStationRepository.save(station);

        eventBus.publish(new BikeReservedEvent(bike.getId(), userId, stationId));
        return bike;
    }

    @Transactional
    public Bike checkoutBike(UUID bikeId, Long userId) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new IllegalArgumentException("Bike not found"));

        if (bike.getStatus() == BikeStatus.RESERVED && !bike.getReservedByUserId().equals(userId)) {
            throw new IllegalStateException("Bike is reserved by another user");
        }

        if (bike.isReservationExpired()) {
            bike.cancelReservation();
            bikeRepository.save(bike);
            eventBus.publish(new ReservationExpiredEvent(bikeId, bike.getReservedByUserId()));
            throw new IllegalStateException("Reservation has expired");
        }

        if (!bike.canCheckout()) {
            throw new IllegalStateException("Bike cannot be checked out due to its status or condition");
        }

        bike.checkout(userId);
        bikeRepository.save(bike);
        bikeLocationPort.unlockBike(bikeId);
        eventBus.publish(new TripStartedEvent(bikeId, userId, bike.getStationId()));
        return bike;
    }

    @Transactional
    public Bike returnBike(UUID bikeId, Long returnStationId, Long userId, double durationMinutes, double distanceKm) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new IllegalArgumentException("Bike not found"));

        if (bike.getStatus() != BikeStatus.IN_USE || !bike.getCurrentUserId().equals(userId)) {
            throw new IllegalStateException("Bike is not currently checked out by this user");
        }

        // Check if return station is active and has capacity
        BikeStation returnStation = bikeStationRepository.findById(returnStationId)
                .orElseThrow(() -> new IllegalArgumentException("Return station not found"));

        if (returnStation.getStatus() != BikeStation.StationStatus.ACTIVE) {
            throw new IllegalStateException("Return station is out of service");
        }

        if (returnStation.getCurrentBikeCount() >= returnStation.getCapacity()) {
            throw new IllegalStateException("Return station is full");
        }

        bike.returnBike(returnStationId);
        bikeRepository.save(bike);
        bikeLocationPort.lockBike(bikeId);

        // Update station count
        returnStation.setCurrentBikeCount(returnStation.getCurrentBikeCount() + 1);
        bikeStationRepository.save(returnStation);

        // Calculate cost (simple pricing model)
        double cost = durationMinutes * 0.1 + distanceKm * 0.5;
        eventBus.publish(new TripEndedEvent(bikeId, userId, returnStationId, durationMinutes, distanceKm, cost));
        return bike;
    }

    @Transactional
    public Bike moveBike(UUID bikeId, Long newStationId, Long operatorId) {
        Bike bike = bikeRepository.findById(bikeId)
                .orElseThrow(() -> new IllegalArgumentException("Bike not found"));

        Long oldStationId = bike.getStationId();
        
        // Check if new station is active and has capacity
        BikeStation newStation = bikeStationRepository.findById(newStationId)
                .orElseThrow(() -> new IllegalArgumentException("Destination station not found"));

        if (newStation.getStatus() != BikeStation.StationStatus.ACTIVE) {
            throw new IllegalStateException("Destination station is out of service");
        }

        if (newStation.getCurrentBikeCount() >= newStation.getCapacity()) {
            throw new IllegalStateException("Destination station is full");
        }

        if (oldStationId.equals(newStationId)) {
            throw new IllegalStateException("Cannot move bike to the same station");
        }

        bike.moveToStation(newStationId);
        bikeRepository.save(bike);

        // Update station counts
        if (oldStationId != null) {
            BikeStation oldStation = bikeStationRepository.findById(oldStationId).orElse(null);
            if (oldStation != null) {
                oldStation.setCurrentBikeCount(Math.max(0, oldStation.getCurrentBikeCount() - 1));
                bikeStationRepository.save(oldStation);
            }
        }

        newStation.setCurrentBikeCount(newStation.getCurrentBikeCount() + 1);
        bikeStationRepository.save(newStation);

        eventBus.publish(new BikeMovedEvent(bikeId, oldStationId, newStationId, operatorId));
        return bike;
    }

    @Transactional
    public void processExpiredReservations() {
        List<Bike> reservedBikes = bikeRepository.findByStatus(BikeStatus.RESERVED);
        for (Bike bike : reservedBikes) {
            if (bike.isReservationExpired()) {
                bike.cancelReservation();
                bikeRepository.save(bike);
                
                // Update station count
                if (bike.getStationId() != null) {
                    BikeStation station = bikeStationRepository.findById(bike.getStationId()).orElse(null);
                    if (station != null) {
                        station.setCurrentBikeCount(station.getCurrentBikeCount() + 1);
                        bikeStationRepository.save(station);
                    }
                }
                
                eventBus.publish(new ReservationExpiredEvent(bike.getId(), bike.getReservedByUserId()));
            }
        }
    }

    public List<Bike> getAvailableBikesAtStation(Long stationId) {
        return bikeRepository.findByStationIdAndStatus(stationId, BikeStatus.AVAILABLE);
    }

    public List<Bike> getAllBikes() {
        return bikeRepository.findAll();
    }

    public Optional<Bike> getBikeById(UUID bikeId) {
        return bikeRepository.findById(bikeId);
    }

    public Optional<Bike> getReservedBikeByUser(Long userId) {
        return bikeRepository.findByReservedByUserId(userId);
    }

    public List<Bike> getBikesByStation(Long stationId) {
        return bikeRepository.findByStationId(stationId);
    }
}
