package com.talan.bankaccount.bankaccount.dao;

import com.talan.bankaccount.bankaccount.domain.Operation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OperationRepository extends CrudRepository<Operation, Long> {
    List<Operation> getOperationsByAccountNumber(long accountNumber);
}
