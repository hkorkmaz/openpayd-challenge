package com.openpayd.rest;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.openpayd.rest.dto.Result;
import com.openpayd.exception.AccountNotFoundException;
import com.openpayd.exception.ClientNotFoundException;
import com.openpayd.exception.InsufficientBalanceException;
import com.openpayd.exception.InvalidBalanceStatusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler({
            ClientNotFoundException.class,
            AccountNotFoundException.class,
            InvalidBalanceStatusException.class,
            InsufficientBalanceException.class
    })
    public ResponseEntity<Result> handleApplicationException(Exception exception) {
        log.error("Application exception while processing request ", exception);
        return Result.Error(BAD_REQUEST).message(exception.getMessage()).build();
    }


    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Result> handleInvalidFormat(Exception exception) {
        log.error("Exception while processing request ", exception);
        return Result.Error(BAD_REQUEST).message(exception.getMessage()).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleOthers(Exception exception) {
        log.error("Exception while processing request ", exception);
        return Result.Error(INTERNAL_SERVER_ERROR).message("Something went wrong").build();
    }
}
