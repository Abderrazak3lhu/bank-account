package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.AccountRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    public Account getAccount(Long accountNumber) {
        Account account =  accountRepository.findOne(accountNumber);
        if(account == null){
            throw new AccountNotFoundException();
        }
        return account;
    }

    public Account updateAccount(Account account) {
        throw new UnsupportedOperationException();
    }
}
