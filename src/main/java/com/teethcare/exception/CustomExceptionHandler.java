package com.teethcare.exception;

import com.teethcare.config.model.response.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(AccountNotFoundException ex) {
        List errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        CustomErrorResponse error = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.toString(),
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(ClinicNotFoundException ex) {
        List errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        CustomErrorResponse error = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(RegisterAccountException ex) {
        List errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        CustomErrorResponse error = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(IdNotFoundException ex) {
        List errors = new ArrayList<String>();
        errors.add(ex.getMessage());
        CustomErrorResponse error = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }
}
