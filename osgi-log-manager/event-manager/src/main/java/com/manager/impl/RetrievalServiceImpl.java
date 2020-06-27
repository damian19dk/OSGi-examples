package com.manager.impl;


import com.manager.api.definition.RetrievalService;
import com.manager.api.model.Event;
import com.manager.api.model.EventType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RetrievalServiceImpl implements RetrievalService {

    private final EventRepository eventRepository = EventRepository.getInstance();

    @Override
    public void retrieve(EventType type, LocalDateTime startTime, LocalDateTime endTime) {
        final List<Event> eventsToSave = this.eventRepository.getEvents().stream()
                .filter(event -> type.equals(event.getType()))
                .filter(event -> isBetweenStartAndEndDate(event, startTime, endTime))
                .sorted(Comparator.comparing(Event::getTime))
                .collect(Collectors.toList());

        saveSystemEventsToCsv(eventsToSave, "/tmp/csvData.csv");
        this.eventRepository.getEvents().removeAll(eventsToSave);
    }

    private void saveSystemEventsToCsv(List<Event> events, String path) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            for (Event event : events) {
                writer.write(event.toCsvLine());
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isBetweenStartAndEndDate(Event event, LocalDateTime startDate, LocalDateTime endDate) {
        return (event.getTime().isAfter(startDate) || event.getTime().isEqual(startDate))
                && (event.getTime().isBefore(endDate) || event.getTime().isEqual(endDate));
    }
}
