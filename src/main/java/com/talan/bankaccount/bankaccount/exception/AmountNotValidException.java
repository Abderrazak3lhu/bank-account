package com.talan.bankaccount.bankaccount.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmountNotValidException extends RuntimeException {

    private Logger logger = LoggerFactory.getLogger(AmountNotValidException.class);

    public AmountNotValidException(String message) {
        logger.error(message);
    }
}
