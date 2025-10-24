package com.qwikride.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BikeMovedEvent extends BaseDomainEvent {
    private UUID bikeId;
    private Long oldStationId;
    private Long newStationId;
    private Long operatorId;

    public BikeMovedEvent(UUID bikeId, Long oldStationId, Long newStationId, Long operatorId) {
        this.bikeId = bikeId;
        this.oldStationId = oldStationId;
        this.newStationId = newStationId;
        this.operatorId = operatorId;
    }
}
