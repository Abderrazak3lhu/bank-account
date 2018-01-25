package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.AccountRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.exception.AccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    Logger logger = LoggerFactory.getLogger(AccountService.class);

    public Account getAccount(Long accountNumber) {
        Account account =  accountRepository.findOne(accountNumber);
        if(account == null){
            throw new AccountNotFoundException("Account not found");
        }
        logger.info("account returned : ", account);
        return account;
    }

    public Account updateAccount(Account account) {
        Account retrievedAccount =  accountRepository.findOne(account.getAccountNumber());
        if(retrievedAccount == null){
            throw new AccountNotFoundException("Account not found");
        }
        logger.info("account updated : ", account);
        return accountRepository.save(account);
    }
}
