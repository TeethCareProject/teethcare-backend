package com.teethcare.exception;

public class ClinicNotFoundException extends RuntimeException {
    public ClinicNotFoundException() {
    }

    public ClinicNotFoundException(String message) {
        super(message);
    }

    public ClinicNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClinicNotFoundException(Throwable cause) {
        super(cause);
    }

    public ClinicNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
