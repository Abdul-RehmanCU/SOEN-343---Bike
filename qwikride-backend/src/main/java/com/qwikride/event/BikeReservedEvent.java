package com.qwikride.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BikeReservedEvent extends BaseDomainEvent {
    private UUID bikeId;
    private Long userId;
    private Long stationId;

    public BikeReservedEvent(UUID bikeId, Long userId, Long stationId) {
        this.bikeId = bikeId;
        this.userId = userId;
        this.stationId = stationId;
    }
}
