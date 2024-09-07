package com.example.eventservice.exception;

import java.io.Serial;

public class ItemAlreadyExistException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 53457023782189737L;

    private static final String DEFAULT_MESSAGE = """
            Item already exist!
            """;

    public ItemAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public ItemAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
