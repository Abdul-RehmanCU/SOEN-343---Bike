package com.qwikride.service;

import com.qwikride.exception.StationOutOfServiceException;
import com.qwikride.model.BikeStation;
import com.qwikride.repository.BikeStationRepository;
import com.qwikride.event.StationStatusChangedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BikeStationService {
    private final BikeStationRepository repository;
    private final ApplicationEventPublisher publisher;

    public List<BikeStation> getAllStations() {
        return repository.findAll();
    }

    public BikeStation createStation(BikeStation station) {
        return repository.save(station);
    }

    public BikeStation getStation(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Station not found"));
    }

    @Transactional
    public BikeStation setStatus(Long id, BikeStation.StationStatus status) {
        BikeStation s = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Station not found"));
        BikeStation.StationStatus old = s.getStatus();
        s.setStatus(status);
        BikeStation saved = repository.save(s);
        // publish event so other components can react to status changes
        publisher.publishEvent(new StationStatusChangedEvent(saved.getId(), old, status));
        return saved;
    }

    /**
     * Ensure the given station is ACTIVE. Call this from
     * reservation/checkout/return flows.
     */
    public void ensureActive(Long stationId) {
        BikeStation s = repository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("Station not found"));
        if (s.getStatus() == BikeStation.StationStatus.OUT_OF_SERVICE) {
            throw new StationOutOfServiceException("Station is out of service");
        }
    }
}
