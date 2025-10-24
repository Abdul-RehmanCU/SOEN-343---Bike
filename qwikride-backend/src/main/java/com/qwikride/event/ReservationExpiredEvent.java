package com.qwikride.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReservationExpiredEvent extends BaseDomainEvent {
    private UUID bikeId;
    private Long userId;

    public ReservationExpiredEvent(UUID bikeId, Long userId) {
        this.bikeId = bikeId;
        this.userId = userId;
    }
}
