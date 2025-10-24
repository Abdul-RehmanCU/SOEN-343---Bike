package com.qwikride.listener;

import com.qwikride.event.StationStatusChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Example listener for station status changes. Keeps behaviour simple: just
 * logs the change.
 * Real listeners could cancel reservations, notify operators, emit metrics,
 * etc.
 */
@Component
@Slf4j
public class StationStatusListener {

    @EventListener
    public void onStationStatusChanged(StationStatusChangedEvent ev) {
        log.info("Station {} status changed: {} -> {}",
                ev.stationId(), ev.oldStatus(), ev.newStatus());
        // TODO: add real reactions (cancel reservations, notify, metrics)
    }
}
