package com.manager.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private final UUID uuid;
    private final LocalDateTime time;
    private final EventType type;
    private final String message;

    public Event(EventType type, String message) {
        this.uuid = UUID.randomUUID();
        this.time = LocalDateTime.now();
        this.type = type;
        this.message = message;
    }

    public UUID getUuid() { return uuid; }

    public LocalDateTime getTime() {
        return time;
    }

    public EventType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s %s", type, message);
    }
}
