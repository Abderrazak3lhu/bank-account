package com.talan.bankaccount.bankaccount.controller;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import com.talan.bankaccount.bankaccount.service.COperationService;
import com.talan.bankaccount.bankaccount.service.OperationService;
import com.talan.bankaccount.bankaccount.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OperationController {

    @Autowired
    OperationService operationService;

    @PostMapping(value = AppConstants.DEPOSIT_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation deposit(@RequestBody OperationDTO operationDTO) {
        log.info("Deposit endpoint invoked");
        return operationService.deposit(operationDTO);
    }

    @PostMapping(value = AppConstants.WITHDRAW_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation withdraw(@RequestBody OperationDTO operationDTO) {
        log.info("Withdraw endpoint invoked");
        return operationService.withdraw(operationDTO);
    }

    @PostMapping(value = AppConstants.TRANSFERT_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation transfert(@RequestBody TransfertDTO transfertDTO) {
        log.info("Transfert endpoint invoked");
        return operationService.transfert(transfertDTO);
    }
    @GetMapping(value = AppConstants.TRANSACTIONS_HISTORY_URL +"/{accountNumber}")
    public List<Operation > transactionsHistory(@PathVariable Long accountNumber) {
        log.info("transactions history endpoint invoked");
        return operationService.getTransactionsHistoryForAccountNumber(accountNumber);
    }
}
