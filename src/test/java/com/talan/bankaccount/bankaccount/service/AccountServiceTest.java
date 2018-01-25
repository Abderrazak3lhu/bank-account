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
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountService accountService;

    private Account retrievedAccount;
    private Account updatedAccount;
    private Account accountToUpdate;

    @Before
    public void initialize() {
        retrievedAccount = new Account(1L, 0D);
        updatedAccount = new Account(1L, 1000D);
        accountToUpdate = retrievedAccount;
    }

    @Test
    public void getAccount_validAccount_accountReturned() {

        given(accountRepository.findOne(anyLong())).willReturn(retrievedAccount);
        retrievedAccount = accountService.getAccount(1L);

        Assertions.assertThat(retrievedAccount.getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(retrievedAccount.getBalance()).isEqualTo(0D);
    }

    @Test(expected = AccountNotFoundException.class)
    public void getAccount_notValidAccount_expect_accountNotFoundException() {

        given(accountRepository.findOne(anyLong())).willReturn(null);
        retrievedAccount = accountService.getAccount(1L);

    }

    @Test
    public void updateAccount_validAccount_accountUpdated() {

        given(accountRepository.findOne(anyLong())).willReturn(retrievedAccount);
        given(accountRepository.save(any(Account.class))).willReturn(updatedAccount);

        Account updatedAccount = accountService.updateAccount(retrievedAccount);

        Assertions.assertThat(updatedAccount.getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(updatedAccount.getBalance()).isEqualTo(1000D);
    }

    @Test(expected = AccountNotFoundException.class)
    public void updateAccount_notFoundAccount_expect_accountNotFoundException() {

        given(accountRepository.findOne(anyLong())).willReturn(null);
        retrievedAccount = accountService.updateAccount(accountToUpdate);

    }
}
