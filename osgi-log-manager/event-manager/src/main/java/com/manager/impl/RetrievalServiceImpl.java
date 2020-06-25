package com.manager.impl;


import com.manager.api.definition.RetrievalService;
import com.manager.api.model.Event;
import com.manager.api.model.EventType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RetrievalServiceImpl implements RetrievalService {

    private final EventRepository eventRepository = EventRepository.getInstance();

    @Override
    public void retrieve(EventType type, LocalDateTime startTime, LocalDateTime endTime) {
        final List<Event> eventsToSave = this.eventRepository.getEvents().stream()
                .filter(event -> type.equals(event.getType()))
                .filter(event -> isBetweenStartAndEndDate(event, startTime, endTime))
                .collect(Collectors.toList());

//        saveSystemEventsToCsv(eventsToSave, "/tmp/csvData.csv");
        this.eventRepository.clearEvents();
    }

//    private void saveSystemEventsToCsv(List<Event> events, String path) {
//        try (final BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
//            final CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("uuid", "time", "type", "message"));
//            csvPrinter.printRecords(events);
//            csvPrinter.flush();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private boolean isBetweenStartAndEndDate(Event event, LocalDateTime startDate, LocalDateTime endDate) {
        return startDate.compareTo(event.getTime()) >= 0
                && endDate.compareTo(event.getTime()) <= 0;
    }
}
