package com.qwikride.factory;

import com.qwikride.model.Bike;
import com.qwikride.model.BikeConfig;

public interface BikeFactory {
    Bike createBike(BikeConfig config);
}
