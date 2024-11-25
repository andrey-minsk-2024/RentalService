package com.videorental.controller.exceptions;

/**
 * @author andrey.semenov
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}