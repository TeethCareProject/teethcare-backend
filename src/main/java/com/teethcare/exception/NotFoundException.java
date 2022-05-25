package com.teethcare.exception;

public class NotFoundException extends RuntimeException{
    private static String message = "NOT FOUND";
//    public NotFoundException(String message, Throwable cause) {
//        super(message, cause);
//    }

    public NotFoundException() {
        super(message);
    }

    public NotFoundException(Throwable cause) {
        super(message, cause);
    }
}
