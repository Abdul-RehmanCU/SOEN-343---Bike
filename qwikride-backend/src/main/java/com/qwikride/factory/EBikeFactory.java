package com.qwikride.factory;

import com.qwikride.model.Bike;
import com.qwikride.model.BikeConfig;
import com.qwikride.model.EBike;
import org.springframework.stereotype.Component;

@Component
public class EBikeFactory implements BikeFactory {
    @Override
    public Bike createBike(BikeConfig config) {
        return new EBike(config.getStationId());
    }
}
