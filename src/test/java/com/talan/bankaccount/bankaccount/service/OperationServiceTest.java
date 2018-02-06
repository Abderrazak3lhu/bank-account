package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.OperationRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;

@RunWith(SpringRunner.class)
public class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private OperationService operationService;

    // deposit setup
    private OperationDTO operationDTO;
    private Operation deposit;
    private Account accountDeposit;
    // withdraw setup
    private OperationDTO notValidOperationDTO;
    private Operation withdraw;
    private Account accountWithdraw;
    // transfert setup
    private TransfertDTO transfertDTO;
    private Operation transfert;
    private Account mainAccount;
    private Account destinationAccount;
    // transaction history
    private List<Operation> transactionsHistory;

    @Before
    public void initialize() {
        operationDTO = new OperationDTO(123123123L, 1000D);
        notValidOperationDTO = new OperationDTO(123123123L, -1000D);
        // deposit init
        accountDeposit = new Account(123123123L, 0);
        deposit = new Operation(accountDeposit, 1000D, OperationType.DEPOSIT);
        // withdraw init
        accountWithdraw = new Account(123123123L, 1000D);
        withdraw = new Operation(accountDeposit, 1000D, OperationType.WITHDRAW);
        // Transfert init
        transfertDTO = new TransfertDTO(1L, 2L, 1000D);
        mainAccount = new Account(1L, 1000D);
        destinationAccount = new Account(2L, 0D);
        transfert = new Operation(mainAccount, destinationAccount, 1000D, OperationType.TRANSFERT);
        // transactions history
        transactionsHistory = new ArrayList<Operation>();
        transactionsHistory.add(transfert);
    }

    @Test
    public void depositValidAmount_validAccount_depositOperationDone() {

        given(accountService.getByAccountNumber(operationDTO.getAccountNumber())).willReturn(accountDeposit);
        given(accountService.update(anyObject())).willReturn(accountDeposit);
        given(operationRepository.save(any(Operation.class))).willReturn(deposit);

        deposit = operationService.deposit(operationDTO);

        Assertions.assertThat(deposit.getAccount().getAccountNumber()).isEqualTo(123123123L);
        Assertions.assertThat(deposit.getAccount().getBalance()).isEqualTo(1000D);
        Assertions.assertThat(deposit.getAmount()).isEqualTo(1000D);
        Assertions.assertThat(deposit.getType()).isEqualTo(OperationType.DEPOSIT);
    }

    @Test(expected = AmountNotValidException.class)
    public void depositNotValidAmount_validAccount_AmountNotValidException() {

        given(accountService.getByAccountNumber(notValidOperationDTO.getAccountNumber())).willReturn(accountDeposit);
        deposit = operationService.deposit(notValidOperationDTO);

    }

    @Test
    public void withdrawValidAmount_validAccount_withdrawOperationDone() {

        given(accountService.getByAccountNumber(operationDTO.getAccountNumber())).willReturn(accountWithdraw);
        given(accountService.update(anyObject())).willReturn(accountWithdraw);
        given(operationRepository.save(any(Operation.class))).willReturn(withdraw);

        withdraw = operationService.withdraw(operationDTO);

        Assertions.assertThat(withdraw.getAccount().getAccountNumber()).isEqualTo(123123123L);
        Assertions.assertThat(withdraw.getAccount().getBalance()).isEqualTo(0D);
        Assertions.assertThat(withdraw.getAmount()).isEqualTo(1000D);
        Assertions.assertThat(withdraw.getType()).isEqualTo(OperationType.WITHDRAW);
    }

    @Test
    public void transfertValidAmount_validAccounts_transfertOperationDone() {

        given(accountService.getByAccountNumber(transfertDTO.getMainAccountNumber())).willReturn(mainAccount);
        given(accountService.getByAccountNumber(transfertDTO.getDestinationAccountNumber())).willReturn(destinationAccount);
        given(accountService.update(mainAccount)).willReturn(mainAccount);
        given(accountService.update(destinationAccount)).willReturn(destinationAccount);
        given(operationRepository.save(any(Operation.class))).willReturn(transfert);

        transfert = operationService.transfert(transfertDTO);

        Assertions.assertThat(transfert.getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(transfert.getAccount().getBalance()).isEqualTo(0D);
        Assertions.assertThat(transfert.getDestinationAccount().getAccountNumber()).isEqualTo(2L);
        Assertions.assertThat(transfert.getDestinationAccount().getBalance()).isEqualTo(1000D);
        Assertions.assertThat(transfert.getAmount()).isEqualTo(1000D);
        Assertions.assertThat(transfert.getType()).isEqualTo(OperationType.TRANSFERT);
    }

    @Test
    public void transactions_recordedTransactions_returnTransactions() {

        given(operationRepository.findByAllOperationsByAccountNumber(anyLong())).willReturn(transactionsHistory);

        transactionsHistory = operationService.getTransactionsHistoryForAccountNumber(1L);

        Assertions.assertThat(transactionsHistory.size()).isEqualTo(1);
        Assertions.assertThat(transactionsHistory.get(0).getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(transactionsHistory.get(0).getDestinationAccount().getAccountNumber()).isEqualTo(2L);
        Assertions.assertThat(transactionsHistory.get(0).getAmount()).isEqualTo(1000D);
        Assertions.assertThat(transactionsHistory.get(0).getType()).isEqualTo(OperationType.TRANSFERT);
    }
}
