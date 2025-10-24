package com.qwikride.factory;

import com.qwikride.model.Bike;
import com.qwikride.model.BikeConfig;
import com.qwikride.model.StandardBike;
import org.springframework.stereotype.Component;

@Component
public class StandardBikeFactory implements BikeFactory {
    @Override
    public Bike createBike(BikeConfig config) {
        return new StandardBike(config.getStationId());
    }
}
