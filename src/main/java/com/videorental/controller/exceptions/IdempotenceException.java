package com.videorental.controller.exceptions;

/**
 * @author andrey.semenov
 */
public class IdempotenceException extends RuntimeException {

    public IdempotenceException(String message) {
        super(message);
    }
}