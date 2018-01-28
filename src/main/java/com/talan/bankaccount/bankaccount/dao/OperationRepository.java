package com.talan.bankaccount.bankaccount.dao;

import com.talan.bankaccount.bankaccount.domain.Operation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperationRepository extends CrudRepository<Operation, Long> {
    @Query("select o from Operation o where ( o.account =:accountNumber or o.destinationAccount =:accountNumber )")
    //@Query("select from operation o where o.main_account or o.destination_account =: accountNumber")
    List<Operation> findByAllOperationsByAccountNumber(@Param("accountNumber") long accountNumber);
}