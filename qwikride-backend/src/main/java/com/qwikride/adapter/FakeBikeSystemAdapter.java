package com.qwikride.adapter;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class FakeBikeSystemAdapter implements BikeLocationPort {
    private final Map<UUID, Boolean> bikeLockStatus = new HashMap<>();

    @Override
    public boolean updateLocation(UUID bikeId, double latitude, double longitude) {
        log.info("Fake System: Updating location for bike {} to ({}, {})", bikeId, latitude, longitude);
        return true;
    }

    @Override
    public boolean lockBike(UUID bikeId) {
        log.info("Fake System: Locking bike {}", bikeId);
        bikeLockStatus.put(bikeId, true);
        return true;
    }

    @Override
    public boolean unlockBike(UUID bikeId) {
        log.info("Fake System: Unlocking bike {}", bikeId);
        bikeLockStatus.put(bikeId, false);
        return true;
    }

    @Override
    public boolean isBikeLocked(UUID bikeId) {
        return bikeLockStatus.getOrDefault(bikeId, true);
    }
}
