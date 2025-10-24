package com.qwikride.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class BaseDomainEvent implements DomainEvent {
    private UUID eventId = UUID.randomUUID();
    private LocalDateTime timestamp = LocalDateTime.now();
    private String eventType = this.getClass().getSimpleName();
}
