package com.qwikride.service;

import com.qwikride.exception.StationOutOfServiceException;
import com.qwikride.model.BikeStation;
import com.qwikride.repository.BikeStationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BikeStationServiceTest {

    @Mock
    private BikeStationRepository repository;

    @Mock
    private org.springframework.context.ApplicationEventPublisher publisher;

    @InjectMocks
    private BikeStationService service;

    @Test
    void setStatus_UpdatesStatus() {
        BikeStation s = new BikeStation(1L, "A", "addr", 10, 3, BikeStation.StationStatus.ACTIVE, null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(s));
        when(repository.save(any(BikeStation.class))).thenAnswer(inv -> inv.getArgument(0));

        BikeStation updated = service.setStatus(1L, BikeStation.StationStatus.OUT_OF_SERVICE);

        assertEquals(BikeStation.StationStatus.OUT_OF_SERVICE, updated.getStatus());
        verify(repository).save(updated);
    }

    @Test
    void ensureActive_ThrowsWhenOutOfService() {
        BikeStation s = new BikeStation(2L, "B", "addr", 5, 1, BikeStation.StationStatus.OUT_OF_SERVICE, null, null);
        when(repository.findById(2L)).thenReturn(Optional.of(s));

        assertThrows(StationOutOfServiceException.class, () -> service.ensureActive(2L));
    }
}
