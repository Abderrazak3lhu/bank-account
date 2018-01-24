package com.talan.bankaccount.bankaccount.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long accountNumber;
    @NonNull
    private double balance;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @Getter(onMethod = @__(@JsonIgnore))
    private Collection<Operation> operations;
}
