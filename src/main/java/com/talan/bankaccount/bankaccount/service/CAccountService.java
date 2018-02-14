package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.AccountRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.exception.AccountNotFoundException;
import com.talan.bankaccount.bankaccount.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CAccountService implements AccountService{

    private final AccountRepository accountRepository;
    @Autowired
    public CAccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account getByAccountNumber(Long accountNumber) {
        Account account = accountRepository.findOne(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(AppConstants.ACCOUNT_NOT_FOUND);
        }
        log.info("account returned : ", account);
        return account;
    }

    public Account update(Account account) {
        Account retrievedAccount = accountRepository.findOne(account.getAccountNumber());
        if (retrievedAccount == null) {
            throw new AccountNotFoundException(AppConstants.ACCOUNT_NOT_FOUND);
        }
        log.info("account updated : ", account);
        return accountRepository.save(account);
    }
}
