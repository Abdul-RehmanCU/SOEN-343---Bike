package com.qwikride.adapter;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j
@Component
public class GPSBikeSystemAdapter implements BikeLocationPort {
    @Override
    public boolean updateLocation(UUID bikeId, double latitude, double longitude) {
        log.info("GPS System: Updating location for bike {} to ({}, {})", bikeId, latitude, longitude);
        // Simulate external GPS system interaction
        return true;
    }

    @Override
    public boolean lockBike(UUID bikeId) {
        log.info("GPS System: Locking bike {}", bikeId);
        // Simulate external GPS system interaction
        return true;
    }

    @Override
    public boolean unlockBike(UUID bikeId) {
        log.info("GPS System: Unlocking bike {}", bikeId);
        // Simulate external GPS system interaction
        return true;
    }

    @Override
    public boolean isBikeLocked(UUID bikeId) {
        log.info("GPS System: Checking lock status for bike {}", bikeId);
        // Simulate external GPS system interaction
        return true;
    }
}
