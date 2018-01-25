package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.OperationRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.util.TransfertDTO;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
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
    }

    // ######### Deposit #########
    @Test
    public void depositValidAmount_validAccount_depositOperationDone() {

        given(accountService.getAccount(operationDTO.getAccountNumber())).willReturn(accountDeposit);
        given(accountService.updateAccount(anyObject())).willReturn(accountDeposit);
        given(operationRepository.save(any(Operation.class))).willReturn(deposit);

        deposit = operationService.deposit(operationDTO);

        Assertions.assertThat(deposit.getAccount().getAccountNumber()).isEqualTo(123123123L);
        Assertions.assertThat(deposit.getAccount().getBalance()).isEqualTo(1000D);
        Assertions.assertThat(deposit.getAmount()).isEqualTo(1000D);
        Assertions.assertThat(deposit.getType()).isEqualTo(OperationType.DEPOSIT);
    }

    @Test(expected = AmountNotValidException.class)
    public void depositNotValidAmount_validAccount_AmountNotValidException() {

        given(accountService.getAccount(notValidOperationDTO.getAccountNumber())).willReturn(accountDeposit);
        deposit = operationService.deposit(notValidOperationDTO);

    }

    // ######### Withdraw #########
    @Test
    public void withdrawValidAmount_validAccount_withdrawalOperationDone() {

        given(accountService.getAccount(operationDTO.getAccountNumber())).willReturn(accountWithdraw);
        given(accountService.updateAccount(anyObject())).willReturn(accountWithdraw);
        given(operationRepository.save(any(Operation.class))).willReturn(withdraw);

        withdraw = operationService.withdraw(operationDTO);

        Assertions.assertThat(withdraw.getAccount().getAccountNumber()).isEqualTo(123123123L);
        Assertions.assertThat(withdraw.getAccount().getBalance()).isEqualTo(0D);
        Assertions.assertThat(withdraw.getAmount()).isEqualTo(1000D);
        Assertions.assertThat(withdraw.getType()).isEqualTo(OperationType.WITHDRAW);
    }

    // ######### Transfert #########
    @Test
    public void transfertValidAmount_validAccounts_transfertOperationDone() {

        given(accountService.getAccount(transfertDTO.getMainAccountNumber())).willReturn(mainAccount);
        given(accountService.getAccount(transfertDTO.getDestinationAccountNumber())).willReturn(destinationAccount);
        given(accountService.updateAccount(mainAccount)).willReturn(mainAccount);
        given(accountService.updateAccount(destinationAccount)).willReturn(destinationAccount);
        given(operationRepository.save(any(Operation.class))).willReturn(transfert);

        transfert = operationService.transfert(transfertDTO);

        Assertions.assertThat(transfert.getAccount().getAccountNumber()).isEqualTo(1L);
        Assertions.assertThat(transfert.getAccount().getBalance()).isEqualTo(0D);
        Assertions.assertThat(transfert.getDestinationAccount().getAccountNumber()).isEqualTo(2L);
        Assertions.assertThat(transfert.getDestinationAccount().getBalance()).isEqualTo(1000D);
        Assertions.assertThat(transfert.getAmount()).isEqualTo(1000D);
        Assertions.assertThat(transfert.getType()).isEqualTo(OperationType.TRANSFERT);
    }
}
