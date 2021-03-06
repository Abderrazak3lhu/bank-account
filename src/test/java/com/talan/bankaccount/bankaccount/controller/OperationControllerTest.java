package com.talan.bankaccount.bankaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AccountNotFoundException;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.exception.NotSufficientFundsException;
import com.talan.bankaccount.bankaccount.service.AccountService;
import com.talan.bankaccount.bankaccount.service.CAccountService;
import com.talan.bankaccount.bankaccount.service.COperationService;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.service.OperationService;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import com.talan.bankaccount.bankaccount.util.AppConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OperationController.class)
public class OperationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OperationService operationService;

    @MockBean
    AccountService accountService;

    private ObjectMapper objectMapper;
    private OperationDTO operationDTO;
    private TransfertDTO transfertDTO;
    private Operation deposit;
    private Operation withdraw;
    private Operation transfert;
    private List<Operation> transactionsHistory;

    @Before
    public void initialize() {
        objectMapper = new ObjectMapper();
        operationDTO = new OperationDTO(123123123L, 1000D);
        // deposit init
        Account accountDeposit = new Account(123123123L, 1000D);
        deposit = new Operation(accountDeposit, 1000L, OperationType.DEPOSIT);
        //withdraw init
        Account accountWithdraw = new Account(123123123L, 0D);
        withdraw = new Operation(accountWithdraw, 1000L, OperationType.WITHDRAW);
        // transfert init
        transfertDTO = new TransfertDTO(1L, 2L, 1000D);
        Account mainAccount = new Account(1L, 0D);
        Account destinationAccount = new Account(2L, 1000D);
        transfert = new Operation(mainAccount, destinationAccount, 1000D, OperationType.TRANSFERT);

        // transactions history
        transactionsHistory = new ArrayList<Operation>();
        transactionsHistory.add(transfert);
    }



    @Test
    public void deposit_validAccount_depositSuccess() throws Exception {

        given(operationService.deposit(anyObject())).willReturn(deposit);
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.DEPOSIT_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("account.accountNumber").value(123123123L))
                .andExpect(jsonPath("account.balance").value(1000D))
                .andExpect(jsonPath("amount").value(1000D))
                .andExpect(jsonPath("type").value("DEPOSIT"));
    }

    @Test
    public void deposit_NotValidAccount_responseStatusNotAcceptable() throws Exception {

        given(operationService.deposit(anyObject())).willThrow(new AccountNotFoundException(AppConstants.ACCOUNT_NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.DEPOSIT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void deposit_NotValidAmount_responseStatusNotAcceptable() throws Exception {

        given(operationService.deposit(anyObject())).willThrow(new AmountNotValidException(AppConstants.AMOUNT_NOT_VALID));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.DEPOSIT_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isBadRequest());

    }


    @Test
    public void withdraw_validAccount_withdrawSuccess() throws Exception {

        given(operationService.withdraw(anyObject())).willReturn(withdraw);
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.WITHDRAW_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("account.accountNumber").value(123123123L))
                .andExpect(jsonPath("account.balance").value(0D))
                .andExpect(jsonPath("amount").value(1000D))
                .andExpect(jsonPath("type").value("WITHDRAW"));
    }

    @Test
    public void withdraw_NotValidAccount_responseStatusNotAcceptable() throws Exception {

        given(operationService.withdraw(anyObject())).willThrow(new AccountNotFoundException(AppConstants.ACCOUNT_NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.WITHDRAW_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void withdraw_NotValidAmount_responseStatusNotAcceptable() throws Exception {

        given(operationService.withdraw(anyObject())).willThrow(new AmountNotValidException(AppConstants.AMOUNT_NOT_VALID));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.WITHDRAW_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void withdraw_notSufficientFunds_responseStatusNotAcceptable() throws Exception {

        given(operationService.withdraw(anyObject())).willThrow(new NotSufficientFundsException(AppConstants.NOT_SUFFICIENT_FUNDS));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.WITHDRAW_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isNotAcceptable());
    }


    @Test
    public void transfert_validAccounts_transfertSuccess() throws Exception {

        given(operationService.transfert(anyObject())).willReturn(transfert);
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.TRANSFERT_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfertDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("account.accountNumber").value(1L))
                .andExpect(jsonPath("account.balance").value(0D))
                .andExpect(jsonPath("destinationAccount.accountNumber").value(2L))
                .andExpect(jsonPath("destinationAccount.balance").value(1000D))
                .andExpect(jsonPath("amount").value(1000D))
                .andExpect(jsonPath("type").value("TRANSFERT"));
    }
    @Test
    public void transfert_NotValidAccount_responseStatusNotAcceptable() throws Exception {

        given(operationService.transfert(anyObject())).willThrow(new AccountNotFoundException(AppConstants.ACCOUNT_NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.TRANSFERT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfertDTO)))
                .andExpect(status().isNotAcceptable());
    }
    @Test
    public void transfert_NotValidAmount_responseStatusNotAcceptable() throws Exception {

        given(operationService.transfert(anyObject())).willThrow(new AmountNotValidException(AppConstants.AMOUNT_NOT_VALID));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.TRANSFERT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfertDTO)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void transfert_notSufficientFunds_responseStatusNotAcceptable() throws Exception {

        given(operationService.transfert(anyObject())).willThrow(new NotSufficientFundsException(AppConstants.NOT_SUFFICIENT_FUNDS));
        mockMvc.perform(MockMvcRequestBuilders.post(AppConstants.TRANSFERT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transfertDTO)))
                .andExpect(status().isNotAcceptable());
    }


    @Test
    public void transactions_recordedTransactions_returnTransactions() throws Exception {

        given(operationService.getTransactionsHistoryForAccountNumber(anyLong())).willReturn(transactionsHistory);
        mockMvc.perform(MockMvcRequestBuilders.get(AppConstants.TRANSACTIONS_HISTORY_URL +"/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].amount").value(1000D))
                .andExpect(jsonPath("$.[0].type").value("TRANSFERT"))
                .andExpect(jsonPath("$.[0].account.accountNumber").value(1L))
                .andExpect(jsonPath("$.[0].destinationAccount.accountNumber").value(2L));
    }

}
