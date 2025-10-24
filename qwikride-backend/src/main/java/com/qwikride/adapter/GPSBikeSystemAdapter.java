package com.qwikride.adapter;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GPSBikeSystemAdapter implements BikeLocationPort {
    @Override
    public boolean updateLocation(UUID bikeId, double latitude, double longitude) {
        System.out.println("GPS System: Updating location for bike " + bikeId + " to (" + latitude + ", " + longitude + ")");
        // Simulate external GPS system interaction
        return true;
    }

    @Override
    public boolean lockBike(UUID bikeId) {
        System.out.println("GPS System: Locking bike " + bikeId);
        // Simulate external GPS system interaction
        return true;
    }

    @Override
    public boolean unlockBike(UUID bikeId) {
        System.out.println("GPS System: Unlocking bike " + bikeId);
        // Simulate external GPS system interaction
        return true;
    }

    @Override
    public boolean isBikeLocked(UUID bikeId) {
        System.out.println("GPS System: Checking lock status for bike " + bikeId);
        // Simulate external GPS system interaction
        return true;
    }
}
