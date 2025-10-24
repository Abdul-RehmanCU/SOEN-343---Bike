package com.qwikride.adapter;

import java.util.UUID;

public interface BikeLocationPort {
    boolean updateLocation(UUID bikeId, double latitude, double longitude);
    boolean lockBike(UUID bikeId);
    boolean unlockBike(UUID bikeId);
    boolean isBikeLocked(UUID bikeId);
}
