package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.OperationRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.exception.NotSufficientFunds;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.util.bankAccountConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OperationService {

    @Autowired
    AccountService accountService;
    @Autowired
    OperationRepository operationRepository;
    
    public Operation deposit(OperationDTO operationDTO) {

        Account account = accountService.getByAccountNumber(operationDTO.getAccountNumber());

        verifyIfAmountIsNotValid(operationDTO.getAmount());

        account.setBalance(account.getBalance() + operationDTO.getAmount());
        account = accountService.update(account);

        Operation deposit = new Operation(account, operationDTO.getAmount(), OperationType.DEPOSIT);
        deposit = operationRepository.save(deposit);

        log.info("deposit saved : ", deposit);
        return deposit;
    }


    public Operation withdraw(OperationDTO operationDTO) {

        Account account = accountService.getByAccountNumber(operationDTO.getAccountNumber());

        verifyIfAmountIsNotValid(operationDTO.getAmount());
        verifyIfFundsAreNotSufficient(operationDTO.getAmount(), account.getBalance());

        account.setBalance(account.getBalance() - operationDTO.getAmount());
        account = accountService.update(account);

        Operation withdraw = new Operation(account, operationDTO.getAmount(), OperationType.WITHDRAW);
        withdraw = operationRepository.save(withdraw);

        log.info("withdraw saved : ", withdraw);
        return withdraw;
    }


    public Operation transfert(TransfertDTO transfertDTO) {
        Account mainAccount = accountService.getByAccountNumber(transfertDTO.getMainAccountNumber());
        Account destinationAccount = accountService.getByAccountNumber(transfertDTO.getDestinationAccountNumber());

        verifyIfAmountIsNotValid(transfertDTO.getAmount());
        verifyIfFundsAreNotSufficient(transfertDTO.getAmount(), mainAccount.getBalance());

        mainAccount.setBalance(mainAccount.getBalance() - transfertDTO.getAmount());
        destinationAccount.setBalance(mainAccount.getBalance() + transfertDTO.getAmount());
        mainAccount = accountService.update(mainAccount);
        destinationAccount = accountService.update(destinationAccount);

        Operation transfert = new Operation(mainAccount, destinationAccount, transfertDTO.getAmount(), OperationType.TRANSFERT);
        transfert = operationRepository.save(transfert);

        log.info("transfert saved : ", transfert);
        return transfert;
    }

    public List<Operation> transactionsHistoryForAccountNumber(long accountNumber) {
        accountService.getByAccountNumber(accountNumber);
        return operationRepository.findByAllOperationsByAccountNumber(accountNumber);
    }

    private void verifyIfAmountIsNotValid(double amount) {
        if (amount <= 0) {
            throw new AmountNotValidException(bankAccountConstants.AMOUNT_NOT_VALID);
        }
    }

    private void verifyIfFundsAreNotSufficient(double amount, double accountBalance) {
        if (amount > accountBalance) {
            throw new NotSufficientFunds(bankAccountConstants.NOT_SUFFICIENT_FUNDS);
        }
    }
}
