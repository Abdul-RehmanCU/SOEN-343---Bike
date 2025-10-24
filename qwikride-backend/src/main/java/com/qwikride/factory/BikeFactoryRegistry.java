package com.qwikride.factory;

import com.qwikride.model.BikeType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BikeFactoryRegistry {
    private final List<BikeFactory> bikeFactories;
    private final Map<BikeType, BikeFactory> registry = new EnumMap<>(BikeType.class);

    @PostConstruct
    public void init() {
        for (BikeFactory factory : bikeFactories) {
            if (factory instanceof StandardBikeFactory) {
                registry.put(BikeType.STANDARD, factory);
            } else if (factory instanceof EBikeFactory) {
                registry.put(BikeType.E_BIKE, factory);
            }
        }
    }

    public BikeFactory getFactory(BikeType type) {
        BikeFactory factory = registry.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for bike type: " + type);
        }
        return factory;
    }
}
