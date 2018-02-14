package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.dao.OperationRepository;
import com.talan.bankaccount.bankaccount.domain.Account;
import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;
import com.talan.bankaccount.bankaccount.exception.AmountNotValidException;
import com.talan.bankaccount.bankaccount.exception.NotSufficientFundsException;
import com.talan.bankaccount.bankaccount.util.OperationType;
import com.talan.bankaccount.bankaccount.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class COperationService implements OperationService{

    private final AccountService accountService;
    private final OperationRepository operationRepository;

    @Autowired
    public COperationService(AccountService accountService, OperationRepository operationRepository){
        this.accountService = accountService;
        this.operationRepository = operationRepository;
    }

    public Operation deposit(OperationDTO operationDTO) {

        Account account = accountService.getByAccountNumber(operationDTO.getAccountNumber());

        verifyIfAmountIsNotValid(operationDTO.getAmount());
        account = updateAccountBalanceForDeposit(account, operationDTO.getAmount());

        Operation deposit = new Operation(account, operationDTO.getAmount(), OperationType.DEPOSIT);
        deposit = operationRepository.save(deposit);

        log.info("deposit saved : ", deposit);
        return deposit;
    }


    public Operation withdraw(OperationDTO operationDTO) {

        Account account = accountService.getByAccountNumber(operationDTO.getAccountNumber());

        verifyIfAmountIsNotValid(operationDTO.getAmount());
        verifyIfFundsAreNotSufficient(operationDTO.getAmount(), account.getBalance());

        account = updateAccountBalanceForWithdrawal(account, operationDTO.getAmount());

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

        mainAccount = updateAccountBalanceForWithdrawal(mainAccount, transfertDTO.getAmount());
        destinationAccount = updateAccountBalanceForDeposit(destinationAccount, transfertDTO.getAmount());

        Operation transfert = new Operation(mainAccount, destinationAccount, transfertDTO.getAmount(), OperationType.TRANSFERT);
        transfert = operationRepository.save(transfert);

        log.info("transfert saved : ", transfert);
        return transfert;
    }

    public List<Operation> getTransactionsHistoryForAccountNumber(long accountNumber) {
        accountService.getByAccountNumber(accountNumber);
        List<Operation> operations = operationRepository.findByAllOperationsByAccountNumber(accountNumber);
        log.info("returned operations : ", operations);
        return operations;
    }

    private void verifyIfAmountIsNotValid(double amount) {
        if (amount <= 0) {
            throw new AmountNotValidException(AppConstants.AMOUNT_NOT_VALID);
        }
    }

    private void verifyIfFundsAreNotSufficient(double amount, double accountBalance) {
        if (amount > accountBalance) {
            throw new NotSufficientFundsException(AppConstants.NOT_SUFFICIENT_FUNDS);
        }
    }

    private Account updateAccountBalanceForDeposit(Account account, Double amount) {
        account.setBalance(account.getBalance() + amount);
        log.info("Account balance updated for deposit", account);
        return accountService.update(account);
    }

    private Account updateAccountBalanceForWithdrawal(Account account, Double amount) {
        account.setBalance(account.getBalance() - amount);
        log.info("Account balance updated for withdrawal", account);
        return accountService.update(account);
    }

}
