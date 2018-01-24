package com.talan.bankaccount.bankaccount.dao;

import com.talan.bankaccount.bankaccount.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long>{
}
