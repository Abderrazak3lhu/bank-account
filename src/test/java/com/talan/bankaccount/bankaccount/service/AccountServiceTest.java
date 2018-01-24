package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.AccountRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.exception.AccountNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;
    @InjectMocks
    AccountService accountService;
    private Account retrievedAccount;

    @Before
    public void initialize() {
        retrievedAccount = new Account(1L, 0D);
    }

    @Test
    public void getAccount_validAccount_expect_accountReturned() {

        given(accountRepository.findOne(anyLong())).willReturn(retrievedAccount);
        retrievedAccount = accountService.getAccount(1L);

        Assertions.assertThat(retrievedAccount.getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(retrievedAccount.getBalance()).isEqualTo(0D);
    }

    @Test(expected = AccountNotFoundException.class)
    public void getAccount_notValidAccount_expect_notFoundAccount() {

        given(accountRepository.findOne(anyLong())).willReturn(null);
        retrievedAccount = accountService.getAccount(1L);

    }
}
