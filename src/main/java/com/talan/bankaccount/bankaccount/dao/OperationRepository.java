package com.talan.bankaccount.bankaccount.dao;

import com.talan.bankaccount.bankaccount.domain.Operation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationRepository extends CrudRepository<Operation, Long> {
    @Query("select o from Operation o where ( o.account.accountNumber =:accountNumber or o.destinationAccount.accountNumber =:accountNumber )")
    List<Operation> findByAllOperationsByAccountNumber(@Param("accountNumber") long accountNumber);
}