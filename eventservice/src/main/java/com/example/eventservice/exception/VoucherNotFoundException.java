package com.example.eventservice.exception;

import java.io.Serial;

public class VoucherNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5854015208796200749L;

    private static final String DEFAULT_MESSAGE = """
            Event not found!
            """;

    public VoucherNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public VoucherNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
