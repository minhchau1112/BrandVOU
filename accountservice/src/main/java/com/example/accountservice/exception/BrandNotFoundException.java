package com.example.accountservice.exception;

import java.io.Serial;

public class BrandNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -3952210155519401565L;

    private static final String DEFAULT_MESSAGE = """
            Brand not found!
            """;

    public BrandNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BrandNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
