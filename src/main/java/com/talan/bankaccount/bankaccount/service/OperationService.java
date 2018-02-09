package com.talan.bankaccount.bankaccount.service;

import com.talan.bankaccount.bankaccount.domain.Operation;
import com.talan.bankaccount.bankaccount.dto.OperationDTO;
import com.talan.bankaccount.bankaccount.dto.TransfertDTO;

import java.util.List;

public interface OperationService {
    Operation deposit(OperationDTO operationDTO);
    Operation withdraw(OperationDTO operationDTO);
    Operation transfert(TransfertDTO transfertDTO);
    List<Operation> getTransactionsHistoryForAccountNumber(long accountNumber);
}
