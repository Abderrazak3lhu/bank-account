package com.talan.bankaccount.bankaccount.exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AmountNotValidException extends RuntimeException {

    public AmountNotValidException(String message) {
        log.error(message);
    }
}
