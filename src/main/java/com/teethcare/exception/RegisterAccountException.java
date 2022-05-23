package com.teethcare.exception;

public class RegisterAccountException extends RuntimeException{
    public RegisterAccountException() {
    }

    public RegisterAccountException(String message) {
        super(message);
    }

    public RegisterAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterAccountException(Throwable cause) {
        super(cause);
    }

    public RegisterAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
