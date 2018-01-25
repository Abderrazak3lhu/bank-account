package com.talan.bankaccount.bankaccount.controller;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AccountNotFoundException;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.exception.NotSufficientFunds;
import com.talan.bankaccount.bankaccount.service.OperationService;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class OperationController {

    @Autowired
    OperationService operationService;

    @PostMapping(value = "/deposit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation deposit(@RequestBody OperationDTO operationDTO) {
        log.info("Deposit endpoint invoked");
        return operationService.deposit(operationDTO);
    }

    @PostMapping(value = "/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation withdraw(@RequestBody OperationDTO operationDTO) {
        log.info("Withdraw endpoint invoked");
        return operationService.withdraw(operationDTO);
    }
    @PostMapping(value = "/transfert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation transfert(@RequestBody TransfertDTO transfertDTO) {
        log.info("Transfert endpoint invoked");
        return operationService.transfert(transfertDTO);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    private void accountNotFoundHandler(AccountNotFoundException e) {
        e.printStackTrace();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void amountNotValidException(AmountNotValidException e) {
        e.printStackTrace();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    private void notSufficientFunds(NotSufficientFunds e) {
        e.printStackTrace();
    }
}
