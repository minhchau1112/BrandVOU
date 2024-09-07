package com.example.eventservice.exception;

import java.io.Serial;

public class ItemNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5854015208697200497L;

    private static final String DEFAULT_MESSAGE = """
            Item not found!
            """;

    public ItemNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ItemNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
