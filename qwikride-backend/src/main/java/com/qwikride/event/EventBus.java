package com.qwikride.event;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventBus {
    private final List<EventSubscriber> subscribers = new ArrayList<>();

    public void subscribe(EventSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void publish(DomainEvent event) {
        for (EventSubscriber subscriber : subscribers) {
            subscriber.onEvent(event);
        }
    }
}
