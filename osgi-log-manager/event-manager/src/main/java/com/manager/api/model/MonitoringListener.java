package com.manager.api.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MonitoringListener {
    private final UUID uuid;
    private final Set<EventType> monitoredEventTypes;

    public MonitoringListener(EventType... eventTypes) {
        if (eventTypes.length <= 0) {
            throw new IllegalArgumentException("Too few event types for monitor");
        }
        this.uuid = UUID.randomUUID();
        this.monitoredEventTypes = new HashSet<>(Arrays.asList(eventTypes));
    }

    public void consume(Event event) {
        for (EventType eventType : monitoredEventTypes) {
            if (eventType.equals(event.getType())) {
                System.out.println("Consumed by : " + uuid.toString() + " -> " + event.toString());
                break;
            }
        }
    }
}

