package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.domain.Account;

public interface AccountService {
    Account getByAccountNumber(Long accountNumber);
    Account update(Account account);
}
