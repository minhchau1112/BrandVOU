package com.example.accountservice.exception;

import java.io.Serial;

public class BrandAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3178948664026920647L;

    private static final String DEFAULT_MESSAGE = """
            Brand already exist!
            """;

    public BrandAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public BrandAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}