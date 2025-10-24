package com.qwikride.event;

public interface EventSubscriber {
    void onEvent(DomainEvent event);
}
