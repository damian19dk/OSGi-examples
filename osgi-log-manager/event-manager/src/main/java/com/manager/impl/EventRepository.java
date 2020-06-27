package com.manager.impl;

import com.manager.api.model.Event;
import com.manager.api.model.MonitoringListener;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Objects.isNull;

public class EventRepository {
    private static EventRepository eventRepositoryInstance = null;
    private final Set<Event> events;

    private final List<MonitoringListener> listeners;

    private EventRepository() {
        this.events = ConcurrentHashMap.newKeySet();
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public static EventRepository getInstance() {
        if (isNull(eventRepositoryInstance)) {
            eventRepositoryInstance = new EventRepository();
        }
        return eventRepositoryInstance;
    }

    void createEvent(Event event) {
        this.events.add(event);
        for (final MonitoringListener listener : listeners) {
            listener.consume(event);
        }
    }

    void clearEvents() {
        this.events.clear();
    }

    Set<Event> getEvents() {
        return this.events;
    }

    void addMonitoringListener(MonitoringListener monitoringListener) {
        this.listeners.add(monitoringListener);
    }

    void clearMonitoringListeners() {
        this.listeners.clear();
    }
}
