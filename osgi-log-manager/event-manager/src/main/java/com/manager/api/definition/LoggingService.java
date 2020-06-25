package com.manager.api.definition;


import com.manager.api.model.EventType;

public interface LoggingService {
    void logEvent(EventType type, String message);
}
