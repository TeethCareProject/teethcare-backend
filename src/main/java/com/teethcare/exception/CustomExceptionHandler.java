package com.teethcare.exception;

import com.teethcare.model.response.CustomErrorResponse;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice

public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(AccountNotFoundException ex) {
        List<String> errors = new ArrayList();
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
    public ResponseEntity<CustomErrorResponse> handleException(NotFoundException ex) {
        List<String> errors = new ArrayList();
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
    public ResponseEntity<CustomErrorResponse> handleException(EntityNotFoundException ex) {
        List<String> errors = new ArrayList();
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
    public ResponseEntity<CustomErrorResponse> handleException(BadRequestException ex) {
        List<String> errors = new ArrayList();
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
    public ResponseEntity<CustomErrorResponse> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(fieldName + " " + errorMessage);
        });
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                errors

        );
        return new ResponseEntity<>(customErrorResponse, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(DataIntegrityViolationException ex) {
        List<String> errors = new ArrayList();
        errors.add(ex.getMessage());
        CustomErrorResponse error = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler
    public ResponseEntity<CustomErrorResponse> handleException(TypeMismatchException ex) {
        List<String> errors = new ArrayList();
        errors.add(ex.getMessage());
        CustomErrorResponse error = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.toString(),
                errors
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
}
