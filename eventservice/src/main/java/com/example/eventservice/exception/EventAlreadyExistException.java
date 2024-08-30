package com.example.eventservice.exception;

import java.io.Serial;

public class EventAlreadyExistException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 53457023789182737L;

    private static final String DEFAULT_MESSAGE = """
            Event already exist!
            """;

    public EventAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public EventAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
