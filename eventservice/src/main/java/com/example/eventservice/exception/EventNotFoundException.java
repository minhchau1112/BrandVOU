package com.example.eventservice.exception;

import java.io.Serial;

public class EventNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5854015208697200749L;

    private static final String DEFAULT_MESSAGE = """
            Event not found!
            """;

    public EventNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public EventNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
