package com.talan.bankaccount.bankaccount.dao;

import com.talan.bankaccount.bankaccount.domain.Operation;
import org.springframework.data.repository.CrudRepository;

public interface OperationRepository extends CrudRepository<Operation, Long> {
}
