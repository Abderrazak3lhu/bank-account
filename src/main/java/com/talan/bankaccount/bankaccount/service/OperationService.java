package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.OperationRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OperationService {

    @Autowired
    AccountService accountService;
    @Autowired
    OperationRepository operationRepository;

    private Logger logger = LoggerFactory.getLogger(OperationService.class);


    public Operation deposit(OperationDTO operationDTO) {

        Account account = accountService.getAccount(operationDTO.getAccountNumber());
        if (operationDTO.getAmount() <= 0) {
            throw new AmountNotValidException("Amount not valid");
        }
        account.setBalance(account.getBalance() + operationDTO.getAmount());
        account = accountService.updateAccount(account);
        Operation deposit = new Operation(account, operationDTO.getAmount(), OperationType.DEPOSIT);
        deposit = operationRepository.save(deposit);
        logger.info("deposit saved : ", deposit);
        return deposit;
    }
}
