package com.talan.bankaccount.bankaccount.controller;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import com.talan.bankaccount.bankaccount.service.OperationService;
import com.talan.bankaccount.bankaccount.util.bankAccountConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OperationController {

    @Autowired
    OperationService operationService;

    @PostMapping(value = bankAccountConstants.DEPOSIT_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation deposit(@RequestBody OperationDTO operationDTO) {
        log.info("Deposit endpoint invoked");
        return operationService.deposit(operationDTO);
    }

    @PostMapping(value = bankAccountConstants.WITHDRAW_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation withdraw(@RequestBody OperationDTO operationDTO) {
        log.info("Withdraw endpoint invoked");
        return operationService.withdraw(operationDTO);
    }

    @PostMapping(value = bankAccountConstants.TRANSFERT_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Operation transfert(@RequestBody TransfertDTO transfertDTO) {
        log.info("Transfert endpoint invoked");
        return operationService.transfert(transfertDTO);
    }
}
