package com.team11.userservice.exceptions;

public class AlreadyLoggedOutException extends RuntimeException {
    public AlreadyLoggedOutException(String message) {
        super(message);
    }
}