package com.team11.bookingservice.exception;

public class UnauthorizedException extends Exception {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause, String message) {
        super(message, cause);
    }
}
