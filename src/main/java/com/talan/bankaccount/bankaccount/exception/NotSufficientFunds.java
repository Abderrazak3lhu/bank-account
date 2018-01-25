package com.talan.bankaccount.bankaccount.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotSufficientFunds extends RuntimeException {
    private Logger logger = LoggerFactory.getLogger(AmountNotValidException.class);

    public NotSufficientFunds(String message) {
        logger.error(message);
    }
}
