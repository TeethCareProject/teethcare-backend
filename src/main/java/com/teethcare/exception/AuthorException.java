package com.teethcare.exception;

public class AuthorException extends RuntimeException{
    public AuthorException() {
    }

    public AuthorException(String message) {
        super(message);
    }

    public AuthorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorException(Throwable cause) {
        super(cause);
    }

    public AuthorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
