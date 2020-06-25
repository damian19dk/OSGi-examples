package com.manager.impl;


import com.manager.api.definition.LoggingService;
import com.manager.api.model.Event;
import com.manager.api.model.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.function.Consumer;

public class LoggingServiceImpl implements LoggingService {

    private static final Logger logger = LoggerFactory.getLogger(LoggingServiceImpl.class);
    private final Random random = new Random();

    private final EventRepository eventRepository = EventRepository.getInstance();

    @Override
    public void logEvent(EventType type, String message) {
        final Event event = new Event(type, message);
        eventRepository.createEvent(event);

        switch (type) {
            case USER:
                logger.info(event.toString());
            case RANDOM:
                final Consumer<String> logMessage = random.nextBoolean() ? logger::warn : logger::debug;
                logMessage.accept("RANDOM " + event.toString());
            case SYSTEM:
                logger.warn(event.toString());
        }
    }
}
