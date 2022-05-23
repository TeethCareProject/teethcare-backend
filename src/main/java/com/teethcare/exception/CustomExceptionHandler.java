package com.teethcare.exception;

import com.teethcare.model.response.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
/*    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<CustomErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        List<String> error = new ArrayList<>();
        error.add("Entity not found");
        CustomErrorResponse response = new CustomErrorResponse(
                new Timestamp(System.currentTimeMillis()),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.toString(),
                error
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }*/
}
