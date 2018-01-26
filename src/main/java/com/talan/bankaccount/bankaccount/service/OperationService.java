package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.OperationRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.exception.NotSufficientFunds;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import com.talan.bankaccount.bankaccount.util.bankAccountConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OperationService {

    @Autowired
    AccountService accountService;
    @Autowired
    OperationRepository operationRepository;

    // ######### Deposit #########
    public Operation deposit(OperationDTO operationDTO) {

        Account account = accountService.getByAccountNumber(operationDTO.getAccountNumber());
        if (operationDTO.getAmount() <= 0) {
            throw new AmountNotValidException(bankAccountConstants.AMOUNT_NOT_VALID);
        }
        account.setBalance(account.getBalance() + operationDTO.getAmount());
        account = accountService.update(account);
        Operation deposit = new Operation(account, operationDTO.getAmount(), OperationType.DEPOSIT);
        deposit = operationRepository.save(deposit);
        log.info("deposit saved : ", deposit);
        return deposit;
    }

    // ######### Withdraw #########
    public Operation withdraw(OperationDTO operationDTO) {

        Account account = accountService.getByAccountNumber(operationDTO.getAccountNumber());
        if (operationDTO.getAmount() <= 0) {
            throw new AmountNotValidException(bankAccountConstants.AMOUNT_NOT_VALID);
        }
        if (operationDTO.getAmount() > account.getBalance()) {
            throw new NotSufficientFunds(bankAccountConstants.NOT_SUFFICIENT_FUNDS);
        }

        account.setBalance(account.getBalance() - operationDTO.getAmount());
        account = accountService.update(account);
        Operation withdraw = new Operation(account, operationDTO.getAmount(), OperationType.WITHDRAW);
        withdraw = operationRepository.save(withdraw);
        log.info("withdraw saved : ", withdraw);
        return withdraw;
    }

    // ######### Transfert #########
    public Operation transfert(TransfertDTO transfertDTO) {
        Account mainAccount = accountService.getByAccountNumber(transfertDTO.getMainAccountNumber());
        Account destinationAccount = accountService.getByAccountNumber(transfertDTO.getDestinationAccountNumber());
        if (transfertDTO.getAmount() <= 0) {
            throw new AmountNotValidException(bankAccountConstants.AMOUNT_NOT_VALID);
        }
        if (transfertDTO.getAmount() > mainAccount.getBalance()) {
            throw new NotSufficientFunds(bankAccountConstants.NOT_SUFFICIENT_FUNDS);
        }
        mainAccount.setBalance(mainAccount.getBalance() - transfertDTO.getAmount());
        destinationAccount.setBalance(mainAccount.getBalance() + transfertDTO.getAmount());
        mainAccount = accountService.update(mainAccount);
        destinationAccount = accountService.update(destinationAccount);

        Operation transfert = new Operation(mainAccount, destinationAccount, transfertDTO.getAmount(), OperationType.TRANSFERT);
        transfert = operationRepository.save(transfert);
        return transfert;
    }

}
