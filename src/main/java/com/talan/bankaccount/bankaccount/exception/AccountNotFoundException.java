package com.talan.bankaccount.bankaccount.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountNotFoundException extends RuntimeException {

    private Logger logger = LoggerFactory.getLogger(AccountNotFoundException.class);

    public AccountNotFoundException(String message) {
        logger.error(message);
    }
}
