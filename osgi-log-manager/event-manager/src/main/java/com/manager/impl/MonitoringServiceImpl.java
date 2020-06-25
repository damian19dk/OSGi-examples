package com.manager.impl;


import com.manager.api.definition.MonitoringService;
import com.manager.api.model.MonitoringListener;

public class MonitoringServiceImpl implements MonitoringService {

    private final EventRepository eventRepository = EventRepository.getInstance();

    @Override
    public void addMonitoringListener(MonitoringListener listener) {
        eventRepository.addMonitoringListener(listener);
    }
}
