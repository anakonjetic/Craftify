package com.tvz.hr.craftify.utilities.exceptions;

public class EntityNotFoundException extends ApplicationException {
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}