package com.videorental.controller;

import com.videorental.controller.exceptions.IdempotenceException;
import com.videorental.controller.exceptions.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author andrey.semenov
 */
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(IdempotenceException.class)
    public ResponseEntity<String> handleCustomNotFoundException(IdempotenceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleCustomNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
