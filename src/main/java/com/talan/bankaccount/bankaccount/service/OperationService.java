package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.OperationRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.exception.NotSufficientFunds;
import com.talan.bankaccount.bankaccount.util.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.util.TransfertDTO;
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

    // ######### Deposit #########
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

    // ######### Withdraw #########
    public Operation withdraw(OperationDTO operationDTO) {

        Account account = accountService.getAccount(operationDTO.getAccountNumber());
        if (operationDTO.getAmount() <= 0) {
            throw new AmountNotValidException("Amount not valid");
        }
        if (operationDTO.getAmount() > account.getBalance()) {
            throw new NotSufficientFunds("Not sufficient funds");
        }

        account.setBalance(account.getBalance() - operationDTO.getAmount());
        account = accountService.updateAccount(account);
        Operation withdraw = new Operation(account, operationDTO.getAmount(), OperationType.WITHDRAW);
        withdraw = operationRepository.save(withdraw);
        logger.info("withdraw saved : ", withdraw);
        return withdraw;
    }

    // ######### Transfert #########
    public Operation transfert(TransfertDTO transfertDTO) {
        Account mainAccount = accountService.getAccount(transfertDTO.getMainAccountNumber());
        Account destinationAccount = accountService.getAccount(transfertDTO.getDestinationAccountNumber());
        if(transfertDTO.getAmount() <= 0){
            throw new AmountNotValidException("Amount not valid");
        }
        if (transfertDTO.getAmount() > mainAccount.getBalance()) {
            throw new NotSufficientFunds("Not sufficient funds");
        }
        mainAccount.setBalance(mainAccount.getBalance() - transfertDTO.getAmount());
        destinationAccount.setBalance(mainAccount.getBalance() + transfertDTO.getAmount());
        mainAccount = accountService.updateAccount(mainAccount);
        destinationAccount = accountService.updateAccount(destinationAccount);

        Operation transfert = new Operation(mainAccount, destinationAccount, transfertDTO.getAmount(), OperationType.TRANSFERT);
        transfert = operationRepository.save(transfert);
        return transfert;

    }

}
