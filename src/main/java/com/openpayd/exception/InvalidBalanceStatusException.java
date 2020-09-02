package com.openpayd.exception;

public class InvalidBalanceStatusException extends RuntimeException {

    public InvalidBalanceStatusException(String message) {
        super(message);
    }
}
