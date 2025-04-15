package com.team11.bookingservice.exception;

public class BookingException extends Exception {

    public BookingException(String message) {
        super(message);
    }

    public BookingException(Throwable cause, String message) {
        super(message, cause);
    }
}
