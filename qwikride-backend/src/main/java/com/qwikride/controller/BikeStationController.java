package com.qwikride.controller;

import com.qwikride.dto.StationStatusDTO;
import com.qwikride.model.BikeStation;
import com.qwikride.service.BikeStationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BikeStationController {
    private final BikeStationService stationService;

    @GetMapping("/stations")
    public ResponseEntity<List<BikeStation>> listStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }

    @PostMapping("/stations")
    public ResponseEntity<BikeStation> createStation(@Valid @RequestBody BikeStation station) {
        return ResponseEntity.ok(stationService.createStation(station));
    }

    @GetMapping("/stations/{id}")
    public ResponseEntity<BikeStation> getStation(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(stationService.getStation(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Operator endpoint to set station status. Protected by security config under /api/operator/**
     */
    @PatchMapping("/operator/stations/{id}/status")
    public ResponseEntity<BikeStation> setStationStatus(@PathVariable Long id,
                                                        @Valid @RequestBody StationStatusDTO dto) {
        try {
            BikeStation.StationStatus status = BikeStation.StationStatus.valueOf(dto.getStatus());
            BikeStation updated = stationService.setStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            // both invalid enum or not found map here
            return ResponseEntity.badRequest().build();
        }
    }
}
