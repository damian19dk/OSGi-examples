package com.manager.api.definition;


import com.manager.api.model.EventType;

import java.time.LocalDateTime;

public interface RetrievalService {
    void retrieve(EventType type, LocalDateTime startTime, LocalDateTime endTime);
}
