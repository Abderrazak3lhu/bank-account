package com.talan.bankaccount.bankaccount.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotSufficientFunds extends RuntimeException {

    public NotSufficientFunds(String message) {
        log.error(message);
    }
}
