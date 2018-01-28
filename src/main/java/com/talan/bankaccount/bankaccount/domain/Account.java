package com.talan.bankaccount.bankaccount.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;
    private double balance;
    @OneToMany(mappedBy = "account")
    @Getter(onMethod = @__(@JsonIgnore))
    private Collection<Operation> operations;

    public Account(long accountNumber, double balance) {

        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
