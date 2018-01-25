package com.talan.bankaccount.bankaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AccountNotFoundException;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.service.AccountService;
import com.talan.bankaccount.bankaccount.service.OperationService;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
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

import static org.mockito.BDDMockito.given;
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
    private Operation deposit;
    private Operation withdraw;

    @Before
    public void initialize() {
        objectMapper = new ObjectMapper();
        operationDTO = new OperationDTO(123123123L, 1000D);
        // deposit init
        Account accountDeposit = new Account(123123123L, 0);
        accountDeposit.setBalance(accountDeposit.getBalance() + operationDTO.getAmount());
        deposit = new Operation(accountDeposit, 1000L, OperationType.DEPOSIT);
        //withdraw init
        Account accountWithdraw = new Account(123123123L, 1000D);
        accountWithdraw.setBalance(accountWithdraw.getBalance() - operationDTO.getAmount());
        withdraw = new Operation(accountWithdraw, 1000L, OperationType.WITHDRAW);
    }

    // ######### Deposit #########

    @Test
    public void deposit_validAccount_depositSuccess() throws Exception {

        given(operationService.deposit(anyObject())).willReturn(deposit);
        mockMvc.perform(MockMvcRequestBuilders.post("/deposit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("account.accountNumber").value(123123123L))
                .andExpect(jsonPath("account.balance").value(1000D))
                .andExpect(jsonPath("amount").value(1000D))
                .andExpect(jsonPath("type").value("DEPOSIT"));
    }

    @Test
    public void deposit_NotValidAccount_responseStatusNotAcceptable() throws Exception {

        given(operationService.deposit(anyObject())).willThrow(new AccountNotFoundException("account not found"));
        mockMvc.perform(MockMvcRequestBuilders.post("/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void deposit_NotValidAmount_responseStatusNotAcceptable() throws Exception {

        given(operationService.deposit(anyObject())).willThrow(new AmountNotValidException("amount not valid"));
        mockMvc.perform(MockMvcRequestBuilders.post("/deposit").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isBadRequest());

    }

    // ######### Withdraw #########

    @Test
    public void withdraw_validAccount_withdrawSuccess() throws Exception {

        given(operationService.withdraw(anyObject())).willReturn(withdraw);
        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(operationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("account.accountNumber").value(123123123L))
                .andExpect(jsonPath("account.balance").value(0D))
                .andExpect(jsonPath("amount").value(1000D))
                .andExpect(jsonPath("type").value("WITHDRAW"));
    }

}
