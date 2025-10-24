package com.qwikride.adapter;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class FakeBikeSystemAdapter implements BikeLocationPort {
    private final Map<UUID, Boolean> bikeLockStatus = new HashMap<>();

    @Override
    public boolean updateLocation(UUID bikeId, double latitude, double longitude) {
        System.out.println("Fake System: Updating location for bike " + bikeId + " to (" + latitude + ", " + longitude + ")");
        return true;
    }

    @Override
    public boolean lockBike(UUID bikeId) {
        System.out.println("Fake System: Locking bike " + bikeId);
        bikeLockStatus.put(bikeId, true);
        return true;
    }

    @Override
    public boolean unlockBike(UUID bikeId) {
        System.out.println("Fake System: Unlocking bike " + bikeId);
        bikeLockStatus.put(bikeId, false);
        return true;
    }

    @Override
    public boolean isBikeLocked(UUID bikeId) {
        return bikeLockStatus.getOrDefault(bikeId, true);
    }
}
