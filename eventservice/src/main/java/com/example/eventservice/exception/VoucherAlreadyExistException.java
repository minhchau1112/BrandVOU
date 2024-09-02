package com.example.eventservice.exception;

import java.io.Serial;

public class VoucherAlreadyExistException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 53457023781892737L;

    private static final String DEFAULT_MESSAGE = """
            Voucher already exist!
            """;

    public VoucherAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public VoucherAlreadyExistException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
