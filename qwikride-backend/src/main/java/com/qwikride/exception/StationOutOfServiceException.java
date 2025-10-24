package com.qwikride.exception;

public class StationOutOfServiceException extends RuntimeException {
    public StationOutOfServiceException(String message) {
        super(message);
    }
}
