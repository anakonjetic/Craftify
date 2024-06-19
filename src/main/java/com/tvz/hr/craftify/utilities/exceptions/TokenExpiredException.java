package com.tvz.hr.craftify.utilities.exceptions;

public class TokenExpiredException extends ApplicationException {
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}