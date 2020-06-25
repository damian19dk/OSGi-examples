package com.manager.api.model;

public enum EventType {
    RANDOM("RANDOM"),
    SYSTEM("SYSTEM"),
    USER("USER");

    private final String text;

    EventType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
