package com.qwikride.controller;

import com.qwikride.dto.*;
import com.qwikride.model.Bike;
import com.qwikride.model.BikeConfig;
import com.qwikride.service.BikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bikes")
@RequiredArgsConstructor
public class BikeController {
    private final BikeService bikeService;

    @GetMapping
    public ResponseEntity<List<Bike>> getAllBikes() {
        return ResponseEntity.ok(bikeService.getAllBikes());
    }

    @GetMapping("/{bikeId}")
    public ResponseEntity<Bike> getBikeById(@PathVariable UUID bikeId) {
        return bikeService.getBikeById(bikeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/station/{stationId}/available")
    public ResponseEntity<List<Bike>> getAvailableBikesAtStation(@PathVariable Long stationId) {
        return ResponseEntity.ok(bikeService.getAvailableBikesAtStation(stationId));
    }

    @GetMapping("/station/{stationId}")
    public ResponseEntity<List<Bike>> getBikesByStation(@PathVariable Long stationId) {
        return ResponseEntity.ok(bikeService.getBikesByStation(stationId));
    }

    @GetMapping("/user/{userId}/reserved")
    public ResponseEntity<Bike> getReservedBikeForUser(@PathVariable Long userId) {
        return bikeService.getReservedBikeByUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Bike> createBike(@Valid @RequestBody BikeConfig config) {
        Bike bike = bikeService.createBike(config);
        return ResponseEntity.ok(bike);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Bike> reserveBike(@Valid @RequestBody BikeReservationRequestDTO request) {
        Bike bike = bikeService.reserveBike(
            request.getStationId(), 
            request.getUserId(), 
            request.getExpiresAfterMinutes()
        );
        return ResponseEntity.ok(bike);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Bike> checkoutBike(@Valid @RequestBody BikeCheckoutRequestDTO request) {
        Bike bike = bikeService.checkoutBike(request.getBikeId(), request.getUserId());
        return ResponseEntity.ok(bike);
    }

    @PostMapping("/return")
    public ResponseEntity<Bike> returnBike(@Valid @RequestBody BikeReturnRequestDTO request) {
        Bike bike = bikeService.returnBike(
            request.getBikeId(), 
            request.getReturnStationId(), 
            request.getUserId(), 
            request.getDurationMinutes(), 
            request.getDistanceKm()
        );
        return ResponseEntity.ok(bike);
    }

    @PostMapping("/move")
    public ResponseEntity<Bike> moveBike(@Valid @RequestBody BikeMoveRequestDTO request) {
        Bike bike = bikeService.moveBike(
            request.getBikeId(), 
            request.getNewStationId(), 
            request.getOperatorId()
        );
        return ResponseEntity.ok(bike);
    }

    @PostMapping("/expired-reservations/process")
    public ResponseEntity<String> processExpiredReservations() {
        bikeService.processExpiredReservations();
        return ResponseEntity.ok("Expired reservations processed");
    }
}